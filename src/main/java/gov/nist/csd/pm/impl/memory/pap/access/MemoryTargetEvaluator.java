package gov.nist.csd.pm.impl.memory.pap.access;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.dag.*;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.pap.dag.DepthFirstGraphWalker;
import gov.nist.csd.pm.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.pap.store.GraphStoreDFS;
import gov.nist.csd.pm.pap.store.PolicyStore;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

import java.util.*;

import static gov.nist.csd.pm.common.graph.node.NodeType.PC;
import static gov.nist.csd.pm.pap.admin.AdminPolicyNode.PM_ADMIN_OBJECT;

public class MemoryTargetEvaluator {

	private final PolicyStore policyStore;

	public MemoryTargetEvaluator(PolicyStore policyStore) {
		this.policyStore = policyStore;
	}

	/**
	 * Perform a depth first search on the object side of the graph.  Start at the target node and recursively visit nodes
	 * until a policy class is reached.  On each node visited, collect any operation the user has on the target. At the
	 * end of each dfs iteration the visitedNodes map will contain the operations the user is permitted on the target under
	 * each policy class.
	 */
	public TargetDagResult evaluate(UserDagResult userCtx, TargetContext targetCtx) throws PMException {
		targetCtx.checkExists(policyStore.graph());

		Set<Long> policyClasses = new LongOpenHashSet(policyStore.graph().getPolicyClasses());
		Map<Long, AccessRightSet> borderTargets = userCtx.borderTargets();
		Set<Long> userProhibitionTargets = collectUserProhibitionTargets(userCtx.prohibitions());
		Map<Long, Map<Long, AccessRightSet>> visitedNodes = new Long2ObjectOpenHashMap<>();
		Set<Long> reachedTargets = new LongOpenHashSet();

		Visitor visitor = node -> {
			// mark the node as reached, to be used for resolving prohibitions
			if (userProhibitionTargets.contains(node)) {
				reachedTargets.add(node);
			}

			Map<Long, AccessRightSet> nodeCtx = visitedNodes.getOrDefault(node, new Long2ObjectOpenHashMap<>());
			if (nodeCtx.isEmpty()) {
				visitedNodes.put(node, nodeCtx);
			}

			if (policyClasses.contains(node)) {
				nodeCtx.put(node, new AccessRightSet());
			} else if (borderTargets.containsKey(node)) {
				Set<String> uaOps = borderTargets.get(node);

				for (long pc : nodeCtx.keySet()) {
					AccessRightSet pcOps = nodeCtx.getOrDefault(pc, new AccessRightSet());
					pcOps.addAll(uaOps);
					nodeCtx.put(pc, pcOps);
				}
			}
		};

		Propagator propagator = (desc, asc) -> {
			Map<Long, AccessRightSet> descCtx = visitedNodes.get(desc);
			Map<Long, AccessRightSet> ascCtx = visitedNodes.getOrDefault(asc, new HashMap<>());

			for (long name : descCtx.keySet()) {
				AccessRightSet ops = ascCtx.getOrDefault(name, new AccessRightSet());
				ops.addAll(descCtx.get(name));
				ascCtx.put(name, ops);
			}

			visitedNodes.put(asc, ascCtx);
		};

		DepthFirstGraphWalker dfs = new GraphStoreDFS(policyStore.graph())
				.withDirection(Direction.DESCENDANTS)
				.withVisitor(visitor)
				.withPropagator(propagator);

		Collection<Long> targetNodes;
		if (targetCtx.isNode()) {
			// if target of decision is a PC, use the PM_ADMIN_OBJECT as the target node
			long target = targetCtx.getTargetId();
			Node targetNode = policyStore.graph().getNodeById(target);
			if (targetNode.getType().equals(PC)) {
				target = PM_ADMIN_OBJECT.nodeId();
			}

			targetNodes = new LongArrayList(LongList.of(target));

			dfs.walk(target);
		} else {
			targetNodes = targetCtx.getAttributeIds();

			dfs.walk(targetNodes);
		}

		return new TargetDagResult(mergeResults(targetNodes, visitedNodes), reachedTargets);
	}

	private Set<Long> collectUserProhibitionTargets(Set<Prohibition> prohibitions) {
		Set<Long> userProhibitionTargets = new HashSet<>();
		for (Prohibition prohibition : prohibitions) {
			for (ContainerCondition cc : prohibition.getContainers()) {
				userProhibitionTargets.add(cc.getId());
			}
		}

		return userProhibitionTargets;
	}

	private Map<Long, AccessRightSet> mergeResults(Collection<Long> targetNodes, Map<Long, Map<Long, AccessRightSet>> visitedNodes) {
		Map<Long, AccessRightSet> merged = new Long2ObjectOpenHashMap<>();

		for (long target : targetNodes) {
			Map<Long, AccessRightSet> pcMap = visitedNodes.getOrDefault(target, new HashMap<>());

			for (Map.Entry<Long, AccessRightSet> entry : pcMap.entrySet()) {
				long pc = entry.getKey();
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

}
