package gov.nist.csd.pm.core.pap.query.access;

import static gov.nist.csd.pm.core.common.graph.node.NodeType.PC;
import static gov.nist.csd.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_POLICY_CLASSES;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.dag.Direction;
import gov.nist.csd.pm.core.common.graph.dag.Propagator;
import gov.nist.csd.pm.core.common.graph.dag.Visitor;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.graph.dag.DepthFirstGraphWalker;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightResolver;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.model.context.AnonymousTargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.AttributeIdsTargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.AttributeNamesTargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.ContextChecker;
import gov.nist.csd.pm.core.pap.query.model.context.IdTargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.NameTargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.store.GraphStoreDFS;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
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
		DepthFirstGraphWalker dfs = createDepthFirstWalker(userDagResult, state);

		// walk the target graph starting at the first level descs
		List<Long> targetNodes = new ArrayList<>();
		switch (targetContext) {
			case IdTargetContext ctx -> {
				targetNodes.add(ctx.targetId());
				dfs.walk(ctx.targetId());
			}
			case NameTargetContext ctx -> {
				long id = policyStore.graph().getNodeByName(ctx.targetName()).getId();
				targetNodes.add(id);
				dfs.walk(id);
			}
			case AttributeIdsTargetContext ctx -> {
				targetNodes.addAll(ctx.attributeIds());
				dfs.walk(ctx.attributeIds());
			}
			case AttributeNamesTargetContext ctx -> {
				Collection<Long> ids = resolveAttributeNames(ctx.attributeNames());
				targetNodes.addAll(ids);
				dfs.walk(ids);
			}
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
					mergedArset.retainAll(pcArset);
					merged.put(pc, mergedArset);
				}
			}
		}

		return merged;
	}

	protected TraversalState initializeEvaluationState(UserDagResult userDagResult, TargetContext targetCtx) throws PMException {
		Collection<Long> firstLevelDescs = new LongArrayList();
		switch (targetCtx) {
			case IdTargetContext ctx ->
				firstLevelDescs.addAll(policyStore.graph().getAdjacentDescendants(ctx.targetId()));
			case NameTargetContext ctx -> {
				long id = policyStore.graph().getNodeByName(ctx.targetName()).getId();
				firstLevelDescs.addAll(policyStore.graph().getAdjacentDescendants(id));
			}
			case AttributeIdsTargetContext ctx ->
				firstLevelDescs.addAll(ctx.attributeIds());
			case AttributeNamesTargetContext ctx ->
				firstLevelDescs.addAll(resolveAttributeNames(ctx.attributeNames()));
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
		Collection<Long> firstLevelDescendantPCs = new ArrayList<>(firstLevelDescs);
		firstLevelDescendantPCs.retainAll(policyClasses);

		if (firstLevelDescendantPCs.isEmpty()) {
			return new AccessRightSet();
		}

		// retain all policy class first level descs
		List<Long> policyClassDescendants = new ArrayList<>(firstLevelDescs);
		policyClassDescendants.retainAll(policyClasses);

		// there are no policy class adjacent descendants, so no privs to evaluate for
		if (policyClassDescendants.isEmpty()) {
			return new AccessRightSet();
		}

		// evaluate the privileges this user has on the PM_ADMIN_POLICY_CLASSES node
		// these privs represent the access rights the user has on policy classes
		TargetDagResult adminTargetResult = evaluate(userDagResult, new IdTargetContext(PM_ADMIN_POLICY_CLASSES.nodeId()));
		return AccessRightResolver.resolvePrivileges(
			userDagResult,
			adminTargetResult,
			policyStore.operations().getResourceAccessRights()
		);
	}

	protected DepthFirstGraphWalker createDepthFirstWalker(UserDagResult userDagResult, TraversalState state) throws PMException {
		Visitor nodeVisitor = createVisitor(userDagResult, state);
		Propagator privilegePropagator = createPropagator(state);

		return new GraphStoreDFS(policyStore.graph())
			.withDirection(Direction.DESCENDANTS)
			.withVisitor(nodeVisitor)
			.withPropagator(privilegePropagator);
	}

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
				nodePrivileges.put(nodeId, adminPrivilegesOnPCs);
			} else if (userDagResult.borderTargets().containsKey(nodeId)) {
				AccessRightSet borderArset = userDagResult.borderTargets().get(nodeId);

				nodePrivileges.forEach((policyClassId, privileges) -> privileges.addAll(borderArset));
			}
		};
	}

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

	protected TargetContext prepareTargetCtx(TargetContext targetContext) throws PMException {
		ContextChecker.checkTargetContextExists(targetContext, policyStore.graph());

		// if already a list of attributes, nothing to prepare
		if (targetContext instanceof AnonymousTargetContext) {
			return targetContext;
		}

		// if the node is a PC, redirect to the PM_ADMIN_PCs node
		Node targetNode = switch ((NodeTargetContext) targetContext) {
			case IdTargetContext ctx -> policyStore.graph().getNodeById(ctx.targetId());
			case NameTargetContext ctx -> policyStore.graph().getNodeByName(ctx.targetName());
		};

		if (targetNode.getType().equals(PC)) {
			return new IdTargetContext(PM_ADMIN_POLICY_CLASSES.nodeId());
		}

		return targetContext;
	}

	protected Set<Long> collectUserProhibitionAttributes(Set<Prohibition> prohibitions) {
		Set<Long> userProhibitionAttrs = new HashSet<>();
		for (Prohibition prohibition : prohibitions) {
			userProhibitionAttrs.addAll(prohibition.getInclusionSet());
			userProhibitionAttrs.addAll(prohibition.getExclusionSet());
		}

		return userProhibitionAttrs;
	}

	private Collection<Long> resolveAttributeNames(Collection<String> names) throws PMException {
		Collection<Long> ids = new ArrayList<>();
		for (String name : names) {
			ids.add(policyStore.graph().getNodeByName(name).getId());
		}
		return ids;
	}

	protected record TraversalState(Collection<Long> firstLevelDescs,
									Set<Long> userProhibitionTargets,
									Map<Long, Map<Long, AccessRightSet>> visitedNodes,
									Set<Long> visitedProhibitionTargets) { }
}
