package gov.nist.csd.pm.audit;

import gov.nist.csd.pm.audit.model.Path;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.Graph;
import gov.nist.csd.pm.graph.dag.propagator.Propagator;
import gov.nist.csd.pm.graph.dag.searcher.DepthFirstSearcher;
import gov.nist.csd.pm.graph.dag.visitor.Visitor;
import gov.nist.csd.pm.graph.model.nodes.Node;

import java.util.*;

import static gov.nist.csd.pm.graph.model.nodes.NodeType.*;

public class PReviewAuditor implements Auditor {

    private Graph graph;

    public PReviewAuditor(Graph graph) {
        this.graph = graph;
    }

    @Override
    public Map<String, List<Path>> explain(long userID, long targetID) throws PMException {
        Node userNode = graph.getNode(userID);
        Node targetNode = graph.getNode(targetID);

        List<Path> userPaths = dfs(userNode);
        List<Path> targetPaths = dfs(targetNode);

        return resolvePaths(userPaths, targetPaths);
    }

    /**
     * Given a set of paths starting at a user, and a set of paths starting at an object, return the paths from
     * the user to the target node (through an association) that belong to each policy class. A path is added to a policy
     * class' entry in the returned map if the user path ends in an association in which the target of the association
     * exists in a target path. That same target path must also end in a policy class. If the path does not end in a policy
     * class the target path is ignored.
     *
     * @param userPaths the set of paths starting with a user.
     * @param targetPaths the set of paths starting with a target ndoe.
     * @return the set of paths from a user to a target node (through an association) for each policy class in the system.
     * @throws PMException if there is an exception traversing the graph
     */
    private Map<String, List<Path>> resolvePaths(List<Path> userPaths, List<Path> targetPaths) throws PMException {
        Map<String, List<Path>> results = new HashMap<>();
        Set<Long> pcs = graph.getPolicies();
        for(Long pc : pcs) {
            Node pcNode = graph.getNode(pc);
            results.put(pcNode.getName(), new ArrayList<>());
        }

        for (Path userPath : userPaths) {
            Path.Edge lastEdge = userPath.getEdges().get(userPath.getEdges().size()-1);

            if (lastEdge.getOperations() == null) {
                continue;
            }

            for(Path targetPath : targetPaths) {
                for(int i = 0; i < targetPath.getEdges().size(); i++) {
                    Path.Edge edge = targetPath.getEdges().get(i);

                    if(lastEdge.getTarget().getID() != edge.getTarget().getID()) {
                        continue;
                    }

                    List<Path.Edge> pathToTarget = new ArrayList<>();
                    for(int j = 0; j <= i; j++) {
                        pathToTarget.add(targetPath.getEdges().get(j));
                    }

                    Path.Edge pcEdge = targetPath.getEdges().get(targetPath.getEdges().size()-1);
                    Path path = resolvePath(userPath, pathToTarget, pcEdge);
                    if (path == null) {
                        continue;
                    }

                    List<Path> paths = results.get(pcEdge.getTarget().getName());
                    paths.add(path);
                    results.put(pcEdge.getTarget().getName(), paths);
                }
            }
        }
        return results;
    }

    private Path resolvePath(Path userPath, List<Path.Edge> pathToTarget, Path.Edge pcEdge) {
        if (pcEdge.getTarget().getType() != PC) {
            return null;
        }

        Path path = new Path();
        Collections.reverse(pathToTarget);
        for(Path.Edge edge : userPath.getEdges()) {
            path.addEdge(edge);
        }
        for(Path.Edge edge : pathToTarget) {
            path.addEdge(edge);
        }
        return path;
    }

    private List<Path> dfs(Node start) throws PMException {
        DepthFirstSearcher searcher = new DepthFirstSearcher(graph);

        final List<Path> paths = new ArrayList<>();
        final Map<Long, List<Path>> propPaths = new HashMap<>();

        Visitor visitor = node -> {
            if (node.getID() == start.getID()) {
                return;
            }

            List<Path> nodePaths = new ArrayList<>();

            for(Long parentID : graph.getParents(node.getID())) {
                Path.Edge edge = new Path.Edge(node, graph.getNode(parentID));
                List<Path> parentPaths = propPaths.get(parentID);
                if(parentPaths.isEmpty()) {
                    Path path = new Path();
                    path.addEdge(edge);
                    nodePaths.add(0, path);
                } else {
                    for(Path parentPath : parentPaths) {
                        parentPath.insertEdge(edge);
                        nodePaths.add(parentPath);
                    }
                }
            }

            Map<Long, Set<String>> assocs = graph.getSourceAssociations(node.getID());
            for(Long targetID : assocs.keySet()) {
                Set<String> ops = assocs.get(targetID);
                Node targetNode = graph.getNode(targetID);
                Path path = new Path();
                path.addEdge(new Path.Edge(node, targetNode, ops));
                nodePaths.add(path);
            }

            propPaths.put(node.getID(), nodePaths);
        };

        Propagator propagator = (parentNode, childNode) -> {
            List<Path> childPaths = propPaths.computeIfAbsent(childNode.getID(), k -> new ArrayList<>());
            List<Path> parentPaths = propPaths.get(parentNode.getID());
            for(Path path : parentPaths) {
                Path newPath = new Path();
                newPath.addAll(path.getEdges());
                Path.Edge edge = new Path.Edge(childNode, parentNode);
                newPath.insertEdge(edge);
                childPaths.add(newPath);
                propPaths.put(childNode.getID(), childPaths);
            }

            if (childNode.getID() == start.getID()) {
                paths.clear();
                paths.addAll(propPaths.get(childNode.getID()));
            }
        };

        searcher.traverse(start, propagator, visitor);
        return paths;
    }
}
