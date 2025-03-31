package gov.nist.csd.pm.impl.memory.pap.access;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.dag.DepthFirstGraphWalker;
import gov.nist.csd.pm.common.graph.dag.Propagator;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.query.model.explain.Path;
import gov.nist.csd.pm.pap.store.GraphStoreDFS;
import gov.nist.csd.pm.pap.store.PolicyStore;

import java.util.*;

public class MemoryUserExplainer {

	private final PolicyStore policyStore;

	public MemoryUserExplainer(PolicyStore policyStore) {
		this.policyStore = policyStore;
	}

	public Map<Node, Set<Path>> explainIntersectionOfTargetPaths(UserContext userCtx, Map<Node, Map<Path, List<Association>>> targetPaths) throws PMException {
		userCtx.checkExists(policyStore.graph());

		// initialize map with the UAs of the target path associations
		Map<Node, Set<Path>> associationUAPaths = new HashMap<>();
		Set<Long> uasFromTargetPathAssociations = new HashSet<>(getUAsFromTargetPathAssociations(targetPaths));
		Map<Node, Set<Path>> pathsToUAs = new HashMap<>();
		for (long ua : uasFromTargetPathAssociations) {
			Node node = policyStore.graph().getNodeById(ua);
			pathsToUAs.put(node, new HashSet<>(Set.of(new Path(node))));
		}

		Propagator propagator = (src, dst) -> {
			Node dstNode = policyStore.graph().getNodeById(dst);
			Node srcNode = policyStore.graph().getNodeById(src);
			
			// don't propagate unless the src is a ua in a target path association or an already propagated to dst node
			if (!uasFromTargetPathAssociations.contains(src) && !pathsToUAs.containsKey(srcNode)) {
				return;
			}

			Set<Path> srcPaths = pathsToUAs.get(srcNode);
			Set<Path> dstPaths = pathsToUAs.getOrDefault(dstNode, new HashSet<>());

			for (Path srcPath : srcPaths) {
				Path copy = new Path(srcPath);
				copy.addFirst(dstNode);
				dstPaths.add(copy);
			}

			pathsToUAs.put(dstNode, dstPaths);
		};

		DepthFirstGraphWalker dfs = new GraphStoreDFS(policyStore.graph())
				.withPropagator(propagator);

		List<Long> nodes = new ArrayList<>();
		if (userCtx.isUserDefined()) {
			long user = userCtx.getUser();
			nodes.add(user);

			dfs.walk(user);
		} else {
			Collection<Long> attributes = userCtx.getAttributeIds();
			nodes.addAll(attributes);

			dfs.walk(attributes);
		}

		// transform the map so that the key is the last ua in the path pointing to it's paths
		for (long node : nodes) {
			Set<Path> userPaths = pathsToUAs.getOrDefault(policyStore.graph().getNodeById(node), new HashSet<>());

			for (Path userPath : userPaths) {
				Node assocUA = userPath.getLast();
				Set<Path> assocUAPaths = associationUAPaths.getOrDefault(assocUA, new HashSet<>());
				assocUAPaths.add(userPath);
				associationUAPaths.put(assocUA, assocUAPaths);
			}
		}

		return associationUAPaths;
	}

	private List<Long> getUAsFromTargetPathAssociations(Map<Node, Map<Path, List<Association>>> targetPaths) {
		List<Long> uas = new ArrayList<>();

		for (Map.Entry<Node, Map<Path, List<Association>>> pcPaths : targetPaths.entrySet()) {
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
