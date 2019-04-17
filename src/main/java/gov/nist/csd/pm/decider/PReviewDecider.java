package gov.nist.csd.pm.decider;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.Graph;
import gov.nist.csd.pm.graph.model.nodes.Node;
import gov.nist.csd.pm.graph.model.nodes.NodeType;

import java.util.*;

/**
 * An implementation of the Decider interface that uses an in memory NGAC graph
 */
public class PReviewDecider implements Decider {

    public static final String ANY_OPERATIONS = "any";
    public static final String ALL_OPERATIONS = "*";

    private Graph graph;

    /**
     * Create a new Decider with with the given NGAC graph, user ID, and process ID.
     *
     * @param graph the NGAC Graph to use in the policy decision.
     */
    public PReviewDecider(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("NGAC graph cannot be null");
        }

        this.graph = graph;
    }

    @Override
    public boolean hasPermissions(long userID, long targetID, String... perms) throws PMException {
        List<String> permsToCheck = Arrays.asList(perms);
        Set<String> permissions = listPermissions(userID, targetID);

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
    public Set<String> listPermissions(long userID, long targetID) throws PMException {
        Set<String> perms = new HashSet<>();

        //walk the user side and get all target nodes reachable by the user through associations
        Map<Long, Set<String>> dc = getBorderTargets(userID);
        if (dc.isEmpty()) {
            return perms;
        }

        Map<Long, Map<Long, Set<String>>> visitedNodes = new HashMap<>();
        Set<Long> pcs = graph.getPolicies();
        //visit the policy class nodes to signal the end of the dfs
        for (long pc : pcs) {
            Map<Long, Set<String>> pcMap = new HashMap<>();
            pcMap.put(pc, new HashSet<>());
            visitedNodes.put(pc, pcMap);
        }

        //start a depth first search on the target node.
        dfs(targetID, visitedNodes, dc);

        // resolve the permissions will get the common permissions available for each  policy class
        return resolvePermissions(visitedNodes.get(targetID));
    }

    private HashSet<String> resolvePermissions(Map<Long, Set<String>> pcMap) {
        HashSet<String> perms = new HashSet<>();
        boolean first = true;
        for (long pc : pcMap.keySet()) {
            Set<String> ops = pcMap.get(pc);
            if(first) {
                perms.addAll(ops);
                first = false;
            } else {
                if (perms.contains(ALL_OPERATIONS)) {
                    // clear all of the existing permissions because the intersection already had *
                    perms.clear();
                    perms.addAll(ops);
                } else {
                    // if the ops for the pc are empty then the user has no permissions on the target
                    if (ops.isEmpty()) {
                        perms.clear();
                        break;
                    } else if (!ops.contains(ALL_OPERATIONS)) {
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

    @Override
    public Collection<Long> filter(long userID, Collection<Long> nodes, String... perms) {
        nodes.removeIf(n -> {
            try {
                return !hasPermissions(userID, n, perms);
            }
            catch (PMException e) {
                return true;
            }
        });
        return nodes;
    }

    @Override
    public Collection<Long> getChildren(long userID, long targetID, String... perms) throws PMException {
        Set<Long> children = graph.getChildren(targetID);
        return filter(userID, children, perms);
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
    private synchronized Map<Long, Set<String>> getBorderTargets(long userID) throws PMException {
        Map<Long, Set<String>> borderTargets = new HashMap<>();

        //get the parents of the user to start bfs on user side
        Set<Long> parents = graph.getParents(userID);
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

        return borderTargets;
    }

    /**
     * Perform a depth first search on the object side of the graph.  Start at the target node and recursively visit nodes
     * until a policy class is reached.  On each node visited, collect any operation the user has on the target. At the
     * end of each dfs iteration the visitedNodes map will contain the operations the user is permitted on the target under
     * each policy class.
     *
     * @param targetID      the ID of the current target node.
     * @param visitedNodes  the map of nodes that have been visited.
     * @param borderTargets the target nodes reachable by the user via associations.
     */
    private synchronized void dfs(long targetID, Map<Long, Map<Long, Set<String>>> visitedNodes, Map<Long, Set<String>> borderTargets) throws PMException {        //visit the current target node
        visitedNodes.put(targetID, new HashMap<>());

        Set<Long> parents = graph.getParents(targetID);

        //iterate over the parents of the target node
        for (Long parent : parents) {
            //if the parent has not been visited yet, make recursive call to dfs on it
            if (!visitedNodes.containsKey(parent)) {
                dfs(parent, visitedNodes, borderTargets);
            }

            //store all the operations and policy classes for this target node
            Map<Long, Set<String>> pcSet = visitedNodes.get(parent);
            for (long pc : pcSet.keySet()) {
                Set<String> ops = pcSet.get(pc);
                Set<String> exOps = visitedNodes.get(targetID).computeIfAbsent(pc, k -> new HashSet<>());
                exOps.addAll(ops);
            }
        }

        //if the target node is a border target, add the operations found during bfs
        if (borderTargets.containsKey(targetID)) {
            Map<Long, Set<String>> pcSet = visitedNodes.get(targetID);
            Set<String> ops = borderTargets.get(targetID);
            for (long pcId : pcSet.keySet()) {
                visitedNodes.get(targetID).get(pcId).addAll(ops);
            }
        }
    }

    @Override
    public synchronized Map<Long, Set<String>> getAccessibleNodes(long userID) throws PMException {
        Map<Long, Set<String>> results = new HashMap<>();

        //get border nodes.  Can be OA or UA.  Return empty set if no OAs are reachable
        Map<Long, Set<String>> borderTargets = getBorderTargets(userID);
        if (borderTargets.isEmpty()) {
            return results;
        }

        // create a virtual node
        // all children/grand children of the virtual node will be al the nodes accessible by the user
        long vNode = createVNode(borderTargets);

        Map<Long, Map<Long, Set<String>>> visitedNodes = new HashMap<>();
        for (long pc : graph.getPolicies()) {
            Map<Long, Set<String>> pcMap = new HashMap<>();
            pcMap.put(pc, new HashSet<>());
            visitedNodes.put(pc, pcMap);
        }

        Set<Long> objects = getAscendants(vNode);

        for (Long objectID : objects) {
            // run dfs on the object
            dfs(objectID, visitedNodes, borderTargets);

            //for every pc the object reaches check to see if they have a common access1 right.
            Set<String> finalOps = new HashSet<>();
            Map<Long, Set<String>> pcMap = visitedNodes.get(objectID);
            boolean addOps = true;
            for (long pc : pcMap.keySet()) {
                if (addOps) {
                    finalOps.addAll(pcMap.get(pc));
                    addOps = false;
                }
                else {
                    finalOps.retainAll(pcMap.get(pc));
                }
            }
            if (!finalOps.isEmpty()) {
                results.put(objectID, finalOps);
            }
        }

        // delete the virtual node
        graph.deleteNode(vNode);

        return results;
    }

    private Set<Long> getAscendants(Long vNode) throws PMException {
        Set<Long> ascendants = new HashSet<>();
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


    private synchronized long createVNode(Map<Long, Set<String>> dc) throws PMException {
        Node vNode = graph.createNode(new Random().nextLong(), "VNODE", NodeType.OA, null);
        for (long nodeID : dc.keySet()) {
            graph.assign(nodeID, vNode.getID());
        }
        return vNode.getID();
    }
}
