package gov.nist.csd.pm.impl.neo4j.embedded.pap.store;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.pap.query.model.subgraph.Subgraph;
import gov.nist.csd.pm.pap.store.GraphStore;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.graphdb.traversal.Uniqueness;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static gov.nist.csd.pm.impl.neo4j.embedded.pap.store.Neo4jUtil.*;

public class Neo4jEmbeddedGraphStore implements GraphStore {

	private final TxHandler txHandler;

	public Neo4jEmbeddedGraphStore(TxHandler txHandler) {
		this.txHandler = txHandler;
	}

	@Override
	public void createNode(long id, String name, NodeType type) throws PMException {
		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node node = tx.createNode(NODE_LABEL, typeToLabel(type));
			node.setProperty(NAME_PROPERTY, name);
			node.setProperty(ID_PROPERTY, id);
		});
	}

	@Override
	public void deleteNode(long id) throws PMException {
		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node node = tx.findNode(NODE_LABEL, ID_PROPERTY, id);

			// delete edges
			for (Relationship relationship : node.getRelationships()) {
				relationship.delete();
			}

			// delete node
			node.delete();
		});
	}

	@Override
	public void setNodeProperties(long id, Map<String, String> properties) throws PMException {
		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node node = tx.findNode(NODE_LABEL, ID_PROPERTY, id);

			Iterable<String> propertyKeys = node.getPropertyKeys();
			for (String key : propertyKeys) {
				if (key.equals(ID_PROPERTY) || key.equals(NAME_PROPERTY)) {
					continue;
				}

				node.removeProperty(key);
			}

			for (Map.Entry<String, String> e : properties.entrySet()) {
				if (e.getKey().equals(ID_PROPERTY) || e.getKey().equals(NAME_PROPERTY)) {
					continue;
				}

				node.setProperty(e.getKey(), e.getValue());
			}

			node.setProperty(ID_PROPERTY, id);
		});
	}

	@Override
	public void createAssignment(long start, long end) throws PMException {
		txHandler.runTx(tx -> {
			tx.findNode(NODE_LABEL, ID_PROPERTY, start)
					.createRelationshipTo(tx.findNode(NODE_LABEL, ID_PROPERTY, end), ASSIGNMENT_RELATIONSHIP_TYPE);
		});
	}

	@Override
	public void deleteAssignment(long start, long end) throws PMException {
		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node childNode = tx.findNode(NODE_LABEL, ID_PROPERTY, start);
			try(ResourceIterable<Relationship> relationships = childNode.getRelationships(Direction.OUTGOING, ASSIGNMENT_RELATIONSHIP_TYPE)) {
				for (Relationship relationship : relationships) {
					if (relationship.getEndNode().getProperty(ID_PROPERTY).equals(end)) {
						relationship.delete();
					}
				}
			}
		});
	}

	@Override
	public void createAssociation(long ua, long target, AccessRightSet arset) throws PMException {
		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node uaNode = tx.findNode(NODE_LABEL, ID_PROPERTY, ua);
			org.neo4j.graphdb.Node targetNode = tx.findNode(NODE_LABEL, ID_PROPERTY, target);

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
			org.neo4j.graphdb.Node uaNode = tx.findNode(NODE_LABEL, ID_PROPERTY, ua);
			try(ResourceIterable<Relationship> relationships = uaNode.getRelationships(Direction.OUTGOING, ASSOCIATION_RELATIONSHIP_TYPE)) {
				for (Relationship relationship : relationships) {
					if (relationship.getEndNode().getProperty(ID_PROPERTY).equals(target)) {
						relationship.delete();
					}
				}
			}
		});
	}

	@Override
	public Node getNodeById(long id) throws PMException {
		Map<String, String> properties = new HashMap<>();
		AtomicReference<String> nameAtomic  = new AtomicReference<>();
		AtomicReference<NodeType> typeAtomic  = new AtomicReference<>();

		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node node = tx.findNode(NODE_LABEL, ID_PROPERTY, id);
			nameAtomic.set((String) node.getProperty(NAME_PROPERTY));
			typeAtomic.set(getNodeType(node));

			Map<String, Object> props = node.getAllProperties();
			for (Map.Entry<String, Object> e : props.entrySet()) {
				if (e.getKey().equals(ID_PROPERTY) || e.getKey().equals(NAME_PROPERTY)) {
					continue;
				}

				properties.put(e.getKey(), String.valueOf(e.getValue()));
			}
		});

		return new Node(id, nameAtomic.get(), typeAtomic.get(), properties);
	}

	@Override
	public Node getNodeByName(String name) throws PMException {
		Map<String, String> properties = new HashMap<>();
		AtomicReference<Long> idAtomic  = new AtomicReference<>();
		AtomicReference<NodeType> typeAtomic  = new AtomicReference<>();

		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node node = tx.findNode(NODE_LABEL, NAME_PROPERTY, name);
			idAtomic.set((long) node.getProperty(ID_PROPERTY));
			typeAtomic.set(getNodeType(node));

			Map<String, Object> props = node.getAllProperties();
			for (Map.Entry<String, Object> e : props.entrySet()) {
				if (e.getKey().equals(ID_PROPERTY) || e.getKey().equals(NAME_PROPERTY)) {
					continue;
				}

				properties.put(e.getKey(), String.valueOf(e.getValue()));
			}
		});

		return new Node(idAtomic.get(), name, typeAtomic.get(), properties);
	}

	@Override
	public boolean nodeExists(long id) throws PMException {
		AtomicBoolean typeAtomic = new AtomicBoolean();

		txHandler.runTx(tx -> {
			boolean b = tx.findNode(NODE_LABEL, ID_PROPERTY, id) != null;
			typeAtomic.set(b);
		});

		return typeAtomic.get();
	}

	@Override
	public boolean nodeExists(String name) throws PMException {
		AtomicBoolean typeAtomic = new AtomicBoolean();

		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node node = tx.findNode(NODE_LABEL, NAME_PROPERTY, name);
			typeAtomic.set(node != null);
		});

		return typeAtomic.get();
	}

	@Override
	public Collection<Long> search(NodeType type, Map<String, String> properties) throws PMException {
		List<Long> results = new LongArrayList();

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

					results.add((long) next.getProperty(ID_PROPERTY));
				}
			}
		});

		return results;
	}

	@Override
	public Collection<Long> getPolicyClasses() throws PMException {
		LongArrayList pcs = new LongArrayList();

		txHandler.runTx(tx -> {
			try(ResourceIterator<org.neo4j.graphdb.Node> iter = tx.findNodes(PC_LABEL)) {
				while (iter.hasNext()) {
					pcs.add((long) iter.next().getProperty(ID_PROPERTY));
				}
			}
		});

		return pcs;
	}

	@Override
	public Collection<Long> getAdjacentDescendants(long id) throws PMException {
		LongArrayList children = new LongArrayList();

		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node node = tx.findNode(NODE_LABEL, ID_PROPERTY, id);
			try(ResourceIterable<Relationship> iter = node.getRelationships(
					Direction.OUTGOING,
					ASSIGNMENT_RELATIONSHIP_TYPE
			)) {
				for (Relationship r : iter) {
					children.add((long)r.getEndNode().getProperty(ID_PROPERTY));
				}
			}
		});

		return children;
	}

	@Override
	public Collection<Long> getAdjacentAscendants(long id) throws PMException {
		LongArrayList parents = new LongArrayList();

		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node node = tx.findNode(NODE_LABEL, ID_PROPERTY, id);
			try(ResourceIterable<Relationship> iter = node.getRelationships(
					Direction.INCOMING,
					ASSIGNMENT_RELATIONSHIP_TYPE
			)) {
				for (Relationship r : iter) {
					parents.add((long)r.getStartNode().getProperty(ID_PROPERTY));
				}
			}
		});

		return parents;
	}

	@Override
	public Collection<Association> getAssociationsWithSource(long uaId) throws PMException {
		List<Association> assocs = new ArrayList<>();

		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node node = tx.findNode(NODE_LABEL, ID_PROPERTY, uaId);
			try(ResourceIterable<Relationship> iter = node.getRelationships(
					Direction.OUTGOING,
					ASSOCIATION_RELATIONSHIP_TYPE
			)) {
				for (Relationship r : iter) {
					assocs.add(new Association(
							uaId,
							(Long) r.getEndNode().getProperty(ID_PROPERTY),
							new AccessRightSet((String[]) r.getProperty(ARSET_PROPERTY))
					));
				}
			}
		});

		return assocs;
	}

	@Override
	public Collection<Association> getAssociationsWithTarget(long targetId) throws PMException {
		List<Association> assocs = new ArrayList<>();

		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node node = tx.findNode(NODE_LABEL, ID_PROPERTY, targetId);
			try (ResourceIterable<Relationship> iter = node.getRelationships(
					Direction.INCOMING,
					ASSOCIATION_RELATIONSHIP_TYPE
			)) {
				for (Relationship relationship : iter) {
					assocs.add(new Association(
							(long) relationship.getStartNode().getProperty(ID_PROPERTY),
							targetId,
							new AccessRightSet((String[])relationship.getProperty(ARSET_PROPERTY))
					));
				}
			}
		});

		return assocs;
	}

	@Override
	public Collection<Long> getPolicyClassDescendants(long id) throws PMException {
		LongArrayList pcdescs = new LongArrayList();

		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node uNode = tx.findNode(NODE_LABEL, ID_PROPERTY, id);

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
				pcdescs.add((long)path.endNode().getProperty(ID_PROPERTY));
			}
		});

		return pcdescs;
	}

	@Override
	public Collection<Long> getAttributeDescendants(long id) throws PMException {
		LongArrayList descs = new LongArrayList();
		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node uNode = tx.findNode(NODE_LABEL, ID_PROPERTY, id);

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
				descs.add((long) travNode.getProperty(ID_PROPERTY));
			}
		});

		return descs;
	}

	@Override
	public Subgraph getDescendantSubgraph(long id) throws PMException {
		List<Subgraph> adjacentSubgraphs = new ArrayList<>();

		txHandler.runTx(tx -> {
			Collection<Long> adjacentDescendants = getAdjacentDescendants(id);
			for (long adjacent : adjacentDescendants) {
				adjacentSubgraphs.add(getDescendantSubgraph(adjacent));
			}
		});

		return new Subgraph(getNodeById(id), adjacentSubgraphs);
	}

	@Override
	public Subgraph getAscendantSubgraph(long id) throws PMException {
		List<Subgraph> adjacentSubgraphs = new ArrayList<>();

		txHandler.runTx(tx -> {
			Collection<Long> adjacentAscendants = getAdjacentAscendants(id);
			for (long adjacent : adjacentAscendants) {
				adjacentSubgraphs.add(getAscendantSubgraph(adjacent));
			}
		});

		return new Subgraph(getNodeById(id), adjacentSubgraphs);
	}

	@Override
	public boolean isAscendant(long asc, long dsc) throws PMException {
		AtomicBoolean b = new AtomicBoolean(false);

		txHandler.runTx(tx -> {
			org.neo4j.graphdb.Node ascNode = tx.findNode(NODE_LABEL, ID_PROPERTY, asc);
			org.neo4j.graphdb.Node descNode = tx.findNode(NODE_LABEL, ID_PROPERTY, dsc);

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
			org.neo4j.graphdb.Node ascNode = tx.findNode(NODE_LABEL, ID_PROPERTY, asc);
			org.neo4j.graphdb.Node descNode = tx.findNode(NODE_LABEL, ID_PROPERTY, dsc);

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
			// ignore the name and id property
			if (key.equals(NAME_PROPERTY) || key.equals(ID_PROPERTY)) {
				continue;
			}

			if (!nodeProperties.containsKey(key)) {
				return false;
			}
		}

		return true;
	}
}
