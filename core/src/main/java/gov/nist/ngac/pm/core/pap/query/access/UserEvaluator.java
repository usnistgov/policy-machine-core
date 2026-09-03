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
