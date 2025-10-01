package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.RoutineDoesNotExistException;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.PAPTestInitializer;
import gov.nist.csd.pm.core.util.SamplePolicy;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class RoutinesQuerierTest extends PAPTestInitializer {

    static Routine<Object, Args> r1 = new Routine<>("r1", List.of()) {
        @Override
        public Object execute(PAP pap, Args args) throws PMException {
            return null;
        }

        @Override
        protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
            return null;
        }
    };
    static Routine<Object, Args> r2 = new Routine<>("r2", List.of()) {

        @Override
        public Object execute(PAP pap, Args args) throws PMException {
            return null;
        }

        @Override
        protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
            return null;
        }
    };

    @Test
    void testGetAdminRoutineNames() throws PMException, IOException {
        SamplePolicy.loadSamplePolicyFromPML(pap);

        pap.modify().routines().createAdminRoutine(r1);
        pap.modify().routines().createAdminRoutine(r2);

        Collection<String> adminRoutineNames = pap.query().routines().getAdminRoutineNames();
        assertEquals(new HashSet<>(adminRoutineNames), Set.of("r1", "r2", "deleteAllProjects"));

        pap.plugins().registerRoutine(new Routine("r3", List.of()) {
            @Override
            public Object execute(PAP pap, Args args) throws PMException {
                return null;
            }

            @Override
            protected Args prepareArgs(Map argsMap) {
                return null;
            }
        });
        adminRoutineNames = pap.query().routines().getAdminRoutineNames();
        assertEquals(new HashSet<>(adminRoutineNames), Set.of("r1", "r2", "r3", "deleteAllProjects"));
    }

    @Nested
    class GetAdminRoutine {

        @Test
        void testSuccess() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.modify().routines().createAdminRoutine(r1);

            Routine<?, ?> actual = pap.query().routines().getAdminRoutine(r1.getName());
            assertEquals(r1, actual);

            pap.plugins().registerRoutine(r2);
            actual = pap.query().routines().getAdminRoutine(r2.getName());
            assertEquals(r2, actual);
        }

        @Test
        void testRoutineDoesNotExist() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            assertThrows(RoutineDoesNotExistException.class, () -> pap.query().routines().getAdminRoutine("r1"));
        }

    }

}