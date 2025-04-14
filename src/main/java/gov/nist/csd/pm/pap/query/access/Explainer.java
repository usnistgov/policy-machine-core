package gov.nist.csd.pm.pap.query.access;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.dag.TargetDagResult;
import gov.nist.csd.pm.common.graph.dag.UserDagResult;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.query.model.explain.*;
import gov.nist.csd.pm.pap.store.PolicyStore;

import java.util.*;

import static gov.nist.csd.pm.pap.query.access.AccessRightResolver.*;

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
		UserDagResult userDagResult = userEvaluator.evaluate(userCtx);

		// evaluate target
		TargetEvaluator targetEvaluator = new TargetEvaluator(policyStore);
		TargetDagResult targetDagResult = targetEvaluator.evaluate(userDagResult, targetCtx);

		// resolve privs and prohibitions
		AccessRightSet priv = resolvePrivileges(userDagResult, targetDagResult, policyStore.operations().getResourceOperations());
		AccessRightSet deniedPriv = resolveDeniedAccessRights(userDagResult, targetDagResult);
		List<Prohibition> prohibitions = computeSatisfiedProhibitions(userDagResult, targetDagResult);

		return new Explain(priv, resolvedPaths, deniedPriv, prohibitions);
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
					long target = pathAssoc.getTarget();
					if (target != node.getId()) {
						continue;
					}

					Node uaNode = policyStore.graph().getNodeById(pathAssoc.getSource());
					Set<Path> userPathsToAssoc = userPaths.getOrDefault(uaNode, new HashSet<>());

					explainAssocs.add(new ExplainAssociation(
							uaNode,
							pathAssoc.getAccessRightSet(),
							new ArrayList<>(userPathsToAssoc)
					));
				}

				explainNodes.add(new ExplainNode(node, explainAssocs));
			}

			paths.add(explainNodes);
		}

		return paths;
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
