package gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.store.GraphStore;
import gov.nist.csd.pm.core.pap.store.ObligationsStore;
import gov.nist.csd.pm.core.pap.store.OperationsStore;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import gov.nist.csd.pm.core.pap.store.ProhibitionsStore;
import gov.nist.csd.pm.core.pap.store.RoutinesStore;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

public class Neo4jEmbeddedPolicyStore implements PolicyStore {

	public static void createIndexes(GraphDatabaseService graphDb) {
		// create the indexes
		try (Transaction tx = graphDb.beginTx()) {
			tx.execute("create index node_name_index if not exists for (n:Node) on (n.name)");
			tx.execute("create index node_id_index if not exists for (n:Node) on (n.node)");
			tx.execute("create index prohibition_name_index if not exists for (n:Prohibition) on (n.name)");
			tx.execute("create index obligation_name_index if not exists for (n:Obligation) on (n.name)");
			tx.execute("create index operation_name_index if not exists for (n:Operation) on (n.name)");
			tx.execute("create index routine_name_index if not exists for (n:Routine) on (n.name)");
			tx.commit();
		}
	}

	private TxHandler txHandler;
	private ClassLoader classLoader;

	/**
	 * Constructor starts a new transaction
	 * @param graphDb The graph database service
	 * @throws PMException If an error occurs initializing the policy store
	 */
	public Neo4jEmbeddedPolicyStore(GraphDatabaseService graphDb, ClassLoader classLoader) throws PMException {
		this.txHandler = new TxHandler(graphDb);
		this.classLoader = classLoader;
	}

	public TxHandler getTxHandler() {
		return txHandler;
	}

	public void setTxHandler(TxHandler txHandler) {
		this.txHandler = txHandler;
	}

	@Override
	public GraphStore graph() {
		return new Neo4jEmbeddedGraphStore(txHandler);
	}

	@Override
	public ProhibitionsStore prohibitions() {
		return new Neo4jEmbeddedProhibitionStore(txHandler);
	}

	@Override
	public ObligationsStore obligations() {
		return new Neo4jEmbeddedObligationStore(txHandler, classLoader);
	}

	@Override
	public OperationsStore operations() {
		return new Neo4jEmbeddedOperationsStore(txHandler, classLoader);
	}

	@Override
	public RoutinesStore routines() {
		return new Neo4jEmbeddedRoutinesStore(txHandler, classLoader);
	}

	@Override
	public void reset() throws PMException {
		txHandler.runTx(tx -> {
			tx.execute("match (n) detach delete n");
		});
	}

	@Override
	public void beginTx() throws PMException {
		txHandler.beginTx();
	}

	@Override
	public void commit() throws PMException {
		txHandler.commit();
	}

	@Override
	public void rollback() throws PMException {
		txHandler.rollback();
	}
}
