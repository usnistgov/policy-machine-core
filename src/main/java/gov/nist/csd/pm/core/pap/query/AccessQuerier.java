package gov.nist.csd.pm.core.pap.query;

import static gov.nist.csd.pm.core.common.graph.node.NodeType.U;
import static gov.nist.csd.pm.core.common.graph.node.Properties.NO_PROPERTIES;
import static gov.nist.csd.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_POLICY_CLASSES;
import static gov.nist.csd.pm.core.pap.query.access.AccessRightResolver.resolveDeniedAccessRights;
import static gov.nist.csd.pm.core.pap.query.access.AccessRightResolver.resolvePrivileges;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.dag.Direction;
import gov.nist.csd.pm.core.common.graph.dag.TargetDagResult;
import gov.nist.csd.pm.core.common.graph.dag.UserDagResult;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.access.Explainer;
import gov.nist.csd.pm.core.pap.query.access.TargetEvaluator;
import gov.nist.csd.pm.core.pap.query.access.UserEvaluator;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.query.model.explain.Explain;
import gov.nist.csd.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
import gov.nist.csd.pm.core.pap.store.GraphStoreBFS;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AccessQuerier extends Querier implements AccessQuery {

    public AccessQuerier(PolicyStore store) {
        super(store);
    }

    @Override
    public AccessRightSet computePrivileges(UserContext userCtx, TargetContext targetCtx) throws PMException {
        // traverse the user side of the graph to get the associations
        UserEvaluator userEvaluator = new UserEvaluator(store);
        UserDagResult userDagResult = userEvaluator.evaluate(userCtx);

        // traverse the target side of the graph to get permissions per policy class
        TargetEvaluator targetEvaluator = new TargetEvaluator(store);
        TargetDagResult targetDagResult = targetEvaluator.evaluate(userDagResult, targetCtx);

        // resolve the permissions
        return resolvePrivileges(userDagResult, targetDagResult, store.operations().getResourceOperations());
    }

    @Override
    public List<AccessRightSet> computePrivileges(UserContext userCtx, List<TargetContext> targetCtxs) throws PMException {
        // traverse the user side of the graph to get the associations
        UserEvaluator userEvaluator = new UserEvaluator(store);
        UserDagResult userDagResult = userEvaluator.evaluate(userCtx);

        // traverse the target side of the graph to get permissions per policy class
        TargetEvaluator targetEvaluator = new TargetEvaluator(store);

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
        UserEvaluator userEvaluator = new UserEvaluator(store);
        UserDagResult userDagResult = userEvaluator.evaluate(userCtx);
        if (userDagResult.borderTargets().isEmpty()) {
            return accessRights;
        }

        // traverse the target side of the graph to get permissions per policy class
        TargetEvaluator targetEvaluator = new TargetEvaluator(store);
        TargetDagResult targetDagResult = targetEvaluator.evaluate(userDagResult, targetCtx);

        // resolve the permissions
        return resolveDeniedAccessRights(userDagResult, targetDagResult);
    }

    @Override
    public Map<Long, AccessRightSet> computeCapabilityList(UserContext userCtx) throws PMException {
        Map<Long, AccessRightSet> results = new HashMap<>();

        //get border nodes.  Can be OA or UA.  Return empty set if no attrs are reachable
        UserEvaluator userEvaluator = new UserEvaluator(store);
        UserDagResult userDagResult = userEvaluator.evaluate(userCtx);
        if (userDagResult.borderTargets().isEmpty()) {
            return results;
        }

        for(long borderTarget : userDagResult.borderTargets().keySet()) {
            // compute permissions on the border attr
            getAndStorePrivileges(results, userDagResult, borderTarget);

            // compute decisions for the subgraph of the border attr
            Set<Long> descendants = getAscendants(borderTarget);
            for (long descendant : descendants) {
                if (results.containsKey(descendant)) {
                    continue;
                }

                getAndStorePrivileges(results, userDagResult, descendant);
            }
        }

        // add policy classes
        if (results.containsKey(PM_ADMIN_POLICY_CLASSES.nodeId())) {
            AccessRightSet arset = results.get(PM_ADMIN_POLICY_CLASSES.nodeId());
            for (long pc : store.graph().getPolicyClasses()) {
                results.put(pc, arset);
            }
        }

        return results;
    }

    @Override
    public Map<Long, AccessRightSet> computeACL(TargetContext targetCtx) throws PMException {
        Map<Long, AccessRightSet> acl = new HashMap<>();
        Collection<Long> search = store.graph().search(U, NO_PROPERTIES);
        for (long user : search) {
            AccessRightSet list = this.computePrivileges(new UserContext(user), targetCtx);
            acl.put(user, list);
        }

        return acl;
    }

    @Override
    public Map<Long, AccessRightSet> computeDestinationAttributes(UserContext userCtx) throws PMException {
        return new UserEvaluator(store)
            .evaluate(userCtx)
            .borderTargets();
    }

    @Override
    public SubgraphPrivileges computeSubgraphPrivileges(UserContext userCtx, long root) throws PMException {
        List<SubgraphPrivileges> subgraphs = new ArrayList<>();

        Collection<Long> adjacentAscendants = store.graph().getAdjacentAscendants(root);
        for (long adjacent : adjacentAscendants) {
            subgraphs.add(computeSubgraphPrivileges(userCtx, adjacent));
        }

        return new SubgraphPrivileges(store.graph().getNodeById(root), computePrivileges(userCtx, new TargetContext(root)), subgraphs);
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(UserContext userCtx, long root) throws PMException {
        Map<Node, AccessRightSet> ascendantPrivs = new HashMap<>();

        Collection<Long> adjacentAscendants = store.graph().getAdjacentAscendants(root);
        for (long adjacentAscendant : adjacentAscendants) {
            Node node = store.graph().getNodeById(adjacentAscendant);
            ascendantPrivs.put(node, computePrivileges(userCtx, new TargetContext(adjacentAscendant)));
        }

        return ascendantPrivs;
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentDescendantPrivileges(UserContext userCtx, long root) throws PMException {
        Map<Node, AccessRightSet> descendantPrivs = new HashMap<>();

        Collection<Long> adjacentDescendants = store.graph().getAdjacentDescendants(root);
        for (long adjacentDescendant : adjacentDescendants) {
            Node node = store.graph().getNodeById(adjacentDescendant);
            descendantPrivs.put(node, computePrivileges(userCtx, new TargetContext(adjacentDescendant)));
        }

        return descendantPrivs;
    }

    @Override
    public Explain explain(UserContext userCtx, TargetContext targetCtx) throws PMException {
        return new Explainer(store)
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
        TargetDagResult result = new TargetEvaluator(store)
            .evaluate(userDagResult, new TargetContext(target));
        AccessRightSet privileges = resolvePrivileges(userDagResult, result, store.operations().getResourceOperations());
        arsetMap.put(target, privileges);
    }

    private Set<Long> getAscendants(long vNode) throws PMException {
        Set<Long> ret = new HashSet<>();

        Collection<Long> ascendants = store.graph().getAdjacentAscendants(vNode);
        if (ascendants.isEmpty()) {
            return ret;
        }

        ret.addAll(ascendants);
        for (long ascendant : ascendants) {
            ret.add(ascendant);
            ret.addAll(getAscendants(ascendant));
        }

        return ret;
    }
}
