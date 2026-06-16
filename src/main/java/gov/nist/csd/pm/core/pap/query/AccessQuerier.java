package gov.nist.csd.pm.core.pap.query;

import static gov.nist.csd.pm.core.common.graph.node.NodeType.U;
import static gov.nist.csd.pm.core.common.graph.node.Properties.NO_PROPERTIES;
import static gov.nist.csd.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_POLICY_CLASSES;
import static gov.nist.csd.pm.core.pap.operation.accessright.AccessRightResolver.resolveDeniedAccessRights;
import static gov.nist.csd.pm.core.pap.operation.accessright.AccessRightResolver.resolvePrivileges;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.graph.Association;
import gov.nist.csd.pm.core.pap.graph.dag.GraphWalker;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.graph.dag.DepthFirstGraphWalker;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.access.CachedTargetEvaluator;
import gov.nist.csd.pm.core.pap.query.access.Explainer;
import gov.nist.csd.pm.core.pap.query.access.TargetDagResult;
import gov.nist.csd.pm.core.pap.query.access.TargetEvaluator;
import gov.nist.csd.pm.core.pap.query.access.UserDagResult;
import gov.nist.csd.pm.core.pap.query.access.UserEvaluator;
import gov.nist.csd.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.query.model.explain.Explain;
import gov.nist.csd.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
import gov.nist.csd.pm.core.pap.graph.dag.BreadthFirstGraphWalker;
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

    public SelfAccessQuery self(UserContext userCtx) {
        return new SelfAccessQuerier(this, userCtx);
    }

    @Override
    public AccessRightSet computePrivileges(UserContext userCtx, TargetContext targetCtx) throws PMException {
        UserDagResult userDagResults = evaluateUser(userCtx);

        // traverse the target side of the graph to get permissions per policy class
        TargetEvaluator targetEvaluator = new TargetEvaluator(store);

        // resolve the permissions
        return computePrivileges(userDagResults, targetCtx, targetEvaluator);
    }

    @Override
    public List<AccessRightSet> computePrivileges(UserContext userCtx, List<TargetContext> targetCtxs) throws PMException {
        // traverse the user side of the graph to get the associations
        UserDagResult userDagResults = evaluateUser(userCtx);

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
        UserDagResult userDagResult = evaluateUser(userCtx);

        TargetEvaluator targetEvaluator = new TargetEvaluator(store);
        TargetDagResult targetDagResult = targetEvaluator.evaluate(userDagResult, targetCtx);
        return resolveDeniedAccessRights(userDagResult.prohibitions(), targetDagResult);
    }

    @Override
    public Map<Long, AccessRightSet> computeCapabilityList(UserContext userCtx) throws PMException {
        Map<Long, AccessRightSet> results = new HashMap<>();

        UserDagResult userDagResult = evaluateUser(userCtx);
        Map<Long, AccessRightSet> borderTargets = userDagResult.borderTargets();
        if (borderTargets.isEmpty()) {
            return results;
        }

        CachedTargetEvaluator cachedTargetEvaluator = new CachedTargetEvaluator(store);

        for (long borderTarget : borderTargets.keySet()) {
            // compute permissions on the border attr
            AccessRightSet arset = computePrivileges(userDagResult, NodeTargetContext.of(borderTarget), cachedTargetEvaluator);
            results.put(borderTarget, arset);

            // compute decisions for the subgraph of the border attr
            Set<Long> ascendants = getAscendants(borderTarget);
            for (long ascendant : ascendants) {
                if (results.containsKey(ascendant)) {
                    continue;
                }

                arset = computePrivileges(userDagResult, NodeTargetContext.of(ascendant), cachedTargetEvaluator);
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
            UserDagResult userDagResults = evaluateUser(NodeUserContext.of(user));

            AccessRightSet list = this.computePrivileges(userDagResults, targetCtx, targetEvaluator);
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
            computePrivileges(userDagResult, NodeTargetContext.of(root), targetEvaluator),
            subgraphs
        );
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(UserContext userCtx, long root) throws PMException {
        Map<Node, AccessRightSet> ascendantPrivs = new HashMap<>();

        UserDagResult userDagResults = evaluateUser(userCtx);
        TargetEvaluator targetEvaluator = new TargetEvaluator(store);

        Collection<Long> adjacentAscendants = store.graph().getAdjacentAscendants(root);
        for (long adjacentAscendant : adjacentAscendants) {
            Node node = store.graph().getNodeById(adjacentAscendant);
            ascendantPrivs.put(node, computePrivileges(userDagResults, NodeTargetContext.of(adjacentAscendant), targetEvaluator));
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
            descendantPrivs.put(node, computePrivileges(userDagResult, NodeTargetContext.of(adjacentDescendant), targetEvaluator));
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

        UserDagResult userDagResults = evaluateUser(userCtx);
        CachedTargetEvaluator cachedTargetEvaluator = new CachedTargetEvaluator(store);

        for (long pc : store.graph().getPolicyClasses()) {
            new BreadthFirstGraphWalker(store.graph()::getAdjacentAscendants)
                .withVisitor(n -> {
                    AccessRightSet privs = computePrivileges(userDagResults, NodeTargetContext.of(n), cachedTargetEvaluator);
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

    @Override
    public Map<Long, Set<Long>> computeRequiredAttributeSets(TargetContext targetCtx, AccessRightSet privileges) throws
                                                                                                                 PMException {
        Map<Long, Set<Long>> result = new HashMap<>();
        Map<Long, Set<Long>> nodeToPcs = new HashMap<>();
        Set<Long> visitedNodes = new HashSet<>();

        Collection<Long> policyClasses = store.graph().getPolicyClasses();

        // DFS from the target to find attributes
        GraphWalker dfs = new DepthFirstGraphWalker(store.graph()::getAdjacentDescendants)
            .withVisitor(nodeId -> {
                visitedNodes.add(nodeId);
                if (policyClasses.contains(nodeId)) {
                    nodeToPcs.computeIfAbsent(nodeId, k -> new HashSet<>()).add(nodeId);
                }
            })
            .withPropagator((parent, child) -> {
                Set<Long> parentPcs = nodeToPcs.get(parent);
                if (parentPcs != null && !parentPcs.isEmpty()) {
                    nodeToPcs.computeIfAbsent(child, k -> new HashSet<>()).addAll(parentPcs);
                }
            });

        dfs.walk(targetCtx, store.graph());

        // For each visited node, collect association source UAs
        for (long nodeId : visitedNodes) {
            Set<Long> pcs = nodeToPcs.get(nodeId);
            if (pcs == null || pcs.isEmpty()) {
                continue;
            }
            for (Association assoc : store.graph().getAssociationsWithTarget(nodeId)) {
                AccessRightSet overlap = new AccessRightSet(assoc.arset());

                // retain only the privileges being searched for
                overlap.retainAll(privileges);
                if (overlap.isEmpty()) {
                    continue;
                }

                // add the ua to source
                long ua = assoc.source();
                for (long pc : pcs) {
                    result.computeIfAbsent(pc, k -> new HashSet<>()).add(ua);
                }
            }
        }

        // Remove UAs whose prohibitions are satisfied by the target path and denied any of the required privileges
        Set<Long> allUas = new HashSet<>();
        result.values().forEach(allUas::addAll);

        Set<Long> prohibitedUas = new HashSet<>();
        TargetDagResult targetDagResult = new TargetDagResult(Map.of(), visitedNodes);
        for (long ua : allUas) {
            Collection<Prohibition> prohibitions = store.prohibitions().getNodeProhibitions(ua);
            if (prohibitions.isEmpty()) {
                continue;
            }
            AccessRightSet denied = resolveDeniedAccessRights(new HashSet<>(prohibitions), targetDagResult);
            denied.retainAll(privileges);
            if (!denied.isEmpty()) {
                prohibitedUas.add(ua);
            }
        }

        if (!prohibitedUas.isEmpty()) {
            result.values().forEach(uas -> uas.removeAll(prohibitedUas));
        }

        return result;
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
        return resolvePrivileges(userDagResult, targetDagResult, store.operations().getResourceAccessRights());
    }
}
