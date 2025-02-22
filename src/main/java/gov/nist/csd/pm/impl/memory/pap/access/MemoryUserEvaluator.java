package gov.nist.csd.pm.impl.memory.pap.access;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.dag.BreadthFirstGraphWalker;
import gov.nist.csd.pm.common.graph.dag.Direction;
import gov.nist.csd.pm.common.graph.dag.UserDagResult;
import gov.nist.csd.pm.common.graph.dag.Visitor;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.store.GraphStoreBFS;
import gov.nist.csd.pm.pap.store.PolicyStore;

import java.util.*;

public class MemoryUserEvaluator {

	private final PolicyStore policyStore;

	public MemoryUserEvaluator(PolicyStore policyStore) {
		this.policyStore = policyStore;
	}

	/**
	 * Find the target nodes that are reachable by the subject via an association. This is done by a breadth first search
	 * starting at the subject node and walking up the user side of the graph until all user attributes the subject is assigned
	 * to have been visited.  For each user attribute visited, get the associations it is the source of and store the
	 * target of that association as well as the operations in a map. If a target node is reached multiple times, add any
	 * new operations to the already existing ones.
	 *
	 * @return a Map of target nodes that the subject can reach via associations and the operations the user has on each.
	 */
	public UserDagResult evaluate(UserContext userCtx) throws PMException {
		userCtx.checkExists(policyStore.graph());

		final Map<Long, AccessRightSet> borderTargets = new HashMap<>();
		// initialize with the prohibitions for the provided process
		final Set<Prohibition> reachedProhibitions = new HashSet<>();
		if (userCtx.getProcess() != null) {
			reachedProhibitions.addAll(policyStore.prohibitions().getProhibitionsWithProcess(userCtx.getProcess()));
		}

		Visitor visitor = node -> {
			// cache prohibitions reached by the user
			Collection<Prohibition> subjectProhibitions = policyStore.prohibitions().getProhibitionsWithNode(node);
			reachedProhibitions.addAll(subjectProhibitions);

			Collection<Association> nodeAssociations = policyStore.graph().getAssociationsWithSource(node);
			collectAssociationsFromBorderTargets(nodeAssociations, borderTargets);
		};

		// start the bfs
		BreadthFirstGraphWalker bfs = new GraphStoreBFS(policyStore.graph())
				.withDirection(Direction.DESCENDANTS)
				.withVisitor(visitor);

		if (userCtx.isUserDefined()) {
			bfs.walk(userCtx.getUser());
		} else {
			bfs.walk(userCtx.getAttributeIds());
		}

		return new UserDagResult(borderTargets, reachedProhibitions);
	}

	private void collectAssociationsFromBorderTargets(Collection<Association> assocs, Map<Long, AccessRightSet> borderTargets) {
		for (Association association : assocs) {
			AccessRightSet ops = association.getAccessRightSet();
			AccessRightSet exOps = borderTargets.getOrDefault(association.getTarget(), new AccessRightSet());
			//if the target is not in the map already, put it
			//else add the found operations to the existing ones.
			exOps.addAll(ops);
			borderTargets.put(association.getTarget(), exOps);
		}
	}

}
