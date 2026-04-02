package gov.nist.csd.pm.core.pap.query.access;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.dag.Direction;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.graph.Association;
import gov.nist.csd.pm.core.pap.graph.dag.BreadthFirstGraphWalker;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.model.context.AttributeIdsContext;
import gov.nist.csd.pm.core.pap.query.model.context.AttributeNamesContext;
import gov.nist.csd.pm.core.pap.query.model.context.CompositeUserContext;
import gov.nist.csd.pm.core.pap.query.model.context.SingleUserContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserIdContext;
import gov.nist.csd.pm.core.pap.query.model.context.UsernameContext;
import gov.nist.csd.pm.core.pap.store.GraphStoreBFS;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
	 * For a CompositeUserContext, returns one UserDagResult per sub-context. For all other contexts,
	 * returns a single-element list.
	 *
	 * @return a UserEvaluationResult containing one UserDagResult per (sub-)context.
	 */
	public UserEvaluationResult evaluate(UserContext userContext) throws PMException {
		return switch (userContext) {
			case CompositeUserContext c -> {
				List<UserDagResult> results = new ArrayList<>();
				for (SingleUserContext ctx : c.contexts()) {
					ctx.checkExists(policyStore.graph());
					results.add(evaluateSingle(ctx));
				}
				yield new UserEvaluationResult(results);
			}
			case SingleUserContext ctx -> {
				ctx.checkExists(policyStore.graph());
				yield new UserEvaluationResult(List.of(evaluateSingle(ctx)));
			}
		};
	}

	private UserDagResult evaluateSingle(SingleUserContext userContext) throws PMException {
		Map<Long, AccessRightSet> borderTargets = new HashMap<>();
		Set<Prohibition> reachedProhibitions = new HashSet<>();

		String process = userContext.getProcess();
		if (process != null && !process.isEmpty()) {
			reachedProhibitions.addAll(policyStore.prohibitions().getProcessProhibitions(process));
		}

		BreadthFirstGraphWalker bfs = new GraphStoreBFS(policyStore.graph())
				.withDirection(Direction.DESCENDANTS)
				.withVisitor(nodeId -> {
					reachedProhibitions.addAll(policyStore.prohibitions().getNodeProhibitions(nodeId));
					for (Association association : policyStore.graph().getAssociationsWithSource(nodeId)) {
						borderTargets.computeIfAbsent(association.target(), k -> new AccessRightSet())
								.addAll(association.arset());
					}
				});

		Collection<Long> startNodes = switch (userContext) {
			case AttributeIdsContext ctx -> ctx.getAdjacentDescendants(policyStore.graph());
			case AttributeNamesContext ctx -> ctx.getAdjacentDescendants(policyStore.graph());
			case UsernameContext ctx -> List.of(policyStore.graph().getNodeByName(ctx.username()).getId());
			case UserIdContext ctx -> List.of(ctx.userId());
		};
		bfs.walk(startNodes);

		return new UserDagResult(borderTargets, reachedProhibitions);
	}
}
