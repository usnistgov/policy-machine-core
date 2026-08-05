package gov.nist.ngac.pm.core.impl.neo4j.embedded.pap.store;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.tx.Transactional;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

/**
 * Wraps a single Neo4j {@link Transaction} with reference-counted begin/commit/rollback, so nested calls
 * from different stores share one transaction and only the outermost commit or rollback actually applies.
 */
public class TxHandler implements Transactional {

	protected Transaction tx;
	protected int txCounter;
	protected GraphDatabaseService graphDb;

	public TxHandler(GraphDatabaseService graphDb) {
		this.graphDb = graphDb;
		this.txCounter = 0;
	}

	/**
	 * Runs the given callback against the current (or a newly begun) transaction, committing on success
	 * or rolling back and rethrowing on failure.
	 *
	 * @param runner the callback to execute against the transaction
	 * @throws PMException if the callback throws, after the transaction has been rolled back
	 */
	public void runTx(Neo4jTxRunner runner) throws PMException {
		try {
			runner.runTx(getTx());
			commit();
		} catch (PMException e) {
			rollback();
			throw e;
		}
	}

	private Transaction getTx() throws PMException {
		if (tx == null) {
			beginTx();
		} else {
			txCounter++;
		}

		return tx;
	}

	@Override
	public void beginTx() throws PMException {
		if (tx == null) {
			tx = graphDb.beginTx();
		}

		txCounter++;
	}

	@Override
	public void commit() throws PMException {
		if (txCounter-1 != 0) {
			txCounter--;
			return;
		}

		txCounter = 0;
		tx.commit();
		tx.close();
		tx = null;
	}

	@Override
	public void rollback() throws PMException {
		txCounter = 0;
		tx.rollback();
		tx.close();
		tx = null;
	}

	/**
	 * Callback invoked with the active Neo4j transaction by {@link TxHandler#runTx}.
	 */
	public interface Neo4jTxRunner {
		void runTx(Transaction tx) throws PMException;
	}
}
