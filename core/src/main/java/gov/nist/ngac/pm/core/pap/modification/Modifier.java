package gov.nist.ngac.pm.core.pap.modification;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.tx.Transactional;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;

abstract class Modifier implements Transactional {

    protected PolicyStore policyStore;

    public Modifier(PolicyStore policyStore) {
        this.policyStore = policyStore;
    }

    public PolicyStore getPolicyStore() {
        return policyStore;
    }

    public void setPolicyStore(PolicyStore policyStore) {
        this.policyStore = policyStore;
    }

    /**
     * Runs the given task in a transaction, committing on success or rolling back on failure.
     *
     * @return the task's result
     * @throws PMException if the task fails, after the transaction has been rolled back
     */
    protected <T> T runTx(Runner<T> txRunner) throws PMException {
        try {
            beginTx();
            T result = txRunner.run();
            commit();
            return result;
        } catch (PMException e) {
            rollback();
            throw e;
        }
    }

    /**
     * Runs the given task in a transaction, committing on success or rolling back on failure.
     *
     * @throws PMException if the task fails, after the transaction has been rolled back
     */
    protected void runTx(VoidRunner txRunner) throws PMException {
        try {
            beginTx();
            txRunner.run();
            commit();
        } catch (PMException e) {
            rollback();
            throw e;
        }
    }

    @Override
    public final void beginTx() throws PMException {
        policyStore.beginTx();
    }

    @Override
    public final void commit() throws PMException {
        policyStore.commit();
    }

    @Override
    public final void rollback() throws PMException {
        policyStore.rollback();
    }

    /**
     * A task run inside a transaction that produces a result.
     */
    public interface Runner<T> {
        T run() throws PMException;
    }

    /**
     * A task run inside a transaction.
     */
    public interface VoidRunner {
        void run() throws PMException;
    }
}
