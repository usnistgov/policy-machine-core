package gov.nist.csd.pm.impl.memory.pap.access;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.dag.DepthFirstGraphWalker;
import gov.nist.csd.pm.pap.graph.dag.Propagator;
import gov.nist.csd.pm.pap.graph.relationship.Association;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.query.model.explain.Path;
import gov.nist.csd.pm.pap.store.GraphStoreDFS;
import gov.nist.csd.pm.pap.store.PolicyStore;

import java.util.*;

public class MemoryUserExplainer {

	private PolicyStore policyStore;

	public MemoryUserExplainer(PolicyStore policyStore) {
		this.policyStore = policyStore;
	}

	public Map<String, Set<Path>> explainIntersectionOfTargetPaths(UserContext userCtx, Map<String, Map<Path, List<Association>>> targetPaths) throws PMException {
		userCtx.checkExists(policyStore.graph());

		// initialize map with the UAs of the target path associations
		Map<String, Set<Path>> associationUAPaths = new HashMap<>();
		Set<String> uasFromTargetPathAssociations = new HashSet<>(getUAsFromTargetPathAssociations(targetPaths));
		Map<String, Set<Path>> pathsToUAs = new HashMap<>();
		for (String ua : uasFromTargetPathAssociations) {
			pathsToUAs.put(ua, new HashSet<>(Set.of(new Path(ua))));
		}

		Propagator propagator = (src, dst) -> {
			// don't propagate unless the src is a ua in a target path association or an already propagated to dst node
			if (!uasFromTargetPathAssociations.contains(src) && !pathsToUAs.containsKey(src)) {
				return;
			}

			Set<Path> srcPaths = pathsToUAs.get(src);
			Set<Path> dstPaths = pathsToUAs.getOrDefault(dst, new HashSet<>());

			for (Path srcPath : srcPaths) {
				Path copy = new Path(srcPath);
				copy.addFirst(dst);
				dstPaths.add(copy);
			}

			pathsToUAs.put(dst, dstPaths);
		};

		DepthFirstGraphWalker dfs = new GraphStoreDFS(policyStore.graph())
				.withPropagator(propagator);

		List<String> nodes = new ArrayList<>();
		if (userCtx.isUser()) {
			String user = userCtx.getUser();
			nodes.add(user);

			dfs.walk(user);
		} else {
			List<String> attributes = userCtx.getAttributes();
			nodes.addAll(attributes);

			dfs.walk(attributes);
		}

		// transform the map so that the key is the last ua in the path pointing to it's paths
		for (String node : nodes) {
			Set<Path> userPaths = pathsToUAs.getOrDefault(node, new HashSet<>());

			for (Path userPath : userPaths) {
				String assocUA = userPath.getLast();
				Set<Path> assocUAPaths = associationUAPaths.getOrDefault(assocUA, new HashSet<>());
				assocUAPaths.add(userPath);
				associationUAPaths.put(assocUA, assocUAPaths);
			}
		}

		return associationUAPaths;
	}

	private List<String> getUAsFromTargetPathAssociations(Map<String, Map<Path, List<Association>>> targetPaths) {
		List<String> uas = new ArrayList<>();

		for (Map.Entry<String, Map<Path, List<Association>>> pcPaths : targetPaths.entrySet()) {
			for (Map.Entry<Path, List<Association>> pathAssociations : pcPaths.getValue().entrySet()) {
				List<Association> associations = pathAssociations.getValue();
				for (Association association : associations) {
					uas.add(association.getSource());
				}
			}
		}

		return uas;
	}
}
