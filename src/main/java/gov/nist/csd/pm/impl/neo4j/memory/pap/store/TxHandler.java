package gov.nist.csd.pm.impl.neo4j.memory.pap.store;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.tx.Transactional;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

public class TxHandler implements Transactional {

	protected Transaction tx;
	protected int txCounter;
	protected GraphDatabaseService graphDb;

	public TxHandler(GraphDatabaseService graphDb) {
		this.graphDb = graphDb;
		this.txCounter = 0;
	}

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

	public interface Neo4jTxRunner {
		void runTx(Transaction tx) throws PMException;
	}
}
