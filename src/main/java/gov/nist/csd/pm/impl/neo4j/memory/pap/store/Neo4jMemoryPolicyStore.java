package gov.nist.csd.pm.impl.neo4j.memory.pap.store;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.store.*;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

public class Neo4jMemoryPolicyStore implements PolicyStore {

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

	/**
	 * Constructor starts a new transaction
	 * @param graphDb The graph database service
	 * @throws PMException If an error occurs initializing the policy store
	 */
	public Neo4jMemoryPolicyStore(GraphDatabaseService graphDb) throws PMException {
		this.txHandler = new TxHandler(graphDb);
	}

	public TxHandler getTxHandler() {
		return txHandler;
	}

	public void setTxHandler(TxHandler txHandler) {
		this.txHandler = txHandler;
	}

	@Override
	public GraphStore graph() throws PMException {
		return new Neo4jMemoryGraphStore(txHandler);
	}

	@Override
	public ProhibitionsStore prohibitions() throws PMException {
		return new Neo4jMemoryProhibitionStore(txHandler);
	}

	@Override
	public ObligationsStore obligations() throws PMException {
		return new Neo4jMemoryObligationStore(txHandler);
	}

	@Override
	public OperationsStore operations() throws PMException {
		return new Neo4jMemoryOperationsStore(txHandler);
	}

	@Override
	public RoutinesStore routines() throws PMException {
		return new Neo4jMemoryRoutinesStore(txHandler);
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
