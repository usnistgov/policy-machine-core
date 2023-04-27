package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.*;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
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

import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;

class TxPolicyStore implements Policy, PolicyEventEmitter, Graph, Prohibitions, Obligations, UserDefinedPML {

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
    public AccessRightSet getResourceAccessRights() throws PMException {
        return null;
    }

    @Override
    public String createPolicyClass(String name, Map<String, String> properties) {
        emitEvent(new CreatePolicyClassEvent(name, properties));
        return name;
    }

    @Override
    public String createPolicyClass(String name) {
        return createPolicyClass(name, NO_PROPERTIES);
    }

    @Override
    public String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents) {
        emitEvent(new CreateUserAttributeEvent(name, properties, parent, parents));
        return name;
    }

    @Override
    public String createUserAttribute(String name, String parent, String... parents) {
        return createUserAttribute(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents) {
        emitEvent(new CreateObjectAttributeEvent(name, properties, parent, parents));
        return name;
    }

    @Override
    public String createObjectAttribute(String name, String parent, String... parents) {
        return createObjectAttribute(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public String createObject(String name, Map<String, String> properties, String parent, String... parents) {
        emitEvent(new CreateObjectEvent(name, properties, parent, parents));
        return name;
    }

    @Override
    public String createObject(String name, String parent, String... parents) {
        return createObject(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public String createUser(String name, Map<String, String> properties, String parent, String... parents) {
        emitEvent(new CreateUserEvent(name, properties, parent, parents));
        return name;
    }

    @Override
    public String createUser(String name, String parent, String... parents) {
        return createUser(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public void setNodeProperties(String name, Map<String, String> properties) throws PMException {
        emitEvent(new TxEvents.MemorySetNodePropertiesEvent(name, memoryPolicyStore.graph().getNode(name).getProperties(), properties));
    }

    @Override
    public boolean nodeExists(String name) throws PMException {
        return false;
    }

    @Override
    public Node getNode(String name) throws PMException {
        return null;
    }

    @Override
    public List<String> search(NodeType type, Map<String, String> properties) throws PMException {
        return null;
    }

    @Override
    public List<String> getPolicyClasses() throws PMException {
        return null;
    }

    @Override
    public void deleteNode(String name) throws PMException {
        emitEvent(new TxEvents.MemoryDeleteNodeEvent(
                name,
                memoryPolicyStore.graph().getNode(name),
                memoryPolicyStore.graph().getParents(name)
        ));
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
    public List<String> getParents(String node) throws PMException {
        return null;
    }

    @Override
    public List<String> getChildren(String node) throws PMException {
        return null;
    }

    @Override
    public void associate(String ua, String target, AccessRightSet accessRights) {
        emitEvent(new AssociateEvent(ua, target, accessRights));
    }

    @Override
    public void dissociate(String ua, String target) throws PMException {
        AccessRightSet accessRightSet = new AccessRightSet();
        for (Association association : memoryPolicyStore.graph().getAssociationsWithSource(ua)) {
            if (association.getTarget().equals(target)) {
                accessRightSet = association.getAccessRightSet();
            }
        }

        emitEvent(new TxEvents.MemoryDissociateEvent(ua, target, accessRightSet));
    }

    @Override
    public List<Association> getAssociationsWithSource(String ua) throws PMException {
        return null;
    }

    @Override
    public List<Association> getAssociationsWithTarget(String target) throws PMException {
        return null;
    }

    @Override
    public void createProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) {
        emitEvent(new CreateProhibitionEvent(label, subject, accessRightSet, intersection, List.of(containerConditions)));
    }

    @Override
    public void updateProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        emitEvent(new TxEvents.MemoryUpdateProhibitionEvent(
                new Prohibition(label, subject, accessRightSet, intersection, List.of(containerConditions)),
                memoryPolicyStore.prohibitions().getProhibition(label)
        ));
    }

    @Override
    public void deleteProhibition(String label) throws PMException {
        emitEvent(new TxEvents.MemoryDeleteProhibitionEvent(memoryPolicyStore.prohibitions().getProhibition(label)));
    }

    @Override
    public Map<String, List<Prohibition>> getProhibitions() throws PMException {
        return null;
    }

    @Override
    public boolean prohibitionExists(String label) throws PMException {
        return false;
    }

    @Override
    public List<Prohibition> getProhibitionsWithSubject(String subject) throws PMException {
        return null;
    }

    @Override
    public Prohibition getProhibition(String label) throws PMException {
        return null;
    }

    @Override
    public void createObligation(UserContext author, String label, Rule... rules) {
        emitEvent(new CreateObligationEvent(author, label, List.of(rules)));
    }

    @Override
    public void updateObligation(UserContext author, String label, Rule... rules) throws PMException {
        emitEvent(new TxEvents.MemoryUpdateObligationEvent(
                new Obligation(author, label, List.of(rules)),
                memoryPolicyStore.obligations().getObligation(label)
        ));
    }

    @Override
    public void deleteObligation(String label) throws PMException {
        emitEvent(new TxEvents.MemoryDeleteObligationEvent(memoryPolicyStore.obligations().getObligation(label)));
    }

    @Override
    public List<Obligation> getObligations() throws PMException {
        return null;
    }

    @Override
    public boolean obligationExists(String label) throws PMException {
        return false;
    }

    @Override
    public Obligation getObligation(String label) throws PMException {
        return null;
    }

    @Override
    public void addFunction(FunctionDefinitionStatement functionDefinitionStatement) {
        emitEvent(new AddFunctionEvent(functionDefinitionStatement));
    }

    @Override
    public void removeFunction(String functionName) throws PMException {
        emitEvent(new TxEvents.MemoryRemoveFunctionEvent(memoryPolicyStore.userDefinedPML().getFunctions().get(functionName)));
    }

    @Override
    public Map<String, FunctionDefinitionStatement> getFunctions() throws PMException {
        return null;
    }

    @Override
    public FunctionDefinitionStatement getFunction(String name) throws PMException {
        return null;
    }

    @Override
    public void addConstant(String constantName, Value constantValue) {
        emitEvent(new AddConstantEvent(constantName, constantValue));
    }

    @Override
    public void removeConstant(String constName) throws PMException {
        emitEvent(new TxEvents.MemoryRemoveConstantEvent(constName, memoryPolicyStore.userDefinedPML().getConstants().get(constName)));
    }

    @Override
    public Map<String, Value> getConstants() throws PMException {
        return null;
    }

    @Override
    public Value getConstant(String name) throws PMException {
        return null;
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

    @Override
    public Graph graph() {
        return this;
    }

    @Override
    public Prohibitions prohibitions() {
        return this;
    }

    @Override
    public Obligations obligations() {
        return this;
    }

    @Override
    public UserDefinedPML userDefinedPML() {
        return this;
    }

    @Override
    public PolicySerializer serialize() throws PMException {
        return memoryPolicyStore.serialize();
    }

    @Override
    public PolicyDeserializer deserialize() throws PMException {
        return memoryPolicyStore.deserialize();
    }
}
