package gov.nist.csd.pm.impl.neo4j.memory.pap.query;

import gov.nist.csd.pm.impl.memory.pap.access.MemoryAccessQuerier;
import gov.nist.csd.pm.impl.neo4j.memory.pap.store.Neo4jMemoryPolicyStore;
import gov.nist.csd.pm.pap.AccessQuerier;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.dag.TargetDagResult;
import gov.nist.csd.pm.common.graph.dag.UserDagResult;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.query.model.explain.Explain;
import gov.nist.csd.pm.pap.query.model.subgraph.SubgraphPrivileges;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.graphdb.traversal.Uniqueness;

import java.util.*;

import static gov.nist.csd.pm.impl.neo4j.memory.pap.store.Neo4jUtil.*;

public class Neo4jMemoryAccessQuerier extends AccessQuerier {

	private Neo4jMemoryPolicyStore store;
	private MemoryAccessQuerier memoryAccessQuerier;

	public Neo4jMemoryAccessQuerier(Neo4jMemoryPolicyStore store) {
		super(store);
		this.store = store;
		this.memoryAccessQuerier = new MemoryAccessQuerier(store);
	}

	@Override
	public AccessRightSet computePrivileges(UserContext userCtx, TargetContext targetCtx) throws PMException {
		return memoryAccessQuerier.computePrivileges(userCtx, targetCtx);
	}

	@Override
	public List<AccessRightSet> computePrivileges(UserContext userCtx, List<TargetContext> targetCtxs) throws PMException {
		return memoryAccessQuerier.computePrivileges(userCtx, targetCtxs);
	}

	@Override
	public AccessRightSet computeDeniedPrivileges(UserContext userCtx, TargetContext targetCtx) throws PMException {
		return memoryAccessQuerier.computeDeniedPrivileges(userCtx, targetCtx);
	}

	@Override
	public Map<Long, AccessRightSet> computeCapabilityList(UserContext userCtx) throws PMException {
		return memoryAccessQuerier.computeCapabilityList(userCtx);
	}

	@Override
	public Map<Long, AccessRightSet> computeACL(TargetContext targetCtx) throws PMException {
		return memoryAccessQuerier.computeACL(targetCtx);
	}

	@Override
	public Map<gov.nist.csd.pm.common.graph.node.Node, AccessRightSet> computeDestinationAttributes(UserContext userCtx) throws PMException {
		return memoryAccessQuerier.computeDestinationAttributes(userCtx);
	}

	@Override
	public SubgraphPrivileges computeSubgraphPrivileges(UserContext userCtx, long root) throws PMException {
		return memoryAccessQuerier.computeSubgraphPrivileges(userCtx, root);
	}

	@Override
	public Map<String, AccessRightSet> computeAdjacentAscendantPrivileges(UserContext userCtx, String root) throws PMException {
		return memoryAccessQuerier.computeAdjacentAscendantPrivileges(userCtx, root);
	}

	@Override
	public Map<gov.nist.csd.pm.common.graph.node.Node, AccessRightSet> computeAdjacentDescendantPrivileges(UserContext userCtx, String root) throws PMException {
		return memoryAccessQuerier.computeAdjacentDescendantPrivileges(userCtx, root);
	}

	@Override
	public Explain explain(UserContext userCtx, TargetContext targetCtx) throws PMException {
		return memoryAccessQuerier.explain(userCtx, targetCtx);
	}

	@Override
	public Map<gov.nist.csd.pm.common.graph.node.Node, AccessRightSet> computePersonalObjectSystem(UserContext userCtx) throws PMException {
		return memoryAccessQuerier.computePersonalObjectSystem(userCtx);
	}

	private UserDagResult evaluateUser(Transaction tx, UserContext userCtx) throws PMException {
		Map<String, AccessRightSet> assocs = new HashMap<>();
		Set<Prohibition> userPros = new HashSet<>();

		Collection<String> attrs;
		if (userCtx.isUserDefined()) {
			attrs = store.graph().getAdjacentDescendants(userCtx.getUser());

			// visit user
			Collection<Prohibition> prohibitions = store.prohibitions().getProhibitions().getOrDefault(userCtx.getUser(), new ArrayList<>());
			userPros.addAll(prohibitions);
		} else {
			attrs = userCtx.getAttributeIds();
		}

		for (String userAttr : attrs) {
			Traverser traverser = traverseUserAttr(tx, userAttr);

			// process user paths
			for (org.neo4j.graphdb.Path path : traverser) {
				Node endNode = path.endNode();
				String endNodeName = endNode.getProperty(NAME_PROPERTY).toString();

				if (endNode.hasLabel(PROHIBITION_LABEL)) {
					userPros.add(getProhibitionFromNode(endNode));
				} else {
					Relationship last = path.lastRelationship();
					AccessRightSet arset = new AccessRightSet((String[]) last.getProperty(ARSET_PROPERTY));

					assocs.put(endNodeName, arset);
				}
			}
		}

		return new UserDagResult(assocs, userPros);
	}

	private Traverser traverseUserAttr(Transaction tx, String userAttr) throws PMException {
		Node uNode = tx.findNode(UA_LABEL, NAME_PROPERTY, userAttr);

		return tx.traversalDescription()
				.breadthFirst()
				.relationships(ASSIGNMENT_RELATIONSHIP_TYPE, Direction.OUTGOING)
				.relationships(ASSOCIATION_RELATIONSHIP_TYPE, Direction.OUTGOING)
				.relationships(PROHIBITION_SUBJECT_REL_TYPE, Direction.OUTGOING)
				.uniqueness(Uniqueness.NONE)
				.evaluator(path -> {
					// check if last rel is an association
					Relationship last = path.lastRelationship();
					if (last == null) {
						return Evaluation.EXCLUDE_AND_CONTINUE;
					} else if (last.isType(ASSOCIATION_RELATIONSHIP_TYPE)) {
						return Evaluation.INCLUDE_AND_CONTINUE;
					}

					// check for reach prohibitions
					Node endNode = path.endNode();
					if (endNode.hasLabel(PROHIBITION_LABEL)) {
						return Evaluation.INCLUDE_AND_CONTINUE;
					}

					return Evaluation.EXCLUDE_AND_CONTINUE;
				})
				.traverse(uNode);
	}

	private TargetDagResult evaluateTarget(Transaction tx, TargetContext targetCtx, UserDagResult userDagResult) throws PMException {
		Map<String, AccessRightSet> pcMap = new HashMap<>();
		Set<String> reachedContainers = new HashSet<>();

		AccessRightSet arsetToTarget = new AccessRightSet();
		Collection<String> attrs;
		if (targetCtx.isNode()) {
			reachedContainers.add(targetCtx.getTargetId());

			attrs = store.graph().getAdjacentDescendants(targetCtx.getTargetId());

			Node targetNode = tx.findNode(NODE_LABEL, NAME_PROPERTY, targetCtx.getTargetId());
			ResourceIterable<Relationship> relationships = targetNode.getRelationships(Direction.INCOMING, ASSOCIATION_RELATIONSHIP_TYPE);
			for (Relationship relationship : relationships) {
				arsetToTarget.addAll(Arrays.asList((String[]) relationship.getProperty(ARSET_PROPERTY)));
			}
		} else {
			attrs = targetCtx.getAttributeIds();
		}

		for (String targetAttr : attrs) {
			Traverser traverser = traverseTarget(tx, targetAttr);

			for (org.neo4j.graphdb.Path path : traverser) {
				Node endNode = path.endNode();
				String endNodeName = String.valueOf(endNode.getProperty(NAME_PROPERTY));

				if (endNode.hasLabel(PROHIBITION_LABEL)) {
					Relationship proContRel = path.lastRelationship();
					Node contNode = proContRel.getStartNode();
					reachedContainers.add(contNode.getProperty(NAME_PROPERTY).toString());
				} else {
					AccessRightSet pcArs = pcMap.getOrDefault(endNodeName, new AccessRightSet());

					for (Node node : path.nodes()) {
						String nodeName = node.getProperty(NAME_PROPERTY).toString();

						AccessRightSet arset = userDagResult.borderTargets().getOrDefault(nodeName, new AccessRightSet());
						pcArs.addAll(arset);

						pcMap.put(endNodeName, pcArs);
					}
				}
			}
		}

		for (Map.Entry<String, AccessRightSet> entry : pcMap.entrySet()) {
			AccessRightSet pcArset = pcMap.getOrDefault(entry.getKey(), new AccessRightSet());
			pcArset.addAll(arsetToTarget);
			pcMap.computeIfAbsent(entry.getKey(), k -> {
				AccessRightSet arset = new AccessRightSet(arsetToTarget);
				arset.addAll(entry.getValue());
				return arset;
			});
		}

		return new TargetDagResult(pcMap, reachedContainers);
	}

	private Traverser traverseTarget(Transaction tx, String object) throws PMException {
		Node oNode = tx.findNode(NODE_LABEL, NAME_PROPERTY, object);

		return tx.traversalDescription()
				.breadthFirst()
				.relationships(ASSIGNMENT_RELATIONSHIP_TYPE, Direction.OUTGOING)
				.relationships(PROHIBITION_CONTAINER_REL_TYPE, Direction.OUTGOING)
				.uniqueness(Uniqueness.NONE)
				.evaluator(path -> {
					// check if last node is a Prohibition or PC
					Node endNode = path.endNode();
					if ((endNode.hasLabel(PROHIBITION_LABEL) || endNode.hasLabel(PC_LABEL))) {
						return Evaluation.INCLUDE_AND_CONTINUE;
					}

					return Evaluation.EXCLUDE_AND_CONTINUE;
				})
				.traverse(oNode);
	}
}
