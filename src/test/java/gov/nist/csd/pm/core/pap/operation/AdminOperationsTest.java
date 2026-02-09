package gov.nist.csd.pm.core.pap.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.AdminOperationDoesNotExistException;
import gov.nist.csd.pm.core.pap.operation.graph.AssignOp;
import gov.nist.csd.pm.core.pap.operation.graph.AssociateOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreateObjectAttributeOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreateObjectOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreatePolicyClassOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreateUserAttributeOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreateUserOp;
import gov.nist.csd.pm.core.pap.operation.graph.DeassignOp;
import gov.nist.csd.pm.core.pap.operation.graph.DeleteNodeOp;
import gov.nist.csd.pm.core.pap.operation.graph.DissociateOp;
import gov.nist.csd.pm.core.pap.operation.graph.SetNodePropertiesOp;
import gov.nist.csd.pm.core.pap.operation.obligation.CreateObligationOp;
import gov.nist.csd.pm.core.pap.operation.obligation.DeleteObligationOp;
import gov.nist.csd.pm.core.pap.operation.operation.CreateOperationOp;
import gov.nist.csd.pm.core.pap.operation.operation.DeleteOperationOp;
import gov.nist.csd.pm.core.pap.operation.operation.SetResourceAccessRights;
import gov.nist.csd.pm.core.pap.operation.prohibition.CreateNodeProhibitionOp;
import gov.nist.csd.pm.core.pap.operation.prohibition.CreateProcessProhibitionOp;
import gov.nist.csd.pm.core.pap.operation.prohibition.DeleteProhibitionOp;
import org.junit.jupiter.api.Test;

class AdminOperationsTest {

    @Test
    void testRegistryContains18Operations() {
        assertEquals(19, AdminOperations.ADMIN_OPERATIONS.size());
    }

    @Test
    void testIsAdminOperationReturnsTrueForAllRegisteredOps() {
        String[] expectedNames = {
                "assign", "associate", "create_object_attribute", "create_object",
                "create_policy_class", "create_user_attribute", "create_user",
                "deassign", "delete_node", "dissociate", "set_node_properties",
                "create_obligation", "delete_obligation",
                "set_resource_access_rights", "create_operation", "delete_operation",
                "create_node_prohibition", "create_process_prohibition", "delete_prohibition"
        };

        for (String name : expectedNames) {
            assertTrue(AdminOperations.isAdminOperation(name),
                    "Expected isAdminOperation to return true for: " + name);
        }
    }

    @Test
    void testIsAdminOperationReturnsFalseForUnknown() {
        assertFalse(AdminOperations.isAdminOperation("nonexistent"));
        assertFalse(AdminOperations.isAdminOperation(""));
    }

    @Test
    void testGetReturnsCorrectClassTypes() throws AdminOperationDoesNotExistException {
        assertInstanceOf(AssignOp.class, AdminOperations.get("assign"));
        assertInstanceOf(DeassignOp.class, AdminOperations.get("deassign"));
        assertInstanceOf(AssociateOp.class, AdminOperations.get("associate"));
        assertInstanceOf(DissociateOp.class, AdminOperations.get("dissociate"));
        assertInstanceOf(CreateObjectAttributeOp.class, AdminOperations.get("create_object_attribute"));
        assertInstanceOf(CreateObjectOp.class, AdminOperations.get("create_object"));
        assertInstanceOf(CreateUserAttributeOp.class, AdminOperations.get("create_user_attribute"));
        assertInstanceOf(CreateUserOp.class, AdminOperations.get("create_user"));
        assertInstanceOf(CreatePolicyClassOp.class, AdminOperations.get("create_policy_class"));
        assertInstanceOf(DeleteNodeOp.class, AdminOperations.get("delete_node"));
        assertInstanceOf(SetNodePropertiesOp.class, AdminOperations.get("set_node_properties"));
        assertInstanceOf(CreateObligationOp.class, AdminOperations.get("create_obligation"));
        assertInstanceOf(DeleteObligationOp.class, AdminOperations.get("delete_obligation"));
        assertInstanceOf(CreateNodeProhibitionOp.class, AdminOperations.get("create_node_prohibition"));
        assertInstanceOf(CreateProcessProhibitionOp.class, AdminOperations.get("create_process_prohibition"));
        assertInstanceOf(DeleteProhibitionOp.class, AdminOperations.get("delete_prohibition"));
        assertInstanceOf(CreateOperationOp.class, AdminOperations.get("create_operation"));
        assertInstanceOf(DeleteOperationOp.class, AdminOperations.get("delete_operation"));
        assertInstanceOf(SetResourceAccessRights.class, AdminOperations.get("set_resource_access_rights"));
    }

    @Test
    void testGetThrowsForNonexistentOperation() {
        assertThrows(AdminOperationDoesNotExistException.class,
                () -> AdminOperations.get("nonexistent"));
    }
}
