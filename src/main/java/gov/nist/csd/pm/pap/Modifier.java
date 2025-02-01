package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.tx.Transactional;
import gov.nist.csd.pm.pap.id.IdGenerator;
import gov.nist.csd.pm.pap.store.PolicyStore;

public abstract class Modifier implements Transactional {

    protected PolicyStore store;
    protected IdGenerator idGenerator;

    public Modifier(PolicyStore store) {
        this.store = store;
    }

    public Modifier(PolicyStore store, IdGenerator idGenerator) {
        this.store = store;
        this.idGenerator = idGenerator;
    }

    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
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
