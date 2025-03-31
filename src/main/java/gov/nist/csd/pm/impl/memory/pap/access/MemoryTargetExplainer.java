package gov.nist.csd.pm.impl.memory.pap.access;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.dag.DepthFirstGraphWalker;
import gov.nist.csd.pm.common.graph.dag.Propagator;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.pap.query.model.explain.Path;
import gov.nist.csd.pm.pap.store.GraphStoreDFS;
import gov.nist.csd.pm.pap.store.PolicyStore;

import java.util.*;

import static gov.nist.csd.pm.common.graph.node.NodeType.PC;
import static gov.nist.csd.pm.pap.admin.AdminPolicyNode.PM_ADMIN_OBJECT;

public class MemoryTargetExplainer {

	private final PolicyStore policyStore;

	public MemoryTargetExplainer(PolicyStore policyStore) {
		this.policyStore = policyStore;
	}

	public Map<Node, Map<Path, List<Association>>> explainTarget(TargetContext targetCtx) throws PMException {
		targetCtx.checkExists(policyStore.graph());

		Collection<Long> policyClasses = policyStore.graph().getPolicyClasses();

		// initialize map with policy classes
		Map<Node, Map<List<Node>, List<Association>>> pcPathAssociations = new HashMap<>();
		for (long pc : policyClasses) {
			Node pcNode = policyStore.graph().getNodeById(pc);
			pcPathAssociations.put(pcNode, new HashMap<>(Map.of(new ArrayList<>(List.of(pcNode)), new ArrayList<>())));
		}

		Propagator propagator = (src, dst) -> {
			Node srcNode = policyStore.graph().getNodeById(src);
			Node dstNode =  policyStore.graph().getNodeById(dst);

			Map<List<Node>, List<Association>> srcPathAssocs = pcPathAssociations.get(srcNode);
			Map<List<Node>, List<Association>> dstPathAssocs = pcPathAssociations.getOrDefault(dstNode, new HashMap<>());

			for (Map.Entry<List<Node>, List<Association>> entry : srcPathAssocs.entrySet()) {
				// add DST to the path from SRC
				List<Node> targetPath = new ArrayList<>(entry.getKey());
				List<Association> associations = new ArrayList<>(entry.getValue());
				targetPath.addFirst(dstNode);

				// collect any associations for the DST node
				Association[] associationsWithTarget = policyStore.graph().getAssociationsWithTarget(dst).toArray(new Association[0]);
				associations.addAll(List.of(associationsWithTarget));
				dstPathAssocs.put(targetPath, associations);
			}

			// update dst entry
			pcPathAssociations.put(dstNode, dstPathAssocs);
		};

		// DFS from target node
		DepthFirstGraphWalker dfs = new GraphStoreDFS(policyStore.graph())
				.withPropagator(propagator);

		List<Node> nodes = new ArrayList<>();
		if (targetCtx.isNode()) {
			long target = targetCtx.getTargetId();
			Node targetNode = policyStore.graph().getNodeById(target);
			if (targetNode.getType().equals(PC)) {
				target = PM_ADMIN_OBJECT.nodeId();
			}

			nodes.add(targetNode);

			dfs.walk(target);
		} else {
			for (long id : targetCtx.getAttributeIds()) {
				nodes.add(policyStore.graph().getNodeById(id));
			}

			dfs.walk(targetCtx.getAttributeIds());
		}

		// convert the map created above into a map where the policy classes are the keys
		Map<Node, Map<Path, List<Association>>> pcMap = new HashMap<>();

		for (Node target : nodes) {
			Map<List<Node>, List<Association>> targetPathAssocs = pcPathAssociations.get(target);
			for (Map.Entry<List<Node>, List<Association>> entry : targetPathAssocs.entrySet()) {
				Path targetPath = new Path(entry.getKey());
				List<Association> associations = new ArrayList<>(entry.getValue());

				Node pc = targetPath.getLast();

				Map<Path, List<Association>> pcPathAssocs = pcMap.getOrDefault(pc, new HashMap<>());
				pcPathAssocs.put(targetPath, associations);
				pcMap.put(pc, pcPathAssocs);
			}
		}

		return pcMap;
	}
}
