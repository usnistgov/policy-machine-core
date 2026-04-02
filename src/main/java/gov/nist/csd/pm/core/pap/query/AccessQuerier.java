package gov.nist.csd.pm.core.pap.query;

import static gov.nist.csd.pm.core.common.graph.node.NodeType.U;
import static gov.nist.csd.pm.core.common.graph.node.Properties.NO_PROPERTIES;
import static gov.nist.csd.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_POLICY_CLASSES;
import static gov.nist.csd.pm.core.pap.operation.accessright.AccessRightResolver.resolveDeniedAccessRights;
import static gov.nist.csd.pm.core.pap.operation.accessright.AccessRightResolver.resolvePrivileges;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.dag.Direction;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.access.CachedTargetEvaluator;
import gov.nist.csd.pm.core.pap.query.access.Explainer;
import gov.nist.csd.pm.core.pap.query.access.TargetDagResult;
import gov.nist.csd.pm.core.pap.query.access.TargetEvaluator;
import gov.nist.csd.pm.core.pap.query.access.UserDagResult;
import gov.nist.csd.pm.core.pap.query.access.UserEvaluationResult;
import gov.nist.csd.pm.core.pap.query.access.UserEvaluator;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserIdContext;
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
        UserEvaluationResult userDagResults = evaluateUser(userCtx);

        // traverse the target side of the graph to get permissions per policy class
        TargetEvaluator targetEvaluator = new TargetEvaluator(store);

        // resolve the permissions
        return computePrivileges(userDagResults, targetCtx, targetEvaluator);
    }

    @Override
    public List<AccessRightSet> computePrivileges(UserContext userCtx, List<TargetContext> targetCtxs) throws PMException {
        // traverse the user side of the graph to get the associations
        UserEvaluationResult userDagResults = evaluateUser(userCtx);

        // traverse the target side of the graph to get permissions per policy class
        TargetEvaluator targetEvaluator = new CachedTargetEvaluator(store);

        List<AccessRightSet> accessRightSets = new ArrayList<>();
        for (TargetContext targetCtx : targetCtxs) {
            AccessRightSet privs = computePrivileges(userDagResults, targetCtx, targetEvaluator);
            accessRightSets.add(privs);
        }

        return accessRightSets;
    }

    @Override
    public AccessRightSet computeDeniedPrivileges(UserContext userCtx, TargetContext targetCtx) throws PMException {
        UserEvaluationResult userDagResults = evaluateUser(userCtx);

        TargetEvaluator targetEvaluator = new TargetEvaluator(store);
        AccessRightSet denied = null;
        for (UserDagResult userDagResult : userDagResults.dagResults()) {
            if (userDagResult.borderTargets().isEmpty()) {
                continue;
            }
            TargetDagResult targetDagResult = targetEvaluator.evaluate(userDagResult, targetCtx);
            AccessRightSet d = resolveDeniedAccessRights(userDagResult, targetDagResult);
            denied = (denied == null) ? d : intersect(denied, d);
        }
        return denied == null ? new AccessRightSet() : denied;
    }

    @Override
    public Map<Long, AccessRightSet> computeCapabilityList(UserContext userCtx) throws PMException {
        Map<Long, AccessRightSet> results = new HashMap<>();

        UserEvaluationResult userDagResults = evaluateUser(userCtx);
        Map<Long, AccessRightSet> borderTargets = unionBorderTargets(userDagResults);
        if (borderTargets.isEmpty()) {
            return results;
        }

        CachedTargetEvaluator cachedTargetEvaluator = new CachedTargetEvaluator(store);

        for (long borderTarget : borderTargets.keySet()) {
            // compute permissions on the border attr
            AccessRightSet arset = computePrivileges(userDagResults, new TargetContext(borderTarget), cachedTargetEvaluator);
            results.put(borderTarget, arset);

            // compute decisions for the subgraph of the border attr
            Set<Long> ascendants = getAscendants(borderTarget);
            for (long ascendant : ascendants) {
                if (results.containsKey(ascendant)) {
                    continue;
                }

                arset = computePrivileges(userDagResults, new TargetContext(ascendant), cachedTargetEvaluator);
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
            UserEvaluationResult userDagResults = evaluateUser(new UserIdContext(user));

            AccessRightSet list = this.computePrivileges(userDagResults, targetCtx, targetEvaluator);
            acl.put(user, list);
        }

        return acl;
    }

    @Override
    public Map<Long, AccessRightSet> computeDestinationAttributes(UserContext userCtx) throws PMException {
        return unionBorderTargets(evaluateUser(userCtx));
    }

    @Override
    public SubgraphPrivileges computeSubgraphPrivileges(UserContext userCtx, long root) throws PMException {
        List<SubgraphPrivileges> subgraphs = new ArrayList<>();

        Collection<Long> adjacentAscendants = store.graph().getAdjacentAscendants(root);
        for (long adjacent : adjacentAscendants) {
            subgraphs.add(computeSubgraphPrivileges(userCtx, adjacent));
        }

        UserEvaluationResult userDagResults = evaluateUser(userCtx);
        TargetEvaluator targetEvaluator = new TargetEvaluator(store);

        return new SubgraphPrivileges(
            store.graph().getNodeById(root),
            computePrivileges(userDagResults, new TargetContext(root), targetEvaluator),
            subgraphs
        );
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(UserContext userCtx, long root) throws PMException {
        Map<Node, AccessRightSet> ascendantPrivs = new HashMap<>();

        UserEvaluationResult userDagResults = evaluateUser(userCtx);
        TargetEvaluator targetEvaluator = new TargetEvaluator(store);

        Collection<Long> adjacentAscendants = store.graph().getAdjacentAscendants(root);
        for (long adjacentAscendant : adjacentAscendants) {
            Node node = store.graph().getNodeById(adjacentAscendant);
            ascendantPrivs.put(node, computePrivileges(userDagResults, new TargetContext(adjacentAscendant), targetEvaluator));
        }

        return ascendantPrivs;
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentDescendantPrivileges(UserContext userCtx, long root) throws PMException {
        Map<Node, AccessRightSet> descendantPrivs = new HashMap<>();

        UserEvaluationResult userDagResults = evaluateUser(userCtx);
        TargetEvaluator targetEvaluator = new TargetEvaluator(store);

        Collection<Long> adjacentDescendants = store.graph().getAdjacentDescendants(root);
        for (long adjacentDescendant : adjacentDescendants) {
            Node node = store.graph().getNodeById(adjacentDescendant);
            descendantPrivs.put(node, computePrivileges(userDagResults, new TargetContext(adjacentDescendant), targetEvaluator));
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

        UserEvaluationResult userDagResults = evaluateUser(userCtx);
        CachedTargetEvaluator cachedTargetEvaluator = new CachedTargetEvaluator(store);

        for (long pc : store.graph().getPolicyClasses()) {
            new GraphStoreBFS(store.graph())
                .withDirection(Direction.ASCENDANTS)
                .withVisitor(n -> {
                    AccessRightSet privs = computePrivileges(userDagResults, new TargetContext(n), cachedTargetEvaluator);
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

    private UserEvaluationResult evaluateUser(UserContext userCtx) throws PMException {
        UserEvaluator userEvaluator = new UserEvaluator(store);
        return userEvaluator.evaluate(userCtx);
    }

    private Map<Long, AccessRightSet> unionBorderTargets(UserEvaluationResult userDagResults) {
        Map<Long, AccessRightSet> union = new HashMap<>();
        for (UserDagResult result : userDagResults.dagResults()) {
            union.putAll(result.borderTargets());
        }
        return union;
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

    private AccessRightSet computePrivileges(UserEvaluationResult userDagResults, TargetContext targetCtx, TargetEvaluator targetEvaluator) throws PMException {
        AccessRightSet result = null;
        for (UserDagResult userDagResult : userDagResults.dagResults()) {
            AccessRightSet privs = computePrivileges(userDagResult, targetCtx, targetEvaluator);
            result = (result == null) ? privs : intersect(result, privs);
        }
        return result == null ? new AccessRightSet() : result;
    }

    private AccessRightSet computePrivileges(UserDagResult userDagResult, TargetContext targetCtx, TargetEvaluator targetEvaluator) throws PMException {
        TargetDagResult targetDagResult = targetEvaluator.evaluate(userDagResult, targetCtx);

        // resolve the permissions
        return resolvePrivileges(userDagResult, targetDagResult, store.operations().getResourceAccessRights());
    }

    private static AccessRightSet intersect(AccessRightSet a, AccessRightSet b) {
        AccessRightSet result = new AccessRightSet();
        result.addAll(a);
        result.retainAll(b);
        return result;
    }
}
