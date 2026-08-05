package gov.nist.ngac.pm.core.impl.memory.pap.store;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.tx.Transactional;

/**
 * Base class for the in-memory sub-stores, delegating transaction control to a shared {@link MemoryTx}.
 */
public class MemoryStore implements Transactional {

    protected MemoryPolicy policy;
    protected MemoryTx tx;
    protected TxCmdTracker txCmdTracker;

    public MemoryStore(MemoryPolicy policy, MemoryTx tx, TxCmdTracker txCmdTracker) {
        this.policy = policy;
        this.tx = tx;
        this.txCmdTracker = txCmdTracker;
    }

    @Override
    public final void beginTx() throws PMException {
        tx.beginTx();
    }

    @Override
    public final void commit() throws PMException {
        tx.commit();
    }

    @Override
    public final void rollback() throws PMException {
        tx.rollback();
    }
}