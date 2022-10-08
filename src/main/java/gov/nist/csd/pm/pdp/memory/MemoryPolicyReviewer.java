package gov.nist.csd.pm.pdp.memory;

import gov.nist.csd.pm.pap.memory.dag.BreadthFirstGraphWalker;
import gov.nist.csd.pm.pap.memory.dag.DepthFirstGraphWalker;
import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.PolicyReader;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.NodeDoesNotExistException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.audit.EdgePath;
import gov.nist.csd.pm.policy.model.audit.Explain;
import gov.nist.csd.pm.policy.model.audit.Path;
import gov.nist.csd.pm.policy.model.audit.PolicyClass;
import gov.nist.csd.pm.policy.model.graph.dag.TargetDagResult;
import gov.nist.csd.pm.policy.model.graph.dag.UserDagResult;
import gov.nist.csd.pm.policy.model.graph.dag.propagator.Propagator;
import gov.nist.csd.pm.policy.model.graph.dag.visitor.Visitor;
import gov.nist.csd.pm.policy.model.graph.dag.walker.Direction;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.graph.relationships.Relationship;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static gov.nist.csd.pm.policy.model.access.UserContext.NO_PROCESS;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;

public class MemoryPolicyReviewer extends PolicyReviewer {

    final PolicyReader policyReader;

    /**
     * Creates a new MemoryPolicyReviewer instance that can listen to a PolicyEventEmitter. The MemoryPolicyStoreListener
     * is initially provided an empty MemoryPolicyStore and will listen for updated on any emitter it is attached to.
     */
    public MemoryPolicyReviewer(PolicyReader policyReader) throws PMException {
        this.policyReader = policyReader;
    }

    @Override
    public synchronized AccessRightSet getAccessRights(UserContext userCtx, String target) throws PMException {
        AccessRightSet accessRights = new AccessRightSet();

        // traverse the user side of the graph to get the associations
        UserDagResult userDagResult = processUserDAG(userCtx.getUser(), userCtx.getProcess());
        if (userDagResult.borderTargets().isEmpty()) {
            return accessRights;
        }

        // traverse the target side of the graph to get permissions per policy class
        TargetDagResult targetDagResult = processTargetDAG(target, userDagResult);

        // resolve the permissions
        return resolvePermissions(userDagResult, targetDagResult, target, policyReader.graph().getResourceAccessRights());
    }

    @Override
    public AccessRightSet getDeniedAccessRights(UserContext userCtx, String target) throws PMException {
        AccessRightSet accessRights = new AccessRightSet();

        // traverse the user side of the graph to get the associations
        UserDagResult userDagResult = processUserDAG(userCtx.getUser(), userCtx.getProcess());
        if (userDagResult.borderTargets().isEmpty()) {
            return accessRights;
        }

        // traverse the target side of the graph to get permissions per policy class
        TargetDagResult targetDagResult = processTargetDAG(target, userDagResult);

        // resolve the permissions
        return resolveProhibitions(userDagResult, targetDagResult, target);
    }

    @Override
    public Map<String, AccessRightSet> getPolicyClassAccessRights(UserContext userCtx, String target) throws PMException {
        // traverse the user side of the graph to get the associations
        UserDagResult userDagResult = processUserDAG(userCtx.getUser(), userCtx.getProcess());
        if (userDagResult.borderTargets().isEmpty()) {
            return new HashMap<>();
        }

        // traverse the target side of the graph to get permissions per policy class
        TargetDagResult targetDagResult = processTargetDAG(target, userDagResult);

        return targetDagResult.pcSet();
    }

    /**
     * Perform a depth first search on the object side of the graph.  Start at the target node and recursively visit nodes
     * until a policy class is reached.  On each node visited, collect any operation the user has on the target. At the
     * end of each dfs iteration the visitedNodes map will contain the operations the user is permitted on the target under
     * each policy class.
     */
    private TargetDagResult processTargetDAG(String target, UserDagResult userCtx) throws PMException {
        if (!policyReader.graph().nodeExists(target)) {
            throw new NodeDoesNotExistException(target);
        }

        Map<String, AccessRightSet> borderTargets = userCtx.borderTargets();
        Map<String, Map<String, AccessRightSet>> visitedNodes = new HashMap<>();
        Set<String> reachedTargets = new HashSet<>();

        Visitor visitor = node -> {
            // mark the node as reached, to be used for resolving prohibitions
            if (userCtx.prohibitionTargets().contains(node)) {
                reachedTargets.add(node);
            }

            Map<String, AccessRightSet> nodeCtx = visitedNodes.getOrDefault(node, new HashMap<>());
            if (nodeCtx.isEmpty()) {
                visitedNodes.put(node, nodeCtx);
            }

            if (policyReader.graph().getPolicyClasses().contains(node)) {
                nodeCtx.put(node, new AccessRightSet());
            } else {
                if (borderTargets.containsKey(node)) {
                    Set<String> uaOps = borderTargets.get(node);
                    for (String pc : nodeCtx.keySet()) {
                        AccessRightSet pcOps = nodeCtx.getOrDefault(pc, new AccessRightSet());
                        pcOps.addAll(uaOps);
                        nodeCtx.put(pc, pcOps);
                    }
                }
            }
        };

        Propagator propagator = (parent, child) -> {
            Map<String, AccessRightSet> parentCtx = visitedNodes.get(parent);
            Map<String, AccessRightSet> nodeCtx = visitedNodes.getOrDefault(child, new HashMap<>());
            for (String name : parentCtx.keySet()) {
                AccessRightSet ops = nodeCtx.getOrDefault(name, new AccessRightSet());
                ops.addAll(parentCtx.get(name));
                nodeCtx.put(name, ops);
            }
            visitedNodes.put(child, nodeCtx);
        };

        new DepthFirstGraphWalker(policyReader.graph())
                .withDirection(Direction.PARENTS)
                .withVisitor(visitor)
                .withPropagator(propagator)
                .walk(target);

        return new TargetDagResult(visitedNodes.get(target), reachedTargets);
    }

    /**
     * Find the target nodes that are reachable by the subject via an association. This is done by a breadth first search
     * starting at the subject node and walking up the user side of the graph until all user attributes the subject is assigned
     * to have been visited.  For each user attribute visited, get the associations it is the source of and store the
     * target of that association as well as the operations in a map. If a target node is reached multiple times, add any
     * new operations to the already existing ones.
     *
     * @return a Map of target nodes that the subject can reach via associations and the operations the user has on each.
     */
    private UserDagResult processUserDAG(String subject, String process) throws PMException {
        if (!policyReader.graph().nodeExists(subject)) {
            throw new NodeDoesNotExistException(subject);
        }

        final Map<String, AccessRightSet> borderTargets = new HashMap<>();
        final Set<String> prohibitionTargets = new HashSet<>();
        // initialize with the prohibitions or the provided process
        final Set<Prohibition> reachedProhibitions = new HashSet<>(policyReader.prohibitions().getWithSubject(process));

        // get the associations for the subject, it the subject is a user, nothing will be returned
        // this is only when a UA is the subject
        List<Association> subjectAssociations = policyReader.graph().getAssociationsWithSource(subject);
        collectAssociations(subjectAssociations, borderTargets);

        Visitor visitor = node -> {
            List<Prohibition> subjectProhibitions = policyReader.prohibitions().getWithSubject(node);
            reachedProhibitions.addAll(subjectProhibitions);
            for (Prohibition prohibition : subjectProhibitions) {
                List<ContainerCondition> containers = prohibition.getContainers();
                for (ContainerCondition cont : containers) {
                    prohibitionTargets.add(cont.name());
                }
            }

            //get the parents of the subject to start bfs on user side
            List<String> parents = policyReader.graph().getParents(node);
            while (!parents.isEmpty()) {
                String parent = parents.iterator().next();
                Node parentNode = policyReader.graph().getNode(parent);
                if (parentNode.getType() == UA) {
                    //get the associations the current parent node is the source of
                    List<Association> nodeAssociations = policyReader.graph().getAssociationsWithSource(parent);

                    //collect the target and operation information for each association
                    collectAssociations(nodeAssociations, borderTargets);
                }

                //add all of the current parent node's parents to the queue
                parents.addAll(policyReader.graph().getParents(parent));

                //remove the current parent from the queue
                parents.remove(parent);
            }
        };

        // start the bfs
        new BreadthFirstGraphWalker(policyReader.graph())
                .withDirection(Direction.PARENTS)
                .withVisitor(visitor)
                .walk(subject);

        return new UserDagResult(borderTargets, reachedProhibitions, prohibitionTargets);
    }

    private void collectAssociations(List<Association> assocs, Map<String, AccessRightSet> borderTargets) {
        for (Association association : assocs) {
            AccessRightSet ops = association.getAccessRightSet();
            Set<String> exOps = borderTargets.get(association.getTarget());
            //if the target is not in the map already, put it
            //else add the found operations to the existing ones.
            if (exOps == null) {
                borderTargets.put(association.getTarget(), ops);
            } else {
                ops.addAll(exOps);
                borderTargets.put(association.getTarget(), ops);
            }
        }
    }

    private Set<String> getDescendants(String vNode) throws PMException {
        Set<String> ascendants = new HashSet<>();

        List<String> children = policyReader.graph().getChildren(vNode);
        if (children.isEmpty()) {
            return ascendants;
        }

        ascendants.addAll(children);
        for (String child : children) {
            ascendants.add(child);
            ascendants.addAll(getDescendants(child));
        }

        return ascendants;
    }

    @Override
    public synchronized Map<String, AccessRightSet> buildCapabilityList(UserContext userCtx) throws PMException {
        Map<String, AccessRightSet> results = new HashMap<>();

        //get border nodes.  Can be OA or UA.  Return empty set if no OAs are reachable
        UserDagResult userDagResult = processUserDAG(userCtx.getUser(), userCtx.getProcess());
        if (userDagResult.borderTargets().isEmpty()) {
            return results;
        }

        for(String borderTarget : userDagResult.borderTargets().keySet()) {
            // compute permissions on the border attr
            putPermissions(results, userDagResult, borderTarget);

            // compute decisions for the subgraph of the border attr
            Set<String> descendants = getDescendants(borderTarget);
            for (String descendant : descendants) {
                if (results.containsKey(descendant)) {
                    continue;
                }

                putPermissions(results, userDagResult, descendant);
            }
        }

        return results;
    }

    private void putPermissions(Map<String, AccessRightSet> permissionsMap, UserDagResult userDagResult, String target) throws PMException {
        TargetDagResult targetCtx = processTargetDAG(target, userDagResult);
        AccessRightSet permissions = resolvePermissions(userDagResult, targetCtx, target, policyReader.graph().getResourceAccessRights());
        permissionsMap.put(target, permissions);
    }

    @Override
    public synchronized Map<String, AccessRightSet> buildACL(String target) throws PMException {
        Map<String, AccessRightSet> acl = new HashMap<>();
        List<String> search = policyReader.graph().search(U, noprops());
        for (String user : search) {
            AccessRightSet list = this.getAccessRights(new UserContext(user), target);
            acl.put(user, list);
        }

        return acl;
    }

    @Override
    public synchronized Map<String, AccessRightSet> getBorderAttributes(String user) throws PMException {
        return processUserDAG(user, NO_PROCESS)
                .borderTargets();
    }

    @Override
    public synchronized Map<String, AccessRightSet> getSubgraphAccessRights(UserContext userCtx, String root) throws PMException {
        Map<String, AccessRightSet> results = new HashMap<>();

        UserDagResult userDagResult = processUserDAG(userCtx.getUser(), userCtx.getProcess());
        if (userDagResult.borderTargets().isEmpty()) {
            return results;
        }

        // compute decisions for the subgraph of the border attr
        Set<String> descendants = getDescendants(root);
        for (String descendant : descendants) {
            if (results.containsKey(descendant)) {
                continue;
            }

            putPermissions(results, userDagResult, descendant);
        }

        return results;
    }

    @Override
    public synchronized Explain explain(UserContext userCtx, String target) throws PMException {
        Node userNode = policyReader.graph().getNode(userCtx.getUser());
        Node targetNode = policyReader.graph().getNode(target);

        List<EdgePath> userPaths = dfs(userNode.getName());
        List<EdgePath> targetPaths = dfs(targetNode.getName());

        Map<String, PolicyClass> resolvedPaths = resolvePaths(userPaths, targetPaths, target);
        Set<String> perms = resolvePermissions(resolvedPaths);

        return new Explain(perms, resolvedPaths);
    }

    @Override
    public Set<String> buildPOS(UserContext userCtx) throws PMException {
        // Prepare the hashset to return.
        HashSet<String> hsOa = new HashSet<>();

        // Call find_border_oa_priv(u). The result is a Hashtable
        // htoa = {oa -> {op -> pcset}}:
        Hashtable<String, Hashtable<String, Set<String>>> htOa = findBorderOaPrivRestrictedInternal(userCtx);

        // For each returned oa (key in htOa)
        for (Enumeration<String> oas = htOa.keys(); oas.hasMoreElements(); ) {
            String oa = oas.nextElement();

            // Compute oa's required PCs by calling find_pc_set(oa).
            HashSet<String> hsReqPcs = inMemFindPcSet(oa);
            // Extract oa's label.
            Hashtable<String, Set<String>> htOaLabel = htOa.get(oa);

            // Walk through the op -> pcset of the oa's label.
            // For each operation/access right
            for (Enumeration ops = htOaLabel.keys(); ops.hasMoreElements(); ) {
                String sOp = (String)ops.nextElement();
                // Extract the pcset corresponding to this operation/access right.
                Set<String> hsActualPcs = htOaLabel.get(sOp);
                // if the set of required PCs is a subset of the actual pcset,
                // then user u has some privileges on the current oa node.
                if (hsActualPcs.containsAll(hsReqPcs)) {
                    hsOa.add(oa);
                    break;
                }
            }
        }

        return new HashSet<>(hsOa);
    }

    @Override
    public List<String> getAccessibleChildren(UserContext userCtx, String root) throws PMException {
        List<String> children = policyReader.graph().getChildren(root);
        children.removeIf(child -> {
            try {
                return getAccessRights(userCtx, child).isEmpty();
            } catch (PMException e) {
                e.printStackTrace();
                return true;
            }
        });

        return children;
    }

    @Override
    public List<String> getAccessibleParents(UserContext userCtx, String root) throws PMException {
        List<String> parents = policyReader.graph().getParents(root);
        parents.removeIf(parent -> {
            try {
                return getAccessRights(userCtx, parent).isEmpty();
            } catch (PMException e) {
                e.printStackTrace();
                return true;
            }
        });

        return parents;
    }

    private Hashtable<String, Hashtable<String, Set<String>>> findBorderOaPrivRestrictedInternal(UserContext userCtx) throws PMException {
        // Uses a hashtable htReachableOas of reachable oas (see find_border_oa_priv(u))
        // An oa is a key in this hashtable. The value is another hashtable that
        // represents a label of the oa. A label is a set of pairs {(op -> pcset)}, with
        // the op being the key and pcset being the value.
        // {oa -> {op -> pcset}}.
        Hashtable<String, Hashtable<String, Set<String>>> htReachableOas = new Hashtable<>();

        // BFS from u (the base node). Prepare a queue.
        Set<String> visited = new HashSet<>();
        String crtNode;

        // Get u's directly assigned attributes and put them into the queue.
        List<String> hsAttrs = policyReader.graph().getParents(userCtx.getUser());
        List<String> queue = new ArrayList<>(hsAttrs);

        // While the queue has elements, extract an element from the queue
        // and visit it.
        while (!queue.isEmpty()) {
            // Extract an ua from queue.
            crtNode = queue.remove(0);
            if (!visited.contains(crtNode)) {
                // If the ua has ua -> oa edges
                if (inMemUattrHasOpsets(crtNode)) {
                    // Find the set of PCs reachable from ua.
                    HashSet<String> hsUaPcs = inMemFindPcSet(crtNode);

                    // From each discovered ua traverse the edges ua -> oa.

                    // Find the opsets of this user attribute. Note that the set of containers for this
                    // node (user attribute) may contain not only opsets.
                    List<Association> assocs = policyReader.graph().getAssociationsWithSource(crtNode);

                    // Go through the containers and only for opsets do the following.
                    // For each opset ops of ua:
                    for (Association assoc : assocs) {
                        String target = assoc.getTarget();
                        // If oa is in htReachableOas
                        if (htReachableOas.containsKey(target)) {
                            // Then oa has a label op1 -> hsPcs1, op2 -> hsPcs2,...
                            // Extract its label:
                            Hashtable<String, Set<String>> htOaLabel = htReachableOas.get(target);

                            // Get the operations from the opset:
                            AccessRightSet arSet = assoc.getAccessRightSet();
                            // For each operation in the opset
                            for (String sOp : arSet) {
                                // If the oa's label already contains the operation sOp
                                if (htOaLabel.containsKey(sOp)) {
                                    // The label contains op -> some pcset.
                                    // Do the union of the old pc with ua's pcset
                                    Set<String> hsPcs = htOaLabel.get(sOp);
                                    hsPcs.addAll(hsUaPcs);
                                } else {
                                    // The op is not in the oa's label.
                                    // Create new op -> ua's pcs mappiing in the label.
                                    Set<String> hsNewPcs = new HashSet<>(hsUaPcs);
                                    htOaLabel.put(sOp, hsNewPcs);
                                }
                            }
                        } else {
                            // oa is not in htReachableOas.
                            // Prepare a new label
                            Hashtable<String, Set<String>> htOaLabel = new Hashtable<>();

                            // Get the operations from the opset:
                            AccessRightSet arSet = assoc.getAccessRightSet();
                            // For each operation in the opset
                            for (String sOp : arSet) {
                                // Add op -> pcs to the label.
                                Set<String> hsNewPcs = new HashSet<>(hsUaPcs);
                                htOaLabel.put(sOp, hsNewPcs);
                            }

                            // Add oa -> {op -> pcs}
                            htReachableOas.put(target,  htOaLabel);
                        }
                    }
                }
                visited.add(crtNode);

                List<String> hsDescs = policyReader.graph().getParents(crtNode);
                queue.addAll(hsDescs);
            }
        }


        // For each reachable oa in htReachableOas.keys
        for (Enumeration<String> keys = htReachableOas.keys(); keys.hasMoreElements() ;) {
            String oa = keys.nextElement();
            // Compute {pc | oa ->+ pc}
            Set<String> hsOaPcs = inMemFindPcSet(oa);
            // Extract oa's label.
            Hashtable<String, Set<String>> htOaLabel = htReachableOas.get(oa);
            // The label contains op1 -> pcs1, op2 -> pcs2,...
            // For each operation in the label
            for (Enumeration<String> lbl = htOaLabel.keys(); lbl.hasMoreElements();) {
                String sOp = lbl.nextElement();
                // Intersect the pcset corresponding to this operation,
                // which comes from the uas, with the oa's pcset.
                Set<String> oaPcs = htOaLabel.get(sOp);
                oaPcs.retainAll(hsOaPcs);
                if (oaPcs.isEmpty()) htOaLabel.remove(sOp);
            }
        }

        return htReachableOas;
    }

    private HashSet<String> inMemFindPcSet(String node) throws PMException {
        HashSet<String> reachable = new HashSet<>();

        // Init the queue, visited
        ArrayList<String> queue = new ArrayList<>();
        HashSet<String> visited = new HashSet<>();

        // The current element
        String crtNode;

        // Insert the start node into the queue
        queue.add(node);

        List<String> policyClasses = policyReader.graph().getPolicyClasses();

        // While queue is not empty
        while (!queue.isEmpty()) {
            // Extract current element from queue
            crtNode = queue.remove(0);
            // If not visited
            if (!visited.contains(crtNode)) {
                // Mark it as visited
                visited.add(crtNode);
                // Extract its direct descendants. If a descendant is an attribute,
                // insert it into the queue. If it is a pc, add it to reachable,
                // if not already there
                List<String> hsContainers = policyReader.graph().getParents(crtNode);
                for (String n : hsContainers) {
                    if (policyClasses.contains(n)) {
                        reachable.add(n);
                    } else {
                        queue.add(n);
                    }
                }
            }
        }
        return reachable;
    }

    private boolean inMemUattrHasOpsets(String uaNode) throws PMException {
        return !policyReader.graph().getAssociationsWithSource(uaNode).isEmpty();
    }

    private Set<String> resolvePermissions(Map<String, PolicyClass> paths) throws PMException {
        Map<String, AccessRightSet> pcPerms = new HashMap<>();
        for (String pc : paths.keySet()) {
            PolicyClass pcPaths = paths.get(pc);
            for(Path p : pcPaths.getPaths()) {
                AccessRightSet ops = p.getAssociation().getAccessRightSet();
                AccessRightSet existingOps = pcPerms.getOrDefault(pc, new AccessRightSet());
                existingOps.addAll(ops);
                pcPerms.put(pc, existingOps);
            }
        }

        return resolveAllowedPermissions(pcPerms, policyReader.graph().getResourceAccessRights());
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
    private Map<String, PolicyClass> resolvePaths(List<EdgePath> userPaths, List<EdgePath> targetPaths, String target) throws PMException {
        Map<String, PolicyClass> results = new HashMap<>();

        for (EdgePath targetPath : targetPaths) {
            Relationship pcEdge = targetPath.getEdges().get(targetPath.getEdges().size()-1);

            // if the last element in the target path is a pc, the target belongs to that pc, add the pc to the results
            // skip to the next target path if it is not a policy class
            if (!isPolicyClass(pcEdge.getTarget())) {
                continue;
            }

            PolicyClass policyClass = results.getOrDefault(pcEdge.getTarget(), new PolicyClass());

            // compute the paths for this target path
            Set<Path> paths = computePaths(userPaths, targetPath, target);

            // add all paths
            Set<Path> existingPaths = policyClass.getPaths();
            existingPaths.addAll(paths);

            // collect all ops
            for (Path p : paths) {
                policyClass.getOperations().addAll(p.getAssociation().getAccessRightSet());
            }

            // update results
            results.put(pcEdge.getTarget(), policyClass);
        }

        return results;
    }

    private boolean isPolicyClass(String node) throws PMException {
        return policyReader.graph().getPolicyClasses().contains(node);
    }

    private Set<Path> computePaths(List<EdgePath> userEdgePaths, EdgePath targetEdgePath, String target) {
        Set<Path> computedPaths = new HashSet<>();

        for(EdgePath userEdgePath : userEdgePaths) {
            Relationship lastUserEdge = userEdgePath.getEdges().get(userEdgePath.getEdges().size()-1);

            // if the last edge does not have any ops, it is not an association, so ignore it
            if (!lastUserEdge.isAssociation()) {
                continue;
            }

            for(int i = 0; i < targetEdgePath.getEdges().size(); i++) {
                Relationship curEdge = targetEdgePath.getEdges().get(i);
                // if the target of the last edge in a user resolvedPath does not match the target of the current edge in the target
                // resolvedPath, continue to the next target edge
                String lastUserEdgeTarget = lastUserEdge.getTarget();
                String curEdgeSource = curEdge.getSource();
                String curEdgeTarget = curEdge.getTarget();

                // if the target of the last edge in a user path does not match the target of the current edge in the target path
                // AND if the target of the last edge in a user path does not match the source of the current edge in the target path
                //     OR if the target of the last edge in a user path does not match the target of the explain
                // continue to the next target edge
                if((!lastUserEdgeTarget.equals(curEdgeTarget)) &&
                        (!lastUserEdgeTarget.equals(curEdgeSource) || lastUserEdgeTarget.equals(target))) {
                    continue;
                }

                List<String> userPathToAssociation = userEdgePath.toPath();
                List<String> targetPathToPolicyClass = targetEdgePath.toPath();


                Path path = new Path(userPathToAssociation, targetPathToPolicyClass,
                        new Association(lastUserEdge.getSource(), lastUserEdgeTarget, lastUserEdge.getAccessRightSet()));

                computedPaths.add(path);
            }
        }

        return computedPaths;
    }

    private List<EdgePath> dfs(String start) throws PMException {
        final List<EdgePath> paths = new ArrayList<>();
        final Map<String, List<EdgePath>> propPaths = new HashMap<>();

        Visitor visitor = nodeName -> {
            Node node = policyReader.graph().getNode(nodeName);
            List<EdgePath> nodePaths = new ArrayList<>();

            for(String parent : policyReader.graph().getParents(nodeName)) {
                Relationship edge = new Relationship(node.getName(), parent);
                List<EdgePath> parentPaths = propPaths.get(parent);
                if(parentPaths.isEmpty()) {
                    EdgePath path = new EdgePath();
                    path.addEdge(edge);
                    nodePaths.add(0, path);
                } else {
                    for(EdgePath p : parentPaths) {
                        EdgePath parentPath = new EdgePath();
                        for(Relationship e : p.getEdges()) {
                            parentPath.addEdge(new Relationship(e.getSource(), e.getTarget(), e.getAccessRightSet()));
                        }

                        parentPath.getEdges().add(0, edge);
                        nodePaths.add(parentPath);
                    }
                }
            }

            List<Association> assocs = policyReader.graph().getAssociationsWithSource(node.getName());
            for(Association association : assocs) {
                Node targetNode = policyReader.graph().getNode(association.getTarget());
                EdgePath path = new EdgePath();
                path.addEdge(new Relationship(node.getName(), targetNode.getName(), association.getAccessRightSet()));
                nodePaths.add(path);
            }

            // if the node being visited is the start node, add all the found nodePaths
            // TODO there might be a more efficient way of doing this
            // we don't need the if for users, only when the target is an OA, so it might have something to do with
            // leafs vs non leafs
            if (node.getName().equals(start)) {
                paths.clear();
                paths.addAll(nodePaths);
            } else {
                propPaths.put(node.getName(), nodePaths);
            }
        };

        Propagator propagator = (parentNodeName, childNodeName) -> {
            Node parentNode = policyReader.graph().getNode(parentNodeName);
            Node childNode = policyReader.graph().getNode(childNodeName);
            List<EdgePath> childPaths = propPaths.computeIfAbsent(childNode.getName(), k -> new ArrayList<>());
            List<EdgePath> parentPaths = propPaths.get(parentNode.getName());

            for(EdgePath p : parentPaths) {
                EdgePath path = new EdgePath();
                for(Relationship edge : p.getEdges()) {
                    path.addEdge(new Relationship(edge.getSource(), edge.getTarget(), edge.getAccessRightSet()));
                }

                EdgePath newPath = new EdgePath();
                newPath.getEdges().addAll(path.getEdges());
                Relationship edge = new Relationship(childNode.getName(), parentNode.getName(), null);
                newPath.getEdges().add(0, edge);
                childPaths.add(newPath);
                propPaths.put(childNode.getName(), childPaths);
            }

            if (childNode.getName().equals(start)) {
                paths.clear();
                paths.addAll(propPaths.get(childNode.getName()));
            }
        };

        new DepthFirstGraphWalker(policyReader.graph())
                .withVisitor(visitor)
                .withPropagator(propagator)
                .withDirection(Direction.PARENTS)
                .walk(start);

        return paths;
    }

    @Override
    public synchronized List<String> getAttributeContainers(String node) throws PMException {
        List<String> attrs = new ArrayList<>();

        new DepthFirstGraphWalker(policyReader.graph())
                .withDirection(Direction.PARENTS)
                .withVisitor((n) -> {
                    Node visitedNode;
                    visitedNode = policyReader.graph().getNode(n);
                    if (visitedNode.getType().equals(UA) ||
                            visitedNode.getType().equals(OA)) {
                        attrs.add(n);
                    }
                })
                .walk(node);

        return attrs;
    }

    @Override
    public synchronized List<String> getPolicyClassContainers(String node) throws PMException {
        List<String> attrs = new ArrayList<>();

        new DepthFirstGraphWalker(policyReader.graph())
                .withDirection(Direction.PARENTS)
                .withVisitor((n) -> {
                    Node visitedNode;
                    visitedNode = policyReader.graph().getNode(n);
                    if (visitedNode.getType().equals(PC)) {
                        attrs.add(n);
                    }
                })
                .walk(node);

        return attrs;
    }

    @Override
    public synchronized boolean isContained(String subject, String container) throws PMException {
        if (!policyReader.graph().nodeExists(subject)) {
            throw new NodeDoesNotExistException(subject);
        } else if (!policyReader.graph().nodeExists(container)){
            throw new NodeDoesNotExistException(container);
        }

        AtomicBoolean found = new AtomicBoolean(false);

        new DepthFirstGraphWalker(policyReader.graph())
                .withDirection(Direction.PARENTS)
                .withVisitor((n) -> {
                    if (n.equals(container)) {
                        found.set(true);
                    }
                })
                .walk(subject);

        return found.get();
    }

    @Override
    public synchronized List<Prohibition> getInheritedProhibitionsFor(String subject) throws PMException {
        List<Prohibition> pros = new ArrayList<>();

        new DepthFirstGraphWalker(policyReader.graph())
                .withVisitor((n) -> {
                    pros.addAll(policyReader.prohibitions().getAll().get(n));
                })
                .withDirection(Direction.PARENTS)
                .walk(subject);

        return pros;
    }

    @Override
    public List<Prohibition> getProhibitionsWithContainer(String container) throws PMException {
        List<Prohibition> pros = new ArrayList<>();

        Map<String, List<Prohibition>> prohibitions = policyReader.prohibitions().getAll();
        for (String subject : prohibitions.keySet()) {
            List<Prohibition> subjectProhibitions = prohibitions.get(subject);
            for (Prohibition prohibition : subjectProhibitions) {
                if (prohibition.getContainers().contains(new ContainerCondition(container, false))) {
                    pros.add(prohibition);
                }
            }
        }

        return pros;
    }

    @Override
    public synchronized List<Obligation> getObligationsWithAuthor(UserContext userCtx) throws PMException {
        List<Obligation> obls = new ArrayList<>();
        for (Obligation obligation : policyReader.obligations().getAll()) {
            if (obligation.getAuthor().equals(userCtx)) {
                obls.add(obligation);
            }
        }

        return obls;
    }

    @Override
    public synchronized List<Obligation> getObligationsWithAttributeInEvent(String attribute) throws PMException {
        List<Obligation> obls = new ArrayList<>();
        for (Obligation obligation : policyReader.obligations().getAll()) {
            List<Rule> rules = obligation.getRules();
            for (Rule rule : rules) {
                Target target = rule.getEvent().getTarget();
                if (target.getType() == Target.Type.POLICY_ELEMENT) {
                    if (target.policyElement().equals(attribute)) {
                        obls.add(obligation);
                    }
                } else if (target.getType() == Target.Type.ANY_POLICY_ELEMENT) {
                    obls.add(obligation);
                } else if (target.getType() == Target.Type.ANY_CONTAINED_IN) {
                    if (getAttributeContainers(attribute).contains(target.anyContainedIn())) {
                        obls.add(obligation);
                    }
                } else if (target.getType() == Target.Type.ANY_OF_SET) {
                    if (target.anyOfSet().contains(attribute)) {
                        obls.add(obligation);
                    }
                }
            }
        }

        return obls;
    }

    @Override
    public synchronized List<Obligation> getObligationsWithAttributeInResponse(String attribute) throws PMException {
        List<Obligation> obls = new ArrayList<>();
        for (Obligation obligation : policyReader.obligations().getAll()) {
            List<Rule> rules = obligation.getRules();
            for (Rule rule : rules) {
                Response response = rule.getResponse();
                for (PALStatement statement : response.getStatements()) {
                    Pattern p = Pattern.compile("\\b(" + attribute + ")\\b");
                    Matcher m = p.matcher(statement.toString());
                    if (m.find()) {
                        obls.add(obligation);
                    }
                }
            }
        }

        return obls;
    }

    @Override
    public synchronized List<Obligation> getObligationsWithEvent(String event) throws PMException {
        List<Obligation> obls = new ArrayList<>();
        for (Obligation obligation : policyReader.obligations().getAll()) {
            List<Rule> rules = obligation.getRules();
            for (Rule rule : rules) {
                if (rule.getEvent().getOperations().contains(event)) {
                    obls.add(obligation);
                }
            }
        }

        return obls;
    }
    @Override
    public synchronized List<Response> getMatchingEventResponses(EventContext evt) throws PMException {
        List<Response> responses = new ArrayList<>();
        for (Obligation obligation : policyReader.obligations().getAll()) {
            for (Rule rule : obligation.getRules()) {
                if (evt.matchesPattern(rule.getEvent(), this)) {
                    responses.add(rule.getResponse());
                }
            }
        }

        return responses;
    }
}
