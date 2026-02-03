package gov.nist.csd.pm.core.pap.query.access;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.dag.Direction;
import gov.nist.csd.pm.core.common.graph.dag.Visitor;
import gov.nist.csd.pm.core.pap.operation.accessrights.AccessRightSet;
import gov.nist.csd.pm.core.pap.graph.Association;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.graph.dag.BreadthFirstGraphWalker;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.store.GraphStoreBFS;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserEvaluator {

	private final PolicyStore policyStore;

	public UserEvaluator(PolicyStore policyStore) {
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
	public UserDagResult evaluate(UserContext userContext) throws PMException {
		userContext.checkExists(policyStore.graph());

		EvaluationState state = initializeEvaluationState(userContext);
		BreadthFirstGraphWalker bfs = createBreadthFirstWalker(state);
		executeEvaluation(userContext, bfs);

		return new UserDagResult(state.borderTargets, state.reachedProhibitions);
	}

	private EvaluationState initializeEvaluationState(UserContext userContext) throws PMException {
		Map<Long, AccessRightSet> borderTargets = new HashMap<>();
		Set<Prohibition> reachedProhibitions = initializeProcessProhibitions(userContext);
		
		return new EvaluationState(borderTargets, reachedProhibitions);
	}

	private Set<Prohibition> initializeProcessProhibitions(UserContext userContext) throws PMException {
		Set<Prohibition> prohibitions = new HashSet<>();
		if (userContext.getProcess() != null) {
			prohibitions.addAll(policyStore.prohibitions().getProhibitionsWithProcess(userContext.getProcess()));
		}
		return prohibitions;
	}

	private BreadthFirstGraphWalker createBreadthFirstWalker(EvaluationState state) {
		Visitor nodeVisitor = createNodeVisitor(state);
		
		return new GraphStoreBFS(policyStore.graph())
				.withDirection(Direction.DESCENDANTS)
				.withVisitor(nodeVisitor);
	}

	private Visitor createNodeVisitor(EvaluationState state) {
		return nodeId -> {
			collectNodeProhibitions(nodeId, state.reachedProhibitions);
			collectNodeAssociations(nodeId, state.borderTargets);
		};
	}

	private void collectNodeProhibitions(long nodeId, Set<Prohibition> reachedProhibitions) throws PMException {
		Collection<Prohibition> nodeProhibitions = policyStore.prohibitions().getProhibitionsWithNode(nodeId);
		reachedProhibitions.addAll(nodeProhibitions);
	}

	private void collectNodeAssociations(long nodeId, Map<Long, AccessRightSet> borderTargets) throws PMException {
		Collection<Association> nodeAssociations = policyStore.graph().getAssociationsWithSource(nodeId);
		collectAssociationsIntoBorderTargets(nodeAssociations, borderTargets);
	}

	private void executeEvaluation(UserContext userContext, BreadthFirstGraphWalker bfs) throws PMException {
		if (userContext.isUserDefined()) {
			bfs.walk(userContext.getUser());
		} else {
			bfs.walk(userContext.getAttributeIds());
		}
	}

	private void collectAssociationsIntoBorderTargets(Collection<Association> associations, 
													 Map<Long, AccessRightSet> borderTargets) {
		for (Association association : associations) {
			long targetId = association.target();
			AccessRightSet associationRights = association.arset();
			
			AccessRightSet existingRights = borderTargets.computeIfAbsent(targetId, k -> new AccessRightSet());
			existingRights.addAll(associationRights);
		}
	}

	private record EvaluationState(Map<Long, AccessRightSet> borderTargets,
								   Set<Prohibition> reachedProhibitions) { }
}
