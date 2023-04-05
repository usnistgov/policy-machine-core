package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.PolicyWriter;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;

class TxPolicyStore implements PolicyWriter, PolicyEventEmitter {

    private final MemoryPolicyStore memoryPolicyStore;

    /**
     * An event listener to track the events that occur during the transaction.
     * These events will be committed to the target policy store on commit.
     */
    protected TxPolicyEventListener txPolicyEventListener;

    public TxPolicyStore(MemoryPolicyStore txStore) {
        this.memoryPolicyStore = txStore;
        this.txPolicyEventListener = new TxPolicyEventListener();
    }

    public TxPolicyEventListener getTxPolicyEventListener() {
        return txPolicyEventListener;
    }

    public void clearEvents() {
        txPolicyEventListener = new TxPolicyEventListener();
    }

    @Override
    public void setResourceAccessRights(AccessRightSet accessRightSet) {
        emitEvent(new SetResourceAccessRightsEvent(accessRightSet));
    }

    @Override
    public String createPolicyClass(String name, Map<String, String> properties) {
        emitEvent(new CreatePolicyClassEvent(name, properties));
        return name;
    }

    @Override
    public String createPolicyClass(String name) {
        return createPolicyClass(name, noprops());
    }

    @Override
    public String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents) {
        emitEvent(new CreateUserAttributeEvent(name, properties, parent, parents));
        return name;
    }

    @Override
    public String createUserAttribute(String name, String parent, String... parents) {
        return createUserAttribute(name, noprops(), parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents) {
        emitEvent(new CreateObjectAttributeEvent(name, properties, parent, parents));
        return name;
    }

    @Override
    public String createObjectAttribute(String name, String parent, String... parents) {
        return createObjectAttribute(name, noprops(), parent, parents);
    }

    @Override
    public String createObject(String name, Map<String, String> properties, String parent, String... parents) {
        emitEvent(new CreateObjectEvent(name, properties, parent, parents));
        return name;
    }

    @Override
    public String createObject(String name, String parent, String... parents) {
        return createObject(name, noprops(), parent, parents);
    }

    @Override
    public String createUser(String name, Map<String, String> properties, String parent, String... parents) {
        emitEvent(new CreateUserEvent(name, properties, parent, parents));
        return name;
    }

    @Override
    public String createUser(String name, String parent, String... parents) {
        return createUser(name, noprops(), parent, parents);
    }

    @Override
    public void setNodeProperties(String name, Map<String, String> properties) {
        emitEvent(new TxEvents.MemorySetNodePropertiesEvent(name, memoryPolicyStore.getNode(name).getProperties(), properties));
    }

    @Override
    public void deleteNode(String name) {
        emitEvent(new TxEvents.MemoryDeleteNodeEvent(name, memoryPolicyStore.getNode(name), memoryPolicyStore.getParents(name)));
    }

    @Override
    public void assign(String child, String parent) {
        emitEvent(new AssignEvent(child, parent));
    }

    @Override
    public void deassign(String child, String parent) {
        emitEvent(new DeassignEvent(child, parent));
    }

    @Override
    public void assignAll(List<String> children, String target) throws PMException {
        emitEvent(new AssignAllEvent(children, target));
    }

    @Override
    public void deassignAll(List<String> children, String target) throws PMException {
        emitEvent(new DeassignAllEvent(children, target));
    }

    @Override
    public void deassignAllFromAndDelete(String target) throws PMException {
        emitEvent(new DeassignAllFromAndDeleteEvent(target));
    }

    @Override
    public void associate(String ua, String target, AccessRightSet accessRights) {
        emitEvent(new AssociateEvent(ua, target, accessRights));
    }

    @Override
    public void dissociate(String ua, String target) {
        AccessRightSet accessRightSet = new AccessRightSet();
        for (Association association : memoryPolicyStore.getAssociationsWithSource(ua)) {
            if (association.getTarget().equals(target)) {
                accessRightSet = association.getAccessRightSet();
            }
        }

        emitEvent(new TxEvents.MemoryDissociateEvent(ua, target, accessRightSet));
    }

    @Override
    public void createProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) {
        emitEvent(new CreateProhibitionEvent(label, subject, accessRightSet, intersection, List.of(containerConditions)));
    }

    @Override
    public void updateProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        emitEvent(new TxEvents.MemoryUpdateProhibitionEvent(
                new Prohibition(label, subject, accessRightSet, intersection, List.of(containerConditions)),
                memoryPolicyStore.getProhibition(label)
        ));
    }

    @Override
    public void deleteProhibition(String label) throws PMException {
        emitEvent(new TxEvents.MemoryDeleteProhibitionEvent(memoryPolicyStore.getProhibition(label)));
    }

    @Override
    public void createObligation(UserContext author, String label, Rule... rules) {
        emitEvent(new CreateObligationEvent(author, label, List.of(rules)));
    }

    @Override
    public void updateObligation(UserContext author, String label, Rule... rules) throws PMException {
        emitEvent(new TxEvents.MemoryUpdateObligationEvent(new Obligation(author, label, List.of(rules)), memoryPolicyStore.getObligation(label)));
    }

    @Override
    public void deleteObligation(String label) throws PMException {
        emitEvent(new TxEvents.MemoryDeleteObligationEvent(memoryPolicyStore.getObligation(label)));
    }

    @Override
    public void addPALFunction(FunctionDefinitionStatement functionDefinitionStatement) {
        emitEvent(new AddFunctionEvent(functionDefinitionStatement));
    }

    @Override
    public void removePALFunction(String functionName) {
        emitEvent(new TxEvents.MemoryRemoveFunctionEvent(memoryPolicyStore.getPALFunctions().get(functionName)));
    }

    @Override
    public void addPALConstant(String constantName, Value constantValue) {
        emitEvent(new AddConstantEvent(constantName, constantValue));
    }

    @Override
    public void removePALConstant(String constName) {
        emitEvent(new TxEvents.MemoryRemoveConstantEvent(constName, memoryPolicyStore.getPALConstants().get(constName)));
    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) {

    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {

    }

    @Override
    public void emitEvent(PolicyEvent event) {
        txPolicyEventListener.handlePolicyEvent(event);
    }
}
