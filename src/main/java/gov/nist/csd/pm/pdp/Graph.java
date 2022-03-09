package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.adjudicator.Adjudicator;
import gov.nist.csd.pm.pdp.reviewer.PolicyReviewer;
import gov.nist.csd.pm.policy.author.GraphAuthor;
import gov.nist.csd.pm.policy.events.PolicyEventEmitter;
import gov.nist.csd.pm.policy.events.PolicyEventListener;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Graph implements GraphAuthor, PolicyEventEmitter {

    private final UserContext userCtx;
    private final PAP pap;
    private final Adjudicator adjudicator;
    private final List<PolicyEventListener> epps;
    
    Graph(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer, List<PolicyEventListener> epps) {
        this.userCtx = userCtx;
        this.adjudicator = new Adjudicator(userCtx, pap, policyReviewer);
        this.pap = pap;
        this.epps = epps;
    }
    
    @Override
    public void setResourceAccessRights(AccessRightSet accessRightSet) throws PMException {
        adjudicator.graph().setResourceAccessRights(accessRightSet);

        pap.graph().setResourceAccessRights(accessRightSet);
    }

    @Override
    public AccessRightSet getResourceAccessRights() throws PMException {
        return pap.graph().getResourceAccessRights();
    }

    @Override
    public String createPolicyClass(String name, Map<String, String> properties) throws PMException {
        adjudicator.graph().createPolicyClass(name, properties);

        pap.graph().createPolicyClass(name, properties);

        emitEvent(new EventContext(userCtx, name, new CreatePolicyClassEvent(name, new HashMap<>())));

        return name;
    }

    @Override
    public String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        adjudicator.graph().createUserAttribute(name, properties, parent, parents);

        pap.graph().createUserAttribute(name, properties, parent, parents);

        CreateUserAttributeEvent event =
                new CreateUserAttributeEvent(name, new HashMap<>(), parent, parents);

        emitCreateNodeEvent(event, name, parent, parents);

        return name;
    }

    @Override
    public String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        adjudicator.graph().createObjectAttribute(name, properties, parent, parents);

        pap.graph().createObjectAttribute(name, properties, parent, parents);

        CreateObjectAttributeEvent event =
                new CreateObjectAttributeEvent(name, new HashMap<>(), parent, parents);

        emitCreateNodeEvent(event, name, parent, parents);


        return name;
    }

    @Override
    public String createObject(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        adjudicator.graph().createObject(name, properties, parent, parents);

        pap.graph().createObject(name, properties, parent, parents);

        CreateObjectEvent event =
                new CreateObjectEvent(name, new HashMap<>(), parent, parents);

        emitCreateNodeEvent(event, name, parent, parents);

        return name;
    }

    @Override
    public String createUser(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        adjudicator.graph().createUser(name, properties, parent, parents);

        pap.graph().createUser(name, properties, parent, parents);

        CreateUserEvent event = new CreateUserEvent(name, new HashMap<>(), parent, parents);

        emitCreateNodeEvent(event, name, parent, parents);

        return name;
    }

    private void emitCreateNodeEvent(PolicyEvent event, String name, String parent, String ... parents) throws PMException {
        // emit event for the new node
        emitEvent(new EventContext(userCtx, name, event));

        // emit event for creating a node in a parent
        emitEvent(new EventContext(userCtx, parent, event));

        // do the same for any additional parents
        for (String p : parents) {
            emitEvent(new EventContext(userCtx, p, event));
        }
    }

    @Override
    public void setNodeProperties(String name, Map<String, String> properties) throws PMException {
        adjudicator.graph().setNodeProperties(name, properties);

        pap.graph().setNodeProperties(name, properties);

        emitEvent(new EventContext(userCtx, name,
                new SetNodePropertiesEvent(name, properties)));
    }

    @Override
    public void deleteNode(String name) throws PMException {
        adjudicator.graph().deleteNode(name);

        // get parents of the deleted node before deleting to process event in the EPP
        List<String> parents = getParents(name);

        pap.graph().deleteNode(name);

        emitDeleteNodeEvent(new DeleteNodeEvent(name), name, parents);
    }

    @Override
    public boolean nodeExists(String name) throws PMException {
        return adjudicator.graph().nodeExists(name);
    }

    @Override
    public Node getNode(String name) throws PMException {
        return adjudicator.graph().getNode(name);
    }

    @Override
    public List<String> search(NodeType type, Map<String, String> properties) throws PMException {
        return adjudicator.graph().search(type, properties);
    }

    @Override
    public List<String> getPolicyClasses() throws PMException {
        return pap.graph().getPolicyClasses();
    }

    private void emitDeleteNodeEvent(PolicyEvent event, String name, List<String> parents) throws PMException {
        // emit delete node event on the deleted node
        emitEvent(new EventContext(userCtx, name, event));

        // emit delete node on each parent
        for (String parent : parents) {
            emitEvent(new EventContext(userCtx, parent, event));
        }
    }

    @Override
    public void assign(String child, String parent) throws PMException {
        adjudicator.graph().assign(child, parent);

        pap.graph().assign(child, parent);

        emitEvent(new EventContext(userCtx, child,
                new AssignEvent(child, parent)));
        emitEvent(new EventContext(userCtx, parent,
                new AssignToEvent(child, parent)));
    }

    @Override
    public void deassign(String child, String parent) throws PMException {
        adjudicator.graph().deassign(child, parent);

        pap.graph().deassign(child, parent);

        emitEvent(new EventContext(userCtx, child,
                new DeassignEvent(child, parent)));
        emitEvent(new EventContext(userCtx, parent,
                new DeassignFromEvent(child, parent)));
    }

    @Override
    public List<String> getChildren(String node) throws PMException {
        return adjudicator.graph().getChildren(node);
    }

    @Override
    public List<String> getParents(String node) throws PMException {
        return adjudicator.graph().getParents(node);
    }

    @Override
    public void associate(String ua, String target, AccessRightSet accessRights) throws PMException {
        adjudicator.graph().associate(ua, target, accessRights);

        pap.graph().associate(ua, target, accessRights);

        emitEvent(new EventContext(userCtx, ua,
                new AssociateEvent(ua, target, accessRights)));
        emitEvent(new EventContext(userCtx, target,
                new AssociateEvent(ua, target, accessRights)));
    }

    @Override
    public void dissociate(String ua, String target) throws PMException {
        adjudicator.graph().dissociate(ua, target);

        pap.graph().dissociate(ua, target);

        emitEvent(new EventContext(userCtx, ua,
                new DissociateEvent(ua, target)));
        emitEvent(new EventContext(userCtx, target,
                new DissociateEvent(ua, target)));
    }

    @Override
    public List<Association> getAssociationsWithSource(String ua) throws PMException {
        return adjudicator.graph().getAssociationsWithSource(ua);
    }

    @Override
    public List<Association> getAssociationsWithTarget(String target) throws PMException {
        return adjudicator.graph().getAssociationsWithTarget(target);
    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {
        // adding event listeners is done by the PDP class
    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {
        // removing event listeners is done by the PDP class
    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        for (PolicyEventListener epp : epps) {
            epp.handlePolicyEvent(event);
        }
    }
}
