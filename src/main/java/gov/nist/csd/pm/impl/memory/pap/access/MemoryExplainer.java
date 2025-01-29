package gov.nist.csd.pm.impl.memory.pap.access;

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

import static gov.nist.csd.pm.pap.AccessRightResolver.*;

public class MemoryExplainer {

	private PolicyStore policyStore;

	public MemoryExplainer(PolicyStore policyStore) {
		this.policyStore = policyStore;
	}

	public Explain explain(UserContext userCtx, TargetContext targetCtx) throws PMException {
		// resolve paths from u to target
		List<PolicyClassExplain> resolvedPaths = resolvePaths(userCtx, targetCtx);

		// evaluate user
		MemoryUserEvaluator userEvaluator = new MemoryUserEvaluator(policyStore);
		UserDagResult userDagResult = userEvaluator.evaluate(userCtx);

		// evaluate target
		MemoryTargetEvaluator targetEvaluator = new MemoryTargetEvaluator(policyStore);
		TargetDagResult targetDagResult = targetEvaluator.evaluate(userDagResult, targetCtx);

		// resolve privs and prohibitions
		AccessRightSet priv = resolvePrivileges(userDagResult, targetDagResult, policyStore.operations().getResourceOperations());
		AccessRightSet deniedPriv = resolveDeniedAccessRights(userDagResult, targetDagResult);
		List<Prohibition> prohibitions = computeSatisfiedProhibitions(userDagResult, targetDagResult);

		return new Explain(priv, resolvedPaths, deniedPriv, prohibitions);
	}

	private List<PolicyClassExplain> resolvePaths(UserContext userCtx, TargetContext targetCtx) throws PMException {
		MemoryUserExplainer userExplainer = new MemoryUserExplainer(policyStore);
		MemoryTargetExplainer targetExplainer = new MemoryTargetExplainer(policyStore);
		Map<Node, Map<Path, List<Association>>> targetPaths = targetExplainer.explainTarget(targetCtx);
		Map<Node, Set<Path>> userPaths = userExplainer.explainIntersectionOfTargetPaths(userCtx, targetPaths);

		List<PolicyClassExplain> result = new ArrayList<>();

		for (Map.Entry<Node, Map<Path, List<Association>>> targetPathEntry : targetPaths.entrySet()) {
			Node pc = targetPathEntry.getKey();
			Map<Path, List<Association>> targetPathAssociations = targetPathEntry.getValue();

			List<List<ExplainNode>> paths = getExplainNodePaths(targetPathAssociations, userPaths);
			AccessRightSet arset = getArsetFromPaths(paths);

			result.add(new PolicyClassExplain(pc, arset, paths));
		}

		return result;
	}

	private List<List<ExplainNode>> getExplainNodePaths(Map<Path, List<Association>> targetPathAssociations,
	                                                    Map<Node, Set<Path>> userPaths) throws PMException {
		List<List<ExplainNode>> paths = new ArrayList<>();

		for (Map.Entry<Path, List<Association>> targetPathEntry : targetPathAssociations.entrySet()) {
			Path path = targetPathEntry.getKey();
			List<Association> pathAssocs = targetPathEntry.getValue();

			List<ExplainNode> explainNodes = new ArrayList<>();
			for (Node node : path) {
				List<ExplainAssociation> explainAssocs = new ArrayList<>();

				for (Association pathAssoc : pathAssocs) {
					long ua = pathAssoc.getSource();
					long target = pathAssoc.getTarget();
					if (target != node.getId()) {
						continue;
					}

					Set<Path> userPathsToAssoc = userPaths.getOrDefault(ua, new HashSet<>());

					explainAssocs.add(new ExplainAssociation(
							policyStore.graph().getNodeById(ua),
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

	private AccessRightSet getArsetFromPaths(List<List<ExplainNode>> paths) {
		AccessRightSet accessRightSet = new AccessRightSet();
		for (List<ExplainNode> path : paths) {
			for (ExplainNode explainNode : path) {
				List<ExplainAssociation> associations = explainNode.associations();
				for (ExplainAssociation association : associations) {
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
