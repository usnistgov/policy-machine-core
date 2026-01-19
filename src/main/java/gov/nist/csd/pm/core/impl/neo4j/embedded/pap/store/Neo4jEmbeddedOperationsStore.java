package gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store;

import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.ADMIN_OPERATION_LABEL;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.ADMIN_ROUTINE_LABEL;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.DATA_PROPERTY;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.NAME_PROPERTY;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.RESOURCE_ARS_LABEL;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.RESOURCE_OPERATION_LABEL;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.deserialize;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.ResourceOperation;
import gov.nist.csd.pm.core.pap.function.Routine;
import gov.nist.csd.pm.core.pap.store.OperationsStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

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
	public void createResourceOperation(ResourceOperation<?> operation) throws PMException {
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
	public void createAdminRoutine(Routine<?> routine) throws PMException {
		String hex = Neo4jUtil.serialize(routine);

		txHandler.runTx(tx -> {
			Node node = tx.createNode(ADMIN_ROUTINE_LABEL);
			node.setProperty(NAME_PROPERTY, routine.getName());
			node.setProperty(DATA_PROPERTY, hex);
		});
	}

	@Override
	public void deleteAdminRoutine(String name) throws PMException {
		txHandler.runTx(tx -> {
			Node node = tx.findNode(ADMIN_ROUTINE_LABEL, NAME_PROPERTY, name);
			if (node == null) {
				return;
			}

			node.delete();
		});
	}

	@Override
	public Collection<String> getAdminRoutineNames() throws PMException {
		List<String> routineNames = new ArrayList<>();

		txHandler.runTx(tx -> {
			ResourceIterator<Node> nodes = tx.findNodes(ADMIN_ROUTINE_LABEL);

			while (nodes.hasNext()) {
				Node node = nodes.next();
				routineNames.add(node.getProperty(NAME_PROPERTY).toString());
			}
		});

		return routineNames;
	}

	@Override
	public Routine<?> getAdminRoutine(String routineName) throws PMException {
		AtomicReference<Routine<?>> routine = new AtomicReference<>();

		txHandler.runTx(tx -> {
			Node node = tx.findNode(ADMIN_ROUTINE_LABEL, NAME_PROPERTY, routineName);
			if (node == null) {
				return;
			}

			routine.set((Routine<?>) deserialize(node.getProperty(DATA_PROPERTY).toString(), classLoader));
		});

		return routine.get();
	}

	@Override
	public boolean operationExists(String operationName) throws PMException {
		AtomicReference<Boolean> opExists = new AtomicReference<>();

		txHandler.runTx(tx -> {
			boolean exists =
				tx.findNode(RESOURCE_OPERATION_LABEL, NAME_PROPERTY, operationName) != null
				|| tx.findNode(ADMIN_OPERATION_LABEL, NAME_PROPERTY, operationName) != null
				|| tx.findNode(ADMIN_ROUTINE_LABEL, NAME_PROPERTY, operationName) != null;


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
