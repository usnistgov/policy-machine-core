package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.*;
import gov.nist.csd.pm.policy.pml.PMLContext;
import gov.nist.csd.pm.policy.pml.PMLExecutable;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Assignment;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.serializer.PolicyDeserializer;
import gov.nist.csd.pm.policy.serializer.PolicySerializer;
import gov.nist.csd.pm.policy.tx.Transactional;

import java.util.*;

import static gov.nist.csd.pm.pap.SuperPolicy.pcRepObjectAttribute;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;

public class PAP implements PolicySync, PolicyEventListener, PolicyEventEmitter, Transactional, PolicySerializable, PMLExecutable, Policy {

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
    public synchronized void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {
        listeners.add(listener);

        if (sync) {
            listener.handlePolicyEvent(policyStore.policySync());
        }
    }

    @Override
    public synchronized void removeEventListener(PolicyEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public synchronized void emitEvent(PolicyEvent event) throws PMException {
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
    public synchronized PolicySynchronizationEvent policySync() throws PMException {
        return this.policyStore.policySync();
    }

    @Override
    public synchronized void beginTx() throws PMException {
        policyStore.beginTx();

        emitEvent(new BeginTxEvent());
    }

    @Override
    public synchronized void commit() throws PMException {
        policyStore.commit();

        emitEvent(new CommitTxEvent());
    }

    @Override
    public synchronized void rollback() throws PMException {
        policyStore.rollback();

        emitEvent(new RollbackTxEvent(this));
    }

    @Override
    public synchronized String toString(PolicySerializer policySerializer) throws PMException {
        return policySerializer.serialize(this);
    }

    @Override
    public synchronized void fromString(String s, PolicyDeserializer policyDeserializer) throws PMException {
        beginTx();

        // reset policy store
        policyStore.reset();

        // verify super policy
        SuperPolicy.verifySuperPolicy(this.policyStore);

        // commit the reset and super policy verification so if the deserialization errors,
        // the super policy will not be reverted
        commit();

        // start a new tx to deserialize policy
        beginTx();

        // deserialize using deserializer
        policyDeserializer.deserialize(this, s);

        commit();
    }

    @Override
    public void executePML(UserContext userContext, String input, FunctionDefinitionStatement... functionDefinitionStatements) throws PMException {
        PMLExecutor.compileAndExecutePML(this, userContext, input, functionDefinitionStatements);
    }
}
