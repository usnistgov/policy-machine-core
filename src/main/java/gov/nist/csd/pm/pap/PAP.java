package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.*;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLExecutable;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.tx.Transactional;

import java.util.HashSet;
import java.util.Set;

public class PAP implements PolicySync, PolicyEventListener, PolicyEventEmitter, Transactional, PMLExecutable, Policy {

    protected PolicyStore policyStore;

    protected Set<PolicyEventListener> listeners;

    private final PAPGraph papGraph;
    private final PAPProhibitions papProhibitions;
    private final PAPObligations papObligations;
    private final PAPUserDefinedPML papUserDefinedPML;

    public PAP(PolicyStore policyStore) throws PMException {
        this.policyStore = policyStore;
        this.listeners = new HashSet<>();

        this.papGraph = new PAPGraph(policyStore, this);
        this.papProhibitions = new PAPProhibitions(policyStore, this);
        this.papObligations = new PAPObligations(policyStore, this);
        this.papUserDefinedPML = new PAPUserDefinedPML(policyStore, this);

        SuperPolicy.verifySuperPolicy(this.policyStore);
    }

    @Override
    public Graph graph() {
        return papGraph;
    }

    @Override
    public Prohibitions prohibitions() {
        return papProhibitions;
    }

    @Override
    public Obligations obligations() {
        return papObligations;
    }

    @Override
    public UserDefinedPML userDefinedPML() {
        return papUserDefinedPML;
    }

    @Override
    public PolicySerializer serialize() throws PMException {
        return policyStore.serialize();
    }

    @Override
    public PolicyDeserializer deserialize() throws PMException {
        return new PAPDeserializer(policyStore);
    }

    @Override
    public void reset() throws PMException {
        policyStore.reset();
    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {
        listeners.add(listener);

        if (sync) {
            listener.handlePolicyEvent(policyStore.policySync());
        }
    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        for (PolicyEventListener listener : listeners) {
            listener.handlePolicyEvent(event);
        }
    }

    @Override
    public void handlePolicyEvent(PolicyEvent event) throws PMException {
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
    public void executePML(UserContext userContext, String input, FunctionDefinitionStatement... functionDefinitionStatements) throws PMException {
        PMLExecutor.compileAndExecutePML(this, userContext, input, functionDefinitionStatements);
    }
}
