package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.Graph;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.events.graph.*;
import gov.nist.csd.pm.policy.exceptions.NodeDoesNotExistException;
import gov.nist.csd.pm.policy.exceptions.PMBackendException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.PMRuntimeException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;

class TxGraph implements Graph, BaseMemoryTx {

    private final TxPolicyEventTracker txPolicyEventTracker;
    private final MemoryGraphStore memoryGraphStore;

    public TxGraph(TxPolicyEventTracker txPolicyEventTracker, MemoryGraphStore memoryGraphStore) {
        this.txPolicyEventTracker = txPolicyEventTracker;
        this.memoryGraphStore = memoryGraphStore;
    }

    @Override
    public void rollback() {
        List<PolicyEvent> events = txPolicyEventTracker.getEvents();
        for (PolicyEvent event : events) {
            try {
                TxCmd<MemoryGraphStore> txCmd = (TxCmd<MemoryGraphStore>) TxCmd.eventToCmd(event);
                txCmd.rollback(memoryGraphStore);
            } catch (PMException e) {
                // throw runtime exception because there is noway back if the rollback fails
                throw new PMRuntimeException("", e);
            }
        }
    }

    @Override
    public void setResourceAccessRights(AccessRightSet accessRightSet) {
        txPolicyEventTracker.trackPolicyEvent(new TxEvents.MemorySetResourceAccessRightsEvent(
                memoryGraphStore.getResourceAccessRights(),
                accessRightSet)
        );
    }

    @Override
    public AccessRightSet getResourceAccessRights() {
        return null;
    }

    @Override
    public String createPolicyClass(String name, Map<String, String> properties) {
        txPolicyEventTracker.trackPolicyEvent(new CreatePolicyClassEvent(name, properties));
        return name;
    }

    @Override
    public String createPolicyClass(String name) {
        return createPolicyClass(name, NO_PROPERTIES);
    }

    @Override
    public String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents) {
        txPolicyEventTracker.trackPolicyEvent(new CreateUserAttributeEvent(name, properties, parent, parents));
        return name;
    }

    @Override
    public String createUserAttribute(String name, String parent, String... parents) {
        return createUserAttribute(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents) {
        txPolicyEventTracker.trackPolicyEvent(new CreateObjectAttributeEvent(name, properties, parent, parents));
        return name;
    }

    @Override
    public String createObjectAttribute(String name, String parent, String... parents) {
        return createObjectAttribute(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public String createObject(String name, Map<String, String> properties, String parent, String... parents) {
        txPolicyEventTracker.trackPolicyEvent(new CreateObjectEvent(name, properties, parent, parents));
        return name;
    }

    @Override
    public String createObject(String name, String parent, String... parents) {
        return createObject(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public String createUser(String name, Map<String, String> properties, String parent, String... parents) {
        txPolicyEventTracker.trackPolicyEvent(new CreateUserEvent(name, properties, parent, parents));
        return name;
    }

    @Override
    public String createUser(String name, String parent, String... parents) {
        return createUser(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public void setNodeProperties(String name, Map<String, String> properties) throws PMBackendException {
        try {
            Map<String, String> oldProperties = memoryGraphStore.getNode(name).getProperties();
            txPolicyEventTracker.trackPolicyEvent(
                    new TxEvents.MemorySetNodePropertiesEvent(name, oldProperties, properties)
            );
        } catch (NodeDoesNotExistException e) {
            throw new PMBackendException(e);
        }
    }

    @Override
    public boolean nodeExists(String name) {
        return false;
    }

    @Override
    public Node getNode(String name) {
        return null;
    }

    @Override
    public List<String> search(NodeType type, Map<String, String> properties) {
        return null;
    }

    @Override
    public List<String> getPolicyClasses() {
        return null;
    }

    @Override
    public void deleteNode(String name) throws PMBackendException {
        try {
            txPolicyEventTracker.trackPolicyEvent(new TxEvents.MemoryDeleteNodeEvent(
                    name,
                    memoryGraphStore.getNode(name),
                    memoryGraphStore.getParents(name)
            ));
        } catch (NodeDoesNotExistException e) {
            throw new PMBackendException(e);
        }
    }

    @Override
    public void assign(String child, String parent) {
        txPolicyEventTracker.trackPolicyEvent(new AssignEvent(child, parent));
    }

    @Override
    public void deassign(String child, String parent) {
        txPolicyEventTracker.trackPolicyEvent(new DeassignEvent(child, parent));
    }

    @Override
    public List<String> getParents(String node) {
        return null;
    }

    @Override
    public List<String> getChildren(String node) {
        return null;
    }

    @Override
    public void associate(String ua, String target, AccessRightSet accessRights) {
        txPolicyEventTracker.trackPolicyEvent(new AssociateEvent(ua, target, accessRights));
    }

    @Override
    public void dissociate(String ua, String target) throws NodeDoesNotExistException, PMBackendException {
        AccessRightSet accessRightSet = new AccessRightSet();
        for (Association association : memoryGraphStore.getAssociationsWithSource(ua)) {
            if (association.getTarget().equals(target)) {
                accessRightSet = association.getAccessRightSet();
            }
        }

        txPolicyEventTracker.trackPolicyEvent(new TxEvents.MemoryDissociateEvent(ua, target, accessRightSet));
    }

    @Override
    public List<Association> getAssociationsWithSource(String ua) {
        return null;
    }

    @Override
    public List<Association> getAssociationsWithTarget(String target) {
        return null;
    }
}