package gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.store.OperationsStore;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.*;

public class Neo4jEmbeddedOperationsStore implements OperationsStore {

	private static final String RESOURCE_OPERATIONS_NODE_NAME = "resource_operations";
	private final TxHandler txHandler;

	public Neo4jEmbeddedOperationsStore(TxHandler txHandler) {
		this.txHandler = txHandler;
	}

	@Override
	public void setResourceOperations(AccessRightSet resourceOperations) throws PMException {
		String[] opsArr = resourceOperations.toArray(String[]::new);

		txHandler.runTx(tx -> {
			Node node = tx.findNode(RESOURCE_OPERATIONS_LABEL, NAME_PROPERTY, RESOURCE_OPERATIONS_NODE_NAME);
			if (node == null) {
				node = tx.createNode(RESOURCE_OPERATIONS_LABEL);
				node.setProperty(NAME_PROPERTY, RESOURCE_OPERATIONS_NODE_NAME);
			}

			node.setProperty(DATA_PROPERTY, opsArr);
		});
	}

	@Override
	public void createAdminOperation(Operation<?, ?> operation) throws PMException {
		String hex = Neo4jUtil.serialize(operation);

		txHandler.runTx(tx -> {
			Node node = tx.createNode(ADMIN_OPERATION_LABEL);
			node.setProperty(NAME_PROPERTY, operation.getName());
			node.setProperty(DATA_PROPERTY, hex);
		});
	}

	@Override
	public void deleteAdminOperation(String operation) throws PMException {
		txHandler.runTx(tx -> {
			Node node = tx.findNode(ADMIN_OPERATION_LABEL, NAME_PROPERTY, operation);
			if (node == null) {
				return;
			}

			node.delete();
		});
	}

	@Override
	public AccessRightSet getResourceOperations() throws PMException {
		AccessRightSet resourceOperations = new AccessRightSet();

		txHandler.runTx(tx -> {
			Node node = tx.findNode(RESOURCE_OPERATIONS_LABEL, NAME_PROPERTY, RESOURCE_OPERATIONS_NODE_NAME);
			if (node == null) {
				return;
			}

			String[] opArr = (String[]) node.getProperty(DATA_PROPERTY);
			resourceOperations.addAll(Arrays.asList(opArr));
		});

		return resourceOperations;
	}

	@Override
	public Collection<String> getAdminOperationNames() throws PMException {
		List<String> operationNames = new ArrayList<>();

		txHandler.runTx(tx -> {
			ResourceIterator<Node> nodes = tx.findNodes(ADMIN_OPERATION_LABEL);

			while (nodes.hasNext()) {
				Node node = nodes.next();
				operationNames.add(node.getProperty(NAME_PROPERTY).toString());
			}
		});

		return operationNames;
	}

	@Override
	public Operation<?, ?> getAdminOperation(String operationName) throws PMException {
		AtomicReference<Operation<?, ?>> operation = new AtomicReference<>();

		txHandler.runTx(tx -> {
			Node node = tx.findNode(ADMIN_OPERATION_LABEL, NAME_PROPERTY, operationName);
			if (node == null) {
				return;
			}

			Operation<?, ?> op = (Operation<?, ?>) deserialize(node.getProperty(DATA_PROPERTY).toString());
			operation.set(op);
		});

		return operation.get();
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
