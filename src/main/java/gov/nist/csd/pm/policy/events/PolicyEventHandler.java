package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.exceptions.PMException;

public class PolicyEventHandler {

    public PolicyEventHandler() {}

    public void handle(PolicyEvent event) throws PMException {
        if (event instanceof AddConstantEvent e) {
            handleAddConstantEvent(e);
        } else if (event instanceof AddFunctionEvent e) {
            handleAddFunctionEvent(e);
        } else if (event instanceof AssignEvent e) {
            handleAssignEvent(e);
        } else if (event instanceof AssignToEvent e) {
            handleAssignToEvent(e);
        } else if (event instanceof AssociateEvent e) {
            handleAssociateEvent(e);
        } else if (event instanceof BeginTxEvent e) {
            handleBeginTxEvent(e);
        } else if (event instanceof CommitTxEvent e) {
            handleCommitTxEvent(e);
        }else if (event instanceof CreateObjectAttributeEvent e) {
            handleCreateObjectAttributeEvent(e);
        } else if (event instanceof CreateObjectEvent e) {
            handleCreateObjectEvent(e);
        } else if (event instanceof CreateObligationEvent e) {
            handleCreateObligationEvent(e);
        } else if (event instanceof CreatePolicyClassEvent e) {
            handleCreatePolicyClassEvent(e);
        } else if (event instanceof CreateProhibitionEvent e) {
            handleCreateProhibitionEvent(e);
        } else if (event instanceof CreateUserAttributeEvent e) {
            handleCreateUserAttributeEvent(e);
        } else if (event instanceof CreateUserEvent e) {
            handleCreateUserEvent(e);
        } else if (event instanceof CreateNodeEvent e) {
            handleCreateNodeEvent(e);
        } else if (event instanceof DeassignEvent e) {
            handleDeassignEvent(e);
        } else if (event instanceof DeassignFromEvent e) {
            handleDeassignFromEvent(e);
        } else if (event instanceof DeleteNodeEvent e) {
            handleDeleteNodeEvent(e);
        } else if (event instanceof DeleteObligationEvent e) {
            handleDeleteObligationEvent(e);
        } else if (event instanceof DeleteProhibitionEvent e) {
            handleDeleteProhibitionEvent(e);
        } else if (event instanceof DissociateEvent e) {
            handleDissociateEvent(e);
        } else if (event instanceof PolicySynchronizationEvent e) {
            handlePolicySyncEvent(e);
        } else if (event instanceof RemoveConstantEvent e) {
            handleRemoveConstantEvent(e);
        } else if (event instanceof RemoveFunctionEvent e) {
            handleRemoveFunctionEvent(e);
        } else if (event instanceof RollbackTxEvent e) {
            handleRollbackTxEvent(e);
        } else if (event instanceof SetNodePropertiesEvent e) {
            handleSetNodePropertiesEvent(e);
        } else if (event instanceof SetResourceAccessRightsEvent e) {
            handleSetResourceAccessRightsEvent(e);
        } else if (event instanceof UpdateObligationEvent e) {
            handleUpdateObligationEvent(e);
        } else if (event instanceof UpdateProhibitionEvent e) {
            handleUpdateProhibitionEvent(e);
        } else if (event instanceof AssignAllEvent e) {
            handleAssignAllEvent(e);
        } else if (event instanceof DeassignAllEvent e) {
            handleDeassignAllEvent(e);
        } else if (event instanceof DeassignAllFromAndDeleteEvent e) {
            handleDeassignAllFromAndDeleteEvent(e);
        }
    }

    public void handleDeassignAllFromAndDeleteEvent(DeassignAllFromAndDeleteEvent e) {}
    public void handleDeassignAllEvent(DeassignAllEvent e) {}
    public void handleAssignAllEvent(AssignAllEvent e) {}
    public void handleAddConstantEvent(AddConstantEvent e) throws PMException {}
    public void handleAddFunctionEvent(AddFunctionEvent e) throws PMException {}
    public void handleAssignEvent(AssignEvent e) throws PMException {}
    public void handleAssignToEvent(AssignToEvent e) throws PMException {}
    public void handleAssociateEvent(AssociateEvent e) throws PMException {}
    public void handleBeginTxEvent(BeginTxEvent e) throws PMException {}
    public void handleCommitTxEvent(CommitTxEvent e) throws PMException {}
    public void handleCreateNodeEvent(CreateNodeEvent e) throws PMException {}
    public void handleCreateObjectAttributeEvent(CreateObjectAttributeEvent e) throws PMException {}
    public void handleCreateObjectEvent(CreateObjectEvent e) throws PMException {}
    public void handleCreateObligationEvent(CreateObligationEvent e) throws PMException {}
    public void handleCreatePolicyClassEvent(CreatePolicyClassEvent e) throws PMException {}
    public void handleCreateProhibitionEvent(CreateProhibitionEvent e) throws PMException {}
    public void handleCreateUserAttributeEvent(CreateUserAttributeEvent e) throws PMException {}
    public void handleCreateUserEvent(CreateUserEvent e) throws PMException {}
    public void handleDeassignEvent(DeassignEvent e) throws PMException {}
    public void handleDeassignFromEvent(DeassignFromEvent e) throws PMException {}
    public void handleDeleteNodeEvent(DeleteNodeEvent e) throws PMException {}
    public void handleDeleteObligationEvent(DeleteObligationEvent e) throws PMException {}
    public void handleDeleteProhibitionEvent(DeleteProhibitionEvent e) throws PMException {}
    public void handleDissociateEvent(DissociateEvent e) throws PMException {}
    public void handlePolicySyncEvent(PolicySynchronizationEvent e) throws PMException {}
    public void handleRemoveConstantEvent(RemoveConstantEvent e) throws PMException {}
    public void handleRemoveFunctionEvent(RemoveFunctionEvent e) throws PMException {}
    public void handleRollbackTxEvent(RollbackTxEvent e) throws PMException {}
    public void handleSetNodePropertiesEvent(SetNodePropertiesEvent e) throws PMException {}
    public void handleSetResourceAccessRightsEvent(SetResourceAccessRightsEvent e) throws PMException {}
    public void handleUpdateObligationEvent(UpdateObligationEvent e) throws PMException {}
    public void handleUpdateProhibitionEvent(UpdateProhibitionEvent e) throws PMException {}
}
