package gov.nist.csd.pm.core.pap.modification;

import static gov.nist.csd.pm.core.pap.PAPTest.ARG_A;
import static gov.nist.csd.pm.core.pap.PAPTest.ARG_B;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_POLICY_CLASS;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.AdminAccessRightExistsException;
import gov.nist.csd.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.OperationExistsException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.accessrights.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.PAPTestInitializer;
import gov.nist.csd.pm.core.pap.modification.OperationsModifier.CannotDeletePluginOperationException;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.Routine;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.graph.AssignOp;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.SamplePolicy;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public abstract class OperationsModifierTest extends PAPTestInitializer {

    static AdminOperation<?> testOp = new AdminOperation<>("test", ANY_TYPE, List.of()) {
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
            pap.modify().operations().createOperation(testOp);

            assertThrows(OperationDoesNotExistException.class, () -> pap.query().operations().getOperation("assign"));
        }

        @Test
        void testOperationExists() throws PMException {
            pap.modify().operations().createOperation(testOp);

            assertThrows(OperationExistsException.class,
                    () -> pap.modify().operations().createOperation(new AssignOp()));
            assertThrows(OperationExistsException.class,
                    () -> pap.modify().operations().createOperation(testOp));

            pap.modify().operations().deleteOperation(testOp.getName());
            pap.plugins().addOperation(testOp);
            assertThrows(OperationExistsException.class,
                () -> pap.modify().operations().createOperation(testOp));
        }
    }

    static Routine<Void> routine1 = new Routine<>(
        "routine1",
        VOID_TYPE,
        List.of(ARG_A, ARG_B)
    ) {
        @Override
        public Void execute(PAP pap, Args args) throws PMException {
            return null;
        }
    };

    static Routine<Void> routine2 = new Routine<>(
        "routine2",
        VOID_TYPE,
        List.of(ARG_A, ARG_B)
    ) {
        @Override
        public Void execute(PAP pap, Args args) throws PMException {
            return null;
        }
    };

    static Routine<Void> routine3 = new Routine<>(
        "routine3",
        VOID_TYPE,
        List.of(ARG_A, ARG_B)
    ) {
        @Override
        public Void execute(PAP pap, Args args) throws PMException {
            return null;
        }
    };

    static Routine<Void> routine4 = new Routine<>(
        "routine4",
        VOID_TYPE,
        List.of(ARG_A, ARG_B)
    ) {
        @Override
        public Void execute(PAP pap, Args args) throws PMException {
            return null;
        }
    };

    @Nested
    class CreateAdminRoutine {

        @Test
        void testSuccess() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.modify().operations().createOperation(routine1);

            assertTrue(pap.query().operations().getOperations().contains(routine1));
        }

        @Test
        void testRoutineExists() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.modify().operations().createOperation(routine1);

            assertThrows(OperationExistsException.class, () -> {
                pap.modify().operations().createOperation(routine1);
            });

            pap.modify().operations().deleteOperation(routine1.getName());
            pap.plugins().addOperation(routine1);
            assertThrows(OperationExistsException.class,
                () -> pap.modify().operations().createOperation(routine1));
        }

        @Test
        void testTx() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.runTx(tx -> {
                tx.modify().operations().createOperation(routine1);
                tx.modify().operations().createOperation(routine2);
            });

            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                tx.modify().operations().createOperation(routine3);
                tx.modify().operations().createOperation(routine4);

                throw new PMException("");
            }));

            assertTrue(pap.query().operations().getOperations().containsAll(List.of(routine1, routine2)));
            assertFalse(pap.query().operations().getOperations().contains(routine3));
            assertFalse(pap.query().operations().getOperations().contains(routine4));
        }
    }

    @Nested
    class DeleteOperation {

        @Test
        void testSuccess() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);
            pap.modify().operations().createOperation(routine1);

            pap.modify().operations().deleteOperation("routine1");
            assertFalse(pap.query().operations().getOperations().contains(routine1));

            pap.plugins().addOperation(routine1);
            assertTrue(pap.query().operations().getOperations().contains(routine1));
            assertThrows(CannotDeletePluginOperationException.class, () ->
                pap.modify().operations().deleteOperation(routine1.getName()));
            assertDoesNotThrow(() -> pap.query().operations().getOperation(routine1.getName()));
        }

        @Test
        void testTx() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.runTx(tx -> {
                tx.modify().operations().createOperation(routine1);
                tx.modify().operations().createOperation(routine2);
            });

            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                tx.modify().operations().deleteOperation("routine1");
                tx.modify().operations().deleteOperation("routine2");

                throw new PMException("");
            }));

            assertTrue(pap.query().operations().getOperations().containsAll(List.of(routine1, routine2)));
        }

        @Test
        void testCannotDeleteBuiltinOperation() {
            assertDoesNotThrow(() -> pap.modify().operations().deleteOperation("assign"));
            assertThrows(OperationDoesNotExistException.class, () -> pap.query().operations().getOperation("assign"));
        }
    }
}