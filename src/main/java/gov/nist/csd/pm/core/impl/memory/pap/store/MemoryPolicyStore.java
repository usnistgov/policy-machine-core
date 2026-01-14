package gov.nist.csd.pm.core.impl.memory.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import java.util.List;

public class MemoryPolicyStore implements PolicyStore {

    private final TxCmdTracker txCmdTracker;
    private final MemoryTx tx;
    private final MemoryPolicy policy;

    private final MemoryGraphStore graph;
    private final MemoryProhibitionsStore prohibitions;
    private final MemoryObligationsStore obligations;
    private final MemoryOperationsStore operations;
    private final MemoryRoutinesStore routines;

    public MemoryPolicyStore() {
        this.policy = new MemoryPolicy();
        this.tx = new MemoryTx();
        this.txCmdTracker = new TxCmdTracker();

        this.graph = new MemoryGraphStore(policy, tx, txCmdTracker);
        this.prohibitions = new MemoryProhibitionsStore(policy, tx, txCmdTracker);
        this.obligations = new MemoryObligationsStore(policy, tx, txCmdTracker);
        this.operations = new MemoryOperationsStore(policy, tx, txCmdTracker);
        this.routines = new MemoryRoutinesStore(policy, tx, txCmdTracker);
    }

    @Override
    public MemoryGraphStore graph() {
        return graph;
    }

    @Override
    public MemoryProhibitionsStore prohibitions() {
        return prohibitions;
    }

    @Override
    public MemoryObligationsStore obligations() {
        return obligations;
    }

    @Override
    public MemoryOperationsStore operations() {
        return operations;
    }

    @Override
    public MemoryRoutinesStore routines() {
        return routines;
    }

    @Override
    public void reset() {
        policy.reset();
    }

    @Override
    public void beginTx() throws PMException {
        tx.beginTx();
    }

    @Override
    public void commit() {
        tx.commit();

        if (tx.getCounter() == 0) {
            txCmdTracker.clearOps();
        }
    }

    @Override
    public void rollback() throws PMException {
        tx.rollback();

        List<TxCmd> events = txCmdTracker.getOperations();
        for (TxCmd txCmd : events) {
            try {
                txCmd.rollback(this);
            } catch (PMException e) {
                throw new PMException("error during tx rollback", e);
            }
        }

        txCmdTracker.clearOps();
    }
}
