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
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.graphdb.traversal.Uniqueness;

import java.util.*;

import static gov.nist.csd.pm.impl.neo4j.memory.pap.store.Neo4jUtil.*;

public class Neo4jMemoryAccessQuerier extends AccessQuerier {

	private final Neo4jMemoryPolicyStore store;
	private final MemoryAccessQuerier memoryAccessQuerier;

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
	public Map<Long, AccessRightSet> computeDestinationAttributes(UserContext userCtx) throws PMException {
		return memoryAccessQuerier.computeDestinationAttributes(userCtx);
	}

	@Override
	public SubgraphPrivileges computeSubgraphPrivileges(UserContext userCtx, long root) throws PMException {
		return memoryAccessQuerier.computeSubgraphPrivileges(userCtx, root);
	}

	@Override
	public Map<gov.nist.csd.pm.common.graph.node.Node, AccessRightSet> computeAdjacentAscendantPrivileges(UserContext userCtx, long root) throws PMException {
		return memoryAccessQuerier.computeAdjacentAscendantPrivileges(userCtx, root);
	}

	@Override
	public Map<gov.nist.csd.pm.common.graph.node.Node, AccessRightSet> computeAdjacentDescendantPrivileges(UserContext userCtx, long root) throws PMException {
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
		Long2ObjectOpenHashMap<AccessRightSet> assocs = new Long2ObjectOpenHashMap<>();
		Set<Prohibition> userPros = new HashSet<>();

		Collection<Long> attrs;
		if (userCtx.isUserDefined()) {
			attrs = store.graph().getAdjacentDescendants(userCtx.getUser());

			// visit user
			Collection<Prohibition> prohibitions = store.prohibitions().getNodeProhibitions().getOrDefault(userCtx.getUser(), new ArrayList<>());
			userPros.addAll(prohibitions);
		} else {
			attrs = userCtx.getAttributeIds();
		}

		for (long userAttr : attrs) {
			Traverser traverser = traverseUserAttr(tx, userAttr);

			// process user paths
			for (org.neo4j.graphdb.Path path : traverser) {
				Node endNode = path.endNode();
				long endNodeId = (long) endNode.getProperty(ID_PROPERTY);

				if (endNode.hasLabel(PROHIBITION_LABEL)) {
					userPros.add(getProhibitionFromNode(endNode));
				} else {
					Relationship last = path.lastRelationship();
					AccessRightSet arset = new AccessRightSet((String[]) last.getProperty(ARSET_PROPERTY));

					assocs.put(endNodeId, arset);
				}
			}
		}

		return new UserDagResult(assocs, userPros);
	}

	private Traverser traverseUserAttr(Transaction tx, long userAttr) throws PMException {
		Node uNode = tx.findNode(UA_LABEL, ID_PROPERTY, userAttr);

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
		Long2ObjectOpenHashMap<AccessRightSet> pcMap = new Long2ObjectOpenHashMap<>();
		LongOpenHashSet reachedContainers = new LongOpenHashSet();

		AccessRightSet arsetToTarget = new AccessRightSet();
		Collection<Long> attrs;
		if (targetCtx.isNode()) {
			reachedContainers.add(targetCtx.getTargetId());

			attrs = store.graph().getAdjacentDescendants(targetCtx.getTargetId());

			Node targetNode = tx.findNode(NODE_LABEL, ID_PROPERTY, targetCtx.getTargetId());
			ResourceIterable<Relationship> relationships = targetNode.getRelationships(Direction.INCOMING, ASSOCIATION_RELATIONSHIP_TYPE);
			for (Relationship relationship : relationships) {
				arsetToTarget.addAll(Arrays.asList((String[]) relationship.getProperty(ARSET_PROPERTY)));
			}
		} else {
			attrs = targetCtx.getAttributeIds();
		}

		for (long targetAttr : attrs) {
			Traverser traverser = traverseTarget(tx, targetAttr);

			for (org.neo4j.graphdb.Path path : traverser) {
				Node endNode = path.endNode();
				long endNodeName = (long) endNode.getProperty(ID_PROPERTY);

				if (endNode.hasLabel(PROHIBITION_LABEL)) {
					Relationship proContRel = path.lastRelationship();
					Node contNode = proContRel.getStartNode();
					reachedContainers.add((long) contNode.getProperty(ID_PROPERTY));
				} else {
					AccessRightSet pcArs = pcMap.getOrDefault(endNodeName, new AccessRightSet());

					for (Node node : path.nodes()) {
						long nodeId = (long) node.getProperty(ID_PROPERTY);

						AccessRightSet arset = userDagResult.borderTargets().getOrDefault(nodeId, new AccessRightSet());
						pcArs.addAll(arset);

						pcMap.put(endNodeName, pcArs);
					}
				}
			}
		}

		for (Long2ObjectMap.Entry<AccessRightSet> entry : pcMap.long2ObjectEntrySet()) {
			long nodeId = entry.getLongKey();

			AccessRightSet pcArset = entry.getValue();
			if (pcArset == null) {
				pcArset = new AccessRightSet();
				pcMap.put(nodeId, pcArset);  // Initialize in the map if it wasn't present
			}

			pcArset.addAll(arsetToTarget);

			pcMap.put(nodeId, new AccessRightSet(pcArset));
		}

		return new TargetDagResult(pcMap, reachedContainers);
	}

	private Traverser traverseTarget(Transaction tx, long object) throws PMException {
		Node oNode = tx.findNode(NODE_LABEL, ID_PROPERTY, object);

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
