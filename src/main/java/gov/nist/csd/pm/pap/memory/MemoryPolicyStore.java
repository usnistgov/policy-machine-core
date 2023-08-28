package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.*;
import gov.nist.csd.pm.policy.*;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.List;

public class MemoryPolicyStore extends PolicyStore implements BaseMemoryTx {

    private MemoryGraphStore graph;
    private MemoryProhibitionsStore prohibitions;
    private MemoryObligationsStore obligations;
    private MemoryUserDefinedPMLStore userDefinedPML;

    private boolean inTx;
    private int txCounter;
    private TxPolicyStore txPolicyStore;

    public MemoryPolicyStore() {
        this.graph = new MemoryGraphStore();
        this.prohibitions = new MemoryProhibitionsStore();
        this.obligations = new MemoryObligationsStore();
        this.userDefinedPML = new MemoryUserDefinedPMLStore();
    }

    public MemoryPolicyStore(Graph graph, Prohibitions prohibitions, Obligations obligations, UserDefinedPML userDefinedPML) throws PMException {
        this.graph = new MemoryGraphStore(graph);
        this.prohibitions = new MemoryProhibitionsStore(prohibitions);
        this.obligations = new MemoryObligationsStore(obligations);
        this.userDefinedPML = new MemoryUserDefinedPMLStore(userDefinedPML);
    }

    MemoryPolicyStore(MemoryGraphStore graph, MemoryProhibitionsStore prohibitions, MemoryObligationsStore obligations, MemoryUserDefinedPMLStore userDefinedPML) throws PMException {
        this.graph = graph;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
        this.userDefinedPML = userDefinedPML;
    }

    private void init() {
        this.graph.setMemoryProhibitions(prohibitions);
        this.graph.setMemoryObligations(obligations);
        this.prohibitions.setMemoryGraph(graph);
        this.obligations.setMemoryGraph(graph);
        this.userDefinedPML.setMemoryGraph(graph);
    }

    public List<PolicyEvent> getTxEvents() {
        return txPolicyStore.getTxEvents();
    }

    public boolean isInTx() {
        return inTx;
    }

    public void setGraph(Graph graph) {
        this.graph = (MemoryGraphStore) graph;
    }

    public void setProhibitions(Prohibitions prohibitions) {
        this.prohibitions = (MemoryProhibitionsStore) prohibitions;
    }

    public void setObligations(Obligations obligations) {
        this.obligations = (MemoryObligationsStore) obligations;
    }

    public void setUserDefinedPML(UserDefinedPML userDefinedPML) {
        this.userDefinedPML = (MemoryUserDefinedPMLStore) userDefinedPML;
    }

    @Override
    public GraphStore graph() {
        return graph;
    }

    @Override
    public ProhibitionsStore prohibitions() {
        return prohibitions;
    }

    @Override
    public ObligationsStore obligations() {
        return obligations;
    }

    @Override
    public UserDefinedPMLStore userDefinedPML() {
        return userDefinedPML;
    }

    @Override
    public PolicySerializer serialize() {
        return new MemoryPolicySerializer(this);
    }

    @Override
    public PolicyDeserializer deserialize() {
        return new MemoryPolicyDeserializer(this);
    }

    @Override
    public void beginTx() throws PMException {
        if (!inTx) {
            txPolicyStore = new TxPolicyStore(this);
        }

        inTx = true;
        txCounter++;

        graph.tx.set(true, txCounter, txPolicyStore.graph());
        prohibitions.tx.set(true, txCounter, txPolicyStore.prohibitions());
        obligations.tx.set(true, txCounter, txPolicyStore.obligations());
        userDefinedPML.tx.set(true, txCounter, txPolicyStore.userDefinedPML());
    }

    @Override
    public void commit() throws PMException {
        txCounter--;
        if(txCounter == 0) {
            inTx = false;
            txPolicyStore.clearEvents();

            graph.tx.set(false, txCounter, txPolicyStore.graph());
            prohibitions.tx.set(false, txCounter, txPolicyStore.prohibitions());
            obligations.tx.set(false, txCounter, txPolicyStore.obligations());
            userDefinedPML.tx.set(false, txCounter, txPolicyStore.userDefinedPML());
        }
    }

    @Override
    public void rollback() throws PMException {
        inTx = false;
        txCounter = 0;

        graph.tx.set(false, txCounter, txPolicyStore.graph());
        prohibitions.tx.set(false, txCounter, txPolicyStore.prohibitions());
        obligations.tx.set(false, txCounter, txPolicyStore.obligations());
        userDefinedPML.tx.set(false, txCounter, txPolicyStore.userDefinedPML());

        List<PolicyEvent> events = txPolicyStore.txPolicyEventTracker.getEvents();
        for (PolicyEvent policyEvent : events) {
            TxCmd txCmd = TxCmd.eventToCmd(policyEvent);
            if (txCmd.getType() == TxCmd.Type.GRAPH) {
                txCmd.rollback(graph);
            } else if (txCmd.getType() == TxCmd.Type.PROHIBITIONS) {
                txCmd.rollback(prohibitions);
            } else if (txCmd.getType() == TxCmd.Type.OBLIGATIONS) {
                txCmd.rollback(obligations);
            } else {
                txCmd.rollback(userDefinedPML);
            }
        }
    }

    @Override
    public void reset() throws PMException {
        graph.clear();
        prohibitions.clear();
        obligations.clear();
        userDefinedPML.clear();
    }
}
