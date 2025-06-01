package gov.nist.csd.pm.core.pap.query.access;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.dag.*;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.dag.DepthFirstGraphWalker;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.store.GraphStoreDFS;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

import java.util.*;

import static gov.nist.csd.pm.core.common.graph.node.NodeType.PC;
import static gov.nist.csd.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_POLICY_CLASSES;

public class TargetEvaluator {

	private final PolicyStore policyStore;

	public TargetEvaluator(PolicyStore policyStore) {
		this.policyStore = policyStore;
	}

	/**
	 * Perform a depth first search on the object side of the graph.  Start at the target node and recursively visit nodes
	 * until a policy class is reached.  On each node visited, collect any operation the user has on the target. At the
	 * end of each dfs iteration the visitedNodes map will contain the operations the user is permitted on the target under
	 * each policy class. If the PM_ADMIN_POLICY_CLASSES is a border target, then add the associated access rights to every policy
	 * class by default.
	 */
	public TargetDagResult evaluate(UserDagResult userContext, TargetContext targetContext) throws PMException {
		targetContext.checkExists(policyStore.graph());
		prepareTargetContext(targetContext);

		EvaluationState state = initializeEvaluationState(userContext, targetContext);
		DepthFirstGraphWalker dfs = createDepthFirstWalker(state);
		List<Long> targetNodes = executeEvaluation(targetContext, dfs);
		
		return new TargetDagResult(mergeResults(targetNodes, state.visitedNodes), state.reachedTargets);
	}

	private EvaluationState initializeEvaluationState(UserDagResult userContext, TargetContext targetContext) throws PMException {
		Set<Long> policyClasses = new LongOpenHashSet(policyStore.graph().getPolicyClasses());
		Map<Long, AccessRightSet> borderTargets = userContext.borderTargets();
		Set<Long> userProhibitionTargets = collectUserProhibitionTargets(userContext.prohibitions());
		Map<Long, Map<Long, AccessRightSet>> visitedNodes = new Long2ObjectOpenHashMap<>();
		Set<Long> reachedTargets = new LongOpenHashSet();

		AccessRightSet adminPrivilegesOnPCs = computeAdminPrivilegesOnPCs(userContext, targetContext, policyClasses);

		return new EvaluationState(policyClasses, borderTargets, userProhibitionTargets, 
			visitedNodes, reachedTargets, adminPrivilegesOnPCs);
	}

	private AccessRightSet computeAdminPrivilegesOnPCs(UserDagResult userContext, TargetContext targetContext,
												  Set<Long> policyClasses) throws PMException {
		Collection<Long> firstLevelDescendantPCs = new ArrayList<>(targetContext.getAttributeIds());
		firstLevelDescendantPCs.retainAll(policyClasses);
		
		if (firstLevelDescendantPCs.isEmpty()) {
			return new AccessRightSet();
		}
		
		return computePrivsOnPCAdminNode(userContext, firstLevelDescendantPCs, policyClasses);
	}

	private DepthFirstGraphWalker createDepthFirstWalker(EvaluationState state) {
		Visitor nodeVisitor = createNodeVisitor(state);
		Propagator privilegePropagator = createPrivilegePropagator(state);

		return new GraphStoreDFS(policyStore.graph())
			.withDirection(Direction.DESCENDANTS)
			.withVisitor(nodeVisitor)
			.withPropagator(privilegePropagator);
	}

	private Visitor createNodeVisitor(EvaluationState state) {
		return nodeId -> {
			markProhibitionTargetIfReached(nodeId, state);
			Map<Long, AccessRightSet> nodePrivileges = getOrCreateNodePrivileges(nodeId, state.visitedNodes);
			
			if (state.policyClasses.contains(nodeId)) {
				nodePrivileges.put(nodeId, state.adminPrivilegesOnPCs);
			} else if (state.borderTargets.containsKey(nodeId)) {
				applyBorderTargetPrivileges(nodeId, nodePrivileges, state.borderTargets);
			}
		};
	}

	private Propagator createPrivilegePropagator(EvaluationState state) {
		return (descendantId, ancestorId) -> {
			Map<Long, AccessRightSet> descendantPrivileges = state.visitedNodes.get(descendantId);
			Map<Long, AccessRightSet> ancestorPrivileges = getOrCreateNodePrivileges(ancestorId, state.visitedNodes);

			propagatePrivileges(descendantPrivileges, ancestorPrivileges);
			state.visitedNodes.put(ancestorId, ancestorPrivileges);
		};
	}

	private void markProhibitionTargetIfReached(long nodeId, EvaluationState state) {
		if (state.userProhibitionTargets.contains(nodeId)) {
			state.reachedTargets.add(nodeId);
		}
	}

	private Map<Long, AccessRightSet> getOrCreateNodePrivileges(long nodeId, 
															   Map<Long, Map<Long, AccessRightSet>> visitedNodes) {
		return visitedNodes.computeIfAbsent(nodeId, k -> new Long2ObjectOpenHashMap<>());
	}

	private void applyBorderTargetPrivileges(long nodeId, Map<Long, AccessRightSet> nodePrivileges, 
											Map<Long, AccessRightSet> borderTargets) {
		AccessRightSet borderOperations = borderTargets.get(nodeId);
		
		nodePrivileges.forEach((policyClassId, privileges) -> privileges.addAll(borderOperations));
	}

	private void propagatePrivileges(Map<Long, AccessRightSet> fromPrivileges, 
									Map<Long, AccessRightSet> toPrivileges) {
		fromPrivileges.forEach((policyClassId, privileges) -> {
			AccessRightSet targetPrivileges = toPrivileges.computeIfAbsent(policyClassId, k -> new AccessRightSet());
			targetPrivileges.addAll(privileges);
		});
	}

	private List<Long> executeEvaluation(TargetContext targetContext, DepthFirstGraphWalker dfs) throws PMException {
		List<Long> targetNodes = new ArrayList<>();
		
		if (targetContext.isNode()) {
			targetNodes.add(targetContext.getTargetId());
			dfs.walk(targetContext.getTargetId());
		} else {
			targetNodes.addAll(targetContext.getAttributeIds());
			dfs.walk(targetContext.getAttributeIds());
		}
		
		return targetNodes;
	}

	private AccessRightSet computePrivsOnPCAdminNode(UserDagResult userDagResult,
													 Collection<Long> firstLevelDescendants,
													 Collection<Long> policyClasses) throws PMException {
		List<Long> policyClassDescendants = new ArrayList<>(firstLevelDescendants);
		policyClassDescendants.retainAll(policyClasses);

		// there are no policy class adjacent descendants, so no privs to evaluate for
		if (policyClassDescendants.isEmpty()) {
			return new AccessRightSet();
		}

		// evaluate the privileges this user has on the PM_ADMIN_POLICY_CLASSES node
		// these privs represent the access rights the user has on policy classes
		TargetDagResult adminTargetResult = evaluate(userDagResult, new TargetContext(PM_ADMIN_POLICY_CLASSES.nodeId()));
		return AccessRightResolver.resolvePrivileges(
			userDagResult,
			adminTargetResult,
			policyStore.operations().getResourceOperations()
		);
	}

	private void prepareTargetContext(TargetContext targetContext) throws PMException {
		Collection<Long> firstLevelDescendants = determineFirstLevelDescendants(targetContext);
		targetContext.setAttributeIds(firstLevelDescendants);
	}

	private Collection<Long> determineFirstLevelDescendants(TargetContext targetContext) throws PMException {
		if (targetContext.isNode()) {
			long targetId = adjustTargetIdForPolicyClass(targetContext.getTargetId());
			targetContext.setTargetId(targetId);
			return policyStore.graph().getAdjacentDescendants(targetId);
		} else {
			return targetContext.getAttributeIds();
		}
	}

	private long adjustTargetIdForPolicyClass(long targetId) throws PMException {
		Node targetNode = policyStore.graph().getNodeById(targetId);
		return targetNode.getType().equals(PC) ? PM_ADMIN_POLICY_CLASSES.nodeId() : targetId;
	}

	private Set<Long> collectUserProhibitionTargets(Set<Prohibition> prohibitions) {
		Set<Long> userProhibitionTargets = new HashSet<>();
		for (Prohibition prohibition : prohibitions) {
			for (ContainerCondition containerCondition : prohibition.getContainers()) {
				userProhibitionTargets.add(containerCondition.getId());
			}
		}
		return userProhibitionTargets;
	}

	private Map<Long, AccessRightSet> mergeResults(Collection<Long> targetNodes, 
												  Map<Long, Map<Long, AccessRightSet>> visitedNodes) {
		Long2ObjectOpenHashMap<AccessRightSet> mergedResults = new Long2ObjectOpenHashMap<>();

		targetNodes.forEach(targetId -> 
			visitedNodes.getOrDefault(targetId, Collections.emptyMap())
				.forEach((policyClassId, privileges) -> 
					mergedResults.merge(policyClassId, privileges, this::intersectPrivileges)
				)
		);

		return mergedResults;
	}

	private AccessRightSet intersectPrivileges(AccessRightSet existing, AccessRightSet incoming) {
		existing.retainAll(incoming);
		return existing;
	}

	private record EvaluationState(Set<Long> policyClasses,
								   Map<Long, AccessRightSet> borderTargets,
								   Set<Long> userProhibitionTargets,
								   Map<Long, Map<Long, AccessRightSet>> visitedNodes,
								   Set<Long> reachedTargets,
								   AccessRightSet adminPrivilegesOnPCs) { }
}
