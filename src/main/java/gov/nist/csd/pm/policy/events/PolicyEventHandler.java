package gov.nist.csd.pm.policy.events;

public class PolicyEventHandler {

    private final PolicyEvent event;

    public PolicyEventHandler(PolicyEvent event) {
        this.event = event;
    }

    public PolicyEventHandler onAddConstantEvent(AddConstantExecutor executor) {
        if (event instanceof AddConstantEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onAddFunctionEvent(AddFunctionExecutor executor) {
        if (event instanceof AddFunctionEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onAssignEvent(AssignExecutor executor) {
        if (event instanceof AssignEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onAssignToEvent(AssignToExecutor executor) {
        if (event instanceof AssignToEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onAssociateEvent(AssociateExecutor executor) {
        if (event instanceof AssociateEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onBeginTxEvent(BeginTxExecutor executor) {
        if (event instanceof BeginTxEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onCommitTxEvent(CommitTxExecutor executor) {
        if (event instanceof CommitTxEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onCreateNodeEvent(CreateNodeExecutor executor) {
        if (event instanceof CreateNodeEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onCreateObjectAttributeEvent(CreateObjectAttributeExecutor executor) {
        if (event instanceof CreateObjectAttributeEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onCreateObjectEvent(CreateObjectExecutor executor) {
        if (event instanceof CreateObjectEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onCreateObligationEvent(CreateObligationExecutor executor) {
        if (event instanceof CreateObligationEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onCreatePolicyClassEvent(CreatePolicyClassExecutor executor) {
        if (event instanceof CreatePolicyClassEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onCreateProhibitionEvent(CreateProhibitionExecutor executor) {
        if (event instanceof CreateProhibitionEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onCreateUserAttributeEvent(CreateUserAttributeExecutor executor) {
        if (event instanceof CreateUserAttributeEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onCreateUserEvent(CreateUserExecutor executor) {
        if (event instanceof CreateUserEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onDeassignEvent(DeassignExecutor executor) {
        if (event instanceof DeassignEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onDeassignFromEvent(DeassignFromExecutor executor) {
        if (event instanceof DeassignFromEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onDeleteNodeEvent(DeleteNodeExecutor executor) {
        if (event instanceof DeleteNodeEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onDeleteObligationEvent(DeleteObligationExecutor executor) {
        if (event instanceof DeleteObligationEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onDeleteProhibitionEvent(DeleteProhibitionExecutor executor) {
        if (event instanceof DeleteProhibitionEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onDissociateEvent(DissociateExecutor executor) {
        if (event instanceof DissociateEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onPolicySyncEvent(PolicySyncExecutor executor) {
        if (event instanceof PolicySynchronizationEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onRemoveConstantEvent(RemoveConstantExecutor executor) {
        if (event instanceof RemoveConstantEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onRemoveFunctionEvent(RemoveFunctionExecutor executor) {
        if (event instanceof RemoveFunctionEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onRollbackTxEvent(RollbackTxExecutor executor) {
        if (event instanceof RollbackTxEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onSetNodePropertiesEvent(SetNodePropertiesExecutor executor) {
        if (event instanceof SetNodePropertiesEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onSetResourceAccessRightsEvent(SetResourceAccessRightsExecutor executor) {
        if (event instanceof SetResourceAccessRightsEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onUpdateObligationEvent(UpdateObligationExecutor executor) {
        if (event instanceof UpdateObligationEvent e) {
            executor.exec(e);
        }

        return this;
    }

    public PolicyEventHandler onUpdateProhibitionEvent(UpdateProhibitionExecutor executor) {
        if (event instanceof UpdateProhibitionEvent e) {
            executor.exec(e);
        }

        return this;
    }

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
