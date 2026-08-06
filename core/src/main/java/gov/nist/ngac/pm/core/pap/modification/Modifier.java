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
     * Runs the given callback inside a transaction, committing on success or rolling back and rethrowing
     * on failure.
     *
     * @return the callback's result
     * @throws PMException if the callback throws, after the transaction has been rolled back
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
     * Runs the given callback inside a transaction, committing on success or rolling back and rethrowing
     * on failure.
     *
     * @throws PMException if the callback throws, after the transaction has been rolled back
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
     * Callback invoked inside a transaction by {@link #runTx(Runner)}, producing a result.
     */
    public interface Runner<T> {
        T run() throws PMException;
    }

    /**
     * Callback invoked inside a transaction by {@link #runTx(VoidRunner)}.
     */
    public interface VoidRunner {
        void run() throws PMException;
    }
}
