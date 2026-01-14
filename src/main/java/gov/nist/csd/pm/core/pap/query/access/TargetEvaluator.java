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
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

import java.util.*;

import static gov.nist.csd.pm.core.common.graph.node.NodeType.PC;
import static gov.nist.csd.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_POLICY_CLASSES;

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
		prepareTargetCtx(targetContext);

		// initialize objects for traversal
		TraversalState state = initializeEvaluationState(userDagResult, targetContext);
		DepthFirstGraphWalker dfs = createDepthFirstWalker(userDagResult, state);

		// walk the target graph starting at the first level descs
		List<Long> targetNodes = new ArrayList<>();
		if (targetContext.isNode()) {
			long targetId = targetContext.getTargetId();
			targetNodes.add(targetId);
			dfs.walk(targetId);
		} else {
			Collection<Long> attributeIds = targetContext.getAttributeIds();
			targetNodes.addAll(attributeIds);
			dfs.walk(attributeIds);
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
		if (targetCtx.isNode()) {
			firstLevelDescs.addAll(policyStore.graph().getAdjacentDescendants(targetCtx.getTargetId()));
		} else {
			firstLevelDescs.addAll(targetCtx.getAttributeIds());
		}

		Set<Long> userProhibitionTargets = collectUserProhibitionTargets(userDagResult.prohibitions());
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
		TargetDagResult adminTargetResult = evaluate(userDagResult, new TargetContext(PM_ADMIN_POLICY_CLASSES.nodeId()));
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

	protected void prepareTargetCtx(TargetContext targetContext) throws PMException {
		// ensure the target context node or attributes exist
		targetContext.checkExists(policyStore.graph());

		// if already list of attributes than nothing to prepare
		if (!targetContext.isNode()) {
			return;
		}

		// if the node is a PC, make the target the PM_ADMIN_PCs node
		long targetId = targetContext.getTargetId();
		Node targetNode = policyStore.graph().getNodeById(targetId);
		targetId = targetNode.getType().equals(PC) ? PM_ADMIN_POLICY_CLASSES.nodeId() : targetId;
		targetContext.setTargetId(targetId);
	}

	protected Set<Long> collectUserProhibitionTargets(Set<Prohibition> prohibitions) {
		Set<Long> userProhibitionTargets = new HashSet<>();
		for (Prohibition prohibition : prohibitions) {
			for (ContainerCondition containerCondition : prohibition.getContainers()) {
				userProhibitionTargets.add(containerCondition.getId());
			}
		}
		return userProhibitionTargets;
	}

	protected record TraversalState(Collection<Long> firstLevelDescs,
									Set<Long> userProhibitionTargets,
									Map<Long, Map<Long, AccessRightSet>> visitedNodes,
									Set<Long> visitedProhibitionTargets) { }
}
