package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.store.*;

import java.util.List;

public class MemoryPolicyStore implements PolicyStore {

    private TxCmdTracker txCmdTracker;
    private MemoryTx tx;
    private MemoryPolicy policy;

    private MemoryGraphStore graph;
    private MemoryProhibitionsStore prohibitions;
    private MemoryObligationsStore obligations;
    private MemoryOperationsStore operations;
    private MemoryRoutinesStore routines;

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
