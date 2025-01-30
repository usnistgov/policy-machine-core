package gov.nist.csd.pm.impl.memory.pap.access;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.dag.Direction;
import gov.nist.csd.pm.common.graph.dag.TargetDagResult;
import gov.nist.csd.pm.common.graph.dag.UserDagResult;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.AccessQuerier;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.pap.query.model.explain.*;
import gov.nist.csd.pm.pap.query.model.subgraph.SubgraphPrivileges;
import gov.nist.csd.pm.pap.store.GraphStoreBFS;
import gov.nist.csd.pm.pap.store.PolicyStore;

import java.util.*;
import java.util.stream.LongStream;

import static gov.nist.csd.pm.pap.admin.AdminPolicyNode.PM_ADMIN_OBJECT;
import static gov.nist.csd.pm.pap.AccessRightResolver.*;
import static gov.nist.csd.pm.common.graph.node.NodeType.U;
import static gov.nist.csd.pm.common.graph.node.Properties.NO_PROPERTIES;

public class MemoryAccessQuerier extends AccessQuerier {
    
    public MemoryAccessQuerier(PolicyStore memoryPolicyStore) {
        super(memoryPolicyStore);
    }

    @Override
    public AccessRightSet computePrivileges(UserContext userCtx, TargetContext targetCtx) throws PMException {
        // traverse the user side of the graph to get the associations
        MemoryUserEvaluator userEvaluator = new MemoryUserEvaluator(store);
        UserDagResult userDagResult = userEvaluator.evaluate(userCtx);

        // traverse the target side of the graph to get permissions per policy class
        MemoryTargetEvaluator targetEvaluator = new MemoryTargetEvaluator(store);
        TargetDagResult targetDagResult = targetEvaluator.evaluate(userDagResult, targetCtx);

        // resolve the permissions
        return resolvePrivileges(userDagResult, targetDagResult, store.operations().getResourceOperations());
    }

    @Override
    public List<AccessRightSet> computePrivileges(UserContext userCtx, List<TargetContext> targetCtxs) throws PMException {
        // traverse the user side of the graph to get the associations
        MemoryUserEvaluator userEvaluator = new MemoryUserEvaluator(store);
        UserDagResult userDagResult = userEvaluator.evaluate(userCtx);

        // traverse the target side of the graph to get permissions per policy class
        MemoryTargetEvaluator targetEvaluator = new MemoryTargetEvaluator(store);

        List<AccessRightSet> accessRightSets = new ArrayList<>();
        for (TargetContext targetCtx : targetCtxs) {
            TargetDagResult targetDagResult = targetEvaluator.evaluate(userDagResult, targetCtx);
            AccessRightSet privs = resolvePrivileges(userDagResult, targetDagResult, store.operations().getResourceOperations());

            accessRightSets.add(privs);
        }

        return accessRightSets;
    }

    @Override
    public AccessRightSet computeDeniedPrivileges(UserContext userCtx, TargetContext targetCtx) throws PMException {
        AccessRightSet accessRights = new AccessRightSet();

        // traverse the user side of the graph to get the associations
        MemoryUserEvaluator userEvaluator = new MemoryUserEvaluator(store);
        UserDagResult userDagResult = userEvaluator.evaluate(userCtx);
        if (userDagResult.borderTargets().isEmpty()) {
            return accessRights;
        }

        // traverse the target side of the graph to get permissions per policy class
        MemoryTargetEvaluator targetEvaluator = new MemoryTargetEvaluator(store);
        TargetDagResult targetDagResult = targetEvaluator.evaluate(userDagResult, targetCtx);

        // resolve the permissions
        return resolveDeniedAccessRights(userDagResult, targetDagResult);
    }

    @Override
    public Map<Long, AccessRightSet> computeCapabilityList(UserContext userCtx) throws PMException {
        Map<Long, AccessRightSet> results = new HashMap<>();

        //get border nodes.  Can be OA or UA.  Return empty set if no attrs are reachable
        MemoryUserEvaluator userEvaluator = new MemoryUserEvaluator(store);
        UserDagResult userDagResult = userEvaluator.evaluate(userCtx);
        if (userDagResult.borderTargets().isEmpty()) {
            return results;
        }

        for(long borderTarget : userDagResult.borderTargets().keySet()) {
            // compute permissions on the border attr
            getAndStorePrivileges(results, userDagResult, borderTarget);

            // compute decisions for the subgraph of the border attr
            Set<Long> descendants = getDescendants(borderTarget);
            for (long descendant : descendants) {
                if (results.containsKey(descendant)) {
                    continue;
                }

                getAndStorePrivileges(results, userDagResult, descendant);
            }
        }

        // add policy classes
        if (results.containsKey(PM_ADMIN_OBJECT.nodeId())) {
            AccessRightSet arset = results.get(PM_ADMIN_OBJECT.nodeId());
            for (long pc : store.graph().getPolicyClasses()) {
                results.put(pc, arset);
            }
        }

        return results;
    }

    @Override
    public Map<Long, AccessRightSet> computeACL(TargetContext targetCtx) throws PMException {
        Map<Long, AccessRightSet> acl = new HashMap<>();
        long[] search = store.graph().search(U, NO_PROPERTIES);
        for (long user : search) {
            AccessRightSet list = this.computePrivileges(new UserContext(user), targetCtx);
            acl.put(user, list);
        }

        return acl;
    }

    @Override
    public Map<Long, AccessRightSet> computeDestinationAttributes(UserContext userCtx) throws PMException {
        return new MemoryUserEvaluator(store)
                .evaluate(userCtx)
                .borderTargets();
    }

    @Override
    public SubgraphPrivileges computeSubgraphPrivileges(UserContext userCtx, long root) throws PMException {
        List<SubgraphPrivileges> subgraphs = new ArrayList<>();

        long[] adjacentAscendants = store.graph().getAdjacentAscendants(root);
        for (long adjacent : adjacentAscendants) {
            subgraphs.add(computeSubgraphPrivileges(userCtx, adjacent));
        }

        return new SubgraphPrivileges(store.graph().getNodeById(root), computePrivileges(userCtx, new TargetContext(root)), subgraphs);
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(UserContext userCtx, long root) throws PMException {
        Map<Node, AccessRightSet> ascendantPrivs = new HashMap<>();

        long[] adjacentAscendants = store.graph().getAdjacentAscendants(root);
        for (long adjacentAscendant : adjacentAscendants) {
            Node node = store.graph().getNodeById(adjacentAscendant);
            ascendantPrivs.put(node, computePrivileges(userCtx, new TargetContext(adjacentAscendant)));
        }

        return ascendantPrivs;
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentDescendantPrivileges(UserContext userCtx, long root) throws PMException {
        Map<Node, AccessRightSet> descendantPrivs = new HashMap<>();

        long[] adjacentDescendants = store.graph().getAdjacentDescendants(root);
        for (long adjacentDescendant : adjacentDescendants) {
            Node node = store.graph().getNodeById(adjacentDescendant);
            descendantPrivs.put(node, computePrivileges(userCtx, new TargetContext(adjacentDescendant)));
        }

        return descendantPrivs;
    }

    @Override
    public Explain explain(UserContext userCtx, TargetContext targetCtx) throws PMException {
        return new MemoryExplainer(store)
                .explain(userCtx, targetCtx);
    }

    @Override
    public Map<Node, AccessRightSet> computePersonalObjectSystem(UserContext userCtx) throws PMException {
        Map<Long, AccessRightSet> pos = new HashMap<>();

        for (long pc : store.graph().getPolicyClasses()) {
            new GraphStoreBFS(store.graph())
                    .withDirection(Direction.ASCENDANTS)
                    .withVisitor(n -> {
                        AccessRightSet privs = computePrivileges(userCtx, new TargetContext(n));
                        if (privs.isEmpty()) {
                            return;
                        }

                        pos.put(n, privs);
                    })
                    .withSinglePathShortCircuit(n -> {
                        return pos.containsKey(n);
                    })
                    .walk(pc);
        }

        Map<Node, AccessRightSet> posWithNodes = new HashMap<>();
        for (Map.Entry<Long, AccessRightSet> entry : pos.entrySet()) {
            posWithNodes.put(store.graph().getNodeById(entry.getKey()), entry.getValue());
        }

        return posWithNodes;
    }

    private void getAndStorePrivileges(Map<Long, AccessRightSet> arsetMap, UserDagResult userDagResult, long target) throws PMException {
        TargetDagResult result = new MemoryTargetEvaluator(store)
                .evaluate(userDagResult, new TargetContext(target));
        AccessRightSet privileges = resolvePrivileges(userDagResult, result, store.operations().getResourceOperations());
        arsetMap.put(target, privileges);
    }

    private Set<Long> getDescendants(long vNode) throws PMException {
        Set<Long> descendants = new HashSet<>();

        long[] ascendants = store.graph().getAdjacentAscendants(vNode);
        if (ascendants.length == 0) {
            return descendants;
        }

        descendants.addAll(LongStream.of(ascendants).boxed().toList());
        for (long ascendant : ascendants) {
            descendants.add(ascendant);
            descendants.addAll(getDescendants(ascendant));
        }

        return descendants;
    }
}
