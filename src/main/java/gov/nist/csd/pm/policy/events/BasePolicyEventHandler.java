package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.pml.PALContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.List;
import java.util.Map;

/**
 * Implements the PolicyEventListener interface to apply policy events to the passed PolicyAuthor.
 */
public abstract class BasePolicyEventHandler implements PolicyEventListener {
    
    protected PolicyAuthor policy;

    protected BasePolicyEventHandler(PolicyAuthor policy) {
        this.policy = policy;
    }

    @Override
    public void handlePolicyEvent(PolicyEvent event) throws PMException {
        if (event instanceof CreateNodeEvent createNodeEvent) {
            handleCreateNodeEvent(createNodeEvent);
        } else if (event instanceof AssignEvent assignEvent) {
            handleAssignEvent(assignEvent);
        } else if (event instanceof AssociateEvent associateEvent) {
            handleAssociateEvent(associateEvent);
        } else if (event instanceof CreateObligationEvent createObligationEvent) {
            handleCreateObligationEvent(createObligationEvent);
        } else if (event instanceof CreateProhibitionEvent createProhibitionEvent) {
            handleCreateProhibitionEvent(createProhibitionEvent);
        } else if (event instanceof DeassignEvent deassignEvent) {
            handleDeassignEvent(deassignEvent);
        } else if (event instanceof DeleteNodeEvent deleteNodeEvent) {
            handleDeleteNodeEvent(deleteNodeEvent);
        } else if (event instanceof DeleteObligationEvent deleteObligationEvent) {
            handleDeleteObligationEvent(deleteObligationEvent);
        } else if (event instanceof DeleteProhibitionEvent deleteProhibitionEvent) {
            handleDeleteProhibitionEvent(deleteProhibitionEvent);
        } else if (event instanceof DissociateEvent dissociateEvent) {
            handleDissociateEvent(dissociateEvent);
        } else if (event instanceof SetNodePropertiesEvent setNodePropertiesEvent) {
            handleSetNodePropertiesEvent(setNodePropertiesEvent);
        } else if (event instanceof SetResourceAccessRightsEvent setResourceAccessRightsEvent) {
            handleSetResourceAccessRights(setResourceAccessRightsEvent);
        } else if (event instanceof UpdateObligationEvent updateObligationEvent) {
            handleUpdateObligationEvent(updateObligationEvent);
        } else if (event instanceof UpdateProhibitionEvent updateProhibitionEvent) {
            handleUpdateProhibitionEvent(updateProhibitionEvent);
        }
    }

    protected void handleUpdateProhibitionEvent(UpdateProhibitionEvent updateProhibitionEvent) throws PMException {
        policy.updateProhibition(
                updateProhibitionEvent.getName(),
                updateProhibitionEvent.getSubject(),
                updateProhibitionEvent.getAccessRightSet(),
                updateProhibitionEvent.isIntersection(),
                updateProhibitionEvent.getContainers().toArray(ContainerCondition[]::new)
        );
    }

    protected void handleUpdateObligationEvent(UpdateObligationEvent updateObligationEvent) throws PMException {
        policy.updateObligation(
                updateObligationEvent.getAuthor(),
                updateObligationEvent.getLabel(),
                updateObligationEvent.getRules().toArray(Rule[]::new)
        );
    }

    protected void handleSetResourceAccessRights(SetResourceAccessRightsEvent setResourceAccessRightsEvent) throws PMException {
        policy.setResourceAccessRights(setResourceAccessRightsEvent.getAccessRightSet());
    }

    protected void handleSetNodePropertiesEvent(SetNodePropertiesEvent setNodePropertiesEvent) throws PMException {
        policy.setNodeProperties(setNodePropertiesEvent.getName(), setNodePropertiesEvent.getProperties());
    }

    protected void handleDissociateEvent(DissociateEvent dissociateEvent) throws PMException {
        policy.dissociate(dissociateEvent.getUa(), dissociateEvent.getTarget());
    }

    protected void handleDeleteProhibitionEvent(DeleteProhibitionEvent deleteProhibitionEvent) throws PMException {
        policy.deleteProhibition(deleteProhibitionEvent.getProhibition().getLabel());
    }

    protected void handleDeleteObligationEvent(DeleteObligationEvent deleteObligationEvent) throws PMException {
        policy.deleteObligation(deleteObligationEvent.getObligation().getLabel());
    }

    protected void handleDeleteNodeEvent(DeleteNodeEvent deleteNodeEvent) throws PMException {
        policy.deleteNode(deleteNodeEvent.getName());
    }

    protected void handleDeassignEvent(DeassignEvent deassignEvent) throws PMException {
        policy.deassign(deassignEvent.getChild(), deassignEvent.getParent());
    }

    protected void handleCreateProhibitionEvent(CreateProhibitionEvent createProhibitionEvent) throws PMException {
        policy.createProhibition(
                createProhibitionEvent.getLabel(),
                createProhibitionEvent.getSubject(),
                createProhibitionEvent.getAccessRightSet(),
                createProhibitionEvent.isIntersection(),
                createProhibitionEvent.getContainers().toArray(ContainerCondition[]::new)
        );
    }

    protected void handleCreateObligationEvent(CreateObligationEvent createObligationEvent) throws PMException {
        policy.createObligation(createObligationEvent.getAuthor(),
                createObligationEvent.getLabel(),
                createObligationEvent.getRules().toArray(Rule[]::new));
    }

    protected void handleAssociateEvent(AssociateEvent associateEvent) throws PMException {
        policy.associate(associateEvent.getUa(), associateEvent.getTarget(), associateEvent.getAccessRightSet());
    }

    protected void handleAssignEvent(AssignEvent assignEvent) throws PMException {
        policy.assign(assignEvent.getChild(), assignEvent.getParent());
    }

    protected void handleCreateNodeEvent(CreateNodeEvent createNodeEvent) throws PMException {
        switch (createNodeEvent.getType()) {
            case PC -> this.policy.createPolicyClass(
                    createNodeEvent.getName(),
                    createNodeEvent.getProperties()
            );
            case OA -> this.policy.createObjectAttribute(
                    createNodeEvent.getName(),
                    createNodeEvent.getProperties(),
                    createNodeEvent.getInitialParent(),
                    createNodeEvent.getAdditionalParents()
            );
            case UA -> this.policy.createUserAttribute(
                    createNodeEvent.getName(),
                    createNodeEvent.getProperties(),
                    createNodeEvent.getInitialParent(),
                    createNodeEvent.getAdditionalParents()
            );
            case O -> this.policy.createObject(
                    createNodeEvent.getName(),
                    createNodeEvent.getProperties(),
                    createNodeEvent.getInitialParent(),
                    createNodeEvent.getAdditionalParents()
            );
            case U -> this.policy.createUser(
                    createNodeEvent.getName(),
                    createNodeEvent.getProperties(),
                    createNodeEvent.getInitialParent(),
                    createNodeEvent.getAdditionalParents()
            );
            default -> { /* ANY will never be passed as a node type */ }
        }
    }

    @Override
    public AccessRightSet getResourceAccessRights() throws PMException {
        return policy.getResourceAccessRights();
    }

    @Override
    public boolean nodeExists(String name) throws PMException {
        return policy.nodeExists(name);
    }

    @Override
    public Node getNode(String name) throws PMException {
        return policy.getNode(name);
    }

    @Override
    public List<String> search(NodeType type, Map<String, String> properties) throws PMException {
        return policy.search(type, properties);
    }

    @Override
    public List<String> getPolicyClasses() throws PMException {
        return policy.getPolicyClasses();
    }

    @Override
    public List<String> getChildren(String node) throws PMException {
        return policy.getChildren(node);
    }

    @Override
    public List<String> getParents(String node) throws PMException {
        return policy.getParents(node);
    }

    @Override
    public List<Association> getAssociationsWithSource(String ua) throws PMException {
        return policy.getAssociationsWithSource(ua);
    }

    @Override
    public List<Association> getAssociationsWithTarget(String target) throws PMException {
        return policy.getAssociationsWithTarget(target);
    }

    @Override
    public Map<String, List<Prohibition>> getProhibitions() throws PMException {
        return policy.getProhibitions();
    }

    @Override
    public boolean prohibitionExists(String label) throws PMException {
        return policy.prohibitionExists(label);
    }

    @Override
    public List<Prohibition> getProhibitionsWithSubject(String subject) throws PMException {
        return policy.getProhibitionsWithSubject(subject);
    }

    @Override
    public Prohibition getProhibition(String label) throws PMException {
        return policy.getProhibition(label);
    }

    @Override
    public List<Obligation> getObligations() throws PMException {
        return policy.getObligations();
    }

    @Override
    public boolean obligationExists(String label) throws PMException {
        return policy.obligationExists(label);
    }

    @Override
    public Obligation getObligation(String label) throws PMException {
        return policy.getObligation(label);
    }

    @Override
    public Map<String, FunctionDefinitionStatement> getPALFunctions() throws PMException {
        return policy.getPALFunctions();
    }

    @Override
    public Map<String, Value> getPALConstants() throws PMException {
        return policy.getPALConstants();
    }

    @Override
    public PALContext getPALContext() throws PMException {
        return policy.getPALContext();
    }
}
