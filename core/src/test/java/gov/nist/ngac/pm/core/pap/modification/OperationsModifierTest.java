/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.modification;

import static gov.nist.ngac.pm.core.pap.PAPTest.ARG_A;
import static gov.nist.ngac.pm.core.pap.PAPTest.ARG_B;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.AdminAccessRightExistsException;
import gov.nist.ngac.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.OperationExistsException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.PAPTestInitializer;
import gov.nist.ngac.pm.core.pap.modification.OperationsModifier.CannotDeleteProtectedOperationException;
import gov.nist.ngac.pm.core.pap.operation.AdminOperation;
import gov.nist.ngac.pm.core.pap.operation.Routine;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.graph.AssignOp;
import gov.nist.ngac.pm.core.util.SamplePolicy;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public abstract class OperationsModifierTest extends PAPTestInitializer {

    static AdminOperation<?> testOp = new AdminOperation<>("test", ANY_TYPE, List.of(), List.of()) {
        @Override
        public Object execute(PAP pap, UserContext userCtx, Args args) throws PMException {
            return null;
        }

    };

    @Nested
    class SetResourceOperations {
        @Test
        void testAdminAccessRightExistsException() {
            assertThrows(AdminAccessRightExistsException.class, () ->
                    pap.modify().operations().setResourceAccessRights(new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE)));
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
            pap.javaOperations().register(testOp);
            pap.modify().operations().createOperation(testOp);

            assertDoesNotThrow(() -> pap.query().operations().getOperation(testOp.getName()));
        }

        @Test
        void testOperationExists() throws PMException {
            pap.javaOperations().register(testOp);
            pap.modify().operations().createOperation(testOp);

            assertThrows(OperationExistsException.class,
                    () -> pap.modify().operations().createOperation(new AssignOp()));
            assertThrows(OperationExistsException.class,
                    () -> pap.modify().operations().createOperation(testOp));
        }

        @Test
        void testRequiresRegistrationFirst() {
            assertThrows(OperationDoesNotExistException.class,
                () -> pap.modify().operations().createOperation(testOp));
        }
    }

    static Routine<Void> routine1 = new Routine<>(
        "routine1",
        VOID_TYPE,
        List.of(ARG_A, ARG_B)
    ) {
        @Override
        public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
            return null;
        }
    };

    static Routine<Void> routine2 = new Routine<>(
        "routine2",
        VOID_TYPE,
        List.of(ARG_A, ARG_B)
    ) {
        @Override
        public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
            return null;
        }
    };

    static Routine<Void> routine3 = new Routine<>(
        "routine3",
        VOID_TYPE,
        List.of(ARG_A, ARG_B)
    ) {
        @Override
        public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
            return null;
        }
    };

    static Routine<Void> routine4 = new Routine<>(
        "routine4",
        VOID_TYPE,
        List.of(ARG_A, ARG_B)
    ) {
        @Override
        public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
            return null;
        }
    };

    @Nested
    class CreateAdminRoutine {

        @Test
        void testSuccess() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.javaOperations().register(routine1);
            pap.modify().operations().createOperation(routine1);

            assertTrue(pap.query().operations().getOperations().contains(routine1));
        }

        @Test
        void testRoutineExists() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.javaOperations().register(routine1);
            pap.modify().operations().createOperation(routine1);

            assertThrows(OperationExistsException.class, () -> {
                pap.modify().operations().createOperation(routine1);
            });
        }

        @Test
        void testTx() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.javaOperations().register(routine1);
            pap.javaOperations().register(routine2);
            pap.runTx(tx -> {
                tx.modify().operations().createOperation(routine1);
                tx.modify().operations().createOperation(routine2);
            });

            pap.javaOperations().register(routine3);
            pap.javaOperations().register(routine4);
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
            pap.javaOperations().register(routine1);
            pap.modify().operations().createOperation(routine1);

            pap.modify().operations().deleteOperation("routine1");
            assertFalse(pap.query().operations().getOperations().contains(routine1));

            // a deleted Java operation remains registered (delete only toggles store-level
            // persistence/activation) and can be re-created without re-registering
            assertDoesNotThrow(() -> pap.modify().operations().createOperation(routine1));
            assertTrue(pap.query().operations().getOperations().contains(routine1));
        }

        @Test
        void testTx() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.javaOperations().register(routine1);
            pap.javaOperations().register(routine2);
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
            assertThrows(CannotDeleteProtectedOperationException.class,
                () -> pap.modify().operations().deleteOperation("assign"));
            assertDoesNotThrow(() -> pap.query().operations().getOperation("assign"));
        }
    }
}