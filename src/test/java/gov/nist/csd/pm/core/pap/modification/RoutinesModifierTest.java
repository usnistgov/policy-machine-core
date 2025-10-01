package gov.nist.csd.pm.core.pap.modification;

import gov.nist.csd.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.OperationExistsException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.RoutineDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.RoutineExistsException;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.PAPTestInitializer;
import gov.nist.csd.pm.core.util.SamplePolicy;
import java.util.Map;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static gov.nist.csd.pm.core.pap.PAPTest.ARG_A;
import static gov.nist.csd.pm.core.pap.PAPTest.ARG_B;
import static org.junit.jupiter.api.Assertions.*;

public abstract class RoutinesModifierTest extends PAPTestInitializer {



    static Routine<Void, Args> routine1 = new Routine<>(
            "routine1",
            List.of(ARG_A, ARG_B)
    ) {
        @Override
        public Void execute(PAP pap, Args args) throws PMException {
            return null;
        }

        @Override
        protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
            return null;
        }
    };

    static Routine<Void, Args> routine2 = new Routine<>(
            "routine2",
            List.of(ARG_A, ARG_B)
    ) {
        @Override
        public Void execute(PAP pap, Args args) throws PMException {
            return null;
        }

        @Override
        protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
            return null;
        }
    };

    static Routine<Void, Args> routine3 = new Routine<>(
            "routine3",
            List.of(ARG_A, ARG_B)
    ) {
        @Override
        public Void execute(PAP pap, Args args) throws PMException {
            return null;
        }

        @Override
        protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
            return null;
        }
    };

    static Routine<Void, Args> routine4 = new Routine<>(
            "routine4",
            List.of(ARG_A, ARG_B)
    ) {
        @Override
        public Void execute(PAP pap, Args args) throws PMException {
            return null;
        }

        @Override
        protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
            return null;
        }
    };

    @Nested
    class CreateAdminRoutine {

        @Test
        void testSuccess() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.modify().routines().createAdminRoutine(routine1);

            assertTrue(pap.query().routines().getAdminRoutineNames().contains("routine1"));
        }

        @Test
        void testRoutineExists() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.modify().routines().createAdminRoutine(routine1);

            assertThrows(RoutineExistsException.class, () -> {
                pap.modify().routines().createAdminRoutine(routine1);
            });

            pap.modify().operations().deleteAdminOperation(routine1.getName());
            pap.plugins().registerRoutine(routine1);
            assertThrows(RoutineExistsException.class,
                () -> pap.modify().routines().createAdminRoutine(routine1));
        }

        @Test
        void testTx() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.runTx(tx -> {
                tx.modify().routines().createAdminRoutine(routine1);
                tx.modify().routines().createAdminRoutine(routine2);
            });

            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                tx.modify().routines().createAdminRoutine(routine3);
                tx.modify().routines().createAdminRoutine(routine4);

                throw new PMException("");
            }));

            assertTrue(pap.query().routines().getAdminRoutineNames().containsAll(List.of("routine1", "routine2")));
            assertFalse(pap.query().routines().getAdminRoutineNames().containsAll(List.of("routine3", "routine4")));
        }
    }

    @Nested
    class DeleteAdminRoutine {

        @Test
        void testSuccess() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);
            pap.modify().routines().createAdminRoutine(routine1);

            pap.modify().routines().deleteAdminRoutine("routine1");
            pap.modify().routines().deleteAdminRoutine("routine1");
            assertFalse(pap.query().routines().getAdminRoutineNames().contains("routine1"));

            pap.plugins().registerRoutine(routine1);
            assertTrue(pap.query().routines().getAdminRoutineNames().contains(routine1.getName()));
            pap.modify().routines().deleteAdminRoutine(routine1.getName());
            assertThrows(RoutineDoesNotExistException.class, () -> pap.query().routines().getAdminRoutine(routine1.getName()));
        }

        @Test
        void testTx() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.runTx(tx -> {
                tx.modify().routines().createAdminRoutine(routine1);
                tx.modify().routines().createAdminRoutine(routine2);
            });

            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                tx.modify().routines().deleteAdminRoutine("routine1");
                tx.modify().routines().deleteAdminRoutine("routine2");

                throw new PMException("");
            }));

            assertTrue(pap.query().routines().getAdminRoutineNames().containsAll(List.of("routine1", "routine2")));
        }
    }

}