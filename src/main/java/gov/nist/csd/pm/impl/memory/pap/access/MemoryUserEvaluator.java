package gov.nist.csd.pm.impl.memory.pap.access;

import gov.nist.csd.pm.impl.memory.pap.store.MemoryPolicyStore;
import gov.nist.csd.pm.pap.GraphQuerier;
import gov.nist.csd.pm.pap.ProhibitionsQuerier;
import gov.nist.csd.pm.pap.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.dag.Direction;
import gov.nist.csd.pm.pap.graph.dag.UserDagResult;
import gov.nist.csd.pm.pap.graph.dag.Visitor;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.graph.relationship.Association;
import gov.nist.csd.pm.pap.prohibition.ContainerCondition;
import gov.nist.csd.pm.pap.prohibition.Prohibition;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.store.GraphStoreBFS;
import gov.nist.csd.pm.pap.store.PolicyStore;

import java.util.*;

public class MemoryUserEvaluator {

	private PolicyStore policyStore;

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
	protected UserDagResult evaluate(UserContext userCtx) throws PMException {
		final Map<String, AccessRightSet> borderTargets = new HashMap<>();
		final Set<String> prohibitionTargets = new HashSet<>();
		// initialize with the prohibitions for the provided process
		final Set<Prohibition> reachedProhibitions = new HashSet<>();
		if (userCtx.getProcess() != null) {
			Collection<Prohibition> processPros = policyStore.prohibitions().getProhibitions().getOrDefault(userCtx.getProcess(), new ArrayList<>());
			reachedProhibitions.addAll(processPros);
		}

		List<String> nodes = userCtx.getNodes();
		for (String subject : nodes) {
			if (!policyStore.graph().nodeExists(subject)) {
				throw new NodeDoesNotExistException(subject);
			}

			// get the associations for the subject, it the subject is a user, nothing will be returned
			// this is only when a UA is the subject
			Collection<Association> subjectAssociations = policyStore.graph().getAssociationsWithSource(subject);
			collectAssociationsFromBorderTargets(subjectAssociations, borderTargets);

			Visitor visitor = node -> {
				Collection<Prohibition> subjectProhibitions = policyStore.prohibitions().getProhibitions().getOrDefault(node, new ArrayList<>());
				reachedProhibitions.addAll(subjectProhibitions);
				for (Prohibition prohibition : subjectProhibitions) {
					Collection<ContainerCondition> containers = prohibition.getContainers();
					for (ContainerCondition cont : containers) {
						prohibitionTargets.add(cont.getName());
					}
				}

				Collection<Association> nodeAssociations = policyStore.graph().getAssociationsWithSource(node);
				collectAssociationsFromBorderTargets(nodeAssociations, borderTargets);
			};

			// start the bfs
			new GraphStoreBFS(policyStore.graph())
					.withDirection(Direction.DESCENDANTS)
					.withVisitor(visitor)
					.walk(subject);
		}

		return new UserDagResult(borderTargets, reachedProhibitions, prohibitionTargets);
	}

	private void collectAssociationsFromBorderTargets(Collection<Association> assocs, Map<String, AccessRightSet> borderTargets) {
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
