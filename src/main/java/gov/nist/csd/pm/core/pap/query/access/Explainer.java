package gov.nist.csd.pm.core.pap.query.access;

import static gov.nist.csd.pm.core.pap.operation.accessright.AccessRightResolver.computeSatisfiedProhibitions;
import static gov.nist.csd.pm.core.pap.operation.accessright.AccessRightResolver.resolveDeniedAccessRights;
import static gov.nist.csd.pm.core.pap.operation.accessright.AccessRightResolver.resolvePrivileges;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.graph.Association;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.query.model.explain.Explain;
import gov.nist.csd.pm.core.pap.query.model.explain.ExplainAssociation;
import gov.nist.csd.pm.core.pap.query.model.explain.ExplainNode;
import gov.nist.csd.pm.core.pap.query.model.explain.Path;
import gov.nist.csd.pm.core.pap.query.model.explain.PolicyClassExplain;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Explainer {

	private final PolicyStore policyStore;

	public Explainer(PolicyStore policyStore) {
		this.policyStore = policyStore;
	}

	public Explain explain(UserContext userCtx, TargetContext targetCtx) throws PMException {
		// resolve paths from u to target
		List<PolicyClassExplain> resolvedPaths = resolvePaths(userCtx, targetCtx);

		// evaluate user
		UserEvaluator userEvaluator = new UserEvaluator(policyStore);
		UserEvaluationResult userEvaluationResult = userEvaluator.evaluate(userCtx);

		// evaluate target and resolve privs per UserDagResult, intersecting across composite sub-contexts
		TargetEvaluator targetEvaluator = new TargetEvaluator(policyStore);
		AccessRightSet priv = null;
		AccessRightSet deniedPriv = null;
		List<Prohibition> prohibitions = new ArrayList<>();
		for (UserDagResult userDagResult : userEvaluationResult.dagResults()) {
			TargetDagResult targetDagResult = targetEvaluator.evaluate(userDagResult, targetCtx);
			AccessRightSet p = resolvePrivileges(userDagResult, targetDagResult, policyStore.operations().getResourceAccessRights());
			AccessRightSet d = resolveDeniedAccessRights(userDagResult, targetDagResult);
			priv = (priv == null) ? p : intersect(priv, p);
			deniedPriv = (deniedPriv == null) ? d : intersect(deniedPriv, d);
			prohibitions.addAll(computeSatisfiedProhibitions(userDagResult, targetDagResult));
		}

		return new Explain(
				priv == null ? new AccessRightSet() : priv,
				resolvedPaths,
				deniedPriv == null ? new AccessRightSet() : deniedPriv,
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
