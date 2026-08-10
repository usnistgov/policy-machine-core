package gov.nist.ngac.pm.core.pap.query.access;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.graph.dag.GraphWalker;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.graph.Association;
import gov.nist.ngac.pm.core.pap.graph.dag.BreadthFirstGraphWalker;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Computes a {@link UserDagResult} for a user by walking their ascendant graph.
 */
public class UserEvaluator {

	private final PolicyStore policyStore;

	public UserEvaluator(PolicyStore policyStore) {
		this.policyStore = policyStore;
	}

	/**
	 * Evaluates the user's ascendant graph.
	 *
	 * @return the border targets and prohibitions reachable by the user
	 */
	public UserDagResult evaluate(UserContext ctx) throws PMException {
		Map<Long, AccessRightSet> borderTargets = new HashMap<>();
		Set<Prohibition> reachedProhibitions = new HashSet<>();

		String process = ctx.getProcess();
		if (process != null && !process.isEmpty()) {
			reachedProhibitions.addAll(policyStore.prohibitions().getProcessProhibitions(process));
		}

		GraphWalker bfs = new BreadthFirstGraphWalker(policyStore.graph()::getAdjacentDescendants)
			.withVisitor(nodeId -> {
				reachedProhibitions.addAll(policyStore.prohibitions().getNodeProhibitions(nodeId));
				for (Association association : policyStore.graph().getAssociationsWithSource(nodeId)) {
					borderTargets.computeIfAbsent(association.target(), k -> new AccessRightSet())
						.addAll(association.arset());
				}
			});

		for (long id : ctx.resolveNodeIds(policyStore.graph())) {
			bfs.walk(id);
		}

		return new UserDagResult(borderTargets, reachedProhibitions);
	}
}
