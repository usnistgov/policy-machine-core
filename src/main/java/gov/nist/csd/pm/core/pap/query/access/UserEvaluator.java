package gov.nist.csd.pm.core.pap.query.access;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.dag.Direction;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.graph.Association;
import gov.nist.csd.pm.core.pap.graph.dag.BreadthFirstGraphWalker;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.model.context.AnonymousUserContext;
import gov.nist.csd.pm.core.pap.query.model.context.AttributeIdsUserContext;
import gov.nist.csd.pm.core.pap.query.model.context.AttributeNamesUserContext;
import gov.nist.csd.pm.core.pap.query.model.context.ConjunctiveUserContext;
import gov.nist.csd.pm.core.pap.query.model.context.ContextChecker;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.query.model.context.IdUserContext;
import gov.nist.csd.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.csd.pm.core.pap.query.model.context.NameUserContext;
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
			case ConjunctiveUserContext c -> {
				List<UserDagResult> results = new ArrayList<>();
				for (UserContext ctx : c.contexts()) {
					results.addAll(evaluate(ctx).dagResults());
				}
				yield new UserEvaluationResult(results);
			}
			case NodeUserContext ctx -> new UserEvaluationResult(List.of(evaluate(ctx)));
			case AnonymousUserContext ctx -> new UserEvaluationResult(List.of(evaluate(ctx)));
		};
	}

	private UserDagResult evaluate(NodeUserContext userContext) throws PMException {
		ContextChecker.checkUserContextExists(userContext, policyStore.graph());

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
			case IdUserContext c -> List.of(c.userId());
			case NameUserContext c -> List.of(policyStore.graph().getNodeByName(c.username()).getId());
		};

		bfs.walk(startNodes);

		return new UserDagResult(borderTargets, reachedProhibitions);
	}

	private UserDagResult evaluate(AnonymousUserContext userContext) throws PMException {
		ContextChecker.checkUserContextExists(userContext, policyStore.graph());

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
			case AttributeIdsUserContext c -> c.attributeIds();
			case AttributeNamesUserContext c -> {
				Collection<Long> ids = new ArrayList<>();
				for (String name : c.attributeNames()) {
					ids.add(policyStore.graph().getNodeByName(name).getId());
				}
				yield ids;
			}
		};
		bfs.walk(startNodes);

		return new UserDagResult(borderTargets, reachedProhibitions);
	}
}
