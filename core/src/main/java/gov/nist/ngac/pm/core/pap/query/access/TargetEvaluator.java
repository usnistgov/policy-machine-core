/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

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
 * Computes the privileges a user has on a target, per policy class, by walking the target's ascendant
 * graph.
 */
public class TargetEvaluator {

	protected final PolicyStore policyStore;

	public TargetEvaluator(PolicyStore policyStore) {
		this.policyStore = policyStore;
	}

	/**
	 * Evaluates a user's privileges on a target, per policy class.
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
	 * Builds the traversal state used by one {@link #evaluate} call.
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
	 * Builds the depth-first {@link GraphWalker} used to traverse the target's ascendant graph.
	 */
	protected GraphWalker createDepthFirstWalker(UserDagResult userDagResult, TraversalState state) throws PMException {
		Visitor nodeVisitor = createVisitor(userDagResult, state);
		Propagator privilegePropagator = createPropagator(state);

		return new DepthFirstGraphWalker(policyStore.graph()::getAdjacentDescendants)
			.withVisitor(nodeVisitor)
			.withPropagator(privilegePropagator);
	}

	/**
	 * Builds the {@link Visitor} that records privileges and prohibition hits for each ascendant node
	 * visited.
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
	 * Builds the {@link Propagator} that merges a descendant's privileges into its ascendant.
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
	 * Redirects a target context for a policy class node to the admin policy classes node, since a policy
	 * class has no ascendants to walk.
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
	 * Collects the node ids referenced in the inclusion or exclusion set of the given prohibitions.
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
	 * Mutable state threaded through one target-side traversal.
	 *
	 * @param firstLevelDescs the target's first-level ascendants, seeding the walk
	 * @param userProhibitionTargets node ids referenced by the user's prohibitions
	 * @param visitedNodes privileges accumulated so far, per node and policy class
	 * @param visitedProhibitionTargets prohibition node ids reached during the walk
	 */
	protected record TraversalState(Collection<Long> firstLevelDescs,
									Set<Long> userProhibitionTargets,
									Map<Long, Map<Long, AccessRightSet>> visitedNodes,
									Set<Long> visitedProhibitionTargets) { }
}
