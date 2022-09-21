package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.store.PolicyStoreConnection;
import gov.nist.csd.pm.policy.author.*;
import gov.nist.csd.pm.policy.author.pal.PALExecutable;
import gov.nist.csd.pm.policy.author.pal.PALExecutor;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.tx.Transactional;

import java.util.ArrayList;
import java.util.List;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_PC;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;

public abstract class PAP extends PolicyAuthor implements PolicyEventEmitter, Transactional, PALExecutable {

    protected PolicyStoreConnection policyStore;
    protected Graph graph;
    protected Prohibitions prohibitions;
    protected Obligations obligations;
    protected PAL pal;
    protected List<PolicyEventListener> listeners;

    protected PAP() {}

    protected PAP(PolicyStoreConnection policyStoreConnection) throws PMException {
        init(policyStoreConnection);
    }

    protected void init(PolicyStoreConnection policyStoreConnection) throws PMException {
        this.policyStore = policyStoreConnection;
        this.listeners = new ArrayList<>();

        /*this.graph = new Graph(
                this.policyStore,
                listeners
        );

        if (!this.graph.nodeExists(SUPER_PC)) {
            this.graph.createPolicyClass(SUPER_PC, noprops());
        }

        this.prohibitions = new Prohibitions(
                this.policyStore,
                listeners
        );

        this.obligations = new Obligations(
                this.policyStore,
                listeners
        );

        this.pal = new PAL(
                this.policyStore,
                listeners
        );*/
    }

    @Override
    public GraphAuthor graph() {
        return new Graph(
                this.policyStore,
                listeners
        );
    }

    @Override
    public ProhibitionsAuthor prohibitions() {
        return new Prohibitions(
                this.policyStore,
                listeners
        );
    }

    @Override
    public ObligationsAuthor obligations() {
        return new Obligations(
                this.policyStore,
                listeners
        );
    }

    @Override
    public PALAuthor pal() {
        return new PAL(
                this.policyStore,
                listeners
        );
    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {
        this.listeners.add(listener);

        if (sync) {
            listener.handlePolicyEvent(policyStore.policySync());
        }
    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        for (PolicyEventListener listener : listeners) {
            listener.handlePolicyEvent(event);
        }
    }

    @Override
    public void beginTx() throws PMException {
        policyStore.beginTx();

        emitEvent(new BeginTxEvent());
    }

    @Override
    public void commit() throws PMException {
        policyStore.commit();

        emitEvent(new CommitTxEvent());
    }

    @Override
    public void rollback() throws PMException {
        policyStore.rollback();

        emitEvent(new RollbackTxEvent());
    }

    @Override
    public List<PALStatement> compilePAL(String input) throws PMException {
        return new PALExecutor(this).compilePAL(input);
    }

    @Override
    public void compileAndExecutePAL(UserContext author, String input) throws PMException {
        new PALExecutor(this).compileAndExecutePAL(author, input);
    }

    @Override
    public String toPAL() throws PMException {
        return new PALExecutor(this).toPAL();
    }
}
