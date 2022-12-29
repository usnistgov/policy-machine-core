package gov.nist.csd.pm.policy.events;

public class PolicyEventHandler {

    public PolicyEventHandler() {}

    public void handle(PolicyEvent event) {
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
        }
    }

    public void handleAddConstantEvent(AddConstantEvent e) {}
    public void handleAddFunctionEvent(AddFunctionEvent e) {}
    public void handleAssignEvent(AssignEvent e) {}
    public void handleAssignToEvent(AssignToEvent e) {}
    public void handleAssociateEvent(AssociateEvent e) {}
    public void handleBeginTxEvent(BeginTxEvent e) {}
    public void handleCommitTxEvent(CommitTxEvent e) {}
    public void handleCreateNodeEvent(CreateNodeEvent e) {}
    public void handleCreateObjectAttributeEvent(CreateObjectAttributeEvent e) {}
    public void handleCreateObjectEvent(CreateObjectEvent e) {}
    public void handleCreateObligationEvent(CreateObligationEvent e) {}
    public void handleCreatePolicyClassEvent(CreatePolicyClassEvent e) {}
    public void handleCreateProhibitionEvent(CreateProhibitionEvent e) {}
    public void handleCreateUserAttributeEvent(CreateUserAttributeEvent e) {}
    public void handleCreateUserEvent(CreateUserEvent e) {}
    public void handleDeassignEvent(DeassignEvent e) {}
    public void handleDeassignFromEvent(DeassignFromEvent e) {}
    public void handleDeleteNodeEvent(DeleteNodeEvent e) {}
    public void handleDeleteObligationEvent(DeleteObligationEvent e) {}
    public void handleDeleteProhibitionEvent(DeleteProhibitionEvent e) {}
    public void handleDissociateEvent(DissociateEvent e) {}
    public void handlePolicySyncEvent(PolicySynchronizationEvent e) {}
    public void handleRemoveConstantEvent(RemoveConstantEvent e) {}
    public void handleRemoveFunctionEvent(RemoveFunctionEvent e) {}
    public void handleRollbackTxEvent(RollbackTxEvent e) {}
    public void handleSetNodePropertiesEvent(SetNodePropertiesEvent e) {}
    public void handleSetResourceAccessRightsEvent(SetResourceAccessRightsEvent e) {}
    public void handleUpdateObligationEvent(UpdateObligationEvent e) {}
    public void handleUpdateProhibitionEvent(UpdateProhibitionEvent e) {}

    interface AddConstantExecutor {
        void exec(AddConstantEvent event);
    }

    interface AddFunctionExecutor {
        void exec(AddFunctionEvent event);
    }

    interface AssignExecutor {
        void exec(AssignEvent event);
    }

    interface AssignToExecutor {
        void exec(AssignToEvent event);
    }

    interface AssociateExecutor {
        void exec(AssociateEvent event);
    }

    interface BeginTxExecutor {
        void exec(BeginTxEvent event);
    }

    interface CommitTxExecutor {
        void exec(CommitTxEvent event);
    }

    interface CreateNodeExecutor {
        void exec(CreateNodeEvent event);
    }

    interface CreateObjectAttributeExecutor {
        void exec(CreateObjectAttributeEvent event);
    }

    interface CreateObjectExecutor {
        void exec(CreateObjectEvent event);
    }

    interface CreateObligationExecutor {
        void exec(CreateObligationEvent event);
    }

    interface CreatePolicyClassExecutor {
        void exec(CreatePolicyClassEvent event);
    }

    interface CreateProhibitionExecutor {
        void exec(CreateProhibitionEvent event);
    }

    interface CreateUserAttributeExecutor {
        void exec(CreateUserAttributeEvent event);
    }

    interface CreateUserExecutor {
        void exec(CreateUserEvent event);
    }

    interface DeassignExecutor {
        void exec(DeassignEvent event);
    }

    interface DeassignFromExecutor {
        void exec(DeassignFromEvent event);
    }

    interface DeleteNodeExecutor {
        void exec(DeleteNodeEvent event);
    }

    interface DeleteObligationExecutor {
        void exec(DeleteObligationEvent event);
    }

    interface DeleteProhibitionExecutor {
        void exec(DeleteProhibitionEvent event);
    }

    interface DissociateExecutor {
        void exec(DissociateEvent event);
    }

    interface PolicySyncExecutor {
        void exec(PolicySynchronizationEvent event);
    }

    interface RemoveConstantExecutor {
        void exec(RemoveConstantEvent event);
    }

    interface RemoveFunctionExecutor {
        void exec(RemoveFunctionEvent event);
    }

    interface RollbackTxExecutor {
        void exec(RollbackTxEvent event);
    }

    interface SetNodePropertiesExecutor {
        void exec(SetNodePropertiesEvent event);
    }

    interface SetResourceAccessRightsExecutor {
        void exec(SetResourceAccessRightsEvent event);
    }

    interface UpdateObligationExecutor {
        void exec(UpdateObligationEvent event);
    }

    interface UpdateProhibitionExecutor {
        void exec(UpdateProhibitionEvent event);
    }
}
