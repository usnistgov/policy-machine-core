package gov.nist.csd.pm.core.pap.modification;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_POLICY_CLASS;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.AdminAccessRightExistsException;
import gov.nist.csd.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.OperationExistsException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.PAPTestInitializer;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.AdminOperation;
import gov.nist.csd.pm.core.pap.function.op.graph.AssignOp;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public abstract class OperationsModifierTest extends PAPTestInitializer {

    static AdminOperation<?> testOp = new AdminOperation<>("test", List.of()) {
        @Override
        public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {

        }

        @Override
        public Object execute(PAP pap, Args args) throws PMException {
            return null;
        }

    };

    @Nested
    class SetResourceOperations {
        @Test
        void testAdminAccessRightExistsException() {
            assertThrows(AdminAccessRightExistsException.class, () ->
                    pap.modify().operations().setResourceAccessRights(new AccessRightSet(CREATE_POLICY_CLASS)));
        }

        @Test
        void testSuccess() throws PMException {
            AccessRightSet arset = new AccessRightSet("read", "write");
            pap.modify().operations().setResourceAccessRights(arset);
            assertEquals(arset, pap.query().operations().getResourceAccessRights());
        }
    }

    @Nested
    class CreateAdminOperation {

        @Test
        void testSuccess() throws PMException {
            pap.modify().operations().createAdminOperation(testOp);

            assertThrows(OperationDoesNotExistException.class, () -> pap.query().operations().getAdminOperation("assign"));
        }

        @Test
        void testOperationExists() throws PMException {
            pap.modify().operations().createAdminOperation(testOp);

            assertThrows(OperationExistsException.class,
                    () -> pap.modify().operations().createAdminOperation(new AssignOp()));
            assertThrows(OperationExistsException.class,
                    () -> pap.modify().operations().createAdminOperation(testOp));

            pap.modify().operations().deleteAdminOperation(testOp.getName());
            pap.plugins().registerOperation(testOp);
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

            pap.plugins().registerOperation(testOp);
            assertTrue(pap.query().operations().getAdminOperationNames().contains(testOp.getName()));
            pap.modify().operations().deleteAdminOperation(testOp.getName());
            assertThrows(OperationDoesNotExistException.class, () -> pap.query().operations().getAdminOperation(testOp.getName()));
        }

        @Test
        void testCannotDeleteBuiltinOperation() {
            assertDoesNotThrow(() -> pap.modify().operations().deleteAdminOperation("assign"));
            assertThrows(OperationDoesNotExistException.class, () -> pap.query().operations().getAdminOperation("assign"));
        }
    }

}