package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.PolicyStore;
import gov.nist.csd.pm.policy.*;
import gov.nist.csd.pm.policy.events.PolicySynchronizationEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.io.Serializable;

public class MemoryPolicyStore extends PolicyStore {

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

    @Override
    public Graph graph() {
        return graph;
    }

    @Override
    public Prohibitions prohibitions() {
        return prohibitions;
    }

    @Override
    public Obligations obligations() {
        return obligations;
    }

    @Override
    public UserDefinedPML userDefinedPML() {
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
    public PolicySynchronizationEvent policySync() {
        return new PolicySynchronizationEvent(
                this
        );
    }

    @Override
    public void beginTx() throws PMException {
        if (!inTx) {
            txPolicyStore = new TxPolicyStore(this);
        }

        inTx = true;
        txCounter++;

        MemoryTx tx = new MemoryTx(true, txCounter, txPolicyStore);
        graph.tx = tx;
        prohibitions.tx = tx;
        obligations.tx = tx;
        userDefinedPML.tx = tx;
    }

    @Override
    public void commit() throws PMException {
        txCounter--;
        if(txCounter == 0) {
            inTx = false;
            txPolicyStore.clearEvents();

            MemoryTx tx = new MemoryTx(false, txCounter, txPolicyStore);
            graph.tx = tx;
            prohibitions.tx = tx;
            obligations.tx = tx;
            userDefinedPML.tx = tx;
        }
    }

    @Override
    public void rollback() throws PMException {
        inTx = false;
        txCounter = 0;

        MemoryTx tx = new MemoryTx(false, txCounter, txPolicyStore);
        graph.tx = tx;
        prohibitions.tx = tx;
        obligations.tx = tx;
        userDefinedPML.tx = tx;

        TxPolicyEventListener txPolicyEventListener = txPolicyStore.getTxPolicyEventListener();
        txPolicyEventListener.revert(this);
    }

    @Override
    protected void reset() throws PMException {
        graph = new MemoryGraph();
        prohibitions = new MemoryProhibitions();
        obligations = new MemoryObligations();
        userDefinedPML = new MemoryUserDefinedPML();

        // a call to rollback will reset tx fields
        rollback();
    }
}
