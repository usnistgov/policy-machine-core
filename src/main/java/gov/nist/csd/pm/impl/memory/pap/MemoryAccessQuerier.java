package gov.nist.csd.pm.impl.memory.pap;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.dag.*;
import gov.nist.csd.pm.pap.graph.node.Node;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.graph.relationship.Association;
import gov.nist.csd.pm.pap.prohibition.ContainerCondition;
import gov.nist.csd.pm.pap.prohibition.Prohibition;
import gov.nist.csd.pm.pap.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.pap.AccessQuerier;
import gov.nist.csd.pm.pap.GraphQuerier;
import gov.nist.csd.pm.pap.ProhibitionsQuerier;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.query.explain.*;
import gov.nist.csd.pm.pap.store.GraphStoreBFS;
import gov.nist.csd.pm.pap.store.PolicyStore;

import java.util.*;

import static gov.nist.csd.pm.pap.graph.node.NodeType.U;
import static gov.nist.csd.pm.pap.graph.node.Properties.NO_PROPERTIES;
import static gov.nist.csd.pm.pap.AccessRightResolver.*;

public class MemoryAccessQuerier extends AccessQuerier {

    private GraphQuerier graphQuerier;
    private ProhibitionsQuerier prohibitionsQuerier;

    public MemoryAccessQuerier(PolicyStore memoryPolicyStore, GraphQuerier graphQuerier, ProhibitionsQuerier prohibitionsQuerier) {
        super(memoryPolicyStore);
        this.graphQuerier = graphQuerier;
        this.prohibitionsQuerier = prohibitionsQuerier;
    }

    @Override
    public AccessRightSet computePrivileges(UserContext userCtx, String target) throws PMException  {
        AccessRightSet accessRights = new AccessRightSet();

        // traverse the user side of the graph to get the associations
        UserDagResult userDagResult = processUserDAG(userCtx.getUser(), userCtx.getProcess());
        if (userDagResult.borderTargets().isEmpty()) {
            return accessRights;
        }

        // traverse the target side of the graph to get permissions per policy class
        TargetDagResult targetDagResult = processTargetDAG(target, userDagResult);

        // resolve the permissions
        return resolvePrivileges(userDagResult, targetDagResult, target, store.operations().getResourceOperations());
    }

    @Override
    public AccessRightSet computeDeniedPrivileges(UserContext userCtx, String target) throws PMException {
        AccessRightSet accessRights = new AccessRightSet();

        // traverse the user side of the graph to get the associations
        UserDagResult userDagResult = processUserDAG(userCtx.getUser(), userCtx.getProcess());
        if (userDagResult.borderTargets().isEmpty()) {
            return accessRights;
        }

        // traverse the target side of the graph to get permissions per policy class
        TargetDagResult targetDagResult = processTargetDAG(target, userDagResult);

        // resolve the permissions
        return resolveDeniedAccessRights(userDagResult, targetDagResult, target);
    }

    @Override
    public Map<String, AccessRightSet> computePolicyClassAccessRights(UserContext userCtx, String target) throws PMException {
        // traverse the user side of the graph to get the associations
        UserDagResult userDagResult = processUserDAG(userCtx.getUser(), userCtx.getProcess());
        if (userDagResult.borderTargets().isEmpty()) {
            return new HashMap<>();
        }

        // traverse the target side of the graph to get permissions per policy class
        TargetDagResult targetDagResult = processTargetDAG(target, userDagResult);

        return targetDagResult.pcSet();
    }

    @Override
    public Map<String, AccessRightSet> computeCapabilityList(UserContext userCtx) throws PMException {
        Map<String, AccessRightSet> results = new HashMap<>();

        //get border nodes.  Can be OA or UA.  Return empty set if no attrs are reachable
        UserDagResult userDagResult = processUserDAG(userCtx.getUser(), userCtx.getProcess());
        if (userDagResult.borderTargets().isEmpty()) {
            return results;
        }

        for(String borderTarget : userDagResult.borderTargets().keySet()) {
            // compute permissions on the border attr
            getAndStorePrivileges(results, userDagResult, borderTarget);

            // compute decisions for the subgraph of the border attr
            Set<String> descendants = getDescendants(borderTarget);
            for (String descendant : descendants) {
                if (results.containsKey(descendant)) {
                    continue;
                }

                getAndStorePrivileges(results, userDagResult, descendant);
            }
        }

        return results;
    }

    @Override
    public Map<String, AccessRightSet> computeACL(String target) throws PMException {
        Map<String, AccessRightSet> acl = new HashMap<>();
        Collection<String> search = graphQuerier.search(U, NO_PROPERTIES);
        for (String user : search) {
            AccessRightSet list = this.computePrivileges(new UserContext(user), target);
            acl.put(user, list);
        }

        return acl;
    }

    @Override
    public Map<String, AccessRightSet> computeDestinationAttributes(String user) throws PMException {
        return processUserDAG(user, UserContext.NO_PROCESS)
                .borderTargets();
    }

    @Override
    public Map<String, AccessRightSet> computeAscendantPrivileges(UserContext userCtx, String root) throws PMException {
        Map<String, AccessRightSet> results = new HashMap<>();

        UserDagResult userDagResult = processUserDAG(userCtx.getUser(), userCtx.getProcess());
        if (userDagResult.borderTargets().isEmpty()) {
            return results;
        }

        Set<String> descendants = getDescendants(root);
        for (String descendant : descendants) {
            if (results.containsKey(descendant)) {
                continue;
            }

            getAndStorePrivileges(results, userDagResult, descendant);
        }

        return results;
    }

    @Override
    public Explain explain(UserContext userCtx, String target) throws PMException {
        Node userNode = graphQuerier.getNode(userCtx.getUser());
        Node targetNode = graphQuerier.getNode(target);

        Map<String, Map<Path, List<Association>>> targetPaths = explainTarget(targetNode.getName());
        Map<String, Set<Path>> userPaths = explainUser(userNode.getName(), targetPaths);
        List<PolicyClassExplain> resolvedPaths = resolvePaths(targetPaths, userPaths);

        UserDagResult userDagResult = processUserDAG(userCtx.getUser(), userCtx.getProcess());
        TargetDagResult targetDagResult = processTargetDAG(target, userDagResult);

        AccessRightSet priv = resolvePrivileges(userDagResult, targetDagResult, target, store.operations().getResourceOperations());
        AccessRightSet deniedPriv = resolveDeniedAccessRights(userDagResult, targetDagResult, target);
        List<Prohibition> prohibitions = computeSatisfiedProhibitions(userDagResult, targetDagResult, target);

        return new Explain(priv, resolvedPaths, deniedPriv, prohibitions);
    }

    @Override
    public Collection<String> computePersonalObjectSystem(UserContext userCtx) throws PMException {
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
    public Collection<String> computeAccessibleAscendants(UserContext userCtx, String root) throws PMException {
        List<String> ascendants = new ArrayList<>(graphQuerier.getAdjacentAscendants(root));
        ascendants.removeIf(ascendant -> {
            try {
                return computePrivileges(userCtx, ascendant).isEmpty();
            } catch (PMException e) {
                e.printStackTrace();
                return true;
            }
        });

        return ascendants;
    }

    @Override
    public Collection<String> computeAccessibleDescendants(UserContext userCtx, String root) throws PMException {
        List<String> descs = new ArrayList<>(graphQuerier.getAdjacentDescendants(root));
        descs.removeIf(desc -> {
            try {
                return computePrivileges(userCtx, desc).isEmpty();
            } catch (PMException e) {
                e.printStackTrace();
                return true;
            }
        });

        return descs;
    }

    private void getAndStorePrivileges(Map<String, AccessRightSet> arsetMap, UserDagResult userDagResult, String target) throws PMException {
        TargetDagResult targetCtx = processTargetDAG(target, userDagResult);
        AccessRightSet privileges = resolvePrivileges(userDagResult, targetCtx, target, store.operations().getResourceOperations());
        arsetMap.put(target, privileges);
    }

    /**
     * Perform a depth first search on the object side of the graph.  Start at the target node and recursively visit nodes
     * until a policy class is reached.  On each node visited, collect any operation the user has on the target. At the
     * end of each dfs iteration the visitedNodes map will contain the operations the user is permitted on the target under
     * each policy class.
     */
    protected TargetDagResult processTargetDAG(String target, UserDagResult userCtx) throws PMException {
        if (!graphQuerier.nodeExists(target)) {
            throw new NodeDoesNotExistException(target);
        }

        Collection<String> policyClasses = graphQuerier.getPolicyClasses();
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

            if (policyClasses.contains(node)) {
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

        Propagator propagator = (desc, asc) -> {
            Map<String, AccessRightSet> descCtx = visitedNodes.get(desc);
            Map<String, AccessRightSet> nodeCtx = visitedNodes.getOrDefault(asc, new HashMap<>());
            for (String name : descCtx.keySet()) {
                AccessRightSet ops = nodeCtx.getOrDefault(name, new AccessRightSet());
                ops.addAll(descCtx.get(name));
                nodeCtx.put(name, ops);
            }
            visitedNodes.put(asc, nodeCtx);
        };

        new DepthFirstGraphWalker(graphQuerier)
                .withDirection(Direction.DESCENDANTS)
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
    protected UserDagResult processUserDAG(String subject, String process) throws PMException  {
        if (!graphQuerier.nodeExists(subject)) {
            throw new NodeDoesNotExistException(subject);
        }

        final Map<String, AccessRightSet> borderTargets = new HashMap<>();
        final Set<String> prohibitionTargets = new HashSet<>();
        // initialize with the prohibitions or the provided process
        final Set<Prohibition> reachedProhibitions = new HashSet<>(prohibitionsQuerier.getProhibitionsWithSubject(process));

        // get the associations for the subject, it the subject is a user, nothing will be returned
        // this is only when a UA is the subject
        Collection<Association> subjectAssociations = graphQuerier.getAssociationsWithSource(subject);
        collectAssociationsFromBorderTargets(subjectAssociations, borderTargets);

        Visitor visitor = node -> {
            Collection<Prohibition> subjectProhibitions = prohibitionsQuerier.getProhibitionsWithSubject(node);
            reachedProhibitions.addAll(subjectProhibitions);
            for (Prohibition prohibition : subjectProhibitions) {
                Collection<ContainerCondition> containers = prohibition.getContainers();
                for (ContainerCondition cont : containers) {
                    prohibitionTargets.add(cont.getName());
                }
            }

            Collection<Association> nodeAssociations = graphQuerier.getAssociationsWithSource(node);
            collectAssociationsFromBorderTargets(nodeAssociations, borderTargets);
        };

        // start the bfs
        new GraphStoreBFS(store.graph())
                .withDirection(Direction.DESCENDANTS)
                .withVisitor(visitor)
                .walk(subject);

        return new UserDagResult(borderTargets, reachedProhibitions, prohibitionTargets);
    }

    private List<PolicyClassExplain> resolvePaths(Map<String, Map<Path, List<Association>>> targetPaths,
                                                         Map<String, Set<Path>> userPaths) {
        List<PolicyClassExplain> result = new ArrayList<>();

        for (Map.Entry<String, Map<Path, List<Association>>> targetPathEntry : targetPaths.entrySet()) {
            String pc = targetPathEntry.getKey();
            Map<Path, List<Association>> targetPathAssociations = targetPathEntry.getValue();

            List<List<ExplainNode>> paths = getExplainNodePaths(targetPathAssociations, userPaths);
            AccessRightSet arset = getArsetFromPaths(paths);

            result.add(new PolicyClassExplain(pc, arset, paths));
        }

        return result;
    }

    private List<List<ExplainNode>> getExplainNodePaths(Map<Path, List<Association>> targetPathAssociations,
                                                        Map<String, Set<Path>> userPaths) {
        List<List<ExplainNode>> paths = new ArrayList<>();

        for (Map.Entry<Path, List<Association>> targetPathEntry : targetPathAssociations.entrySet()) {
            Path path = targetPathEntry.getKey();
            List<Association> pathAssocs = targetPathEntry.getValue();

            List<ExplainNode> explainNodes = new ArrayList<>();
            for (String node : path) {
                List<ExplainAssociation> explainAssocs = new ArrayList<>();

                for (Association pathAssoc : pathAssocs) {
                    String ua = pathAssoc.getSource();
                    String target = pathAssoc.getTarget();
                    if (!target.equals(node)) {
                        continue;
                    }

                    Set<Path> userPathsToAssoc = userPaths.getOrDefault(ua, new HashSet<>());

                    explainAssocs.add(new ExplainAssociation(
                            ua,
                            pathAssoc.getAccessRightSet(),
                            new ArrayList<>(userPathsToAssoc)
                    ));
                }

                explainNodes.add(new ExplainNode(node, explainAssocs));
            }

            paths.add(explainNodes);
        }

        return paths;
    }

    private AccessRightSet getArsetFromPaths(List<List<ExplainNode>> paths) {
        AccessRightSet accessRightSet = new AccessRightSet();
        for (List<ExplainNode> path : paths) {
            for (ExplainNode explainNode : path) {
                List<ExplainAssociation> associations = explainNode.associations();
                for (ExplainAssociation association : associations) {
                    if (association.userPaths().isEmpty()) {
                        continue;
                    }

                    accessRightSet.addAll(association.arset());
                }
            }
        }

        return accessRightSet;
    }

    private Map<String, Map<Path, List<Association>>> explainTarget(String target) throws PMException {
        Collection<String> policyClasses = graphQuerier.getPolicyClasses();

        // initialize map with policy classes
        Map<String, Map<List<String>, List<Association>>> pcPathAssociations = new HashMap<>();
        for (String pc : policyClasses) {
            pcPathAssociations.put(pc, new HashMap<>(Map.of(new ArrayList<>(List.of(pc)), new ArrayList<>())));
        }

        Propagator propagator = (src, dst) -> {
            Map<List<String>, List<Association>> srcPathAssocs = pcPathAssociations.get(src);
            Map<List<String>, List<Association>> dstPathAssocs = pcPathAssociations.getOrDefault(dst, new HashMap<>());

            for (Map.Entry<List<String>, List<Association>> entry : srcPathAssocs.entrySet()) {
                // add DST to the path from SRC
                List<String> targetPath = new ArrayList<>(entry.getKey());
                List<Association> associations = new ArrayList<>(entry.getValue());
                targetPath.add(0, dst);

                // collect any associations for the DST node
                Collection<Association> associationsWithTarget = graphQuerier.getAssociationsWithTarget(dst);
                associations.addAll(associationsWithTarget);
                dstPathAssocs.put(targetPath, associations);
            }

            // update dst entry
            pcPathAssociations.put(dst, dstPathAssocs);
        };

        // DFS from target node
        new DepthFirstGraphWalker(graphQuerier)
                .withPropagator(propagator)
                .walk(target);

        // convert the map created above into a map where the policy classes are the keys
        Map<List<String>, List<Association>> targetPathAssocs = pcPathAssociations.get(target);
        Map<String, Map<Path, List<Association>>> pcMap = new HashMap<>();
        for (Map.Entry<List<String>, List<Association>> entry : targetPathAssocs.entrySet()) {
            Path targetPath = new Path(entry.getKey());
            List<Association> associations = new ArrayList<>(entry.getValue());

            String pc = targetPath.get(targetPath.size()-1);

            Map<Path, List<Association>> pcPathAssocs = pcMap.getOrDefault(pc, new HashMap<>());
            pcPathAssocs.put(targetPath, associations);
            pcMap.put(pc, pcPathAssocs);
        }

        return pcMap;
    }

    private Map<String, Set<Path>> explainUser(String user, Map<String, Map<Path, List<Association>>> targetPaths) throws PMException {
        // initialize map with the UAs of the target path associations
        Set<String> uasFromTargetPathAssociations = new HashSet<>(getUAsFromTargetPathAssociations(targetPaths));
        Map<String, Set<Path>> pathsToUAs = new HashMap<>();
        for (String ua : uasFromTargetPathAssociations) {
            pathsToUAs.put(ua, new HashSet<>(Set.of(new Path(ua))));
        }

        Propagator propagator = (src, dst) -> {
            // don't propagate unless the src is a ua in a target path association or an already propagated to dst node
            if (!uasFromTargetPathAssociations.contains(src) && !pathsToUAs.containsKey(src)) {
                return;
            }

            Set<Path> srcPaths = pathsToUAs.get(src);
            Set<Path> dstPaths = pathsToUAs.getOrDefault(dst, new HashSet<>());

            for (Path srcPath : srcPaths) {
                Path copy = new Path(srcPath);
                copy.add(0, dst);
                dstPaths.add(copy);
            }

            pathsToUAs.put(dst, dstPaths);
        };

        new DepthFirstGraphWalker(graphQuerier)
                .withPropagator(propagator)
                .walk(user);

        // transform the map so that the key is the last ua in the path pointing to it's paths
        Set<Path> userPaths = pathsToUAs.getOrDefault(user, new HashSet<>());
        Map<String, Set<Path>> associationUAPaths = new HashMap<>();
        for (Path userPath : userPaths) {
            String assocUA = userPath.get(userPath.size()-1);
            Set<Path> assocUAPaths = associationUAPaths.getOrDefault(assocUA, new HashSet<>());
            assocUAPaths.add(userPath);
            associationUAPaths.put(assocUA, assocUAPaths);
        }

        return associationUAPaths;
    }

    private List<String> getUAsFromTargetPathAssociations(Map<String, Map<Path, List<Association>>> targetPaths) {
        List<String> uas = new ArrayList<>();

        for (Map.Entry<String, Map<Path, List<Association>>> pcPaths : targetPaths.entrySet()) {
            for (Map.Entry<Path, List<Association>> pathAssociations : pcPaths.getValue().entrySet()) {
                List<Association> associations = pathAssociations.getValue();
                for (Association association : associations) {
                    uas.add(association.getSource());
                }
            }
        }

        return uas;
    }

    private void collectAssociationsFromBorderTargets(Collection<Association> assocs, Map<String, AccessRightSet> borderTargets) {
        for (Association association : assocs) {
            AccessRightSet ops = association.getAccessRightSet();
            AccessRightSet exOps = borderTargets.getOrDefault(association.getTarget(), new AccessRightSet());
            //if the target is not in the map already, put it
            //else add the found operations to the existing ones.
            exOps.addAll(ops);
            borderTargets.put(association.getTarget(), exOps);
        }
    }

    private Set<String> getDescendants(String vNode) throws PMException {
        Set<String> descendants = new HashSet<>();

        Collection<String> ascendants = graphQuerier.getAdjacentAscendants(vNode);
        if (ascendants.isEmpty()) {
            return descendants;
        }

        descendants.addAll(ascendants);
        for (String ascendant : ascendants) {
            descendants.add(ascendant);
            descendants.addAll(getDescendants(ascendant));
        }

        return descendants;
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
        Collection<String> hsAttrs = graphQuerier.getAdjacentDescendants(userCtx.getUser());
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
                    Collection<Association> assocs = graphQuerier.getAssociationsWithSource(crtNode);

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

                Collection<String> hsDescs = graphQuerier.getAdjacentDescendants(crtNode);
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

        Collection<String> policyClasses = graphQuerier.getPolicyClasses();

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
                Collection<String> hsContainers = graphQuerier.getAdjacentDescendants(crtNode);
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
        return !graphQuerier.getAssociationsWithSource(uaNode).isEmpty();
    }
}
