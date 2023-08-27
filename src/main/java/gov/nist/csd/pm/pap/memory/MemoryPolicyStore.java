package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.PolicyStore;
import gov.nist.csd.pm.policy.*;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.List;

public class MemoryPolicyStore extends PolicyStore implements BaseMemoryTx {

    private MemoryGraph graph;
    private MemoryProhibitions prohibitions;
    private MemoryObligations obligations;
    private MemoryUserDefinedPML userDefinedPML;

    private boolean inTx;
    private int txCounter;
    private TxPolicyStore txPolicyStore;

    public MemoryPolicyStore() {
        this.graph = new MemoryGraph();
        this.prohibitions = new MemoryProhibitions();
        this.obligations = new MemoryObligations();
        this.userDefinedPML = new MemoryUserDefinedPML();
    }

    public MemoryPolicyStore(Graph graph, Prohibitions prohibitions, Obligations obligations, UserDefinedPML userDefinedPML) throws PMException {
        this.graph = new MemoryGraph(graph);
        this.prohibitions = new MemoryProhibitions(prohibitions);
        this.obligations = new MemoryObligations(obligations);
        this.userDefinedPML = new MemoryUserDefinedPML(userDefinedPML);
    }

    MemoryPolicyStore(MemoryGraph graph, MemoryProhibitions prohibitions, MemoryObligations obligations, MemoryUserDefinedPML userDefinedPML) throws PMException {
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
        this.graph = (MemoryGraph) graph;
    }

    public void setProhibitions(Prohibitions prohibitions) {
        this.prohibitions = (MemoryProhibitions) prohibitions;
    }

    public void setObligations(Obligations obligations) {
        this.obligations = (MemoryObligations) obligations;
    }

    public void setUserDefinedPML(UserDefinedPML userDefinedPML) {
        this.userDefinedPML = (MemoryUserDefinedPML) userDefinedPML;
    }

    @Override
    public MemoryGraph graph() {
        return graph;
    }

    @Override
    public MemoryProhibitions prohibitions() {
        return prohibitions;
    }

    @Override
    public MemoryObligations obligations() {
        return obligations;
    }

    @Override
    public MemoryUserDefinedPML userDefinedPML() {
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
