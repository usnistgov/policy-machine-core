package gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store;

import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.OPERATION_LABEL;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.DATA_PROPERTY;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.NAME_PROPERTY;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.OP_TYPE_PROPERTY;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.RESOURCE_ARS_LABEL;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.deserialize;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.BasicFunction;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.QueryOperation;
import gov.nist.csd.pm.core.pap.operation.ResourceOperation;
import gov.nist.csd.pm.core.pap.operation.Routine;
import gov.nist.csd.pm.core.pap.store.OperationsStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

public class Neo4jEmbeddedOperationsStore implements OperationsStore {

	private enum OpTypeProperty {
		ADMIN,
		RESOURCE,
		ROUTINE,
		QUERY,
		FUNCTION
	}

	private static final String RESOURCE_ACCESS_RIGHTS_NODE_NAME = "resource_operations";
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
	public void createResourceOperation(ResourceOperation<?> operation) throws PMException {
		createOp(operation, OpTypeProperty.RESOURCE);
	}

	@Override
	public void createAdminOperation(AdminOperation<?> operation) throws PMException {
		createOp(operation, OpTypeProperty.ADMIN);
	}

	@Override
	public void createAdminRoutine(Routine<?> routine) throws PMException {
		createOp(routine, OpTypeProperty.ROUTINE);
	}

	@Override
	public void createQueryOperation(QueryOperation<?> operation) throws PMException {
		createOp(operation, OpTypeProperty.QUERY);
	}

	@Override
	public void createBasicFunction(BasicFunction<?> function) throws PMException {
		createOp(function, OpTypeProperty.FUNCTION);
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
	public Collection<String> getResourceOperationNames() throws PMException {
		return getOpNames(OpTypeProperty.RESOURCE);
	}

	@Override
	public ResourceOperation<?> getResourceOperation(String operationName) throws PMException {
		return getOp(operationName);
	}

	@Override
	public Collection<String> getAdminOperationNames() throws PMException {
		return getOpNames(OpTypeProperty.ADMIN);
	}

	@Override
	public AdminOperation<?> getAdminOperation(String operationName) throws PMException {
		return getOp(operationName);
	}

	@Override
	public Collection<String> getAdminRoutineNames() throws PMException {
		return getOpNames(OpTypeProperty.ROUTINE);
	}

	@Override
	public Routine<?> getAdminRoutine(String routineName) throws PMException {
		return getOp(routineName);
	}

	@Override
	public Collection<String> getQueryOperationNames() throws PMException {
		return getOpNames(OpTypeProperty.QUERY);
	}

	@Override
	public QueryOperation<?> getQueryOperation(String name) throws PMException {
		return getOp(name);
	}

	@Override
	public Collection<String> getBasicFunctionNames() throws PMException {
		return getOpNames(OpTypeProperty.FUNCTION);
	}

	@Override
	public BasicFunction<?> getBasicFunction(String name) throws PMException {
		return getOp(name);
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

	private void createOp(Operation<?> op, OpTypeProperty opTypeProperty) throws PMException {
		String hex = Neo4jUtil.serialize(op);

		txHandler.runTx(tx -> {
			Node node = tx.createNode(OPERATION_LABEL);
			node.setProperty(NAME_PROPERTY, op.getName());
			node.setProperty(OP_TYPE_PROPERTY, opTypeProperty.name());
			node.setProperty(DATA_PROPERTY, hex);
		});
	}

	private List<String> getOpNames(OpTypeProperty opTypeProperty) throws PMException {
		List<String> operationNames = new ArrayList<>();

		txHandler.runTx(tx -> {
			ResourceIterator<Node> nodes = tx.findNodes(OPERATION_LABEL, OP_TYPE_PROPERTY, opTypeProperty.name());

			while (nodes.hasNext()) {
				Node node = nodes.next();
				operationNames.add(node.getProperty(NAME_PROPERTY).toString());
			}
		});

		return operationNames;
	}

	private <T extends Operation<?>> T getOp(String name) throws PMException {
		AtomicReference<T> op = new AtomicReference<>();

		txHandler.runTx(tx -> {
			Node node = tx.findNode(OPERATION_LABEL, NAME_PROPERTY, name);
			if (node == null) {
				return;
			}

			op.set((T) deserialize(node.getProperty(DATA_PROPERTY).toString(), classLoader));
		});

		return op.get();
	}
}
