package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.store.*;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.events.PolicySynchronizationEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.tx.TxPolicyEventListener;

import java.util.List;

class TxPolicyStore extends PolicyStore {

    /**
     * The policy store to operate on during the transaction
     */
    protected final MemoryPolicyStore txStore;

    /**
     * An event listener to track the events that occur during the transaction.
     * These events will be committed to the target policy store on commit.
     */
    protected final TxPolicyEventListener txPolicyEventListener;

    public TxPolicyStore(MemoryPolicyStore txStore) {
        this.txStore = copy(txStore);
        this.txPolicyEventListener = new TxPolicyEventListener();
    }

    private MemoryPolicyStore copy(MemoryPolicyStore memoryPolicyStore) {
        PolicySynchronizationEvent policySync = memoryPolicyStore.policySync();
        return new MemoryPolicyStore(policySync);
    }

    public MemoryPolicyStore txStore() {
        return txStore;
    }

    public List<PolicyEvent> getEvents() {
        return txPolicyEventListener.getEvents();
    }

    @Override
    public GraphStore graph() {
        return new TxGraph(txStore.graph(), txPolicyEventListener);
    }

    @Override
    public ProhibitionsStore prohibitions() {
        return new TxProhibitions(txStore.prohibitions(), txPolicyEventListener);
    }

    @Override
    public ObligationsStore obligations() {
        return new TxObligations(txStore.obligations(), txPolicyEventListener);
    }

    @Override
    public PALStore pal() {
        return new TxPAL(txStore.pal(), txPolicyEventListener);
    }

    @Override
    public PolicySynchronizationEvent policySync() throws PMException {
        return txStore.policySync();
    }

    @Override
    public void beginTx() throws PMException {

    }

    @Override
    public void commit() throws PMException {

    }

    @Override
    public void rollback() throws PMException {

    }
}
