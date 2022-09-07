package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.store.GraphStore;
import gov.nist.csd.pm.policy.author.GraphAuthor;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.tx.TxPolicyEventListener;

import java.util.List;
import java.util.Map;

class TxGraph extends GraphStore implements PolicyEventEmitter {

    private final MemoryGraphStore store;
    private final TxPolicyEventListener txPolicyEventListener;

    public TxGraph(MemoryGraphStore store, TxPolicyEventListener txPolicyEventListener) {
        this.store = store;
        this.txPolicyEventListener = txPolicyEventListener;
    }

    @Override
    public void setResourceAccessRights(AccessRightSet accessRightSet) throws PMException {
        emitEvent(new SetResourceAccessRightsEvent(accessRightSet));
        store.setResourceAccessRights(accessRightSet);
    }

    @Override
    public AccessRightSet getResourceAccessRights() throws PMException {
        return store.getResourceAccessRights();
    }

    @Override
    public String createPolicyClass(String name, Map<String, String> properties) throws PMException {
        emitEvent(new CreatePolicyClassEvent(name, properties));
        return store.createPolicyClass(name, properties);
    }

    @Override
    public String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        emitEvent(new CreateUserAttributeEvent(name, properties, parent, parents));
        return store.createUserAttribute(name, properties, parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        emitEvent(new CreateObjectAttributeEvent(name, properties, parent, parents));
        return store.createObjectAttribute(name, properties, parent, parents);
    }

    @Override
    public String createObject(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        emitEvent(new CreateObjectEvent(name, properties, parent, parents));
        return store.createObject(name, properties, parent, parents);
    }

    @Override
    public String createUser(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        emitEvent(new CreateUserEvent(name, properties, parent, parents));
        return store.createUser(name, properties, parent, parents);
    }

    @Override
    public void setNodeProperties(String name, Map<String, String> properties) throws PMException {
        emitEvent(new SetNodePropertiesEvent(name, properties));
        store.setNodeProperties(name, properties);
    }

    @Override
    public void deleteNode(String name) throws PMException {
        emitEvent(new DeleteNodeEvent(name));
        store.deleteNode(name);
    }

    @Override
    public boolean nodeExists(String name) throws PMException {
        return store.nodeExists(name);
    }

    @Override
    public Node getNode(String name) throws PMException {
        return store.getNode(name);
    }

    @Override
    public List<String> search(NodeType type, Map<String, String> properties) throws PMException {
        return store.search(type, properties);
    }

    @Override
    public List<String> getPolicyClasses() throws PMException {
        return store.getPolicyClasses();
    }

    @Override
    public void assign(String child, String parent) throws PMException {
        emitEvent(new AssignEvent(child, parent));
        store.assign(child, parent);
    }

    @Override
    public void deassign(String child, String parent) throws PMException {
        emitEvent(new DeassignEvent(child, parent));
        store.deassign(child, parent);
    }

    @Override
    public List<String> getChildren(String node) throws PMException {
        return store.getChildren(node);
    }

    @Override
    public List<String> getParents(String node) throws PMException {
        return store.getParents(node);
    }

    @Override
    public void associate(String ua, String target, AccessRightSet accessRights) throws PMException {
        emitEvent(new AssociateEvent(ua, target, accessRights));
        store.associate(ua, target, accessRights);
    }

    @Override
    public void dissociate(String ua, String target) throws PMException {
        emitEvent(new DissociateEvent(ua, target));
        store.dissociate(ua, target);
    }

    @Override
    public List<Association> getAssociationsWithSource(String ua) throws PMException {
        return store.getAssociationsWithSource(ua);
    }

    @Override
    public List<Association> getAssociationsWithTarget(String target) throws PMException {
        return store.getAssociationsWithTarget(target);
    }

    @Override
    public void beginTx() throws PMException {

    }

    @Override
    public void commit() throws PMException {

    }

    @Override
    public void rollback() throws PMException {

    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {

    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {

    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        txPolicyEventListener.handlePolicyEvent(event);
    }
}
