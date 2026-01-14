package gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.function.op.AdminOperation;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.op.ResourceOperation;
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
	private ClassLoader classLoader;

	public Neo4jEmbeddedOperationsStore(TxHandler txHandler, ClassLoader classLoader) {
		this.txHandler = txHandler;
		this.classLoader = classLoader;
	}

	@Override
	public void setResourceAccessRights(AccessRightSet resourceAccessRights) throws PMException {
		String[] opsArr = resourceAccessRights.toArray(String[]::new);

		txHandler.runTx(tx -> {
			Node node = tx.findNode(RESOURCE_ARS_LABEL, NAME_PROPERTY, RESOURCE_OPERATIONS_NODE_NAME);
			if (node == null) {
				node = tx.createNode(RESOURCE_ARS_LABEL);
				node.setProperty(NAME_PROPERTY, RESOURCE_OPERATIONS_NODE_NAME);
			}

			node.setProperty(DATA_PROPERTY, opsArr);
		});
	}

	@Override
	public void createResourceOperation(ResourceOperation operation) throws PMException {
		String hex = Neo4jUtil.serialize(operation);

		txHandler.runTx(tx -> {
			Node node = tx.createNode(RESOURCE_OPERATION_LABEL);
			node.setProperty(NAME_PROPERTY, operation.getName());
			node.setProperty(DATA_PROPERTY, hex);
		});
	}

	@Override
	public void deleteResourceOperation(String operation) throws PMException {
		txHandler.runTx(tx -> {
			Node node = tx.findNode(RESOURCE_OPERATION_LABEL, NAME_PROPERTY, operation);
			if (node == null) {
				return;
			}

			node.delete();
		});
	}

	@Override
	public void createAdminOperation(AdminOperation<?> operation) throws PMException {
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
	public AccessRightSet getResourceAccessRights() throws PMException {
		AccessRightSet resourceOperations = new AccessRightSet();

		txHandler.runTx(tx -> {
			Node node = tx.findNode(RESOURCE_ARS_LABEL, NAME_PROPERTY, RESOURCE_OPERATIONS_NODE_NAME);
			if (node == null) {
				return;
			}

			String[] opArr = (String[]) node.getProperty(DATA_PROPERTY);
			resourceOperations.addAll(Arrays.asList(opArr));
		});

		return resourceOperations;
	}

	@Override
	public Collection<String> getResourceOperationNames() throws PMException {
		return List.of();
	}

	@Override
	public ResourceOperation getResourceOperation(String operationName) throws PMException {
		return null;
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
	public AdminOperation<?> getAdminOperation(String operationName) throws PMException {
		AtomicReference<AdminOperation<?>> operation = new AtomicReference<>();

		txHandler.runTx(tx -> {
			Node node = tx.findNode(ADMIN_OPERATION_LABEL, NAME_PROPERTY, operationName);
			if (node == null) {
				return;
			}

			AdminOperation<?> op = (AdminOperation<?>) deserialize(node.getProperty(DATA_PROPERTY).toString(), classLoader);
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
