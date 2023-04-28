package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.Graph;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
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

import java.util.*;

import static gov.nist.csd.pm.pap.SuperPolicy.pcRepObjectAttribute;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.wildcardAccessRights;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;

class PAPGraph implements Graph, PolicyEventEmitter {

    protected PolicyStore policyStore;

    protected PolicyEventListener listener;

    public PAPGraph(PolicyStore policyStore, PolicyEventListener listener) throws PMException {
        this.policyStore = policyStore;
        this.listener = listener;

        SuperPolicy.verifySuperPolicy(this.policyStore);
    }

    @Override
    public synchronized void setResourceAccessRights(AccessRightSet accessRightSet) throws PMException {
        for (String ar : accessRightSet) {
            if (isAdminAccessRight(ar) || isWildcardAccessRight(ar)) {
                throw new AdminAccessRightExistsException(ar);
            }
        }

        policyStore.graph().setResourceAccessRights(accessRightSet);

        // notify listeners of policy modification
        emitEvent(new SetResourceAccessRightsEvent(accessRightSet));
    }

    @Override
    public synchronized AccessRightSet getResourceAccessRights() throws PMException {
        return policyStore.graph().getResourceAccessRights();
    }

    @Override
    public synchronized String createPolicyClass(String name, Map<String, String> properties) throws PMException {
        if (nodeExists(name)) {
            if (SuperPolicy.isSuperPolicyNode(name)) {
                return name;
            }

            throw new NodeNameExistsException(name);
        }

        List<PolicyEvent> events = SuperPolicy.createPolicyClass(policyStore, name, properties);
        for (PolicyEvent event : events) {
            emitEvent(event);
        }

        return name;
    }

    @Override
    public synchronized String createPolicyClass(String name) throws PMException {
        return createPolicyClass(name, NO_PROPERTIES);
    }

    @Override
    public synchronized String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        return createNode(name, UA, properties, parent, parents);
    }

    @Override
    public synchronized String createUserAttribute(String name, String parent, String... parents) throws PMException {
        return createUserAttribute(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public synchronized String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        return createNode(name, OA, properties, parent, parents);
    }

    @Override
    public synchronized String createObjectAttribute(String name, String parent, String... parents) throws PMException {
        return createObjectAttribute(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public synchronized String createObject(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        return createNode(name, O, properties, parent, parents);
    }

    @Override
    public synchronized String createObject(String name, String parent, String... parents) throws PMException {
        return createObject(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public synchronized String createUser(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        return createNode(name, U, properties, parent, parents);
    }

    @Override
    public synchronized String createUser(String name, String parent, String... parents) throws PMException {
        return createUser(name, NO_PROPERTIES, parent, parents);
    }

    private String createNode(String name, NodeType type, Map<String, String> properties, String parent, String ... parents) throws PMException {
        if (nodeExists(name)) {
            if (SuperPolicy.isSuperPolicyNode(name)) {
                return name;
            }

            throw new NodeNameExistsException(name);
        }

        // collect any parents that are of type PC
        // this will also check if the nodes exist by calling getNode
        List<String> pcParents = new ArrayList<>();
        List<String> parentsList = new ArrayList<>(Arrays.asList(parents));
        parentsList.add(parent);

        for (String p : parentsList) {
            Node parentNode = getNode(p);
            if (parentNode.getType() != PC) {
                continue;
            }

            pcParents.add(p);
        }

        switch (type) {
            case OA -> {
                policyStore.graph().createObjectAttribute(name, properties, parent, parents);
                emitEvent(new CreateObjectAttributeEvent(name, properties, parent, parents));
            }
            case UA -> {
                policyStore.graph().createUserAttribute(name, properties, parent, parents);
                emitEvent(new CreateUserAttributeEvent(name, properties, parent, parents));
            }
            case O -> {
                policyStore.graph().createObject(name, properties, parent, parents);
                emitEvent(new CreateObjectEvent(name, properties, parent, parents));
            }
            case U -> {
                policyStore.graph().createUser(name, properties, parent, parents);
                emitEvent(new CreateUserEvent(name, properties, parent, parents));
            }
            default -> { /* PC and ANY should not ever be passed to this private method */ }
        }

        // for any pc parents, create any necessary super policy configurations
        for (String pc : pcParents) {
            List<PolicyEvent> events = SuperPolicy.assignedToPolicyClass(policyStore, name, pc);
            for (PolicyEvent e : events) {
                emitEvent(e);
            }
        }

        return name;
    }

    @Override
    public synchronized void setNodeProperties(String name, Map<String, String> properties) throws PMException {
        if (!nodeExists(name)) {
            throw new NodeDoesNotExistException(name);
        }

        policyStore.graph().setNodeProperties(name, properties);

        emitEvent(new SetNodePropertiesEvent(name, properties));
    }

    @Override
    public synchronized boolean nodeExists(String name) throws PMException {
        return policyStore.graph().nodeExists(name);
    }

    @Override
    public synchronized Node getNode(String name) throws PMException {
        if (!nodeExists(name)) {
            throw new NodeDoesNotExistException(name);
        }

        return policyStore.graph().getNode(name);
    }

    @Override
    public synchronized List<String> search(NodeType type, Map<String, String> properties) throws PMException {
        return policyStore.graph().search(type, properties);
    }

    @Override
    public synchronized List<String> getPolicyClasses() throws PMException {
        return policyStore.graph().getPolicyClasses();
    }

    @Override
    public synchronized void deleteNode(String name) throws PMException {
        if (!nodeExists(name)) {
            return;
        }

        List<String> children = getChildren(name);
        if (!children.isEmpty()) {
            throw new NodeHasChildrenException(name);
        }

        checkIfNodeInProhibition(name);
        checkIfNodeInObligation(name);

        NodeType type = getNode(name).getType();

        // delete the rep node if node is a PC
        policyStore.beginTx();

        if (type == PC) {
            String rep = pcRepObjectAttribute(name);
            policyStore.graph().deleteNode(rep);
            emitEvent(new DeleteNodeEvent(rep));
        }

        policyStore.graph().deleteNode(name);
        emitEvent(new DeleteNodeEvent(name));

        policyStore.commit();
    }

    private void checkIfNodeInProhibition(String name) throws PMException {
        Map<String, List<Prohibition>> prohibitions = policyStore.prohibitions().getAll();
        for (List<Prohibition> subjPros : prohibitions.values()) {
            for (Prohibition p : subjPros) {
                if (nodeInProhibition(name, p)) {
                    throw new NodeReferencedInProhibitionException(name, p.getLabel());
                }
            }
        }
    }

    private boolean nodeInProhibition(String name, Prohibition prohibition) {
        if (prohibition.getSubject().getName().equals(name)) {
            return true;
        }

        for (ContainerCondition containerCondition : prohibition.getContainers()) {
            if (containerCondition.name().equals(name)) {
                return true;
            }
        }

        return false;
    }

    private void checkIfNodeInObligation(String name) throws PMException {
        List<Obligation> obligations = policyStore.obligations().getAll();
        for (Obligation obligation : obligations) {
            // if the node is the author of the obligation or referenced in any rules throw an exception
            if (obligation.getAuthor().getUser().equals(name)
                    || nodeInObligation(name, obligation)) {
                throw new NodeReferencedInObligationException(name, obligation.getLabel());
            }
        }
    }

    private boolean nodeInObligation(String name, Obligation obligation) {
        for (Rule rule : obligation.getRules()) {
            if (nodeInEvent(name, rule.getEventPattern())) {
                return true;
            }
        }

        return false;
    }

    private boolean nodeInEvent(String name, EventPattern event) {
        // check subject
        EventSubject subject = event.getSubject();
        if ((subject.getType() == EventSubject.Type.ANY_USER_WITH_ATTRIBUTE && subject.anyUserWithAttribute().equals(name))
                || (subject.getType() == EventSubject.Type.USERS && subject.users().contains(name))) {
            return true;
        }

        // check the target
        Target target = event.getTarget();
        return (target.getType() == Target.Type.ANY_CONTAINED_IN && target.anyContainedIn().equals(name))
                || (target.getType() == Target.Type.ANY_OF_SET && target.anyOfSet().contains(name))
                || (target.getType() == Target.Type.POLICY_ELEMENT && target.policyElement().equals(name));
    }

    @Override
    public synchronized void assign(String child, String parent) throws PMException {
        Node childNode = getNode(child);
        Node parentNode = getNode(parent);

        // ignore if assignment already exists
        if (getParents(child).contains(parent)) {
            return;
        }

        // check node types make a valid assignment relation
        Assignment.checkAssignment(childNode.getType(), parentNode.getType());

        policyStore.graph().assign(child, parent);
        emitEvent(new AssignEvent(child, parent));

        // if the parent is a policy class, need to associate the super ua with the child
        if (parentNode.getType() == PC) {
            List<PolicyEvent> events = SuperPolicy.assignedToPolicyClass(policyStore, child, parent);
            for (PolicyEvent e : events) {
                emitEvent(e);
            }
        }
    }


    @Override
    public synchronized void deassign(String child, String parent) throws PMException {
        if ((!nodeExists(child) || !nodeExists(parent))
                || (!getParents(child).contains(parent))) {
            return;
        }

        List<String> parents = policyStore.graph().getParents(child);
        if (parents.size() == 1) {
            throw new DisconnectedNodeException(child, parent);
        }

        policyStore.graph().deassign(child, parent);

        emitEvent(new DeassignEvent(child, parent));
    }

    @Override
    public void assignAll(List<String> children, String target) throws PMException {
        policyStore.beginTx();

        Node targetNode = getNode(target);

        for(String child : children) {
            Node childNode = getNode(child);
            Assignment.checkAssignment(childNode.getType(), targetNode.getType());
        }

        policyStore.graph().assignAll(children, target);

        policyStore.commit();

        emitEvent(new AssignAllEvent(children, target));
    }

    @Override
    public void deassignAll(List<String> children, String target) throws PMException {
        policyStore.beginTx();

        Node targetNode = getNode(target);

        for(String child : children) {
            Node childNode = getNode(child);

            List<String> parents = policyStore.graph().getParents(child);
            if (parents.contains(target) && parents.size() == 1) {
                throw new DisconnectedNodeException(child, target);
            }
        }

        policyStore.graph().deassignAll(children, target);

        policyStore.commit();

        emitEvent(new AssignAllEvent(children, target));
    }

    @Override
    public void deassignAllFromAndDelete(String target) throws PMException {
        deassignAll(getChildren(target), target);
        deleteNode(target);
    }

    @Override
    public synchronized List<String> getChildren(String node) throws PMException {
        if (!nodeExists(node)) {
            throw new NodeDoesNotExistException(node);
        }

        return policyStore.graph().getChildren(node);
    }

    @Override
    public synchronized void associate(String ua, String target, AccessRightSet accessRights) throws PMException {
        Node uaNode = getNode(ua);
        Node targetNode = getNode(target);

        // check the access rights are valid
        checkAccessRightsValid(policyStore.graph(), accessRights);

        // check the types of each node make a valid association
        Association.checkAssociation(uaNode.getType(), targetNode.getType());

        // associate and emit event
        policyStore.graph().associate(ua, target, accessRights);
        emitEvent(new AssociateEvent(ua, target, accessRights));
    }

    static void checkAccessRightsValid(Graph graph, AccessRightSet accessRightSet) throws PMException {
        AccessRightSet resourceAccessRights = graph.getResourceAccessRights();

        for (String ar : accessRightSet) {
            if (!resourceAccessRights.contains(ar)
                    && !allAdminAccessRights().contains(ar)
                    && !wildcardAccessRights().contains(ar)) {
                throw new UnknownAccessRightException(ar);
            }
        }
    }

    @Override
    public synchronized void dissociate(String ua, String target) throws PMException {
        if ((!nodeExists(ua) || !nodeExists(target))
                || (!getAssociationsWithSource(ua).contains(new Association(ua, target)))) {
            return;
        }

        policyStore.graph().dissociate(ua, target);

        emitEvent(new DissociateEvent(ua, target));
    }

    @Override
    public synchronized List<String> getParents(String node) throws PMException {
        if (!nodeExists(node)) {
            throw new NodeDoesNotExistException(node);
        }

        return policyStore.graph().getParents(node);
    }

    @Override
    public synchronized List<Association> getAssociationsWithSource(String ua) throws PMException {
        if (!nodeExists(ua)) {
            throw new NodeDoesNotExistException(ua);
        }

        return policyStore.graph().getAssociationsWithSource(ua);
    }

    @Override
    public synchronized List<Association> getAssociationsWithTarget(String target) throws PMException {
        if (!nodeExists(target)) {
            throw new NodeDoesNotExistException(target);
        }

        return policyStore.graph().getAssociationsWithTarget(target);
    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {

    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {

    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        listener.handlePolicyEvent(event);
    }
}
