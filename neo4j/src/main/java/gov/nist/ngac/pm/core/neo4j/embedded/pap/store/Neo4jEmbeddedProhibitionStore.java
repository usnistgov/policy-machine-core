/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST.
 *
 * This file is part of the policy-machine-core-neo4j module, which compiles against
 * and embeds org.neo4j:neo4j (GPLv3, Community Edition). As a combined work, this
 * module is distributed under the GNU General Public License v3.0, not the CC BY 4.0
 * license used elsewhere in this repository. See neo4j/LICENSE for the full text.
 */

// This file is part of the policy-machine-core-neo4j module, which links against
// org.neo4j:neo4j (GPLv3) and is distributed under GPLv3. See neo4j/LICENSE.
package gov.nist.ngac.pm.core.neo4j.embedded.pap.store;

import static gov.nist.ngac.pm.core.neo4j.embedded.pap.store.Neo4jUtil.ARSET_PROPERTY;
import static gov.nist.ngac.pm.core.neo4j.embedded.pap.store.Neo4jUtil.EXCLUSION_SET_PROPERTY;
import static gov.nist.ngac.pm.core.neo4j.embedded.pap.store.Neo4jUtil.INCLUSION_SET_PROPERTY;
import static gov.nist.ngac.pm.core.neo4j.embedded.pap.store.Neo4jUtil.IS_CONJUNCTIVE_PROPERTY;
import static gov.nist.ngac.pm.core.neo4j.embedded.pap.store.Neo4jUtil.NAME_PROPERTY;
import static gov.nist.ngac.pm.core.neo4j.embedded.pap.store.Neo4jUtil.NODE_ID_PROPERTY;
import static gov.nist.ngac.pm.core.neo4j.embedded.pap.store.Neo4jUtil.PROCESS_PROPERTY;
import static gov.nist.ngac.pm.core.neo4j.embedded.pap.store.Neo4jUtil.PROHIBITION_LABEL;
import static gov.nist.ngac.pm.core.neo4j.embedded.pap.store.Neo4jUtil.getProhibitionFromNode;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.store.ProhibitionsStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

/**
 * A {@link ProhibitionsStore} backed by an embedded Neo4j database, storing each prohibition as a
 * labeled node linked to its subject and container nodes.
 */
public class Neo4jEmbeddedProhibitionStore implements ProhibitionsStore {

	private final TxHandler txHandler;

	public Neo4jEmbeddedProhibitionStore(TxHandler txHandler) {
		this.txHandler = txHandler;
	}

	@Override
	public void createNodeProhibition(String name,
									  long nodeId,
									  AccessRightSet accessRightSet,
									  Set<Long> inclusionSet,
									  Set<Long> exclusionSet,
									  boolean isConjunctive) throws PMException {
		txHandler.runTx(tx -> {
			Node prohibitionNode = tx.createNode(PROHIBITION_LABEL);
			prohibitionNode.setProperty(NAME_PROPERTY, name);
			prohibitionNode.setProperty(NODE_ID_PROPERTY, nodeId);
			prohibitionNode.setProperty(ARSET_PROPERTY, accessRightSet.toArray(new String[]{}));
			prohibitionNode.setProperty(INCLUSION_SET_PROPERTY, inclusionSet.stream().mapToLong(Long::longValue).toArray());
			prohibitionNode.setProperty(EXCLUSION_SET_PROPERTY, exclusionSet.stream().mapToLong(Long::longValue).toArray());
			prohibitionNode.setProperty(IS_CONJUNCTIVE_PROPERTY, isConjunctive);
		});
	}

	@Override
	public void createProcessProhibition(String name,
										 long userId,
										 String process,
										 AccessRightSet accessRightSet,
										 Set<Long> inclusionSet,
										 Set<Long> exclusionSet,
										 boolean isConjunctive) throws PMException {
		txHandler.runTx(tx -> {
			Node prohibitionNode = tx.createNode(PROHIBITION_LABEL);
			prohibitionNode.setProperty(NAME_PROPERTY, name);
			prohibitionNode.setProperty(NODE_ID_PROPERTY, userId);
			prohibitionNode.setProperty(PROCESS_PROPERTY, process);
			prohibitionNode.setProperty(ARSET_PROPERTY, accessRightSet.toArray(new String[]{}));
			prohibitionNode.setProperty(INCLUSION_SET_PROPERTY, inclusionSet.stream().mapToLong(Long::longValue).toArray());
			prohibitionNode.setProperty(EXCLUSION_SET_PROPERTY, exclusionSet.stream().mapToLong(Long::longValue).toArray());
			prohibitionNode.setProperty(IS_CONJUNCTIVE_PROPERTY, isConjunctive);
		});
	}

	@Override
	public void deleteProhibition(String name) throws PMException {
		txHandler.runTx(tx -> {
			Node node = tx.findNode(PROHIBITION_LABEL, NAME_PROPERTY, name);
			if (node == null) {
				return;
			}

			node.delete();
		});
	}

	@Override
	public Collection<Prohibition> getAllProhibitions() throws PMException {
		Collection<Prohibition> prohibitions = new ArrayList<>();

		txHandler.runTx(tx -> {
			try(ResourceIterator<Node> proNodes = tx.findNodes(PROHIBITION_LABEL)) {
				while (proNodes.hasNext()) {
					Node next = proNodes.next();
					Prohibition prohibition = getProhibitionFromNode(next);
					prohibitions.add(prohibition);

				}
			}
		});

		return prohibitions;
	}

	@Override
	public Collection<Prohibition> getNodeProhibitions(long nodeId) throws PMException {
		Collection<Prohibition> prohibitions = new ArrayList<>();

		txHandler.runTx(tx -> {
			try(ResourceIterator<Node> proNodes = tx.findNodes(PROHIBITION_LABEL, NODE_ID_PROPERTY, nodeId)) {
				while (proNodes.hasNext()) {
					Node next = proNodes.next();
					Prohibition prohibition = getProhibitionFromNode(next);
					if (prohibition instanceof ProcessProhibition) {
						continue;
					}

					prohibitions.add(prohibition);

				}
			}
		});

		return prohibitions;
	}

	@Override
	public Collection<Prohibition> getProcessProhibitions(String process) throws PMException {
		Collection<Prohibition> prohibitions = new ArrayList<>();

		txHandler.runTx(tx -> {
			try(ResourceIterator<Node> proNodes = tx.findNodes(PROHIBITION_LABEL, PROCESS_PROPERTY, process)) {
				while (proNodes.hasNext()) {
					Node next = proNodes.next();
					Prohibition prohibition = getProhibitionFromNode(next);
					prohibitions.add(prohibition);
				}
			}
		});

		return prohibitions;
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
	public void beginTx() throws PMException {

	}

	@Override
	public void commit() throws PMException {

	}

	@Override
	public void rollback() throws PMException {

	}
}
