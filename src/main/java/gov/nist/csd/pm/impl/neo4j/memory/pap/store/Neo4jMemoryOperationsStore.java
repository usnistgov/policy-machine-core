package gov.nist.csd.pm.impl.neo4j.memory.pap.store;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.statement.operation.CreatePolicyStatement;
import gov.nist.csd.pm.pap.store.OperationsStore;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static gov.nist.csd.pm.impl.neo4j.memory.pap.store.Neo4jUtil.*;

public class Neo4jMemoryOperationsStore implements OperationsStore {

	private TxHandler txHandler;

	public Neo4jMemoryOperationsStore(TxHandler txHandler) {
		this.txHandler = txHandler;
	}

	@Override
	public void setResourceOperations(AccessRightSet resourceOperations) throws PMException {
		txHandler.runTx(tx -> {
			Node node = tx.createNode(RESOURCE_OPERATIONS_LABEL);
			node.setProperty(DATA_PROPERTY, resourceOperations.toArray(String[]::new));
		});
	}

	@Override
	public void createAdminOperation(Operation<?> operation) throws PMException {
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
			ResourceIterator<Node> nodes = tx.findNodes(RESOURCE_OPERATIONS_LABEL);
			if (!nodes.hasNext()) {
				return;
			}

			Node node = nodes.next();
			resourceOperations.addAll(Arrays.asList((String[]) node.getProperty(DATA_PROPERTY)));
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
	public Operation<?> getAdminOperation(String operationName) throws PMException {
		AtomicReference<Operation<?>> operation = new AtomicReference<>();

		txHandler.runTx(tx -> {
			Node node = tx.findNode(ADMIN_OPERATION_LABEL, NAME_PROPERTY, operationName);
			if (node == null) {
				return;
			}

			Operation<?> op = (Operation<?>) deserialize(node.getProperty(DATA_PROPERTY).toString());
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