package gov.nist.csd.pm.pap.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.exception.RoutineExistsException;
import gov.nist.csd.pm.pap.executable.routine.Routine;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PAPTestInitializer;
import gov.nist.csd.pm.util.SamplePolicy;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public abstract class RoutinesModifierTest extends PAPTestInitializer {

    static Routine routine1 = new Routine<Void>(
            "routine1",
            List.of("a", "b")
    ) {
        @Override
        public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
            return null;
        }
    };

    static Routine routine2 = new Routine<Void>(
            "routine2",
            List.of("a", "b")
    ) {
        @Override
        public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
            return null;
        }
    };

    static Routine routine3 = new Routine<Void>(
            "routine3",
            List.of("a", "b")
    ) {
        @Override
        public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
            return null;
        }
    };

    static Routine routine4 = new Routine<Void>(
            "routine4",
            List.of("a", "b")
    ) {
        @Override
        public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
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