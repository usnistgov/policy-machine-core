package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.events.handler.PolicyEventHandler;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PolicyEventHandlerTest {

    @Test
    void testPolicyEventHandler() throws PMException {
        TestPolicyEventHandler handler = new TestPolicyEventHandler();

        handler.handle(new AddConstantEvent("", new Value()));
        handler.handle(new AddFunctionEvent(new FunctionDefinitionStatement(null, null, null, List.of())));
        handler.handle(new AssignEvent("", ""));
        handler.handle(new AssignToEvent("", ""));
        handler.handle(new AssociateEvent("", "", new AccessRightSet()));
        handler.handle(new BeginTxEvent());
        handler.handle(new CommitTxEvent());
        handler.handle(new CreateObjectAttributeEvent("", null, null, null));
        handler.handle(new CreateObjectEvent(null, null, null, null));
        handler.handle(new CreateObligationEvent(null, null, null));
        handler.handle(new CreatePolicyClassEvent(null, null));
        handler.handle(new CreateProhibitionEvent(null, null, null, false, null));
        handler.handle(new CreateUserAttributeEvent(null, null, null));
        handler.handle(new CreateUserEvent(null, null, null));
        handler.handle(new DeassignEvent("", ""));
        handler.handle(new DeassignFromEvent("", ""));
        handler.handle(new DeleteNodeEvent(""));
        handler.handle(new DeleteObligationEvent(new Obligation(new UserContext(""), "")));
        handler.handle(new DeleteProhibitionEvent(null));
        handler.handle(new DissociateEvent("", ""));
        handler.handle(new PolicySynchronizationEvent(null));
        handler.handle(new RemoveConstantEvent(null));
        handler.handle(new RemoveFunctionEvent(null));
        handler.handle(new RollbackTxEvent(null));
        handler.handle(new SetNodePropertiesEvent(null, null));
        handler.handle(new SetResourceAccessRightsEvent(null));
        handler.handle(new UpdateObligationEvent(null, null, null));
        handler.handle(new UpdateProhibitionEvent(null, null, null, false, null));

        assertEquals(28, handler.count);
    }

    class TestPolicyEventHandler extends PolicyEventHandler {
        int count = 0;
        TestPolicyEventHandler() {}
        @Override
        public void handleAddConstantEvent(AddConstantEvent e) {
            count++;
        }

        @Override
        public void handleAddFunctionEvent(AddFunctionEvent e) {
            count++;
        }

        @Override
        public void handleAssignEvent(AssignEvent e) {
            count++;
        }

        @Override
        public void handleAssignToEvent(AssignToEvent e) {
            count++;
        }

        @Override
        public void handleAssociateEvent(AssociateEvent e) {
            count++;
        }

        @Override
        public void handleBeginTxEvent(BeginTxEvent e) {
            count++;
        }

        @Override
        public void handleCommitTxEvent(CommitTxEvent e) {
            count++;
        }

        @Override
        public void handleCreateNodeEvent(CreateNodeEvent e) {
            count++;
        }

        @Override
        public void handleCreateObjectAttributeEvent(CreateObjectAttributeEvent e) {
            count++;
        }

        @Override
        public void handleCreateObjectEvent(CreateObjectEvent e) {
            count++;
        }

        @Override
        public void handleCreateObligationEvent(CreateObligationEvent e) {
            count++;
        }

        @Override
        public void handleCreatePolicyClassEvent(CreatePolicyClassEvent e) {
            count++;
        }

        @Override
        public void handleCreateProhibitionEvent(CreateProhibitionEvent e) {
            count++;
        }

        @Override
        public void handleCreateUserAttributeEvent(CreateUserAttributeEvent e) {
            count++;
        }

        @Override
        public void handleCreateUserEvent(CreateUserEvent e) {
            count++;
        }

        @Override
        public void handleDeassignEvent(DeassignEvent e) {
            count++;
        }

        @Override
        public void handleDeassignFromEvent(DeassignFromEvent e) {
            count++;
        }

        @Override
        public void handleDeleteNodeEvent(DeleteNodeEvent e) {
            count++;
        }

        @Override
        public void handleDeleteObligationEvent(DeleteObligationEvent e) {
            count++;
        }

        @Override
        public void handleDeleteProhibitionEvent(DeleteProhibitionEvent e) {
            count++;
        }

        @Override
        public void handleDissociateEvent(DissociateEvent e) {
            count++;
        }

        @Override
        public void handlePolicySyncEvent(PolicySynchronizationEvent e) {
            count++;
        }

        @Override
        public void handleRemoveConstantEvent(RemoveConstantEvent e) {
            count++;
        }

        @Override
        public void handleRemoveFunctionEvent(RemoveFunctionEvent e) {
            count++;
        }

        @Override
        public void handleRollbackTxEvent(RollbackTxEvent e) {
            count++;
        }

        @Override
        public void handleSetNodePropertiesEvent(SetNodePropertiesEvent e) {
            count++;
        }

        @Override
        public void handleSetResourceAccessRightsEvent(SetResourceAccessRightsEvent e) {
            count++;
        }

        @Override
        public void handleUpdateObligationEvent(UpdateObligationEvent e) {
            count++;
        }

        @Override
        public void handleUpdateProhibitionEvent(UpdateProhibitionEvent e) {
            count++;
        }
    }

}