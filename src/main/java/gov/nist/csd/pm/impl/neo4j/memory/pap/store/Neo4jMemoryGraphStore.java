package gov.nist.csd.pm.impl.neo4j.memory.pap.store;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.pap.query.model.subgraph.AscendantSubgraph;
import gov.nist.csd.pm.pap.query.model.subgraph.DescendantSubgraph;
import gov.nist.csd.pm.pap.store.GraphStore;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.graphdb.traversal.Uniqueness;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static gov.nist.csd.pm.impl.neo4j.memory.pap.store.Neo4jUtil.*;

public class Neo4jMemoryGraphStore implements GraphStore {

	private TxHandler txHandler;

	public Neo4jMemoryGraphStore(TxHandler txHandler) {
		this.txHandler = txHandler;
	}

	@Override
	public void createNode(long id, String name, NodeType type) throws PMException {
		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node node = tx.createNode(NODE_LABEL, typeToLabel(type));
			node.setProperty(NAME_PROPERTY, name);
		});
	}

	@Override
	public void deleteNode(long id) throws PMException {
		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node node = tx.findNode(NODE_LABEL, NAME_PROPERTY, id);

			// delete edges
			for (Relationship relationship : node.getRelationships()) {
				relationship.delete();
			}

			// delete node
			node.delete();
		});
	}

	@Override
	public void setNodeProperties(long name, Map<String, String> properties) throws PMException {
		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node node = tx.findNode(NODE_LABEL, NAME_PROPERTY, name);

			Iterable<String> propertyKeys = node.getPropertyKeys();
			for (String key : propertyKeys) {
				node.removeProperty(key);
			}

			for (Map.Entry<String, String> entry : properties.entrySet()) {
				node.setProperty(entry.getKey(), entry.getValue());
			}

			node.setProperty(NAME_PROPERTY, name);
		});
	}

	@Override
	public void createAssignment(long start, long end) throws PMException {
		txHandler.runTx(tx -> {
			tx.findNode(NODE_LABEL, NAME_PROPERTY, start)
					.createRelationshipTo(tx.findNode(NODE_LABEL, NAME_PROPERTY, end), ASSIGNMENT_RELATIONSHIP_TYPE);
		});
	}

	@Override
	public void deleteAssignment(long start, long end) throws PMException {
		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node childNode = tx.findNode(NODE_LABEL, NAME_PROPERTY, start);
			try(ResourceIterable<Relationship> relationships = childNode.getRelationships(Direction.OUTGOING, ASSIGNMENT_RELATIONSHIP_TYPE)) {
				for (Relationship relationship : relationships) {
					if (relationship.getEndNode().getProperty(NAME_PROPERTY).equals(end)) {
						relationship.delete();
					}
				}
			}
		});
	}

	@Override
	public void createAssociation(long ua, long target, AccessRightSet arset) throws PMException {
		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node uaNode = tx.findNode(NODE_LABEL, NAME_PROPERTY, ua);
			org.neo4j.graphdb.Node targetNode = tx.findNode(NODE_LABEL, NAME_PROPERTY, target);

			String[] arsetArr = arset.toArray(String[]::new);

			boolean updated = false;
			ResourceIterable<Relationship> assocRel = uaNode.getRelationships(Direction.OUTGOING, ASSOCIATION_RELATIONSHIP_TYPE);
			for (Relationship relationship : assocRel) {
				if (relationship.getEndNode().equals(targetNode)) {
					relationship.setProperty(ARSET_PROPERTY, arsetArr);
					updated = true;
				}
			}

			if (!updated) {
				uaNode.createRelationshipTo(targetNode, ASSOCIATION_RELATIONSHIP_TYPE)
						.setProperty(ARSET_PROPERTY, arsetArr);
			}
		});
	}

	@Override
	public void deleteAssociation(long ua, long target) throws PMException {
		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node uaNode = tx.findNode(NODE_LABEL, NAME_PROPERTY, ua);
			try(ResourceIterable<Relationship> relationships = uaNode.getRelationships(Direction.OUTGOING, ASSOCIATION_RELATIONSHIP_TYPE)) {
				for (Relationship relationship : relationships) {
					if (relationship.getEndNode().getProperty(NAME_PROPERTY).equals(target)) {
						relationship.delete();
					}
				}
			}
		});
	}

	@Override
	public Node getNodeById(long name) throws PMException {
		Map<String, String> properties = new HashMap<>();
		AtomicReference<NodeType> typeAtomic  = new AtomicReference<>();

		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node node = tx.findNode(NODE_LABEL, NAME_PROPERTY, name);
			typeAtomic.set(getNodeType(node));

			Map<String, Object> props = node.getAllProperties();
			props.remove("name");


			for (Map.Entry<String, Object> e : props.entrySet()) {
				properties.put(e.getKey(), String.valueOf(e.getValue()));
			}
		});

		return new Node(name, typeAtomic.get(), properties);
	}

	@Override
	public boolean nodeExists(String name) throws PMException {
		AtomicBoolean typeAtomic = new AtomicBoolean();

		txHandler.runTx(tx -> {
			boolean b = tx.findNode(NODE_LABEL, NAME_PROPERTY, name) != null;
			typeAtomic.set(b);
		});

		return typeAtomic.get();
	}

	@Override
	public long[] search(NodeType type, Map<String, String> properties) throws PMException {
		List<String> results = new ArrayList<>();

		txHandler.runTx(tx -> {
			Label label;
			if (type == null || type == NodeType.ANY) {
				label = NODE_LABEL;
			} else {
				label = Label.label(type.toString());
			}

			try(ResourceIterator<org.neo4j.graphdb.Node> iter = tx.findNodes(label)) {
				while (iter.hasNext()) {
					org.neo4j.graphdb.Node next = iter.next();
					Map<String, Object> allProperties = next.getAllProperties();

					if (!hasAllKeys(allProperties, properties)
							|| !valuesMatch(allProperties, properties)) {
						continue;
					}

					results.add(next.getProperty(NAME_PROPERTY).toString());
				}
			}
		});

		return results;
	}

	@Override
	public long[] getPolicyClasses() throws PMException {
		List<String> pcs = new ArrayList<>();

		txHandler.runTx(tx -> {
			try(ResourceIterator<org.neo4j.graphdb.Node> iter = tx.findNodes(PC_LABEL)) {
				while (iter.hasNext()) {
					pcs.add(iter.next().getProperty(NAME_PROPERTY).toString());
				}
			}
		});

		return pcs;
	}

	@Override
	public long[] getAdjacentDescendants(String name) throws PMException {
		List<String> children = new ArrayList<>();

		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node node = tx.findNode(NODE_LABEL, NAME_PROPERTY, name);
			try(ResourceIterable<Relationship> iter = node.getRelationships(
					Direction.OUTGOING,
					ASSIGNMENT_RELATIONSHIP_TYPE
			)) {
				for (Relationship r : iter) {
					children.add(r.getEndNode().getProperty(NAME_PROPERTY).toString());
				}
			}
		});

		return children;
	}

	@Override
	public long[] getAdjacentAscendants(String name) throws PMException {
		List<String> parents = new ArrayList<>();

		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node node = tx.findNode(NODE_LABEL, NAME_PROPERTY, name);
			try(ResourceIterable<Relationship> iter = node.getRelationships(
					Direction.INCOMING,
					ASSIGNMENT_RELATIONSHIP_TYPE
			)) {
				for (Relationship r : iter) {
					parents.add(r.getStartNode().getProperty(NAME_PROPERTY).toString());
				}
			}
		});
		return parents;
	}

	@Override
	public Association[] getAssociationsWithSource(String ua) throws PMException {
		List<Association> assocs = new ArrayList<>();

		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node node = tx.findNode(NODE_LABEL, NAME_PROPERTY, ua);
			try(ResourceIterable<Relationship> iter = node.getRelationships(
					Direction.OUTGOING,
					ASSOCIATION_RELATIONSHIP_TYPE
			)) {
				for (Relationship r : iter) {
					assocs.add(new Association(
							ua,
							r.getEndNode().getProperty(NAME_PROPERTY).toString(),
							new AccessRightSet((String[]) r.getProperty(ARSET_PROPERTY))
					));
				}
			}
		});

		return assocs;
	}

	@Override
	public Collection<Association> getAssociationsWithTarget(String target) throws PMException {
		List<Association> assocs = new ArrayList<>();

		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node node = tx.findNode(NODE_LABEL, NAME_PROPERTY, target);
			try (ResourceIterable<Relationship> iter = node.getRelationships(
					Direction.INCOMING,
					ASSOCIATION_RELATIONSHIP_TYPE
			)) {
				for (Relationship relationship : iter) {
					assocs.add(new Association(
							relationship.getStartNode().getProperty(NAME_PROPERTY).toString(),
							target,
							new AccessRightSet((String[])relationship.getProperty(ARSET_PROPERTY))
					));
				}
			}
		});

		return assocs;
	}

	@Override
	public Collection<String> getPolicyClassDescendants(String node) throws PMException {
		List<String> pcdescs = new ArrayList<>();

		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node uNode = tx.findNode(NODE_LABEL, NAME_PROPERTY, node);

			Traverser traverse = tx.traversalDescription()
					.breadthFirst()
					.relationships(ASSIGNMENT_RELATIONSHIP_TYPE, Direction.OUTGOING)
					.relationships(ASSOCIATION_RELATIONSHIP_TYPE, Direction.OUTGOING)
					.relationships(PROHIBITION_SUBJECT_REL_TYPE, Direction.OUTGOING)
					.uniqueness(Uniqueness.NONE)
					.evaluator(path -> {
						org.neo4j.graphdb.Node endNode = path.endNode();
						if (endNode.hasLabel(PC_LABEL)) {
							return Evaluation.INCLUDE_AND_CONTINUE;
						}

						return Evaluation.EXCLUDE_AND_CONTINUE;
					})
					.traverse(uNode);

			for (Path path : traverse) {
				pcdescs.add(path.endNode().getProperty(NAME_PROPERTY).toString());
			}
		});

		return pcdescs;
	}

	@Override
	public Collection<String> getAttributeDescendants(String node) throws PMException {
		List<String> descs = new ArrayList<>();

		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node uNode = tx.findNode(NODE_LABEL, NAME_PROPERTY, node);

			Traverser traverse = tx.traversalDescription()
					.breadthFirst()
					.relationships(ASSIGNMENT_RELATIONSHIP_TYPE, Direction.OUTGOING)
					.relationships(ASSOCIATION_RELATIONSHIP_TYPE, Direction.OUTGOING)
					.relationships(PROHIBITION_SUBJECT_REL_TYPE, Direction.OUTGOING)
					.uniqueness(Uniqueness.NONE)
					.evaluator(path -> {
						org.neo4j.graphdb.Node endNode = path.endNode();
						if (endNode.hasLabel(UA_LABEL) || endNode.hasLabel(OA_LABEL)) {
							return Evaluation.INCLUDE_AND_CONTINUE;
						}

						return Evaluation.EXCLUDE_AND_CONTINUE;
					})
					.traverse(uNode);

			for (org.neo4j.graphdb.Node travNode : traverse.nodes()) {
				descs.add(travNode.getProperty(NAME_PROPERTY).toString());
			}
		});

		return descs;
	}

	@Override
	public DescendantSubgraph getDescendantSubgraph(String node) throws PMException {
		List<DescendantSubgraph> adjacentSubgraphs = new ArrayList<>();

		txHandler.runTx(tx -> {
			Collection<String> adjacentDescendants = getAdjacentDescendants(node);
			for (String adjacent : adjacentDescendants) {
				adjacentSubgraphs.add(getDescendantSubgraph(adjacent));
			}
		});

		return new DescendantSubgraph(node, adjacentSubgraphs);
	}

	@Override
	public AscendantSubgraph getAscendantSubgraph(String node) throws PMException {
		List<AscendantSubgraph> adjacentSubgraphs = new ArrayList<>();

		txHandler.runTx(tx -> {
			Collection<String> adjacentAscendants = getAdjacentAscendants(node);
			for (String adjacent : adjacentAscendants) {
				adjacentSubgraphs.add(getAscendantSubgraph(adjacent));
			}
		});

		return new AscendantSubgraph(node, adjacentSubgraphs);
	}

	@Override
	public boolean isAscendant(long asc, long dsc) throws PMException {
		AtomicBoolean b = new AtomicBoolean(false);

		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node ascNode = tx.findNode(NODE_LABEL, NAME_PROPERTY, asc);
			org.neo4j.graphdb.Node descNode = tx.findNode(NODE_LABEL, NAME_PROPERTY, dsc);

			Traverser traverse = tx.traversalDescription()
					.breadthFirst()
					.relationships(ASSIGNMENT_RELATIONSHIP_TYPE, Direction.OUTGOING)
					.uniqueness(Uniqueness.NONE)
					.evaluator(path -> {
						org.neo4j.graphdb.Node endNode = path.endNode();

						if (endNode.equals(descNode)) {
							return Evaluation.INCLUDE_AND_PRUNE;
						} else {
							return Evaluation.EXCLUDE_AND_CONTINUE;
						}
					})
					.traverse(ascNode);

			b.set(traverse.iterator().hasNext());
		});

		return b.get();
	}

	@Override
	public boolean isDescendant(long asc, long dsc) throws PMException {
		AtomicBoolean b = new AtomicBoolean(false);

		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node ascNode = tx.findNode(NODE_LABEL, NAME_PROPERTY, asc);
			org.neo4j.graphdb.Node descNode = tx.findNode(NODE_LABEL, NAME_PROPERTY, dsc);

			Traverser traverse = tx.traversalDescription()
					.breadthFirst()
					.relationships(ASSIGNMENT_RELATIONSHIP_TYPE, Direction.OUTGOING)
					.uniqueness(Uniqueness.NONE)
					.evaluator(path -> {
						org.neo4j.graphdb.Node endNode = path.endNode();

						if (endNode.equals(descNode)) {
							return Evaluation.INCLUDE_AND_PRUNE;
						} else {
							return Evaluation.EXCLUDE_AND_CONTINUE;
						}
					})
					.traverse(ascNode);

			b.set(traverse.iterator().hasNext());
		});

		return b.get();
	}

	@Override
	public void beginTx() throws PMException {
		// tx is handled by Transaction field
	}

	@Override
	public void commit() throws PMException {
		// tx is handled by Transaction field
	}

	@Override
	public void rollback() throws PMException {
		// tx is handled by Transaction field
	}

	private boolean valuesMatch(Map<String, Object> nodeProperties, Map<String, String> searchProperties) {
		for (Map.Entry<String, String> entry : searchProperties.entrySet()) {
			String checkKey = entry.getKey();
			String checkValue = entry.getValue();
			if (!checkValue.equals(nodeProperties.get(checkKey))
					&& !checkValue.equals("*")) {
				return false;
			}
		}

		return true;
	}

	private boolean hasAllKeys(Map<String, Object> nodeProperties, Map<String, String> searchProperties) {
		for (String key : searchProperties.keySet()) {
			// ignore the name property
			if (key.equals(NAME_PROPERTY)) {
				continue;
			}

			if (!nodeProperties.containsKey(key)) {
				return false;
			}
		}

		return true;
	}
}
