package gov.nist.csd.pm.pap.modification;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PAPTestInitializer;
import gov.nist.csd.pm.pap.exception.AdminAccessRightExistsException;
import gov.nist.csd.pm.pap.exception.OperationExistsException;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.op.graph.AssignOp;
import gov.nist.csd.pm.pap.query.UserContext;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.CREATE_POLICY_CLASS;
import static org.junit.jupiter.api.Assertions.*;

public abstract class OperationsModifierTest extends PAPTestInitializer {

    Operation<?> testOp = new Operation<>("test", List.of()) {
        @Override
        public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {

        }

        @Override
        public Object execute(PAP pap, Map<String, Object> operands) throws PMException {
            return null;
        }
    };

    @Nested
    class SetResourceOperations {
        @Test
        void testAdminAccessRightExistsException() {
            assertThrows(AdminAccessRightExistsException.class, () ->
                    pap.modify().operations().setResourceOperations(new AccessRightSet(CREATE_POLICY_CLASS)));
        }

        @Test
        void testSuccess() throws PMException {
            AccessRightSet arset = new AccessRightSet("read", "write");
            pap.modify().operations().setResourceOperations(arset);
            assertEquals(arset, pap.query().operations().getResourceOperations());
        }
    }

    @Nested
    class CreateAdminOperation {

        @Test
        void testSuccess() throws PMException {
            pap.modify().operations().createAdminOperation(testOp);

            assertDoesNotThrow(() -> pap.query().operations().getAdminOperation("assign"));
        }

        @Test
        void testOperationExists() throws PMException {
            pap.modify().operations().createAdminOperation(testOp);

            assertThrows(OperationExistsException.class,
                    () -> pap.modify().operations().createAdminOperation(new AssignOp()));
            assertThrows(OperationExistsException.class,
                    () -> pap.modify().operations().createAdminOperation(testOp));

        }

    }

    @Nested
    class DeleteAdminOperation {

        @Test
        void testSuccess() throws PMException {
            pap.modify().operations().createAdminOperation(testOp);
            pap.modify().operations().deleteAdminOperation("test");
            assertDoesNotThrow(() -> pap.modify().operations().deleteAdminOperation("assign"));
        }

        @Test
        void testCannotDeleteBuiltinOperation() {
            assertDoesNotThrow(() -> pap.modify().operations().deleteAdminOperation("assign"));
            assertDoesNotThrow(() -> pap.query().operations().getAdminOperation("assign"));
        }
    }

}