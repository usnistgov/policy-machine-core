package gov.nist.csd.pm.core.impl.memory.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.tx.Transactional;

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