package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.store.PolicyStoreConnection;
import gov.nist.csd.pm.policy.PolicyReader;
import gov.nist.csd.pm.policy.author.*;
import gov.nist.csd.pm.policy.author.pal.PALExecutable;
import gov.nist.csd.pm.policy.author.pal.PALExecutor;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.tx.Transactional;

import java.util.ArrayList;
import java.util.List;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_PC;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;

public abstract class PAP extends PolicyAuthor implements PolicySync, PolicyEventEmitter, Transactional, PALExecutable, PolicyReader {

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

        this.graph = new Graph(this.policyStore);
        if (!this.graph.nodeExists(SUPER_PC)) {
            this.graph.createPolicyClass(SUPER_PC, noprops());
        }

        this.prohibitions = new Prohibitions(this.policyStore);
        this.obligations = new Obligations(this.policyStore);
        this.pal = new PAL(this.policyStore);
    }

    @Override
    public GraphAuthor graph() {
        return graph;
    }

    @Override
    public ProhibitionsAuthor prohibitions() {
        return prohibitions;
    }

    @Override
    public ObligationsAuthor obligations() {
        return obligations;
    }

    @Override
    public PALAuthor pal() {
        return pal;
    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {
        this.graph.addEventListener(listener, false);
        this.prohibitions.addEventListener(listener, false);
        this.obligations.addEventListener(listener, false);
        this.pal.addEventListener(listener, false);

        if (sync) {
            listener.handlePolicyEvent(policyStore.policySync());
        }
    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {
        this.graph.removeEventListener(listener);
        this.prohibitions.removeEventListener(listener);
        this.obligations.removeEventListener(listener);
        this.pal.removeEventListener(listener);
    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        for (PolicyEventListener listener : listeners) {
            listener.handlePolicyEvent(event);
        }
    }

    @Override
    public PolicySynchronizationEvent policySync() throws PMException {
        return this.policyStore.policySync();
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

        emitEvent(new RollbackTxEvent(this));
    }

    @Override
    public List<PALStatement> compilePAL(String input, FunctionDefinitionStatement ... customBuiltinFunctions) throws PMException {
        return new PALExecutor(this).compilePAL(input, customBuiltinFunctions);
    }

    @Override
    public void compileAndExecutePAL(UserContext author, String input, FunctionDefinitionStatement... customBuiltinFunctions) throws PMException {
        new PALExecutor(this).compileAndExecutePAL(author, input, customBuiltinFunctions);
    }

    @Override
    public String toPAL() throws PMException {
        return new PALExecutor(this).toPAL();
    }
}
