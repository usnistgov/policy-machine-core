package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.PolicyStore;
import gov.nist.csd.pm.policy.Graph;
import gov.nist.csd.pm.policy.Obligations;
import gov.nist.csd.pm.policy.Prohibitions;
import gov.nist.csd.pm.policy.UserDefinedPML;
import gov.nist.csd.pm.policy.pml.PMLContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.events.PolicySynchronizationEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.*;

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
    public synchronized PolicySynchronizationEvent policySync() {
        return new PolicySynchronizationEvent(
                this
        );
    }

    @Override
    public synchronized void beginTx() throws PMException {
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
    public synchronized void commit() throws PMException {
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
    public synchronized void rollback() throws PMException {
        inTx = false;
        txCounter = 0;

        MemoryTx tx = new MemoryTx(true, txCounter, txPolicyStore);
        graph.tx = tx;
        prohibitions.tx = tx;
        obligations.tx = tx;
        userDefinedPML.tx = tx;

        TxPolicyEventListener txPolicyEventListener = txPolicyStore.getTxPolicyEventListener();
        txPolicyEventListener.revert(this);
    }

    @Override
    protected void reset() {
        graph = new MemoryGraph();
        prohibitions = new MemoryProhibitions();
        obligations = new MemoryObligations();
        userDefinedPML = new MemoryUserDefinedPML();
    }
}
