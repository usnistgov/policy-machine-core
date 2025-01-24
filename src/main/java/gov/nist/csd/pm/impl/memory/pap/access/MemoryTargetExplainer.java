package gov.nist.csd.pm.impl.memory.pap.access;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.dag.DepthFirstGraphWalker;
import gov.nist.csd.pm.common.graph.dag.Propagator;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.pap.query.model.explain.Path;
import gov.nist.csd.pm.pap.store.GraphStoreDFS;
import gov.nist.csd.pm.pap.store.PolicyStore;

import java.util.*;

import static gov.nist.csd.pm.pap.admin.AdminPolicyNode.PM_ADMIN_OBJECT;
import static gov.nist.csd.pm.common.graph.node.NodeType.PC;

public class MemoryTargetExplainer {

	private PolicyStore policyStore;

	public MemoryTargetExplainer(PolicyStore policyStore) {
		this.policyStore = policyStore;
	}

	public Map<String, Map<Path, List<Association>>> explainTarget(TargetContext targetCtx) throws PMException {
		targetCtx.checkExists(policyStore.graph());

		Collection<String> policyClasses = policyStore.graph().getPolicyClasses();

		// initialize map with policy classes
		Map<String, Map<List<String>, List<Association>>> pcPathAssociations = new HashMap<>();
		for (String pc : policyClasses) {
			pcPathAssociations.put(pc, new HashMap<>(Map.of(new ArrayList<>(List.of(pc)), new ArrayList<>())));
		}

		Propagator propagator = (src, dst) -> {
			Map<List<String>, List<Association>> srcPathAssocs = pcPathAssociations.get(src);
			Map<List<String>, List<Association>> dstPathAssocs = pcPathAssociations.getOrDefault(dst, new HashMap<>());

			for (Map.Entry<List<String>, List<Association>> entry : srcPathAssocs.entrySet()) {
				// add DST to the path from SRC
				List<String> targetPath = new ArrayList<>(entry.getKey());
				List<Association> associations = new ArrayList<>(entry.getValue());
				targetPath.addFirst(dst);

				// collect any associations for the DST node
				Collection<Association> associationsWithTarget = policyStore.graph().getAssociationsWithTarget(dst);
				associations.addAll(associationsWithTarget);
				dstPathAssocs.put(targetPath, associations);
			}

			// update dst entry
			pcPathAssociations.put(dst, dstPathAssocs);
		};

		// DFS from target node
		DepthFirstGraphWalker dfs = new GraphStoreDFS(policyStore.graph())
				.withPropagator(propagator);

		List<String> nodes = new ArrayList<>();
		if (targetCtx.isNode()) {
			String target = targetCtx.getTargetId();
			Node targetNode = policyStore.graph().getNodeById(target);
			if (targetNode.getType().equals(PC)) {
				target = PM_ADMIN_OBJECT.nodeName();
			}

			nodes.add(target);

			dfs.walk(target);
		} else {
			nodes.addAll(targetCtx.getAttributeIds());

			dfs.walk(targetCtx.getAttributeIds());
		}

		// convert the map created above into a map where the policy classes are the keys
		Map<String, Map<Path, List<Association>>> pcMap = new HashMap<>();

		for (String target : nodes) {
			Map<List<String>, List<Association>> targetPathAssocs = pcPathAssociations.get(target);
			for (Map.Entry<List<String>, List<Association>> entry : targetPathAssocs.entrySet()) {
				Path targetPath = new Path(entry.getKey());
				List<Association> associations = new ArrayList<>(entry.getValue());

				String pc = targetPath.getLast();

				Map<Path, List<Association>> pcPathAssocs = pcMap.getOrDefault(pc, new HashMap<>());
				pcPathAssocs.put(targetPath, associations);
				pcMap.put(pc, pcPathAssocs);
			}
		}

		return pcMap;
	}
}
