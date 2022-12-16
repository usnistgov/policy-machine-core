package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.store.*;
import gov.nist.csd.pm.policy.serializer.PolicyDeserializer;
import gov.nist.csd.pm.policy.serializer.PolicySerializer;
import gov.nist.csd.pm.policy.events.PolicySynchronizationEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;

public class MemoryPolicyStore extends PolicyStore {

    private MemoryGraphStore graph;
    private MemoryProhibitionsStore prohibitions;
    private MemoryObligationsStore obligations;
    private MemoryPALStore pal;
    private boolean inTx;
    private int txCounter;
    private TxPolicyStore txPolicyStore;

    public MemoryPolicyStore() {
        graph = new MemoryGraphStore();
        prohibitions = new MemoryProhibitionsStore();
        obligations = new MemoryObligationsStore();
        pal = new MemoryPALStore();
    }

    public MemoryPolicyStore(PolicySynchronizationEvent event) {
        graph = new MemoryGraphStore(event.getGraph());
        prohibitions = new MemoryProhibitionsStore(event.getProhibitions());
        obligations = new MemoryObligationsStore(event.getObligations());
        pal = new MemoryPALStore(event.getPALContext());
    }

    MemoryGraphStore getGraph() {
        return graph;
    }

    MemoryProhibitionsStore getProhibitions() {
        return prohibitions;
    }

    MemoryObligationsStore getObligations() {
        return obligations;
    }

    MemoryPALStore getPAL() {
        return pal;
    }

    @Override
    public synchronized PolicySynchronizationEvent policySync() {
        return new PolicySynchronizationEvent(
                new MemoryGraphStore(graph).getGraph(),
                new MemoryProhibitionsStore(prohibitions.getAll()).getAll(),
                new MemoryObligationsStore(obligations.getAll()).getAll(),
                new MemoryPALStore().getContext()
        );
    }

    @Override
    public synchronized void beginTx() throws PMException {
        if (!inTx) {
            txPolicyStore = new TxPolicyStore(this);
        }

        inTx = true;
        txCounter++;
    }

    @Override
    public synchronized void commit() throws PMException {
        txCounter--;
        if(txCounter == 0) {
            inTx = false;
            txPolicyStore.clearEvents();
        }
    }

    @Override
    public synchronized void rollback() throws PMException {
        inTx = false;
        txCounter = 0;

        TxPolicyEventListener txPolicyEventListener = txPolicyStore.getTxPolicyEventListener();
        txPolicyEventListener.revert(this);
    }

    @Override
    public GraphStore graph() {
        if (inTx) {
            return txPolicyStore.graph();
        }

        return graph;
    }

    @Override
    public ProhibitionsStore prohibitions() {
        if (inTx) {
            return txPolicyStore.prohibitions();
        }

        return prohibitions;
    }

    @Override
    public ObligationsStore obligations() {
        if (inTx) {
            return txPolicyStore.obligations();
        }

        return obligations;
    }

    @Override
    public PALStore pal() {
        if (inTx) {
            return txPolicyStore.pal();
        }

        return pal;
    }

    @Override
    public String toString(PolicySerializer policySerializer) throws PMException {
        return policySerializer.serialize(this);
    }

    @Override
    public void fromString(String s, PolicyDeserializer policyDeserializer) throws PMException {
        if (inTx) {
            policyDeserializer.deserialize(txPolicyStore, s);
        } else {
            // clear policy
            this.graph = new MemoryGraphStore();
            this.prohibitions = new MemoryProhibitionsStore();
            this.obligations = new MemoryObligationsStore();
            this.pal = new MemoryPALStore();

            policyDeserializer.deserialize(this, s);
        }
    }
}
