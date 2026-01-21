package gov.nist.csd.pm.core.pap.modification;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.tx.Transactional;
import gov.nist.csd.pm.core.pap.store.PolicyStore;

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

    public interface Runner<T> {
        T run() throws PMException;
    }

    public interface VoidRunner {
        void run() throws PMException;
    }
}
