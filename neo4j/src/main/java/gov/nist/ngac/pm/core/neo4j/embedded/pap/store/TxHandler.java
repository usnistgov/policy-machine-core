/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST.
 *
 * This file is part of the policy-machine-core-neo4j module, which compiles against
 * and embeds org.neo4j:neo4j (GPLv3, Community Edition). As a combined work, this
 * module is distributed under the GNU General Public License v3.0, not the CC BY 4.0
 * license used elsewhere in this repository. See neo4j/LICENSE for the full text.
 */

// This file is part of the policy-machine-core-neo4j module, which links against
// org.neo4j:neo4j (GPLv3) and is distributed under GPLv3. See neo4j/LICENSE.
package gov.nist.ngac.pm.core.neo4j.embedded.pap.store;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.tx.Transactional;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

/**
 * Wraps a Neo4j {@link Transaction} with reference-counted begin/commit/rollback, so nested calls share
 * one transaction and only the outermost commit or rollback applies.
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
	 * Runs a callback within the current or a newly begun transaction, committing on success or rolling
	 * back on failure.
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
	 * A callback that runs against the active Neo4j transaction.
	 */
	public interface Neo4jTxRunner {
		void runTx(Transaction tx) throws PMException;
	}
}
