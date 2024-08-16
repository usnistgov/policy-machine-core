package gov.nist.csd.pm.pap.modification;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.tx.Transactional;
import gov.nist.csd.pm.pap.store.PolicyStore;

public abstract class Modifier implements Transactional {

    protected PolicyStore store;

    public Modifier(PolicyStore store) {
        this.store = store;
    }

    public Modifier(Modifier modifier) {
        this(modifier.store);
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
        store.beginTx();
    }

    @Override
    public final void commit() throws PMException {
        store.commit();
    }

    @Override
    public final void rollback() throws PMException {
        store.rollback();
    }

    public interface Runner<T> {
        T run() throws PMException;
    }

    public interface VoidRunner {
        void run() throws PMException;
    }
}
