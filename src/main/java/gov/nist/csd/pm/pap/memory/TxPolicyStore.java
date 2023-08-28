package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.*;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.List;

class TxPolicyStore implements Policy, BaseMemoryTx {

    private final MemoryPolicyStore memoryPolicyStore;

    protected TxPolicyEventTracker txPolicyEventTracker;

    private TxGraph txGraph;
    private TxProhibitions txProhibitions;
    private TxObligations txObligations;
    private TxUserDefinedPML txUserDefinedPML;

    public TxPolicyStore(MemoryPolicyStore txStore) {
        memoryPolicyStore = txStore;
        txPolicyEventTracker = new TxPolicyEventTracker();
        txGraph = new TxGraph(txPolicyEventTracker, (MemoryGraphStore) txStore.graph());
        txProhibitions = new TxProhibitions(txPolicyEventTracker, (MemoryProhibitionsStore) txStore.prohibitions());
        txObligations = new TxObligations(txPolicyEventTracker, (MemoryObligationsStore) txStore.obligations());
        txUserDefinedPML = new TxUserDefinedPML(txPolicyEventTracker, (MemoryUserDefinedPMLStore) txStore.userDefinedPML());
    }

    public List<PolicyEvent> getTxEvents() {
        return txPolicyEventTracker.getEvents();
    }

    protected TxPolicyEventTracker getTxPolicyEventListener() {
        return txPolicyEventTracker;
    }

    public void clearEvents() {
        txPolicyEventTracker = new TxPolicyEventTracker();
    }

    @Override
    public TxGraph graph() {
        return txGraph;
    }

    @Override
    public TxProhibitions prohibitions() {
        return txProhibitions;
    }

    @Override
    public TxObligations obligations() {
        return txObligations;
    }

    @Override
    public TxUserDefinedPML userDefinedPML() {
        return txUserDefinedPML;
    }

    @Override
    public PolicySerializer serialize() throws PMException {
        return memoryPolicyStore.serialize();
    }

    @Override
    public PolicyDeserializer deserialize() throws PMException {
        return memoryPolicyStore.deserialize();
    }

    @Override
    public void reset() throws PMException {
        memoryPolicyStore.reset();
        clearEvents();
    }

    @Override
    public void rollback() throws PMException {
        List<PolicyEvent> events = txPolicyEventTracker.getEvents();
    }
}
