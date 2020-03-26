package gov.nist.csd.pm.pdp.audit;

import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pdp.audit.model.Explain;
import gov.nist.csd.pm.pdp.audit.model.Path;
import gov.nist.csd.pm.pdp.audit.model.PolicyClass;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.dag.propagator.Propagator;
import gov.nist.csd.pm.pip.graph.dag.searcher.DepthFirstSearcher;
import gov.nist.csd.pm.pip.graph.dag.searcher.Direction;
import gov.nist.csd.pm.pip.graph.dag.visitor.Visitor;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

import java.util.*;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;

public class PReviewAuditor implements Auditor {

    private static final String ALL_OPERATIONS = "*";

    private Graph graph;

    public PReviewAuditor(Graph graph) {
        this.graph = graph;
    }

    @Override
    public Explain explain(String userID, String target) throws PMException {
        Node userNode = graph.getNode(userID);
        Node targetNode = graph.getNode(target);

        List<EdgePath> userPaths = dfs(userNode);
        List<EdgePath> targetPaths = dfs(targetNode);

        Map<String, PolicyClass> resolvedPaths = resolvePaths(userPaths, targetPaths, target);
        Set<String> perms = resolvePermissions(resolvedPaths);

        return new Explain(perms, resolvedPaths);
    }

    private Set<String> resolvePermissions(Map<String, PolicyClass> paths) {
        Map<String, Set<String>> pcPerms = new HashMap<>();
        for (String pc : paths.keySet()) {
            PolicyClass pcPaths = paths.get(pc);
            for(Path p : pcPaths.getPaths()) {
                Set<String> ops = p.getOperations();
                Set<String> exOps = pcPerms.getOrDefault(pc, new HashSet<>());
                exOps.addAll(ops);
                pcPerms.put(pc, exOps);
            }
        }

        Set<String> perms = new HashSet<>();
        boolean first = true;

        for(String pc : pcPerms.keySet()) {
            Set<String> ops = pcPerms.get(pc);
            if (first) {
                perms.addAll(ops);
                first = false;
            }
            else {
                if (perms.contains(ALL_OPERATIONS)) {
                    perms.remove(ALL_OPERATIONS);
                    perms.addAll(ops);
                }
                else {
                    // if the ops for the pc are empty then the user has no permissions on the target
                    if (ops.isEmpty()) {
                        perms.clear();
                        break;
                    }
                    else if (!ops.contains(ALL_OPERATIONS)) {
                        perms.retainAll(ops);
                    }
                }
            }
        }

        // if the permission set includes *, ignore all other permissions
        if (perms.contains(ALL_OPERATIONS)) {
            perms.clear();
            perms.add(ALL_OPERATIONS);
        }

        return perms;
    }

    /**
     * Given a set of paths starting at a user, and a set of paths starting at an object, return the paths from
     * the user to the target node (through an association) that belong to each policy class. A path is added to a policy
     * class' entry in the returned map if the user path ends in an association in which the target of the association
     * exists in a target path. That same target path must also end in a policy class. If the path does not end in a policy
     * class the target path is ignored.
     *
     * @param userPaths the set of paths starting with a user.
     * @param targetPaths the set of paths starting with a target node.
     * @param target the name of the target node.
     * @return the set of paths from a user to a target node (through an association) for each policy class in the system.
     * @throws PMException if there is an exception traversing the graph
     */
    private Map<String, PolicyClass> resolvePaths(List<EdgePath> userPaths, List<EdgePath> targetPaths, String target) {
        Map<String, PolicyClass> results = new HashMap<>();
        for (EdgePath targetPath : targetPaths) {
            EdgePath.Edge lastTargetEdge = targetPath.getEdges().get(targetPath.getEdges().size()-1);

            // if the last element in the target path is a pc, the target belongs to that pc, add the pc to the results
            // skip to the next target path if it is not a policy class
            if (lastTargetEdge.getTarget().getType() == PC) {
                if(!results.containsKey(lastTargetEdge.getTarget().getName())) {
                    results.put(lastTargetEdge.getTarget().getName(), new PolicyClass());
                }
            } else {
                continue;
            }

            for(EdgePath userPath : userPaths) {
                EdgePath.Edge lastUserEdge = userPath.getEdges().get(userPath.getEdges().size()-1);

                // if the last edge does not have any ops, it is not an association, so ignore it
                if (lastUserEdge.getOps() == null) {
                    continue;
                }

                for(int i = 0; i < targetPath.getEdges().size(); i++) {
                    EdgePath.Edge e = targetPath.getEdges().get(i);

                    // if the target of the last edge in a user resolvedPath does not match the target of the current edge in the target
                    // resolvedPath, continue to the next target edge
                    if((lastUserEdge.getTarget().getName().equals(e.getTarget().getName()))
                            && (!lastUserEdge.getTarget().getName().equals(e.getSource().getName()) || lastUserEdge.getTarget().getName().equals(target))) {
                        continue;
                    }

                    List<EdgePath.Edge> pathToTarget = new ArrayList<>();
                    for(int j = 0; j <= i; j++) {
                        pathToTarget.add(targetPath.getEdges().get(j));
                    }

                    ResolvedPath resolvedPath = resolvePath(userPath, pathToTarget, lastTargetEdge);
                    if (resolvedPath == null) {
                        continue;
                    }

                    Path nodePath = resolvedPath.toNodePath(target);

                    // check that the resolved resolvedPath does not already exist in the results
                    // this can happen if there is more than one resolvedPath to a UA/OA from a U/O
                    PolicyClass exPC = results.get(resolvedPath.getPc().getName());
                    boolean found = false;
                    for(Path p : exPC.getPaths()) {
                        if(p.equals(nodePath)) {
                            found = true;
                            break;
                        }
                    }
                    if(found) {
                        continue;
                    }

                    // add resolvedPath to policy class' paths
                    exPC.getPaths().add(nodePath);
                    exPC.getOperations().addAll(resolvedPath.getOps());
               }
            }
        }
        return results;
    }

    private ResolvedPath resolvePath(EdgePath userPath, List<EdgePath.Edge> pathToTarget, EdgePath.Edge pcEdge) {
        if (pcEdge.getTarget().getType() != PC) {
            return null;
        }

        // get the operations in this path
        // the operations are the ops of the association in the user path
        Set<String> ops = new HashSet<>();
        for(EdgePath.Edge edge : userPath.getEdges()) {
            if(edge.getOps() != null) {
                ops = edge.getOps();
                break;
            }
        }

        EdgePath path = new EdgePath();
        Collections.reverse(pathToTarget);
        for(EdgePath.Edge edge : userPath.getEdges()) {
            path.addEdge(edge);
        }
        for(EdgePath.Edge edge : pathToTarget) {
            path.addEdge(edge);
        }

        return new ResolvedPath(pcEdge.getTarget(), path, ops);
    }

    private List<EdgePath> dfs(Node start) throws PMException {
        DepthFirstSearcher searcher = new DepthFirstSearcher(graph);

        final List<EdgePath> paths = new ArrayList<>();
        final Map<String, List<EdgePath>> propPaths = new HashMap<>();

        Visitor visitor = node -> {
            List<EdgePath> nodePaths = new ArrayList<>();

            for(String parent : graph.getParents(node.getName())) {
                EdgePath.Edge edge = new EdgePath.Edge(node, graph.getNode(parent), null);
                List<EdgePath> parentPaths = propPaths.get(parent);
                if(parentPaths.isEmpty()) {
                    EdgePath path = new EdgePath();
                    path.addEdge(edge);
                    nodePaths.add(0, path);
                } else {
                    for(EdgePath p : parentPaths) {
                        EdgePath parentPath = new EdgePath();
                        for(EdgePath.Edge e : p.getEdges()) {
                            parentPath.addEdge(new EdgePath.Edge(e.getSource(), e.getTarget(), e.getOps()));
                        }

                        parentPath.getEdges().add(0, edge);
                        nodePaths.add(parentPath);
                    }
                }
            }

            Map<String, OperationSet> assocs = graph.getSourceAssociations(node.getName());
            for(String target : assocs.keySet()) {
                Set<String> ops = assocs.get(target);
                Node targetNode = graph.getNode(target);
                EdgePath path = new EdgePath();
                path.addEdge(new EdgePath.Edge(node, targetNode, ops));
                nodePaths.add(path);
            }

            // if the node being visited is the start node, add all the found nodePaths
            // TODO there might be a more efficient way of doing this
            // we don't need the if for users, only when the target is an OA, so it might have something to do with
            // leafs vs non leafs
            if (node.getName().equals(start.getName())) {
                paths.clear();
                paths.addAll(nodePaths);
            } else {
                propPaths.put(node.getName(), nodePaths);
            }
        };

        Propagator propagator = (parentNode, childNode) -> {
            List<EdgePath> childPaths = propPaths.computeIfAbsent(childNode.getName(), k -> new ArrayList<>());
            List<EdgePath> parentPaths = propPaths.get(parentNode.getName());

            for(EdgePath p : parentPaths) {
                EdgePath path = new EdgePath();
                for(EdgePath.Edge edge : p.getEdges()) {
                    path.addEdge(new EdgePath.Edge(edge.getSource(), edge.getTarget(), edge.getOps()));
                }

                EdgePath newPath = new EdgePath();
                newPath.getEdges().addAll(path.getEdges());
                EdgePath.Edge edge = new EdgePath.Edge(childNode, parentNode, null);
                newPath.getEdges().add(0, edge);
                childPaths.add(newPath);
                propPaths.put(childNode.getName(), childPaths);
            }

            if (childNode.getName().equals(start.getName())) {
                paths.clear();
                paths.addAll(propPaths.get(childNode.getName()));
            }
        };

        searcher.traverse(start, propagator, visitor, Direction.PARENTS);
        return paths;
    }

    private static class ResolvedPath {
        private Node pc;
        private EdgePath path;
        private Set<String> ops;

        public ResolvedPath() {

        }

        public ResolvedPath(Node pc, EdgePath path, Set<String> ops) {
            this.pc = pc;
            this.path = path;
            this.ops = ops;
        }

        public Node getPc() {
            return pc;
        }

        public EdgePath getPath() {
            return path;
        }

        public Set<String> getOps() {
            return ops;
        }

        public Path toNodePath(String target) {
            Path nodePath = new Path();
            nodePath.setOperations(this.ops);

            if(this.path.getEdges().isEmpty()) {
                return nodePath;
            }

            boolean foundAssoc = false;
            for(EdgePath.Edge edge : this.path.getEdges()) {
                Node node;
                if(!foundAssoc) {
                    node = edge.getTarget();
                } else {
                    node = edge.getSource();
                }

                if(nodePath.getNodes().isEmpty()) {
                    nodePath.getNodes().add(edge.getSource());
                }

                if (!nodePath.getNodes().contains(node)) {
                    nodePath.getNodes().add(node);
                }

                if(edge.getOps() != null) {
                    foundAssoc = true;
                    if(edge.getTarget().getName().equals(target)) {
                        return nodePath;
                    }
                }
            }

            return nodePath;
        }
    }

    private static class EdgePath {
        private List<Edge> edges;

        public EdgePath() {
            this.edges = new ArrayList<>();
        }

        public EdgePath(List<Edge> edges) {
            this.edges = edges;
        }

        public List<Edge> getEdges() {
            return edges;
        }

        public void addEdge(Edge e) {
            this.edges.add(e);
        }

        public String toString() {
            return edges.toString();
        }

        private static class Edge {
            private Node source;
            private Node target;
            private Set<String> ops;

            public Edge(Node source, Node target, Set<String> ops) {
                this.source = source;
                this.target = target;
                this.ops = ops;
            }

            public Node getSource() {
                return source;
            }

            public void setSource(Node source) {
                this.source = source;
            }

            public Node getTarget() {
                return target;
            }

            public void setTarget(Node target) {
                this.target = target;
            }

            public Set<String> getOps() {
                return ops;
            }

            public void setOps(Set<String> ops) {
                this.ops = ops;
            }

            public String toString() {
                return source.getName() + "(" + source.getType() + ")" + "-->" + (ops != null ? ops + "-->" : "") + target.getName() + "(" + target.getType() + ")";
            }
        }
    }
}
