package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.GraphReader;
import gov.nist.csd.pm.policy.ObligationsReader;
import gov.nist.csd.pm.policy.PolicyReader;
import gov.nist.csd.pm.policy.ProhibitionsReader;
import gov.nist.csd.pm.policy.author.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;

/**
 * Implements the PolicyEventListener interface to apply policy events to the passed PolicyAuthor.
 */
public abstract class BasePolicyEventHandler implements PolicyEventListener, PolicyReader {
    
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
        policy.prohibitions().update(
                updateProhibitionEvent.getName(),
                updateProhibitionEvent.getSubject(),
                updateProhibitionEvent.getAccessRightSet(),
                updateProhibitionEvent.isIntersection(),
                updateProhibitionEvent.getContainers().toArray(ContainerCondition[]::new)
        );
    }

    protected void handleUpdateObligationEvent(UpdateObligationEvent updateObligationEvent) throws PMException {
        policy.obligations().update(
                updateObligationEvent.getAuthor(),
                updateObligationEvent.getLabel(),
                updateObligationEvent.getRules().toArray(Rule[]::new)
        );
    }

    protected void handleSetResourceAccessRights(SetResourceAccessRightsEvent setResourceAccessRightsEvent) throws PMException {
        policy.graph().setResourceAccessRights(setResourceAccessRightsEvent.getAccessRightSet());
    }

    protected void handleSetNodePropertiesEvent(SetNodePropertiesEvent setNodePropertiesEvent) throws PMException {
        policy.graph().setNodeProperties(setNodePropertiesEvent.getName(), setNodePropertiesEvent.getProperties());
    }

    protected void handleDissociateEvent(DissociateEvent dissociateEvent) throws PMException {
        policy.graph().dissociate(dissociateEvent.getUa(), dissociateEvent.getTarget());
    }

    protected void handleDeleteProhibitionEvent(DeleteProhibitionEvent deleteProhibitionEvent) throws PMException {
        policy.prohibitions().delete(deleteProhibitionEvent.getLabel());
    }

    protected void handleDeleteObligationEvent(DeleteObligationEvent deleteObligationEvent) throws PMException {
        policy.obligations().delete(deleteObligationEvent.getLabel());
    }

    protected void handleDeleteNodeEvent(DeleteNodeEvent deleteNodeEvent) throws PMException {
        policy.graph().deleteNode(deleteNodeEvent.getName());
    }

    protected void handleDeassignEvent(DeassignEvent deassignEvent) throws PMException {
        policy.graph().deassign(deassignEvent.getChild(), deassignEvent.getParent());
    }

    protected void handleCreateProhibitionEvent(CreateProhibitionEvent createProhibitionEvent) throws PMException {
        policy.prohibitions().create(
                createProhibitionEvent.getLabel(),
                createProhibitionEvent.getSubject(),
                createProhibitionEvent.getAccessRightSet(),
                createProhibitionEvent.isIntersection(),
                createProhibitionEvent.getContainers().toArray(ContainerCondition[]::new)
        );
    }

    protected void handleCreateObligationEvent(CreateObligationEvent createObligationEvent) throws PMException {
        policy.obligations().create(createObligationEvent.getAuthor(),
                createObligationEvent.getLabel(),
                createObligationEvent.getRules().toArray(Rule[]::new));
    }

    protected void handleAssociateEvent(AssociateEvent associateEvent) throws PMException {
        policy.graph().associate(associateEvent.getUa(), associateEvent.getTarget(), associateEvent.getAccessRightSet());
    }

    protected void handleAssignEvent(AssignEvent assignEvent) throws PMException {
        policy.graph().assign(assignEvent.getChild(), assignEvent.getParent());
    }

    protected void handleCreateNodeEvent(CreateNodeEvent createNodeEvent) throws PMException {
        switch (createNodeEvent.getType()) {
            case PC -> this.policy.graph().createPolicyClass(
                    createNodeEvent.getName(),
                    createNodeEvent.getProperties()
            );
            case OA -> this.policy.graph().createObjectAttribute(
                    createNodeEvent.getName(),
                    createNodeEvent.getProperties(),
                    createNodeEvent.getInitialParent(),
                    createNodeEvent.getAdditionalParents()
            );
            case UA -> this.policy.graph().createUserAttribute(
                    createNodeEvent.getName(),
                    createNodeEvent.getProperties(),
                    createNodeEvent.getInitialParent(),
                    createNodeEvent.getAdditionalParents()
            );
            case O -> this.policy.graph().createObject(
                    createNodeEvent.getName(),
                    createNodeEvent.getProperties(),
                    createNodeEvent.getInitialParent(),
                    createNodeEvent.getAdditionalParents()
            );
            case U -> this.policy.graph().createUser(
                    createNodeEvent.getName(),
                    createNodeEvent.getProperties(),
                    createNodeEvent.getInitialParent(),
                    createNodeEvent.getAdditionalParents()
            );
            default -> { /* ANY will never be passed as a node type */ }
        }
    }

    @Override
    public GraphReader graph() {
        return policy.graph();
    }

    @Override
    public ProhibitionsReader prohibitions() {
        return policy.prohibitions();
    }

    @Override
    public ObligationsReader obligations() {
        return policy.obligations();
    }
}
