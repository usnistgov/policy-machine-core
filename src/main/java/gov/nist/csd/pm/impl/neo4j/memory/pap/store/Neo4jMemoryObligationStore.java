package gov.nist.csd.pm.impl.neo4j.memory.pap.store;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.obligation.Obligation;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.pap.store.ObligationsStore;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static gov.nist.csd.pm.impl.neo4j.memory.pap.store.Neo4jUtil.*;

public class Neo4jMemoryObligationStore implements ObligationsStore {

	private final TxHandler txHandler;

	public Neo4jMemoryObligationStore(TxHandler txHandler) {
		this.txHandler = txHandler;
	}

	@Override
	public void createObligation(long authorId, String name, List<Rule> rules) throws PMException {
		Obligation obligation = new Obligation(authorId, name, rules);
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

					obligations.add((Obligation) deserialize(next.getProperty(DATA_PROPERTY).toString()));
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

			obligation.set((Obligation) deserialize(node.getProperty(DATA_PROPERTY).toString()));
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
