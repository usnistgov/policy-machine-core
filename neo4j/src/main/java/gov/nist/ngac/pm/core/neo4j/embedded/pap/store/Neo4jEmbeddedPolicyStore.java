// This file is part of the policy-machine-core-neo4j module, which links against
// org.neo4j:neo4j (GPLv3) and is distributed under GPLv3. See neo4j/LICENSE.
package gov.nist.ngac.pm.core.neo4j.embedded.pap.store;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.store.GraphStore;
import gov.nist.ngac.pm.core.pap.store.ObligationsStore;
import gov.nist.ngac.pm.core.pap.store.OperationsStore;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;
import gov.nist.ngac.pm.core.pap.store.ProhibitionsStore;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

/**
 * A {@link PolicyStore} backed by an embedded Neo4j database. Its sub-stores share a single
 * {@link TxHandler} so they participate in the same transaction.
 */
public class Neo4jEmbeddedPolicyStore implements PolicyStore {

	/**
	 * Creates the indexes used by this store's queries, if they don't already exist.
	 *
	 * @param graphDb the database to create indexes on
	 */
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
	 * Creates a policy store backed by the given database.
	 * @param graphDb the graph database service
	 * @throws PMException if an error occurs initializing the policy store
	 */
	public Neo4jEmbeddedPolicyStore(GraphDatabaseService graphDb) throws PMException {
		this.txHandler = new TxHandler(graphDb);
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
		return new Neo4jEmbeddedObligationStore(txHandler);
	}

	@Override
	public OperationsStore operations() {
		return new Neo4jEmbeddedOperationsStore(txHandler);
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
