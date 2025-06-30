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
import gov.nist.csd.pm.core.pap.query.access.CachedTargetEvaluator;
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
        UserDagResult userDagResult = evaluateUser(userCtx);

        // traverse the target side of the graph to get permissions per policy class
        TargetEvaluator targetEvaluator = new TargetEvaluator(store);

        // resolve the permissions
        return computePrivileges(userDagResult, targetCtx, targetEvaluator);
    }

    @Override
    public List<AccessRightSet> computePrivileges(UserContext userCtx, List<TargetContext> targetCtxs) throws PMException {
        // traverse the user side of the graph to get the associations
        UserDagResult userDagResult = evaluateUser(userCtx);

        // traverse the target side of the graph to get permissions per policy class
        TargetEvaluator targetEvaluator = new CachedTargetEvaluator(store);

        List<AccessRightSet> accessRightSets = new ArrayList<>();
        for (TargetContext targetCtx : targetCtxs) {
            AccessRightSet privs = computePrivileges(userDagResult, targetCtx, targetEvaluator);

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

        // get border nodes.  Can be OA or UA.  Return empty set if no attrs are reachable
        UserEvaluator userEvaluator = new UserEvaluator(store);
        UserDagResult userDagResult = userEvaluator.evaluate(userCtx);
        if (userDagResult.borderTargets().isEmpty()) {
            return results;
        }

        CachedTargetEvaluator cachedTargetEvaluator = new CachedTargetEvaluator(store);

        for(long borderTarget : userDagResult.borderTargets().keySet()) {
            // compute permissions on the border attr
            AccessRightSet arset = computePrivileges(userDagResult, new TargetContext(borderTarget), cachedTargetEvaluator);
            results.put(borderTarget, arset);

            // compute decisions for the subgraph of the border attr
            Set<Long> ascendants = getAscendants(borderTarget);
            for (long ascendant : ascendants) {
                if (results.containsKey(ascendant)) {
                    continue;
                }

                arset = computePrivileges(userDagResult, new TargetContext(ascendant), cachedTargetEvaluator);
                results.put(ascendant, arset);
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

        TargetEvaluator targetEvaluator = new CachedTargetEvaluator(store);
        for (long user : search) {
            UserDagResult userDagResult = evaluateUser(new UserContext(user));

            AccessRightSet list = this.computePrivileges(userDagResult, targetCtx, targetEvaluator);
            acl.put(user, list);
        }

        return acl;
    }

    @Override
    public Map<Long, AccessRightSet> computeDestinationAttributes(UserContext userCtx) throws PMException {
        return evaluateUser(userCtx).borderTargets();
    }

    @Override
    public SubgraphPrivileges computeSubgraphPrivileges(UserContext userCtx, long root) throws PMException {
        List<SubgraphPrivileges> subgraphs = new ArrayList<>();

        Collection<Long> adjacentAscendants = store.graph().getAdjacentAscendants(root);
        for (long adjacent : adjacentAscendants) {
            subgraphs.add(computeSubgraphPrivileges(userCtx, adjacent));
        }

        UserDagResult userDagResult = evaluateUser(userCtx);
        TargetEvaluator targetEvaluator = new TargetEvaluator(store);

        return new SubgraphPrivileges(
            store.graph().getNodeById(root),
            computePrivileges(userDagResult, new TargetContext(root), targetEvaluator),
            subgraphs
        );
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(UserContext userCtx, long root) throws PMException {
        Map<Node, AccessRightSet> ascendantPrivs = new HashMap<>();

        UserDagResult userDagResult = evaluateUser(userCtx);
        TargetEvaluator targetEvaluator = new TargetEvaluator(store);

        Collection<Long> adjacentAscendants = store.graph().getAdjacentAscendants(root);
        for (long adjacentAscendant : adjacentAscendants) {
            Node node = store.graph().getNodeById(adjacentAscendant);
            ascendantPrivs.put(node, computePrivileges(userDagResult, new TargetContext(adjacentAscendant), targetEvaluator));
        }

        return ascendantPrivs;
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentDescendantPrivileges(UserContext userCtx, long root) throws PMException {
        Map<Node, AccessRightSet> descendantPrivs = new HashMap<>();

        UserDagResult userDagResult = evaluateUser(userCtx);
        TargetEvaluator targetEvaluator = new TargetEvaluator(store);

        Collection<Long> adjacentDescendants = store.graph().getAdjacentDescendants(root);
        for (long adjacentDescendant : adjacentDescendants) {
            Node node = store.graph().getNodeById(adjacentDescendant);
            descendantPrivs.put(node, computePrivileges(userDagResult, new TargetContext(adjacentDescendant), targetEvaluator));
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

        UserDagResult userDagResult = evaluateUser(userCtx);
        CachedTargetEvaluator cachedTargetEvaluator = new CachedTargetEvaluator(store);

        for (long pc : store.graph().getPolicyClasses()) {
            new GraphStoreBFS(store.graph())
                .withDirection(Direction.ASCENDANTS)
                .withVisitor(n -> {
                    AccessRightSet privs = computePrivileges(userDagResult, new TargetContext(n), cachedTargetEvaluator);
                    if (privs.isEmpty()) {
                        return;
                    }

                    pos.put(n, privs);
                })
                .withSinglePathShortCircuit(pos::containsKey)
                .walk(pc);
        }

        Map<Node, AccessRightSet> posWithNodes = new HashMap<>();
        for (Map.Entry<Long, AccessRightSet> entry : pos.entrySet()) {
            posWithNodes.put(store.graph().getNodeById(entry.getKey()), entry.getValue());
        }

        return posWithNodes;
    }

    private UserDagResult evaluateUser(UserContext userCtx) throws PMException {
        UserEvaluator userEvaluator = new UserEvaluator(store);
        return userEvaluator.evaluate(userCtx);
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

    private AccessRightSet computePrivileges(UserDagResult userDagResult, TargetContext targetCtx, TargetEvaluator targetEvaluator) throws PMException {
        TargetDagResult targetDagResult = targetEvaluator.evaluate(userDagResult, targetCtx);

        // resolve the permissions
        return resolvePrivileges(userDagResult, targetDagResult, store.operations().getResourceOperations());
    }
}
