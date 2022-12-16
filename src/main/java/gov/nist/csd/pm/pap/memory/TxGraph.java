package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.store.GraphStore;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.Graph;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;

class TxGraph extends GraphStore implements PolicyEventEmitter {

    private final MemoryGraphStore store;
    private final TxPolicyEventListener txPolicyEventListener;

    public TxGraph(MemoryGraphStore store, TxPolicyEventListener txPolicyEventListener) {
        this.store = store;
        this.txPolicyEventListener = txPolicyEventListener;
    }

    void setGraph(Graph graph) {
        this.store.setGraph(graph);
    }

    @Override
    public void setResourceAccessRights(AccessRightSet accessRightSet) {
        emitEvent(new SetResourceAccessRightsEvent(accessRightSet));
        store.setResourceAccessRights(accessRightSet);
    }

    @Override
    public AccessRightSet getResourceAccessRights() {
        return store.getResourceAccessRights();
    }

    @Override
    public String createPolicyClass(String name, Map<String, String> properties) {
        emitEvent(new CreatePolicyClassEvent(name, properties));
        return store.createPolicyClass(name, properties);
    }

    @Override
    public String createPolicyClass(String name) {
        return createPolicyClass(name, noprops());
    }

    @Override
    public String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents) {
        emitEvent(new CreateUserAttributeEvent(name, properties, parent, parents));
        return store.createUserAttribute(name, properties, parent, parents);
    }

    @Override
    public String createUserAttribute(String name, String parent, String... parents) {
        return createUserAttribute(name, noprops(), parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents) {
        emitEvent(new CreateObjectAttributeEvent(name, properties, parent, parents));
        return store.createObjectAttribute(name, properties, parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, String parent, String... parents) {
        return createObjectAttribute(name, noprops(), parent, parents);
    }

    @Override
    public String createObject(String name, Map<String, String> properties, String parent, String... parents) {
        emitEvent(new CreateObjectEvent(name, properties, parent, parents));
        return store.createObject(name, properties, parent, parents);
    }

    @Override
    public String createObject(String name, String parent, String... parents) {
        return createObject(name, noprops(), parent, parents);
    }

    @Override
    public String createUser(String name, Map<String, String> properties, String parent, String... parents) {
        emitEvent(new CreateUserEvent(name, properties, parent, parents));
        return store.createUser(name, properties, parent, parents);
    }

    @Override
    public String createUser(String name, String parent, String... parents) {
        return createUser(name, noprops(), parent, parents);
    }

    @Override
    public void setNodeProperties(String name, Map<String, String> properties) {
        emitEvent(new TxEvents.MemorySetNodePropertiesEvent(name, store.getNode(name).getProperties(), properties));
        store.setNodeProperties(name, properties);
    }

    @Override
    public void deleteNode(String name) {
        emitEvent(new TxEvents.MemoryDeleteNodeEvent(name, store.getNode(name), store.getParents(name)));
        store.deleteNode(name);
    }

    @Override
    public boolean nodeExists(String name) {
        return store.nodeExists(name);
    }

    @Override
    public Node getNode(String name) {
        return store.getNode(name);
    }

    @Override
    public List<String> search(NodeType type, Map<String, String> properties) {
        return store.search(type, properties);
    }

    @Override
    public List<String> getPolicyClasses() {
        return store.getPolicyClasses();
    }

    @Override
    public void assign(String child, String parent) {
        emitEvent(new AssignEvent(child, parent));
        store.assign(child, parent);
    }

    @Override
    public void deassign(String child, String parent) {
        emitEvent(new DeassignEvent(child, parent));
        store.deassign(child, parent);
    }

    @Override
    public List<String> getChildren(String node) {
        return store.getChildren(node);
    }

    @Override
    public List<String> getParents(String node) {
        return store.getParents(node);
    }

    @Override
    public void associate(String ua, String target, AccessRightSet accessRights) {
        emitEvent(new AssociateEvent(ua, target, accessRights));
        store.associate(ua, target, accessRights);
    }

    @Override
    public void dissociate(String ua, String target) {
        AccessRightSet accessRightSet = new AccessRightSet();
        for (Association association : store.getAssociationsWithSource(ua)) {
            if (association.getTarget().equals(target)) {
                accessRightSet = association.getAccessRightSet();
            }
        }

        emitEvent(new TxEvents.MemoryDissociateEvent(ua, target, accessRightSet));
        store.dissociate(ua, target);
    }

    @Override
    public List<Association> getAssociationsWithSource(String ua) {
        return store.getAssociationsWithSource(ua);
    }

    @Override
    public List<Association> getAssociationsWithTarget(String target) {
        return store.getAssociationsWithTarget(target);
    }

    @Override
    public void beginTx() {

    }

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

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
