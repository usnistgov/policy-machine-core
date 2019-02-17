package gov.nist.csd.pm.decider;

import gov.nist.csd.pm.exceptions.PMDBException;
import gov.nist.csd.pm.exceptions.PMGraphException;
import gov.nist.csd.pm.graph.Graph;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;
import gov.nist.csd.pm.graph.model.nodes.NodeType;

import java.util.*;

/**
 * An implementation of the Decider interface that uses an in memory NGAC graph
 */
public class PReviewDecider implements Decider {

    public static String ANY_OPERATIONS = "any";
    public static String ALL_OPERATIONS = "*";

    private Graph              graph;

    /**
     * Create a new Decider with with the given NGAC graph, user ID, and process ID.
     * @param graph the NGAC Graph to use in the policy decision.
     */
    public PReviewDecider(Graph graph) throws IllegalArgumentException {
        if (graph == null) {
            throw new IllegalArgumentException("NGAC graph cannot be null");
        }

        this.graph = graph;
    }

    @Override
    public boolean hasPermissions(long userID, long processID, long targetID, String... perms) throws PMGraphException, PMDBException {
        List<String> permsToCheck = Arrays.asList(perms);
        HashSet<String> permissions = listPermissions(userID, processID, targetID);

        //if just checking for any operations, return true if the resulting permissions set is not empty.
        //if the resulting permissions set contains * or all operations, return true.
        //if neither of the above apply, return true iff the resulting permissions set contains all the provided
        // permissions to check for
        if(permsToCheck.contains(ANY_OPERATIONS)) {
            return !permissions.isEmpty();
        } else if(permissions.contains(ALL_OPERATIONS)) {
            return true;
        } else if(permissions.isEmpty()) {
            return false;
        } else {
            return permissions.containsAll(permsToCheck);
        }
    }

    @Override
    public HashSet<String> listPermissions(long userID, long processID, long targetID) throws PMDBException, PMGraphException {
        HashSet<String> perms = new HashSet<>();

        //walk the user side and get all target nodes reachable by the user through associations
        HashMap<Long, HashSet<String>> dc = getBorderTargets(userID);
        if(dc.isEmpty()){
            return perms;
        }

        HashMap<Long, HashMap<Long, HashSet<String>>> visitedNodes = new HashMap<>();
        HashSet<Long> pcs = graph.getPolicies();
        //visit the policy class nodes to signal the end of the dfs
        for(long pc : pcs){
            HashMap<Long, HashSet<String>> pcMap = new HashMap<>();
            pcMap.put(pc, new HashSet<>());
            visitedNodes.put(pc, pcMap);
        }

        //start a depth first search on the target node.
        dfs(targetID, visitedNodes, dc);

        //get the intersection of permissions the user has on the target in each policy class
        HashMap<Long, HashSet<String>> pcMap = visitedNodes.get(targetID);
        boolean addOps = true;
        for(long pc : pcMap.keySet()){
            HashSet<String> ops = pcMap.get(pc);
            if(ops.isEmpty()) {// if the ops for the pc are empty then the user has no permissions on the target
                perms.clear();
                break;
            } else if(addOps){// if this is the first time were adding ops just add to perms
                perms.addAll(ops);
                addOps = false;
            }else{// remove any ops that aren't in both sets
                perms.retainAll(ops);
            }
        }

        return perms;
    }

    @Override
    public HashSet<NodeContext> filter(long userID, long processID, HashSet<NodeContext> nodes, String... perms) {
        nodes.removeIf((n) -> {
            try {
                return !hasPermissions(userID, processID, n.getID(), perms);
            }
            catch (PMGraphException | PMDBException e) {
                return true;
            }
        });
        return nodes;
    }

    @Override
    public HashSet<NodeContext> getChildren(long userID, long processID, long targetID, String... perms) throws PMGraphException, PMDBException {
        HashSet<NodeContext> children = graph.getChildren(targetID);
        return filter(userID, processID, children, perms);
    }

    /**
     * Find the target nodes that are reachable by the user via an Association. This is done by a breadth first search
     * starting at the user node and walking up the user side of the graph until all user attributes the user is assigned
     * to have been visited.  For each user attribute visited, get the associations it is the source of and store the
     * target of that association as well as the operations in a map. If a target node is reached multiple times, add any
     * new operations to the already existing ones.
     * @return a Map of target nodes that the user can reach via associations and the operations the user has on each.
     */
    private synchronized HashMap<Long, HashSet<String>> getBorderTargets(long userID) throws PMGraphException, PMDBException {
        HashMap<Long, HashSet<String>> borderTargets = new HashMap<>();

        //get the parents of the user to start bfs on user side
        HashSet<NodeContext> parents = graph.getParents(userID);
        while(!parents.isEmpty()){
            NodeContext parentNode = parents.iterator().next();

            //get the associations the current parent node is the source of
            HashMap<Long, HashSet<String>> assocs = graph.getSourceAssociations(parentNode.getID());

            //collect the target and operation information for each association
            for (long targetID : assocs.keySet()) {
                HashSet<String> ops = assocs.get(targetID);
                HashSet<String> exOps = borderTargets.get(targetID);
                //if the target is not in the map already, put it
                //else add the found operations to the existing ones.
                if (exOps == null) {
                    borderTargets.put(targetID, ops);
                } else {
                    ops.addAll(exOps);
                    borderTargets.put(targetID, ops);
                }
            }

            //add all of the current parent node's parents to the queue
            parents.addAll(graph.getParents(parentNode.getID()));

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
     * @param targetID the ID of the current target node.
     * @param visitedNodes the map of nodes that have been visited.
     * @param borderTargets the target nodes reachable by the user via associations.
     */
    private synchronized void dfs(long targetID, HashMap<Long, HashMap<Long, HashSet<String>>> visitedNodes, HashMap<Long, HashSet<String>> borderTargets) throws PMGraphException, PMDBException {
        //visit the current target node
        visitedNodes.put(targetID, new HashMap<>());

        HashSet<NodeContext> parents = graph.getParents(targetID);

        //iterate over the parents of the target node
        for(NodeContext parent : parents){
            //if the parent has not been visited yet, make recursive call to dfs on it
            if(!visitedNodes.containsKey(parent.getID())){
                dfs(parent.getID(), visitedNodes, borderTargets);
            }

            //store all the operations and policy classes for this target node
            HashMap<Long, HashSet<String>> pcSet = visitedNodes.get(parent.getID());
            for(long pc : pcSet.keySet()){
                HashSet<String> ops = pcSet.get(pc);
                HashSet<String> exOps = visitedNodes.get(targetID).computeIfAbsent(pc, k -> new HashSet<>());
                exOps.addAll(ops);
            }
        }

        //if the target node is a border target, add the operations found during bfs
        if(borderTargets.containsKey(targetID)){
            HashMap<Long, HashSet<String>> pcSet = visitedNodes.get(targetID);
            HashSet<String> ops = borderTargets.get(targetID);
            for(long pcId : pcSet.keySet()){
                visitedNodes.get(targetID).get(pcId).addAll(ops);
            }
        }
    }

    /**
     * Given a User ID, return every node the user has access to and the permissions they have on each.
     *
     * @param userID the ID of the User.
     * @return a Map of nodes the user has access to and the permissions on each.
     * @throws PMDBException if the graph accesses a database and encounters an error.
     * @throws PMGraphException if there is an error traversing the graph.
     */
    public synchronized HashMap<Long, HashSet<String>> getAccessibleNodes(long userID) throws PMDBException, PMGraphException {
        //Node->{ops}
        HashMap<Long, HashSet<String>> results = new HashMap<>();

        //get border nodes.  Can be OA or UA.  Return empty set if no OAs are reachable
        HashMap<Long, HashSet<String>> borderTargets = getBorderTargets(userID);
        if(borderTargets.isEmpty()){
            return results;
        }

        // create a virtual node
        // all children/grand children of the virtual node will be al the nodes accessible by the user
        long vNode = createVNode(borderTargets);

        HashMap<Long, HashMap<Long, HashSet<String>>> visitedNodes = new HashMap<>();
        for(long pc : graph.getPolicies()){
            HashMap<Long, HashSet<String>> pcMap = new HashMap<>();
            pcMap.put(pc, new HashSet<>());
            visitedNodes.put(pc, pcMap);
        }

        HashSet<NodeContext> objects = getAscendants(vNode);

        for(NodeContext objectNode : objects){
            long objectID = objectNode.getID();

            // run dfs on the object
            dfs(objectID, visitedNodes, borderTargets);

            //for every pc the object reaches check to see if they have a common access1 right.
            HashSet<String> finalOps = new HashSet<>();
            HashMap<Long, HashSet<String>> pcMap = visitedNodes.get(objectID);
            boolean addOps = true;
            for(long pc : pcMap.keySet()){
                if(addOps){
                    finalOps.addAll(pcMap.get(pc));
                    addOps = false;
                }else{
                    finalOps.retainAll(pcMap.get(pc));
                }
            }
            if(!finalOps.isEmpty()) {
                results.put(objectID, finalOps);
            }
        }

        // delete the virtual node
        graph.deleteNode(vNode);

        return results;
    }

    private HashSet<NodeContext> getAscendants(Long vNode) throws PMGraphException, PMDBException {
        HashSet<NodeContext> ascendants = new HashSet<>();
        HashSet<NodeContext> children = graph.getChildren(vNode);
        if(children.isEmpty()){
            return ascendants;
        }

        ascendants.addAll(children);
        for(NodeContext child : children){
            ascendants.addAll(getAscendants(child.getID()));
        }

        return ascendants;
    }


    private synchronized long createVNode(HashMap<Long, HashSet<String>> dc) throws PMGraphException, PMDBException {
        NodeContext vNode = new NodeContext(new Random().nextLong(), "VNODE", NodeType.OA, null);
        long vNodeID = graph.createNode(vNode);
        for(long nodeID : dc.keySet()){
            graph.assign(new NodeContext(nodeID, NodeType.OA), new NodeContext(vNode.getID(), NodeType.OA));
        }
        return vNodeID;
    }
}
