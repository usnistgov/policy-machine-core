package gov.nist.csd.pm.impl.memory.pap.access;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.dag.*;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.AccessQuerier;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.pap.query.model.explain.*;
import gov.nist.csd.pm.pap.query.model.subgraph.SubgraphPrivileges;
import gov.nist.csd.pm.pap.store.GraphStoreBFS;
import gov.nist.csd.pm.pap.store.PolicyStore;

import java.util.*;

import static gov.nist.csd.pm.pap.admin.AdminPolicyNode.PM_ADMIN_OBJECT;
import static gov.nist.csd.pm.pap.AccessRightResolver.*;
import static gov.nist.csd.pm.pap.graph.node.NodeType.U;
import static gov.nist.csd.pm.pap.graph.node.Properties.NO_PROPERTIES;

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
    public Map<String, AccessRightSet> computeCapabilityList(UserContext userCtx) throws PMException {
        Map<String, AccessRightSet> results = new HashMap<>();

        //get border nodes.  Can be OA or UA.  Return empty set if no attrs are reachable
        MemoryUserEvaluator userEvaluator = new MemoryUserEvaluator(store);
        UserDagResult userDagResult = userEvaluator.evaluate(userCtx);
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

        // add policy classes
        if (results.containsKey(PM_ADMIN_OBJECT.nodeName())) {
            AccessRightSet arset = results.get(PM_ADMIN_OBJECT.nodeName());
            for (String pc : store.graph().getPolicyClasses()) {
                results.put(pc, arset);
            }
        }

        return results;
    }

    @Override
    public Map<String, AccessRightSet> computeACL(TargetContext targetCtx) throws PMException {
        Map<String, AccessRightSet> acl = new HashMap<>();
        Collection<String> search = store.graph().search(U, NO_PROPERTIES);
        for (String user : search) {
            AccessRightSet list = this.computePrivileges(new UserContext(user), targetCtx);
            acl.put(user, list);
        }

        return acl;
    }

    @Override
    public Map<String, AccessRightSet> computeDestinationAttributes(UserContext userCtx) throws PMException {
        return new MemoryUserEvaluator(store)
                .evaluate(userCtx)
                .borderTargets();
    }

    @Override
    public SubgraphPrivileges computeSubgraphPrivileges(UserContext userCtx, String root) throws PMException {
        List<SubgraphPrivileges> subgraphs = new ArrayList<>();

        Collection<String> adjacentAscendants = store.graph().getAdjacentAscendants(root);
        for (String adjacent : adjacentAscendants) {
            subgraphs.add(computeSubgraphPrivileges(userCtx, adjacent));
        }

        return new SubgraphPrivileges(root, computePrivileges(userCtx, new TargetContext(root)), subgraphs);
    }

    @Override
    public Map<String, AccessRightSet> computeAdjacentAscendantPrivileges(UserContext userCtx, String root) throws PMException {
        Map<String, AccessRightSet> ascendantPrivs = new HashMap<>();

        Collection<String> adjacentAscendants = store.graph().getAdjacentAscendants(root);
        for (String adjacentAscendant : adjacentAscendants) {
            ascendantPrivs.put(adjacentAscendant, computePrivileges(userCtx, new TargetContext(adjacentAscendant)));
        }

        return ascendantPrivs;
    }

    @Override
    public Map<String, AccessRightSet> computeAdjacentDescendantPrivileges(UserContext userCtx, String root) throws PMException {
        Map<String, AccessRightSet> descendantPrivs = new HashMap<>();

        Collection<String> adjacentDescendants = store.graph().getAdjacentDescendants(root);
        for (String adjacentDescendant : adjacentDescendants) {
            descendantPrivs.put(adjacentDescendant, computePrivileges(userCtx, new TargetContext(adjacentDescendant)));
        }

        return descendantPrivs;
    }

    @Override
    public Explain explain(UserContext userCtx, TargetContext targetCtx) throws PMException {
        return new MemoryExplainer(store)
                .explain(userCtx, targetCtx);
    }

    @Override
    public Map<String, AccessRightSet> computePersonalObjectSystem(UserContext userCtx) throws PMException {
        Map<String, AccessRightSet> pos = new HashMap<>();

        for (String pc : store.graph().getPolicyClasses()) {
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
        return pos;
    }

    private void getAndStorePrivileges(Map<String, AccessRightSet> arsetMap, UserDagResult userDagResult, String target) throws PMException {
        TargetDagResult result = new MemoryTargetEvaluator(store)
                .evaluate(userDagResult, new TargetContext(target));
        AccessRightSet privileges = resolvePrivileges(userDagResult, result, store.operations().getResourceOperations());
        arsetMap.put(target, privileges);
    }

    private Set<String> getDescendants(String vNode) throws PMException {
        Set<String> descendants = new HashSet<>();

        Collection<String> ascendants = store.graph().getAdjacentAscendants(vNode);
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
}
