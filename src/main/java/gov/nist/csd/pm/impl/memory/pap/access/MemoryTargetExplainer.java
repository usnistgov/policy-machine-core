package gov.nist.csd.pm.impl.memory.pap.access;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.dag.Propagator;
import gov.nist.csd.pm.pap.graph.relationship.Association;
import gov.nist.csd.pm.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.pap.query.model.explain.Path;
import gov.nist.csd.pm.pap.store.GraphStoreDFS;
import gov.nist.csd.pm.pap.store.PolicyStore;

import java.util.*;

public class MemoryTargetExplainer {

	private PolicyStore policyStore;

	public MemoryTargetExplainer(PolicyStore policyStore) {
		this.policyStore = policyStore;
	}

	public Map<String, Map<Path, List<Association>>> explainTarget(TargetContext targetCtx) throws PMException {
		Collection<String> policyClasses = policyStore.graph().getPolicyClasses();

		// initialize map with policy classes
		Map<String, Map<Path, List<Association>>> pcMap = new HashMap<>();
		Map<String, Map<List<String>, List<Association>>> pcPathAssociations = new HashMap<>();
		for (String pc : policyClasses) {
			pcPathAssociations.put(pc, new HashMap<>(Map.of(new ArrayList<>(List.of(pc)), new ArrayList<>())));
		}

		List<String> nodes = targetCtx.getNodes();
		for (String target : nodes) {
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
			new GraphStoreDFS(policyStore.graph())
					.withPropagator(propagator)
					.walk(target);

			// convert the map created above into a map where the policy classes are the keys
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
