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

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.graph.dag.GraphWalker;
import gov.nist.ngac.pm.core.pap.graph.dag.Propagator;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.pap.graph.Association;
import gov.nist.ngac.pm.core.pap.graph.dag.DepthFirstGraphWalker;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pap.query.model.explain.Path;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Walks the user's ascendant graph to find how the user reaches user attributes referenced by target-side
 * association paths.
 */
public class UserExplainer {

	private final PolicyStore policyStore;

	public UserExplainer(PolicyStore policyStore) {
		this.policyStore = policyStore;
	}

	/**
	 * Finds every ascendant path from the user to a user attribute referenced by the given target paths.
	 *
	 * @param userCtx the user to explain
	 * @param targetPaths the target-side paths to intersect against
	 * @return a map from user attribute node to the user's paths reaching it
	 * @throws PMException if the walk fails
	 */
	public Map<Node, Set<Path>> explainIntersectionOfTargetPaths(UserContext userCtx, Map<Node, Map<Path, List<Association>>> targetPaths) throws PMException {
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

		GraphWalker dfs = new DepthFirstGraphWalker(policyStore.graph()::getAdjacentDescendants)
				.withPropagator(propagator);

		List<Long> nodes = new ArrayList<>(userCtx.resolveNodeIds(policyStore.graph()));
		for (long node : nodes) {
			dfs.walk(node);
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
					uas.add(association.source());
				}
			}
		}

		return uas;
	}
}
