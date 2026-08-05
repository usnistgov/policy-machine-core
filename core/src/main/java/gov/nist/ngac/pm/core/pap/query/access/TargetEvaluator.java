package gov.nist.ngac.pm.core.pap.query.access;

import static gov.nist.ngac.pm.core.common.graph.node.NodeType.PC;
import static gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_POLICY_CLASSES;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.graph.dag.GraphWalker;
import gov.nist.ngac.pm.core.pap.graph.dag.Propagator;
import gov.nist.ngac.pm.core.pap.graph.dag.Visitor;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.graph.dag.DepthFirstGraphWalker;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightResolver;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.query.model.context.AnonymousTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Computes, per policy class, the privileges a user's {@link UserDagResult} grants on a target by
 * depth-first walking the target's ascendant graph and merging in the border-target access rights
 * reached along the way.
 */
public class TargetEvaluator {

	protected final PolicyStore policyStore;

	public TargetEvaluator(PolicyStore policyStore) {
		this.policyStore = policyStore;
	}

	/**
	 * Perform a depth first search on the object side of the graph.  Start at the target node and recursively visit nodes
	 * until a policy class is reached.  On each node visited, collect any operation the user has on the target. At the
	 * end of each dfs iteration the visitedNodes map will contain the operations the user is permitted on the target under
	 * each policy class. If the target has one or more PCs as adjacent descendants, first check the users privileges on
	 * those PCs and add them to the entries of those PCs in the resulting TargetDagResult
	 */
	public TargetDagResult evaluate(UserDagResult userDagResult, TargetContext targetContext) throws PMException {
		targetContext = prepareTargetCtx(targetContext);

		// initialize objects for traversal
		TraversalState state = initializeEvaluationState(userDagResult, targetContext);
		GraphWalker dfs = createDepthFirstWalker(userDagResult, state);

		List<Long> targetNodes = new ArrayList<>(targetContext.resolveNodeIds(policyStore.graph()));
		for (long id : targetNodes) {
			dfs.walk(id);
		}

		Map<Long, AccessRightSet> pcMap = computePcMap(targetNodes, state.visitedNodes);

		return new TargetDagResult(pcMap, state.visitedProhibitionTargets);
	}

	private Map<Long, AccessRightSet> computePcMap(List<Long> targetNodes, Map<Long, Map<Long, AccessRightSet>> visitedNodes) {
		HashMap<Long, AccessRightSet> merged = new HashMap<>();

		for (Long target : targetNodes) {
			Map<Long, AccessRightSet> pcMap = visitedNodes.getOrDefault(target, new HashMap<>());

			for (Map.Entry<Long, AccessRightSet> entry : pcMap.entrySet()) {
				Long pc = entry.getKey();
				AccessRightSet pcArset = entry.getValue();

				if (!merged.containsKey(pc)) {
					merged.put(pc, pcArset);
				} else {
					AccessRightSet mergedArset = merged.get(pc);
					mergedArset.addAll(pcArset);
					merged.put(pc, mergedArset);
				}
			}
		}

		return merged;
	}

	/**
	 * Builds the mutable traversal state for one {@link #evaluate} call: the target's first-level
	 * ascendants to seed the walk, the node ids the user's prohibitions reference, and empty maps to
	 * accumulate visited privileges and prohibition hits into.
	 */
	protected TraversalState initializeEvaluationState(UserDagResult userDagResult, TargetContext targetCtx) throws PMException {
		Collection<Long> firstLevelDescs = new LongArrayList();
		Collection<Long> resolvedIds = targetCtx.resolveNodeIds(policyStore.graph());

		if (targetCtx instanceof NodeTargetContext) {
			long id = resolvedIds.iterator().next();
			firstLevelDescs.addAll(policyStore.graph().getAdjacentDescendants(id));
		} else {
			// AnonymousTargetContext: attribute ids are themselves the starting points
			firstLevelDescs.addAll(resolvedIds);
		}

		Set<Long> userProhibitionTargets = collectUserProhibitionAttributes(userDagResult.prohibitions());
		Map<Long, Map<Long, AccessRightSet>> visitedNodes = new Long2ObjectOpenHashMap<>();
		Set<Long> visitedProhibitionTargets = new LongOpenHashSet();

		return new TraversalState(
			firstLevelDescs,
			userProhibitionTargets,
			visitedNodes,
			visitedProhibitionTargets
		);
	}

	protected AccessRightSet computePrivilegesOnPCs(UserDagResult userDagResult,
													Collection<Long> firstLevelDescs,
													Collection<Long> policyClasses) throws PMException {
		List<Long> firstLevelDescendantPCs = new ArrayList<>(firstLevelDescs);
		firstLevelDescendantPCs.retainAll(policyClasses);

		if (firstLevelDescendantPCs.isEmpty()) {
			return new AccessRightSet();
		}

		// evaluate the privileges this user has on the PM_ADMIN_POLICY_CLASSES node
		// these privs represent the access rights the user has on policy classes
		TargetDagResult adminTargetResult = evaluate(userDagResult, NodeTargetContext.of(PM_ADMIN_POLICY_CLASSES.nodeId()));
		return AccessRightResolver.resolvePrivileges(
			userDagResult,
			adminTargetResult,
			policyStore.operations().getResourceAccessRights()
		);
	}

	/**
	 * Builds the depth-first {@link GraphWalker} used to traverse the target's ascendant graph, wired
	 * with this evaluator's visitor and propagator.
	 */
	protected GraphWalker createDepthFirstWalker(UserDagResult userDagResult, TraversalState state) throws PMException {
		Visitor nodeVisitor = createVisitor(userDagResult, state);
		Propagator privilegePropagator = createPropagator(state);

		return new DepthFirstGraphWalker(policyStore.graph()::getAdjacentDescendants)
			.withVisitor(nodeVisitor)
			.withPropagator(privilegePropagator);
	}

	/**
	 * Builds the {@link Visitor} that, for each ascendant node visited, records it as a reached
	 * prohibition target if applicable, seeds its privileges when it's a policy class, and merges in the
	 * user's border-target access rights when the node is itself a border target.
	 */
	protected Visitor createVisitor(UserDagResult userDagResult, TraversalState state) throws PMException {
		Collection<Long> policyClasses = policyStore.graph().getPolicyClasses();
		AccessRightSet adminPrivilegesOnPCs = computePrivilegesOnPCs(userDagResult, state.firstLevelDescs, policyClasses);

		return nodeId -> {
			// track visited prohibition container nodes
			if (state.userProhibitionTargets.contains(nodeId)) {
				state.visitedProhibitionTargets.add(nodeId);
			}

			Map<Long, AccessRightSet> nodePrivileges = state.visitedNodes.computeIfAbsent(nodeId, __ -> new Long2ObjectOpenHashMap<>());

			if (policyClasses.contains(nodeId)) {
				nodePrivileges.put(nodeId, new AccessRightSet(adminPrivilegesOnPCs));
			} else if (userDagResult.borderTargets().containsKey(nodeId)) {
				AccessRightSet borderArset = userDagResult.borderTargets().get(nodeId);
				nodePrivileges.forEach((policyClassId, privileges) -> privileges.addAll(borderArset));
			}
		};
	}

	/**
	 * Builds the {@link Propagator} that merges a visited descendant's per-policy-class privileges into
	 * its ascendant as the walk moves up the target graph.
	 */
	protected Propagator createPropagator(TraversalState state) {
		return (descendantId, ascendantId) -> {
			Map<Long, AccessRightSet> descsPrivs = state.visitedNodes.get(descendantId);
			Map<Long, AccessRightSet> ascsPrivs = state.visitedNodes.computeIfAbsent(ascendantId, __ -> new Long2ObjectOpenHashMap<>());

			for (long id : descsPrivs.keySet()) {
				AccessRightSet ops = ascsPrivs.getOrDefault(id, new AccessRightSet());
				ops.addAll(descsPrivs.getOrDefault(id, new AccessRightSet()));
				ascsPrivs.put(id, ops);
			}

			state.visitedNodes.put(ascendantId, ascsPrivs);
		};
	}

	/**
	 * Redirects a target context that resolves to a policy class node to the PM_ADMIN_POLICY_CLASSES
	 * node, since a policy class has no ascendants of its own to walk; other target contexts pass through
	 * unchanged.
	 */
	protected TargetContext prepareTargetCtx(TargetContext targetContext) throws PMException {
		// if already a list of attributes, nothing to prepare
		if (targetContext instanceof AnonymousTargetContext) {
			return targetContext;
		}

		// if the node is a PC, redirect to the PM_ADMIN_PCs node
		long nodeId = targetContext.resolveNodeIds(policyStore.graph()).iterator().next();
		Node targetNode = policyStore.graph().getNodeById(nodeId);

		if (targetNode.getType().equals(PC)) {
			return NodeTargetContext.of(PM_ADMIN_POLICY_CLASSES.nodeId());
		}

		return targetContext;
	}

	/**
	 * Collects every node id referenced in the inclusion or exclusion set of any of the given
	 * prohibitions.
	 */
	protected Set<Long> collectUserProhibitionAttributes(Set<Prohibition> prohibitions) {
		Set<Long> userProhibitionAttrs = new HashSet<>();
		for (Prohibition prohibition : prohibitions) {
			userProhibitionAttrs.addAll(prohibition.getInclusionSet());
			userProhibitionAttrs.addAll(prohibition.getExclusionSet());
		}

		return userProhibitionAttrs;
	}

	/**
	 * Mutable state threaded through one target-side traversal: the target's first-level ascendants, the
	 * node ids referenced by the user's prohibitions, the per-node per-policy-class privileges
	 * accumulated so far, and the prohibition targets reached during the walk.
	 */
	protected record TraversalState(Collection<Long> firstLevelDescs,
									Set<Long> userProhibitionTargets,
									Map<Long, Map<Long, AccessRightSet>> visitedNodes,
									Set<Long> visitedProhibitionTargets) { }
}
