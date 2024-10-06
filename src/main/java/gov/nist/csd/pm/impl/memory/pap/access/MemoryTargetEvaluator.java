package gov.nist.csd.pm.impl.memory.pap.access;

import gov.nist.csd.pm.pap.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.dag.*;
import gov.nist.csd.pm.pap.graph.node.Node;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.pap.store.GraphStoreDFS;
import gov.nist.csd.pm.pap.store.PolicyStore;

import java.util.*;

import static gov.nist.csd.pm.pap.admin.AdminPolicyNode.PM_ADMIN_OBJECT;
import static gov.nist.csd.pm.pap.graph.node.NodeType.PC;

public class MemoryTargetEvaluator {

	private PolicyStore policyStore;

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
		List<String> targetNodes = targetCtx.getNodes();
		for (int i = 0; i < targetNodes.size(); i++) {
			String target = targetNodes.get(i);
			if (!policyStore.graph().nodeExists(target)) {
				throw new NodeDoesNotExistException(target);
			}

			Node targetNode = policyStore.graph().getNode(target);
			if (targetNode.getType().equals(PC)) {
				 targetNodes.set(i, PM_ADMIN_OBJECT.nodeName());
			}
		}

		Collection<String> policyClasses = policyStore.graph().getPolicyClasses();
		Map<String, AccessRightSet> borderTargets = userCtx.borderTargets();
		Map<String, Map<String, AccessRightSet>> visitedNodes = new HashMap<>();
		Set<String> reachedTargets = new HashSet<>();

		for (String target : targetNodes) {
			Visitor visitor = node -> {
				// mark the node as reached, to be used for resolving prohibitions
				if (userCtx.prohibitionTargets().contains(node)) {
					reachedTargets.add(node);
				}

				Map<String, AccessRightSet> nodeCtx = visitedNodes.getOrDefault(node, new HashMap<>());
				if (nodeCtx.isEmpty()) {
					visitedNodes.put(node, nodeCtx);
				}

				if (policyClasses.contains(node)) {
					nodeCtx.put(node, new AccessRightSet());
				} else {
					if (borderTargets.containsKey(node)) {
						Set<String> uaOps = borderTargets.get(node);
						for (String pc : nodeCtx.keySet()) {
							AccessRightSet pcOps = nodeCtx.getOrDefault(pc, new AccessRightSet());
							pcOps.addAll(uaOps);
							nodeCtx.put(pc, pcOps);
						}
					}
				}
			};

			Propagator propagator = (desc, asc) -> {
				Map<String, AccessRightSet> descCtx = visitedNodes.get(desc);
				Map<String, AccessRightSet> nodeCtx = visitedNodes.getOrDefault(asc, new HashMap<>());
				for (String name : descCtx.keySet()) {
					AccessRightSet ops = nodeCtx.getOrDefault(name, new AccessRightSet());
					ops.addAll(descCtx.get(name));
					nodeCtx.put(name, ops);
				}
				visitedNodes.put(asc, nodeCtx);
			};

			new GraphStoreDFS(policyStore.graph())
					.withDirection(Direction.DESCENDANTS)
					.withVisitor(visitor)
					.withPropagator(propagator)
					.walk(target);
		}

		return new TargetDagResult(mergeResults(targetNodes, visitedNodes), reachedTargets);
	}

	private Map<String, AccessRightSet> mergeResults(List<String> targetNodes, Map<String, Map<String, AccessRightSet>> visitedNodes) {
		HashMap<String, AccessRightSet> merged = new HashMap<>();

		for (String target : targetNodes) {
			Map<String, AccessRightSet> pcMap = visitedNodes.getOrDefault(target, new HashMap<>());

			for (Map.Entry<String, AccessRightSet> entry : pcMap.entrySet()) {
				String pc = entry.getKey();
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
