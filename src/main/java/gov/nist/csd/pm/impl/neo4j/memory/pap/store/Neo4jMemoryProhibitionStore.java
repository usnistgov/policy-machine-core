package gov.nist.csd.pm.impl.neo4j.memory.pap.store;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.store.ProhibitionsStore;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.checkerframework.checker.units.qual.A;
import org.neo4j.graphdb.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static gov.nist.csd.pm.impl.neo4j.memory.pap.store.Neo4jUtil.*;

public class Neo4jMemoryProhibitionStore implements ProhibitionsStore {

	private final TxHandler txHandler;

	public Neo4jMemoryProhibitionStore(TxHandler txHandler) {
		this.txHandler = txHandler;
	}

	@Override
	public void createProhibition(String name, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, Collection<ContainerCondition> containerConditions) throws PMException {
		txHandler.runTx(tx -> {
			Node prohibitionNode = tx.createNode(PROHIBITION_LABEL);
			prohibitionNode.setProperty(NAME_PROPERTY, name);
			prohibitionNode.setProperty(ARSET_PROPERTY, accessRightSet.toArray(new String[]{}));

			createSubject(tx, subject, prohibitionNode);

			prohibitionNode.setProperty(INTERSECTION_PROPERTY, intersection);

			createContainers(tx, containerConditions, prohibitionNode);
		});
	}

	@Override
	public void deleteProhibition(String name) throws PMException {
		txHandler.runTx(tx -> {
			Node node = tx.findNode(PROHIBITION_LABEL, NAME_PROPERTY, name);
			if (node == null) {
				return;
			}

			Relationship subjectRel = node.getSingleRelationship(PROHIBITION_SUBJECT_REL_TYPE, Direction.INCOMING);
			Node subjectNode = subjectRel.getStartNode();
			// delete process node if this is the only prohibition it's assigned to
			if (subjectNode.hasLabel(PROCESS_LABEL)
					&& subjectNode.getRelationships().stream().count() == 1) {
				subjectNode.delete();
			}

			// delete prohibition node
			ResourceIterable<Relationship> relationships = node.getRelationships();
			for (Relationship relationship : relationships) {
				relationship.delete();
			}

			node.delete();
		});
	}

	@Override
	public Map<Long, Collection<Prohibition>> getNodeProhibitions() throws PMException {
		Map<Long, Collection<Prohibition>> all = new HashMap<>();

		txHandler.runTx(tx -> {
			try(ResourceIterator<Node> proNodes = tx.findNodes(PROHIBITION_LABEL)) {
				while (proNodes.hasNext()) {
					Node next = proNodes.next();
					Prohibition prohibition = getProhibitionFromNode(next);
					if (!prohibition.getSubject().isNode()) {
						continue;
					}

					all.computeIfAbsent(prohibition.getSubject().getNodeId(), k -> new ArrayList<>())
							.add(prohibition);
				}
			}
		});

		return all;
	}

	@Override
	public Map<String, Collection<Prohibition>> getProcessProhibitions() throws PMException {
		Map<String, Collection<Prohibition>> all = new HashMap<>();

		txHandler.runTx(tx -> {
			try(ResourceIterator<Node> proNodes = tx.findNodes(PROHIBITION_LABEL)) {
				while (proNodes.hasNext()) {
					Node next = proNodes.next();
					Prohibition prohibition = getProhibitionFromNode(next);
					if (prohibition.getSubject().isNode()) {
						continue;
					}

					all.computeIfAbsent(prohibition.getSubject().getProcess(), k -> new ArrayList<>())
							.add(prohibition);
				}
			}
		});

		return all;
	}

	@Override
	public Prohibition getProhibition(String name) throws PMException {
		AtomicReference<Prohibition> prohibition = new AtomicReference<>();

		txHandler.runTx(tx -> {
			Node node = tx.findNode(PROHIBITION_LABEL, NAME_PROPERTY, name);
			if (node == null) {
				return;
			}

			prohibition.set(getProhibitionFromNode(node));
		});

		return prohibition.get();
	}

	@Override
	public boolean prohibitionExists(String name) throws PMException {
		AtomicBoolean b = new AtomicBoolean(false);

		txHandler.runTx(tx -> {
			Node node = tx.findNode(PROHIBITION_LABEL, NAME_PROPERTY, name);
			b.set(node != null);
		});

		return b.get();
	}

	@Override
	public Collection<Prohibition> getProhibitionsWithNode(long subject) throws PMException {
		ObjectArrayList<Prohibition> prohibitions = new ObjectArrayList<>();

		txHandler.runTx(tx -> {
			Node node = tx.findNode(NODE_LABEL, ID_PROPERTY, subject);

			ResourceIterable<Relationship> relationships = node.getRelationships(Direction.OUTGOING, PROHIBITION_SUBJECT_REL_TYPE);
			for (Relationship relationship : relationships) {
				Node next = relationship.getEndNode();
				Prohibition prohibition = getProhibitionFromNode(next);
				prohibitions.add(prohibition);
			}
		});

		return prohibitions;
	}

	@Override
	public Collection<Prohibition> getProhibitionsWithProcess(String subject) throws PMException {
		ObjectArrayList<Prohibition> prohibitions = new ObjectArrayList<>();

		txHandler.runTx(tx -> {
			Node node = tx.findNode(PROCESS_LABEL, ID_PROPERTY, subject);
			if (node == null) {
				return;
			}

			ResourceIterable<Relationship> relationships = node.getRelationships(Direction.OUTGOING, PROHIBITION_SUBJECT_REL_TYPE);
			for (Relationship relationship : relationships) {
				Node next = relationship.getEndNode();
				Prohibition prohibition = getProhibitionFromNode(next);
				prohibitions.add(prohibition);
			}
		});

		return prohibitions;
	}

	@Override
	public void beginTx() throws PMException {

	}

	@Override
	public void commit() throws PMException {

	}

	@Override
	public void rollback() throws PMException {

	}

	private void createContainers(Transaction tx, Collection<ContainerCondition> containerConditions, Node prohibitionNode) throws PMException {
		for (ContainerCondition cc : containerConditions) {
			Node targetNode = tx.findNode(NODE_LABEL, ID_PROPERTY, cc.getId());
			targetNode.createRelationshipTo(prohibitionNode, PROHIBITION_CONTAINER_REL_TYPE)
					.setProperty(COMPLEMENT_PROPERTY, cc.isComplement());
		}
	}

	private Node createSubject(Transaction tx, ProhibitionSubject subject, Node prohibitionNode) throws PMException {
		Node subjectNode;
		if (subject.isNode()) {
			// look for a user or ua node with the subject name
			subjectNode = tx.findNode(U_LABEL, ID_PROPERTY, subject.getNodeId());
			if (subjectNode == null) {
				subjectNode = tx.findNode(UA_LABEL, ID_PROPERTY, subject.getNodeId());
			}
		} else {
			// if still null it's a process, add process subjectNode
			subjectNode = tx.findNode(PROCESS_LABEL, ID_PROPERTY, subject.getProcess());
			if (subjectNode == null) {
				subjectNode = tx.createNode(PROCESS_LABEL);
				subjectNode.setProperty(ID_PROPERTY, subject.getProcess());
			}
		}

		subjectNode.createRelationshipTo(prohibitionNode, PROHIBITION_SUBJECT_REL_TYPE);

		return subjectNode;
	}
}
