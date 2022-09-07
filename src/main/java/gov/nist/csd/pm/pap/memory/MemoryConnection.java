package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.store.*;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.List;

class MemoryConnection extends PolicyStoreConnection {

    private final MemoryPolicyStore main;
    private final TxHandler<TxPolicyStore> txHandler;

    public MemoryConnection(MemoryPolicyStore main) {
        this.main = main;
        this.txHandler = new TxHandler<>();
    }

    @Override
    public void beginTx() throws PMException {
        if (!txHandler.isInTx()) {
            TxPolicyStore txPolicyStore = new TxPolicyStore(main);
            txHandler.setState(txPolicyStore);
        }

        txHandler.beginTx();
    }

    @Override
    public void commit() throws PMException {
        if (!txHandler.isInTx()) {
            // if the connection is not in a tx it's operating on the main store anyways
            return;
        } else if (!txHandler.isTxDoneOnCommit()) {
            // this handles potential nested txs
            // if the tx won't be done on this commit then continue using the tx and do not commit
            // but decrement the tx's counter through tx.commit()
            txHandler.commit();
            return;
        }

        synchronized (main) {
            MemoryPolicyStore rollback = new MemoryPolicyStore(txHandler.getState().policySync());

            // apply events
            try {
                MemoryPolicyStoreListener listener = new MemoryPolicyStoreListener(main);

                List<PolicyEvent> events = txHandler.getState().getEvents();
                for (PolicyEvent event : events) {
                    listener.handlePolicyEvent(event);
                }
            } catch (PMException e) {
                main.setGraph(rollback.getGraph());
                main.setProhibitions(rollback.getProhibitions());
                main.setObligations(rollback.getObligations());
                main.setPAL(rollback.getPAL());
            }
        }

        txHandler.commit();
    }

    @Override
    public void rollback() throws PMException {
        // rollback all changes even from nested txs
        txHandler.rollback();
    }

    @Override
    public GraphStore graph() {
        if (txHandler.isInTx()) {
            return txHandler.getState().graph();
        }

        return main.graph();
    }

    @Override
    public ProhibitionsStore prohibitions() {
        if (txHandler.isInTx()) {
            return txHandler.getState().prohibitions();
        }

        return main.prohibitions();
    }

    @Override
    public ObligationsStore obligations() {
        if (txHandler.isInTx()) {
            return txHandler.getState().obligations();
        }

        return main.obligations();
    }

    @Override
    public PALStore pal() {
        if (txHandler.isInTx()) {
            return txHandler.getState().pal();
        }

        return main.pal();
    }

    @Override
    public PolicySynchronizationEvent policySync() throws PMException {
        if (txHandler.isInTx()) {
            return txHandler.getState().policySync();
        }

        return main.policySync();
    }
}
