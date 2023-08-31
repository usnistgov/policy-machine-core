package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.exceptions.PMBackendException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.tx.Transactional;

public abstract class MemoryStore<T extends BaseMemoryTx> implements Transactional, BaseMemoryTx {

    protected MemoryTx<T> tx;

    public MemoryStore() {
        tx = new MemoryTx<>();
    }

    public void setTx(boolean active, int counter, T policyStore) {
        this.tx.set(active, counter, policyStore);
    }

    public boolean inTx() {
        return tx.isActive();
    }

    protected void runInternalTx(MemoryTxRunner txRunner) throws PMBackendException {
        try {
            beginTx();
            txRunner.runTx();
            commit();
        } catch (PMException e) {
            try {
                rollback();
            } catch (PMException ex) {
                throw new PMBackendException(ex);
            }
            throw new PMBackendException(e);
        }
    }

    protected interface MemoryTxRunner {
        void runTx() throws PMException;
    }

    protected void handleTxIfActive(MemoryTxGraphHandler<T> handler) throws PMBackendException {
        if (!inTx()) {
            return;
        }

        try {
            handler.handle(tx.getStore());
        } catch (PMException e) {
            throw new PMBackendException("error handling internal tx", e);
        }
    }

    protected interface MemoryTxGraphHandler<T> {
        void handle(T t) throws PMException;
    }

}
