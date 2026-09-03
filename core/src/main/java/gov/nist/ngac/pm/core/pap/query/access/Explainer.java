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

import static gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightResolver.computeSatisfiedProhibitions;
import static gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightResolver.resolveDeniedAccessRights;
import static gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightResolver.resolvePrivileges;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.graph.Association;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pap.query.model.explain.Explain;
import gov.nist.ngac.pm.core.pap.query.model.explain.ExplainAssociation;
import gov.nist.ngac.pm.core.pap.query.model.explain.ExplainNode;
import gov.nist.ngac.pm.core.pap.query.model.explain.Path;
import gov.nist.ngac.pm.core.pap.query.model.explain.PolicyClassExplain;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Builds a full {@link Explain} of a user's access to a target.
 */
public class Explainer {

	private final PolicyStore policyStore;

	public Explainer(PolicyStore policyStore) {
		this.policyStore = policyStore;
	}

	/**
	 * Computes the full explanation of a user's access to a target.
	 *
	 * @param userCtx the user to explain access for
	 * @param targetCtx the target to explain access to
	 * @return the privileges, denied privileges, satisfied prohibitions, and justifying paths
	 * @throws PMException if evaluation fails
	 */
	public Explain explain(UserContext userCtx, TargetContext targetCtx) throws PMException {
		// resolve paths from u to target
		List<PolicyClassExplain> resolvedPaths = resolvePaths(userCtx, targetCtx);

		// evaluate user
		UserEvaluator userEvaluator = new UserEvaluator(policyStore);
		UserDagResult userDagResult = userEvaluator.evaluate(userCtx);

		// evaluate target and resolve privs per UserDagResult, intersecting across composite sub-contexts
		TargetEvaluator targetEvaluator = new TargetEvaluator(policyStore);
		List<Prohibition> prohibitions = new ArrayList<>();

		TargetDagResult targetDagResult = targetEvaluator.evaluate(userDagResult, targetCtx);
		AccessRightSet priv = resolvePrivileges(userDagResult, targetDagResult, policyStore.operations().getResourceAccessRights());
		AccessRightSet deniedPriv = resolveDeniedAccessRights(userDagResult.prohibitions(), targetDagResult);
		prohibitions.addAll(computeSatisfiedProhibitions(userDagResult, targetDagResult));

		return new Explain(
			priv,
			resolvedPaths,
			deniedPriv,
			prohibitions
		);
	}

	private List<PolicyClassExplain> resolvePaths(UserContext userCtx, TargetContext targetCtx) throws PMException {
		UserExplainer userExplainer = new UserExplainer(policyStore);
		TargetExplainer targetExplainer = new TargetExplainer(policyStore);
		Map<Node, Map<Path, List<Association>>> targetPaths = targetExplainer.explainTarget(targetCtx);
		Map<Node, Set<Path>> userPaths = userExplainer.explainIntersectionOfTargetPaths(userCtx, targetPaths);

		List<PolicyClassExplain> result = new ArrayList<>();

		for (Map.Entry<Node, Map<Path, List<Association>>> targetPathEntry : targetPaths.entrySet()) {
			Node pc = targetPathEntry.getKey();
			Map<Path, List<Association>> targetPathAssociations = targetPathEntry.getValue();

			Collection<List<ExplainNode>> paths = getExplainNodePaths(targetPathAssociations, userPaths);
			AccessRightSet arset = getArsetFromPaths(paths);

			result.add(new PolicyClassExplain(pc, arset, paths));
		}

		return result;
	}

	private Collection<List<ExplainNode>> getExplainNodePaths(Map<Path, List<Association>> targetPathAssociations,
	                                                          Map<Node, Set<Path>> userPaths) throws PMException {
		Collection<List<ExplainNode>> paths = new ArrayList<>();

		for (Map.Entry<Path, List<Association>> targetPathEntry : targetPathAssociations.entrySet()) {
			Path path = targetPathEntry.getKey();
			List<Association> pathAssocs = targetPathEntry.getValue();

			List<ExplainNode> explainNodes = new ArrayList<>();
			for (Node node : path) {
				List<ExplainAssociation> explainAssocs = new ArrayList<>();

				for (Association pathAssoc : pathAssocs) {
					long target = pathAssoc.target();
					if (target != node.getId()) {
						continue;
					}

					Node uaNode = policyStore.graph().getNodeById(pathAssoc.source());
					Set<Path> userPathsToAssoc = userPaths.getOrDefault(uaNode, new HashSet<>());

					explainAssocs.add(new ExplainAssociation(
						uaNode,
						pathAssoc.arset(),
						new ArrayList<>(userPathsToAssoc)
					));
				}

				explainNodes.add(new ExplainNode(node, explainAssocs));
			}

			paths.add(explainNodes);
		}

		return paths;
	}

	private static AccessRightSet intersect(AccessRightSet a, AccessRightSet b) {
		AccessRightSet result = new AccessRightSet();
		result.addAll(a);
		result.retainAll(b);
		return result;
	}

	private AccessRightSet getArsetFromPaths(Collection<List<ExplainNode>> paths) {
		AccessRightSet accessRightSet = new AccessRightSet();
		for (Collection<ExplainNode> path : paths) {
			for (ExplainNode explainNode : path) {
				for (ExplainAssociation association : explainNode.associations()) {
					if (association.userPaths().isEmpty()) {
						continue;
					}

					accessRightSet.addAll(association.arset());
				}
			}
		}

		return accessRightSet;
	}
}
