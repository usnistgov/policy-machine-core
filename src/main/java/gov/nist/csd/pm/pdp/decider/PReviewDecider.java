package gov.nist.csd.pm.pdp.decider;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.dag.propagator.Propagator;
import gov.nist.csd.pm.pip.graph.dag.searcher.BreadthFirstSearcher;
import gov.nist.csd.pm.pip.graph.dag.searcher.DepthFirstSearcher;
import gov.nist.csd.pm.pip.graph.dag.visitor.Visitor;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.*;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;

/**
 * An implementation of the Decider interface that uses an in memory NGAC graph
 */
public class PReviewDecider implements Decider {

    public static final String ANY_OPERATIONS = "any";
    public static final String ALL_OPERATIONS = "*";

    private Graph graph;
    private Prohibitions prohibitions;

    public PReviewDecider(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("NGAC graph cannot be null");
        }

        this.graph = graph;
        this.prohibitions = new MemProhibitions();
    }

    public PReviewDecider(Graph graph, Prohibitions prohibitions) {
        if (graph == null) {
            throw new IllegalArgumentException("NGAC graph cannot be null");
        }

        if (prohibitions == null) {
            prohibitions = new MemProhibitions();
        }

        this.graph = graph;
        this.prohibitions = prohibitions;
    }

    @Override
    public boolean check(long userID, long processID, long targetID, String... perms) throws PMException {
        List<String> permsToCheck = Arrays.asList(perms);
        Set<String> permissions = list(userID, processID, targetID);

        //if just checking for any operations, return true if the resulting permissions set is not empty.
        //if the resulting permissions set contains * or all operations, return true.
        //if neither of the above apply, return true iff the resulting permissions set contains all the provided
        // permissions to check for
        if (permsToCheck.contains(ANY_OPERATIONS)) {
            return !permissions.isEmpty();
        }
        else if (permissions.contains(ALL_OPERATIONS)) {
            return true;
        }
        else if (permissions.isEmpty()) {
            return false;
        }
        else {
            return permissions.containsAll(permsToCheck);
        }
    }

    @Override
    public Set<String> list(long userID, long processID, long targetID) throws PMException {
        Set<String> perms = new HashSet<>();

        // traverse the user side of the graph to get the associations
        UserContext userCtx = processUserDAG(userID, processID);
        if (userCtx.getBorderTargets().isEmpty()) {
            return perms;
        }

        // traverse the target side of the graph to get permissions per policy class
        TargetContext targetCtx = processTargetDAG(targetID, userCtx);

        // resolve the permissions
        return resolvePermissions(userCtx, targetCtx);
    }

    @Override
    public Collection<Long> filter(long userID, long processID, Collection<Long> nodes, String... perms) {
        nodes.removeIf(n -> {
            try {
                return !check(userID, processID, n, perms);
            }
            catch (PMException e) {
                return true;
            }
        });
        return nodes;
    }

    @Override
    public Collection<Long> getChildren(long userID, long processID, long targetID, String... perms) throws PMException {
        Set<Long> children = graph.getChildren(targetID);
        return filter(userID, processID, children, perms);
    }

    @Override
    public synchronized Map<Long, Set<String>> getAccessibleNodes(long userID, long processID) throws PMException {
        Map<Long, Set<String>> results = new HashMap<>();

        //get border nodes.  Can be OA or UA.  Return empty set if no OAs are reachable
        UserContext userCtx = processUserDAG(userID, processID);
        if (userCtx.getBorderTargets().isEmpty()) {
            return results;
        }

        Set<Long> visited = new HashSet<>();
        for(Long borderTargetID : userCtx.getBorderTargets().keySet()) {
            Set<Long> objects = getAscendants(borderTargetID);
            for (Long objectID : objects) {
                if(visited.contains(objectID)) {
                    continue;
                }
                // run dfs on the object
                TargetContext targetCtx = processTargetDAG(objectID, userCtx);

                HashSet<String> permissions = resolvePermissions(userCtx, targetCtx);
                results.put(objectID, permissions);
            }
        }

        return results;
    }

    @Override
    public Map<Long, Set<String>> generateACL(long oaID, long processID) {
        Map<Long, Set<String>> currNodes = new HashMap<>();
        try {
            Map<Long, Set<String>> targetAssociations = graph.getTargetAssociations(oaID);
            for (long id: targetAssociations.keySet()) {
                generateACLRecursiveHelper(id, targetAssociations.get(id), targetAssociations, currNodes);
            }
        } catch (PMException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return currNodes;
    }

    private void generateACLRecursiveHelper (long id, Set<String> perms, Map<Long, Set<String>> targetAssociations, Map<Long, Set<String>> nodesWPerms) throws PMException {
        if (nodesWPerms.get(id) == null) {
            nodesWPerms.put(id, perms);
            for (Long childID: graph.getChildren(id)) {
                HashSet<String> childPerms = new HashSet<>(perms);
                Set<String> fromAssoc = targetAssociations.get(childID);
                if (fromAssoc != null) {
                    childPerms.addAll(fromAssoc);
                }
                System.out.println(childPerms);
                generateACLRecursiveHelper(childID, childPerms, targetAssociations, nodesWPerms);
            }
        }
    }

    private HashSet<String> resolvePermissions(UserContext userContext, TargetContext targetCtx) throws PMException {
        Map<Long, Set<String>> pcMap = targetCtx.getPcSet();

        HashSet<String> inter = new HashSet<>();
        boolean first = true;
        for (long pc : pcMap.keySet()) {
            Set<String> ops = pcMap.get(pc);
            if(first) {
                inter.addAll(ops);
                first = false;
            } else {
                if (inter.contains(ALL_OPERATIONS)) {
                    // clear all of the existing permissions because the intersection already had *
                    // all permissions can be added
                    inter.clear();
                    inter.addAll(ops);
                } else {
                    // if the ops for the pc are empty then the user has no permissions on the target
                    if (ops.isEmpty()) {
                        inter.clear();
                        break;
                    } else if (!ops.contains(ALL_OPERATIONS)) {
                        inter.retainAll(ops);
                    }
                }
            }
        }

        // remove any prohibited operations
        Set<String> denied = resolveProhibitions(userContext, targetCtx);
        inter.removeAll(denied);

        // if the permission set includes *, ignore all other permissions
        if (inter.contains(ALL_OPERATIONS)) {
            inter.clear();
            inter.add(ALL_OPERATIONS);
        }

        return inter;
    }

    private Set<String> resolveProhibitions(UserContext userCtx, TargetContext targetCtx) {
        Set<String> denied = new HashSet<>();

        Set<Prohibition> prohibitions = userCtx.getProhibitions();
        Map<Prohibition, Set<Long>> reachedProhibitedTargets = targetCtx.getReachedProhibitedTargets();

        for(Prohibition p : prohibitions) {
            boolean inter = p.isIntersection();
            List<Prohibition.Node> nodes = p.getNodes();
            Set<Long> reachedTargets = reachedProhibitedTargets.getOrDefault(p, new HashSet<>());

            boolean addOps = false;
            for (Prohibition.Node n : nodes) {
                if (!n.isComplement() && reachedTargets.contains(n.getID()) ||
                        n.isComplement() && !reachedTargets.contains(n.getID())) {
                    addOps = true;
                } else {
                    // since the intersection requires the target to satisfy each node condition in the prohibition
                    // if one is not satisfied then the whole is not satisfied
                    addOps = false;

                    // if the prohibition is the intersection, one unsatisfied container condition means the whole
                    // prohibition is not satisfied
                    if (inter) {
                        break;
                    }
                }
            }

            if (addOps) {
                denied.addAll(p.getOperations());
            }
        }
        return denied;
    }

    /**
     * Perform a depth first search on the object side of the graph.  Start at the target node and recursively visit nodes
     * until a policy class is reached.  On each node visited, collect any operation the user has on the target. At the
     * end of each dfs iteration the visitedNodes map will contain the operations the user is permitted on the target under
     * each policy class.
     *
     * @param targetID      the ID of the current target node.
     */
    private TargetContext processTargetDAG(long targetID, UserContext userCtx) throws PMException {
        Map<Long, Set<String>> borderTargets = userCtx.getBorderTargets();
        Map<Long, List<Prohibition>> prohibitedTargets = userCtx.getProhibitedTargets();

        Map<Long, Map<Long, Set<String>>> visitedNodes = new HashMap<>();
        Map<Prohibition, Set<Long>> reachedProhibitedTargets = new HashMap<>();

        Visitor visitor = node -> {
            // add this node to reached prohibited targets if it has any prohibitions
            if(prohibitedTargets.containsKey(node.getID())) {
                List<Prohibition> pros = prohibitedTargets.get(node.getID());
                for(Prohibition p : pros) {
                    Set<Long> r = reachedProhibitedTargets.getOrDefault(p, new HashSet<>());
                    for(Prohibition.Node n : p.getNodes()) {
                        r.add(n.getID());
                    }

                    reachedProhibitedTargets.put(p, r);
                }
            }

            Map<Long, Set<String>> nodeCtx = visitedNodes.getOrDefault(node.getID(), new HashMap<>());
            if (nodeCtx.isEmpty()) {
                visitedNodes.put(node.getID(), nodeCtx);
            }

            if (node.getType().equals(NodeType.PC)) {
                nodeCtx.put(node.getID(), new HashSet<>());
            } else {
                if (borderTargets.containsKey(node.getID())) {
                    Set<String> uaOps = borderTargets.get(node.getID());
                    for (Long pc : nodeCtx.keySet()) {
                        Set<String> pcOps = nodeCtx.getOrDefault(pc, new HashSet<>());
                        pcOps.addAll(uaOps);
                        nodeCtx.put(pc, pcOps);
                    }
                }
            }
        };

        Propagator propagator = (parent, child) -> {
            Map<Long, Set<String>> parentCtx = visitedNodes.get(parent.getID());
            Map<Long, Set<String>> nodeCtx = visitedNodes.getOrDefault(child.getID(), new HashMap<>());
            for (Long id : parentCtx.keySet()) {
                Set<String> ops = nodeCtx.getOrDefault(id, new HashSet<>());
                ops.addAll(parentCtx.get(id));
                nodeCtx.put(id, ops);
            }
            visitedNodes.put(child.getID(), nodeCtx);
        };

        DepthFirstSearcher searcher = new DepthFirstSearcher(graph);
        searcher.traverse(graph.getNode(targetID), propagator, visitor);

        return new TargetContext(visitedNodes.get(targetID), reachedProhibitedTargets);
    }

    /**
     * Find the target nodes that are reachable by the user via an Association. This is done by a breadth first search
     * starting at the user node and walking up the user side of the graph until all user attributes the user is assigned
     * to have been visited.  For each user attribute visited, get the associations it is the source of and store the
     * target of that association as well as the operations in a map. If a target node is reached multiple times, add any
     * new operations to the already existing ones.
     *
     * @return a Map of target nodes that the user can reach via associations and the operations the user has on each.
     */
    private UserContext processUserDAG(long userID, long processID) throws PMException {
        BreadthFirstSearcher searcher = new BreadthFirstSearcher(graph);

        Node start = graph.getNode(userID);

        final Map<Long, Set<String>> borderTargets = new HashMap<>();
        final Set<Prohibition> prohibitions = new HashSet<>();
        final Map<Long, List<Prohibition>> prohibitedTargets = getProhibitionTargets(processID);
        for(Long l : prohibitedTargets.keySet()) {
            prohibitions.addAll(prohibitedTargets.get(l));
        }

        Visitor visitor = node -> {
            Map<Long, List<Prohibition>> pts = getProhibitionTargets(node.getID());
            prohibitedTargets.forEach(
                    (k, v) -> pts.merge(k, v, (v1, v2) -> {
                        v1.addAll(v2);
                        return v1;
                    })
            );

            // add any new prohibitions that were reached
            for(Long l : pts.keySet()) {
                prohibitions.addAll(pts.get(l));
            }

            //get the parents of the user to start bfs on user side
            Set<Long> parents = graph.getParents(node.getID());
            while (!parents.isEmpty()) {
                Long parentNode = parents.iterator().next();

                //get the associations the current parent node is the source of
                Map<Long, Set<String>> assocs = graph.getSourceAssociations(parentNode);

                //collect the target and operation information for each association
                for (long targetID : assocs.keySet()) {
                    Set<String> ops = assocs.get(targetID);
                    Set<String> exOps = borderTargets.get(targetID);
                    //if the target is not in the map already, put it
                    //else add the found operations to the existing ones.
                    if (exOps == null) {
                        borderTargets.put(targetID, ops);
                    }
                    else {
                        ops.addAll(exOps);
                        borderTargets.put(targetID, ops);
                    }
                }

                //add all of the current parent node's parents to the queue
                parents.addAll(graph.getParents(parentNode));

                //remove the current parent from the queue
                parents.remove(parentNode);
            }
        };

        // nothing is being propagated
        Propagator propagator = (parentNode, childNode) -> {};

        // start the bfs
        searcher.traverse(start, propagator, visitor);

        return new UserContext(borderTargets, prohibitedTargets, prohibitions);
    }

    private Map<Long, List<Prohibition>> getProhibitionTargets(long subjectID) throws PMException {
        List<Prohibition> pros = prohibitions.getProhibitionsFor(subjectID);
        Map<Long, List<Prohibition>> prohibitionTargets = new HashMap<>();
        for(Prohibition p : pros) {
            for(Prohibition.Node n : p.getNodes()) {
                List<Prohibition> exPs = prohibitionTargets.getOrDefault(n.getID(), new ArrayList<>());
                exPs.add(p);
                prohibitionTargets.put(n.getID(), exPs);
            }
        }

        return prohibitionTargets;
    }

    private Set<Long> getAscendants(Long vNode) throws PMException {
        Set<Long> ascendants = new HashSet<>();
        ascendants.add(vNode);

        Set<Long> children = graph.getChildren(vNode);
        if (children.isEmpty()) {
            return ascendants;
        }

        ascendants.addAll(children);
        for (Long child : children) {
            ascendants.addAll(getAscendants(child));
        }

        return ascendants;
    }

    private class UserContext {
        private Map<Long, Set<String>> borderTargets;
        private Map<Long, List<Prohibition>> prohibitedTargets;
        private Set<Prohibition> prohibitions;

        UserContext(Map<Long, Set<String>> borderTargets, Map<Long, List<Prohibition>> prohibitedTargets, Set<Prohibition> prohibitions) {
            this.borderTargets = borderTargets;
            this.prohibitedTargets = prohibitedTargets;
            this.prohibitions = prohibitions;
        }

        Map<Long, Set<String>> getBorderTargets() {
            return borderTargets;
        }

        Map<Long, List<Prohibition>> getProhibitedTargets() {
            return prohibitedTargets;
        }

        Set<Prohibition> getProhibitions() {
            return prohibitions;
        }
    }

    private class TargetContext {
        Map<Long, Set<String>> pcSet;
        Map<Prohibition, Set<Long>> reachedProhibitedTargets;

        public TargetContext(Map<Long, Set<String>> pcSet, Map<Prohibition, Set<Long>> reachedProhiitedTargets) {
            this.pcSet = pcSet;
            this.reachedProhibitedTargets = reachedProhiitedTargets;
        }

        public Map<Long, Set<String>> getPcSet() {
            return pcSet;
        }

        public Map<Prohibition, Set<Long>> getReachedProhibitedTargets() {
            return reachedProhibitedTargets;
        }
    }
}
