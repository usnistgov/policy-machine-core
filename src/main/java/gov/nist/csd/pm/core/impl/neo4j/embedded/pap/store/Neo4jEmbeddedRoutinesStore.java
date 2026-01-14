package gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store;

import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.ADMIN_ROUTINE_LABEL;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.DATA_PROPERTY;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.NAME_PROPERTY;
import static gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store.Neo4jUtil.deserialize;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
import gov.nist.csd.pm.core.pap.store.RoutinesStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

public class Neo4jEmbeddedRoutinesStore implements RoutinesStore {

	private final TxHandler txHandler;
	private final ClassLoader classLoader;

	public Neo4jEmbeddedRoutinesStore(TxHandler txHandler, ClassLoader classLoader) {
		this.txHandler = txHandler;
		this.classLoader = classLoader;
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
	public void beginTx() throws PMException {

	}

	@Override
	public void commit() throws PMException {

	}

	@Override
	public void rollback() throws PMException {

	}
}
