package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.store.*;
import gov.nist.csd.pm.policy.serializer.PolicyDeserializer;
import gov.nist.csd.pm.policy.serializer.PolicySerializer;
import gov.nist.csd.pm.policy.events.PolicySynchronizationEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;

class TxPolicyStore extends PolicyStore {

    /**
     * The policy store to operate on during the transaction
     */
    protected final MemoryPolicyStore txStore;

    /**
     * An event listener to track the events that occur during the transaction.
     * These events will be committed to the target policy store on commit.
     */
    protected TxPolicyEventListener txPolicyEventListener;
    private final TxGraph graph;
    private final TxProhibitions prohibitions;
    private final TxObligations obligations;
    private final TxPAL pal;

    public TxPolicyStore(MemoryPolicyStore txStore) {
        this.txStore = txStore;
        this.txPolicyEventListener = new TxPolicyEventListener();
        this.graph = new TxGraph(txStore.getGraph(), txPolicyEventListener);
        this.prohibitions = new TxProhibitions(txStore.getProhibitions(), txPolicyEventListener);
        this.obligations = new TxObligations(txStore.getObligations(), txPolicyEventListener);
        this.pal = new TxPAL(txStore.getPAL(), txPolicyEventListener);
    }

    public TxPolicyEventListener getTxPolicyEventListener() {
        return txPolicyEventListener;
    }

    public void clearEvents() {
        txPolicyEventListener = new TxPolicyEventListener();
    }

    @Override
    public TxGraph graph() {
        return graph;
    }

    @Override
    public TxProhibitions prohibitions() {
        return prohibitions;
    }

    @Override
    public TxObligations obligations() {
        return obligations;
    }

    @Override
    public TxPAL pal() {
        return pal;
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

    @Override
    public String toString(PolicySerializer policySerializer) throws PMException {
        return txStore.toString(policySerializer);
    }

    @Override
    public void fromString(String s, PolicyDeserializer policyDeserializer) throws PMException {
        // clear tx events
        clearEvents();

        policyDeserializer.deserialize(this, s);

        txPolicyEventListener.handlePolicyEvent(txStore.policySync());
    }
}
