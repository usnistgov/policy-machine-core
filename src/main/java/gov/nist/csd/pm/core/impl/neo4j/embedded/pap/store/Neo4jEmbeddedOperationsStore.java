package gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store;

import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.DATA_PROPERTY;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.NAME_PROPERTY;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.OPERATION_KIND_PROPERTY;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.OPERATION_LABEL;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.PML_TEXT_PROPERTY;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.RESOURCE_ARS_LABEL;

import gov.nist.csd.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.OperationKind;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperation;
import gov.nist.csd.pm.core.pap.store.OperationsStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

public class Neo4jEmbeddedOperationsStore implements OperationsStore {

	private static final String RESOURCE_ACCESS_RIGHTS_NODE_NAME = "resource_access_rights";
	private final TxHandler txHandler;

	public Neo4jEmbeddedOperationsStore(TxHandler txHandler) {
		this.txHandler = txHandler;
	}

	@Override
	public void setResourceAccessRights(AccessRightSet resourceAccessRights) throws PMException {
		String[] opsArr = resourceAccessRights.toArray(String[]::new);

		txHandler.runTx(tx -> {
			Node node = tx.findNode(RESOURCE_ARS_LABEL, NAME_PROPERTY, RESOURCE_ACCESS_RIGHTS_NODE_NAME);
			if (node == null) {
				node = tx.createNode(RESOURCE_ARS_LABEL);
				node.setProperty(NAME_PROPERTY, RESOURCE_ACCESS_RIGHTS_NODE_NAME);
			}

			node.setProperty(DATA_PROPERTY, opsArr);
		});
	}

	@Override
	public void createOperation(Operation<?> operation) throws PMException {
		boolean isPml = operation instanceof PMLOperation;
		OperationKind kind = isPml ? OperationKind.PML : OperationKind.NATIVE;
		String pmlText = isPml ? operation.toString() : null;

		txHandler.runTx(tx -> {
			Node node = tx.createNode(OPERATION_LABEL);
			node.setProperty(NAME_PROPERTY, operation.getName());
			node.setProperty(OPERATION_KIND_PROPERTY, kind.name());
			if (pmlText != null) {
				node.setProperty(PML_TEXT_PROPERTY, pmlText);
			}
		});
	}

	@Override
	public void deleteOperation(String name) throws PMException {
		txHandler.runTx(tx -> {
			Node node = tx.findNode(OPERATION_LABEL, NAME_PROPERTY, name);
			if (node == null) {
				return;
			}

			node.delete();
		});
	}

	@Override
	public AccessRightSet getResourceAccessRights() throws PMException {
		AccessRightSet resourceOperations = new AccessRightSet();

		txHandler.runTx(tx -> {
			Node node = tx.findNode(RESOURCE_ARS_LABEL, NAME_PROPERTY, RESOURCE_ACCESS_RIGHTS_NODE_NAME);
			if (node == null) {
				return;
			}

			String[] opArr = (String[]) node.getProperty(DATA_PROPERTY);
			resourceOperations.addAll(Arrays.asList(opArr));
		});

		return resourceOperations;
	}

	@Override
	public Collection<String> getOperationNames() throws PMException {
		List<String> names = new ArrayList<>();

		txHandler.runTx(tx -> {
			ResourceIterator<Node> nodes = tx.findNodes(OPERATION_LABEL);
			if (nodes == null) {
				return;
			}

			while (nodes.hasNext()) {
				names.add((String) nodes.next().getProperty(NAME_PROPERTY));
			}
		});

		return names;
	}

	@Override
	public Optional<String> getOperationPml(String name) throws PMException {
		OperationRow row = readRow(name);
		if (row == null || row.kind() == OperationKind.NATIVE) {
			return Optional.empty();
		}

		return Optional.of(row.pmlText());
	}

	@Override
	public OperationKind getOperationKind(String name) throws PMException {
		OperationRow row = readRow(name);
		if (row == null) {
			throw new OperationDoesNotExistException(name);
		}

		return row.kind();
	}

	/**
	 * Reads a row's kind and PML text (if any) while the underlying Neo4j transaction is still open — a
	 * {@link Node} becomes unusable once {@code txHandler.runTx} returns, so every property read must happen
	 * inside the lambda, never on the {@code Node} reference afterward.
	 */
	private OperationRow readRow(String name) throws PMException {
		AtomicReference<OperationRow> rowRef = new AtomicReference<>();

		txHandler.runTx(tx -> {
			Node node = tx.findNode(OPERATION_LABEL, NAME_PROPERTY, name);
			if (node == null) {
				return;
			}

			OperationKind kind = kindOf(node);
			String pmlText = kind == OperationKind.PML ? (String) node.getProperty(PML_TEXT_PROPERTY) : null;
			rowRef.set(new OperationRow(kind, pmlText));
		});

		return rowRef.get();
	}

	@Override
	public boolean operationExists(String operationName) throws PMException {
		AtomicReference<Boolean> opExists = new AtomicReference<>();

		txHandler.runTx(tx -> {
			boolean exists = tx.findNode(OPERATION_LABEL, NAME_PROPERTY, operationName) != null;

			opExists.set(exists);
		});

		return opExists.get();
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

	private static OperationKind kindOf(Node node) {
		return OperationKind.valueOf((String) node.getProperty(OPERATION_KIND_PROPERTY));
	}

	private record OperationRow(OperationKind kind, String pmlText) {
	}
}
