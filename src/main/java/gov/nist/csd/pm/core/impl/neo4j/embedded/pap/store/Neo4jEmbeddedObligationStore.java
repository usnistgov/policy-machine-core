package gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store;

import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.DATA_PROPERTY;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.NAME_PROPERTY;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.OBLIGATION_LABEL;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.deserialize;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.csd.pm.core.pap.store.ObligationsStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

public class Neo4jEmbeddedObligationStore implements ObligationsStore {

	private final TxHandler txHandler;
	private ClassLoader classLoader;

	public Neo4jEmbeddedObligationStore(TxHandler txHandler, ClassLoader classLoader) {
		this.txHandler = txHandler;
		this.classLoader = classLoader;
	}

	@Override
	public void createObligation(long authorId, String name, EventPattern eventPattern, ObligationResponse response) throws PMException {
		Obligation obligation = new Obligation(authorId, name, eventPattern, response);
		String hex = Neo4jUtil.serialize(obligation);

		txHandler.runTx(tx -> {
			Node node = tx.createNode(OBLIGATION_LABEL);
			node.setProperty(NAME_PROPERTY, name);
			node.setProperty(DATA_PROPERTY, hex);
		});
	}

	@Override
	public void deleteObligation(String name) throws PMException {
		txHandler.runTx(tx -> {
			Node node = tx.findNode(OBLIGATION_LABEL, NAME_PROPERTY, name);
			if (node == null) {
				return;
			}

			node.delete();
		});
	}

	@Override
	public Collection<Obligation> getObligations() throws PMException {
		List<Obligation> obligations = new ArrayList<>();
		txHandler.runTx(tx -> {
			try(ResourceIterator<Node> nodes = tx.findNodes(OBLIGATION_LABEL)) {
				while (nodes.hasNext()) {
					Node next = nodes.next();

					obligations.add((Obligation) deserialize(next.getProperty(DATA_PROPERTY).toString(), classLoader));
				}
			}
		});

		return obligations;
	}

	@Override
	public boolean obligationExists(String name) throws PMException {
		AtomicBoolean b = new AtomicBoolean(false);
		txHandler.runTx(tx -> {
			Node node = tx.findNode(OBLIGATION_LABEL, NAME_PROPERTY, name);
			b.set(node != null);
		});

		return b.get();
	}

	@Override
	public Obligation getObligation(String name) throws PMException {
		AtomicReference<Obligation> obligation = new AtomicReference<>();

		txHandler.runTx(tx -> {
			Node node = tx.findNode(OBLIGATION_LABEL, NAME_PROPERTY, name);
			if (node == null) {
				obligation.set(null);
				return;
			}

			obligation.set((Obligation) deserialize(node.getProperty(DATA_PROPERTY).toString(), classLoader));
		});

		return obligation.get();
	}

	@Override
	public Collection<Obligation> getObligationsWithAuthor(long userId) throws PMException {
		Collection<Obligation> obligations = new ArrayList<>(getObligations());
		obligations.removeIf(o -> o.getAuthorId() != userId);
		return obligations;
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
