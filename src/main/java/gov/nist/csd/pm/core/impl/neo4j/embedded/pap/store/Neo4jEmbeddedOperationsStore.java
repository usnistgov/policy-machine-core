package gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store;

import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.DATA_PROPERTY;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.NAME_PROPERTY;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.OPERATION_LABEL;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.RESOURCE_ARS_LABEL;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.deserialize;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.store.OperationsStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

public class Neo4jEmbeddedOperationsStore implements OperationsStore {

	private static final String RESOURCE_ACCESS_RIGHTS_NODE_NAME = "resource_access_rights";
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
		String hex = Neo4jUtil.serialize(operation);

		txHandler.runTx(tx -> {
			Node node = tx.createNode(OPERATION_LABEL);
			node.setProperty(NAME_PROPERTY, operation.getName());
			node.setProperty(DATA_PROPERTY, hex);
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
	public Collection<Operation<?>> getOperations() throws PMException {
		List<Operation<?>> operations = new ArrayList<>();

		txHandler.runTx(tx -> {
			ResourceIterator<Node> nodes = tx.findNodes(OPERATION_LABEL);
			if (nodes == null) {
				return;
			}

			while (nodes.hasNext()) {
				Node next = nodes.next();
				operations.add((Operation<?>) deserialize(next.getProperty(DATA_PROPERTY).toString(), classLoader));
			}
		});

		return operations;
	}

	@Override
	public Collection<String> getOperationNames() throws PMException {
		return getOperations().stream().map(Operation::getName).toList();
	}

	@Override
	public Operation<?> getOperation(String name) throws PMException {
		AtomicReference<Operation<?>> operation = new AtomicReference<>();

		txHandler.runTx(tx -> {
			Node node = tx.findNode(OPERATION_LABEL, NAME_PROPERTY, name);
			if (node == null) {
				return;
			}

			operation.set((Operation<?>) deserialize(node.getProperty(DATA_PROPERTY).toString(), classLoader));
		});

		return operation.get();
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
}
