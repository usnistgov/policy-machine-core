package gov.nist.ngac.pm.core.impl.neo4j.embedded.pap.store;

import static gov.nist.ngac.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.AUTHOR_ID_PROPERTY;
import static gov.nist.ngac.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.AUTHOR_NAME_PROPERTY;
import static gov.nist.ngac.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.AUTHOR_PROCESS_PROPERTY;
import static gov.nist.ngac.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.NAME_PROPERTY;
import static gov.nist.ngac.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.OBLIGATION_LABEL;
import static gov.nist.ngac.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.PML_TEXT_PROPERTY;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.pap.store.ObligationsStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

/**
 * A {@link ObligationsStore} backed by an embedded Neo4j database, storing each obligation as a labeled
 * node with its PML source and author properties.
 */
public class Neo4jEmbeddedObligationStore implements ObligationsStore {

	private final TxHandler txHandler;

	public Neo4jEmbeddedObligationStore(TxHandler txHandler) {
		this.txHandler = txHandler;
	}

	@Override
	public void createObligation(Obligation obligation) throws PMException {
		String pmlText = obligation.toString();
		NodeUserContext author = obligation.getAuthor();

		txHandler.runTx(tx -> {
			Node node = tx.createNode(OBLIGATION_LABEL);
			node.setProperty(NAME_PROPERTY, obligation.getName());
			node.setProperty(PML_TEXT_PROPERTY, pmlText);

			if (author.getName() != null) {
				node.setProperty(AUTHOR_NAME_PROPERTY, author.getName());
			} else {
				node.setProperty(AUTHOR_ID_PROPERTY, author.getId());
			}

			if (author.getProcess() != null) {
				node.setProperty(AUTHOR_PROCESS_PROPERTY, author.getProcess());
			}
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
	public boolean obligationExists(String name) throws PMException {
		AtomicBoolean b = new AtomicBoolean(false);
		txHandler.runTx(tx -> {
			Node node = tx.findNode(OBLIGATION_LABEL, NAME_PROPERTY, name);
			b.set(node != null);
		});

		return b.get();
	}

	@Override
	public ObligationPml getObligationPml(String name) throws PMException {
		AtomicReference<ObligationPml> rowRef = new AtomicReference<>();

		txHandler.runTx(tx -> {
			Node node = tx.findNode(OBLIGATION_LABEL, NAME_PROPERTY, name);
			if (node == null) {
				return;
			}

			rowRef.set(readRow(node));
		});

		return rowRef.get();
	}

	@Override
	public Collection<ObligationPml> getObligationPmls() throws PMException {
		List<ObligationPml> rows = new ArrayList<>();

		txHandler.runTx(tx -> {
			try (ResourceIterator<Node> nodes = tx.findNodes(OBLIGATION_LABEL)) {
				while (nodes.hasNext()) {
					rows.add(readRow(nodes.next()));
				}
			}
		});

		return rows;
	}

	@Override
	public Collection<String> getObligationNamesWithAuthor(NodeUserContext author) throws PMException {
		List<String> names = new ArrayList<>();

		txHandler.runTx(tx -> {
			try (ResourceIterator<Node> nodes = tx.findNodes(OBLIGATION_LABEL)) {
				while (nodes.hasNext()) {
					Node node = nodes.next();
					if (author.equals(readAuthor(node))) {
						names.add((String) node.getProperty(NAME_PROPERTY));
					}
				}
			}
		});

		return names;
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

	private static ObligationPml readRow(Node node) {
		String name = (String) node.getProperty(NAME_PROPERTY);
		String pmlText = (String) node.getProperty(PML_TEXT_PROPERTY);

		return new ObligationPml(name, pmlText, readAuthor(node));
	}

	private static NodeUserContext readAuthor(Node node) {
		Long authorId = node.hasProperty(AUTHOR_ID_PROPERTY) ? (Long) node.getProperty(AUTHOR_ID_PROPERTY) : null;
		String authorName = node.hasProperty(AUTHOR_NAME_PROPERTY) ? (String) node.getProperty(AUTHOR_NAME_PROPERTY) : null;
		String authorProcess = node.hasProperty(AUTHOR_PROCESS_PROPERTY) ? (String) node.getProperty(AUTHOR_PROCESS_PROPERTY) : null;

		return toAuthor(authorId, authorName, authorProcess);
	}

	private static NodeUserContext toAuthor(Long id, String name, String process) {
		if (name != null) {
			return process != null ? NodeUserContext.of(name, process) : NodeUserContext.of(name);
		}

		return process != null ? NodeUserContext.of(id, process) : NodeUserContext.of(id);
	}
}
