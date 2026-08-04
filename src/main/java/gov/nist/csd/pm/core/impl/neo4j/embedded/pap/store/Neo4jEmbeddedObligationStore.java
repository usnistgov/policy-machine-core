package gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store;

import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.AUTHOR_ID_PROPERTY;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.AUTHOR_NAME_PROPERTY;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.AUTHOR_PROCESS_PROPERTY;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.NAME_PROPERTY;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.OBLIGATION_LABEL;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.PML_TEXT_PROPERTY;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.StatementVisitor;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateObligationStatement;
import gov.nist.csd.pm.core.pap.query.model.context.NodeUserContext;
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
	private final PAP pap;

	public Neo4jEmbeddedObligationStore(TxHandler txHandler, PAP pap) {
		this.txHandler = txHandler;
		this.pap = pap;
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
	public Collection<Obligation> getObligations() throws PMException {
		List<ObligationRow> rows = new ArrayList<>();

		txHandler.runTx(tx -> {
			try (ResourceIterator<Node> nodes = tx.findNodes(OBLIGATION_LABEL)) {
				while (nodes.hasNext()) {
					rows.add(readRow(nodes.next()));
				}
			}
		});

		List<Obligation> obligations = new ArrayList<>();
		for (ObligationRow row : rows) {
			obligations.add(toObligation(row));
		}

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
		AtomicReference<ObligationRow> rowRef = new AtomicReference<>();

		txHandler.runTx(tx -> {
			Node node = tx.findNode(OBLIGATION_LABEL, NAME_PROPERTY, name);
			if (node == null) {
				return;
			}

			rowRef.set(readRow(node));
		});

		ObligationRow row = rowRef.get();
		if (row == null) {
			return null;
		}

		return toObligation(row);
	}

	@Override
	public Collection<Obligation> getObligationsWithAuthor(NodeUserContext authorCtx) throws PMException {
		Collection<Obligation> obligations = new ArrayList<>(getObligations());
		obligations.removeIf(o -> !authorCtx.equals(o.getAuthor()));
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

	private static ObligationRow readRow(Node node) {
		Long authorId = node.hasProperty(AUTHOR_ID_PROPERTY) ? (Long) node.getProperty(AUTHOR_ID_PROPERTY) : null;
		String authorName = node.hasProperty(AUTHOR_NAME_PROPERTY) ? (String) node.getProperty(AUTHOR_NAME_PROPERTY) : null;
		String authorProcess = node.hasProperty(AUTHOR_PROCESS_PROPERTY) ? (String) node.getProperty(AUTHOR_PROCESS_PROPERTY) : null;
		String pmlText = (String) node.getProperty(PML_TEXT_PROPERTY);

		return new ObligationRow(pmlText, authorId, authorName, authorProcess);
	}

	private Obligation toObligation(ObligationRow row) throws PMException {
		NodeUserContext author = toAuthor(row.authorId(), row.authorName(), row.authorProcess());
		PMLStatement<?> statement = StatementVisitor.fromString(pap, row.pmlText());
		return ((CreateObligationStatement) statement).toObligation(author);
	}

	private static NodeUserContext toAuthor(Long id, String name, String process) {
		if (name != null) {
			return process != null ? NodeUserContext.of(name, process) : NodeUserContext.of(name);
		}

		return process != null ? NodeUserContext.of(id, process) : NodeUserContext.of(id);
	}

	private record ObligationRow(String pmlText, Long authorId, String authorName, String authorProcess) {
	}
}
