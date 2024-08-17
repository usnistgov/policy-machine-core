package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PAPTestInitializer;
import gov.nist.csd.pm.pap.exception.RoutineDoesNotExistException;
import gov.nist.csd.pm.pap.routine.Routine;
import gov.nist.csd.pm.util.SamplePolicy;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public abstract class RoutinesQuerierTest extends PAPTestInitializer {

    @Test
    void testGetAdminRoutineNames() throws PMException, IOException {
        SamplePolicy.loadSamplePolicyFromPML(pap);

        pap.modify().routines().createAdminRoutine(new Routine<Object>("r1", List.of()) {
            @Override
            public Object execute(PAP pap, Map<String, Object> operands) throws PMException {
                return null;
            }
        });

        pap.modify().routines().createAdminRoutine(new Routine<Object>("r2", List.of()) {
            @Override
            public Object execute(PAP pap, Map<String, Object> operands) throws PMException {
                return null;
            }
        });

        Collection<String> adminRoutineNames = pap.query().routines().getAdminRoutineNames();
        assertEquals(new HashSet<>(adminRoutineNames), Set.of("r1", "r2", "deleteAllProjects"));
    }

    @Nested
    class GetAdminRoutine {

        @Test
        void testSuccess() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            Routine<Object> routine = new Routine<>("r1", List.of()) {
                @Override
                public Object execute(PAP pap, Map<String, Object> operands) throws PMException {
                    return null;
                }
            };

            pap.modify().routines().createAdminRoutine(routine);

            Routine<?> actual = pap.query().routines().getAdminRoutine(routine.getName());
            assertEquals(routine, actual);
        }

        @Test
        void testRoutineDoesNotExist() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            assertThrows(RoutineDoesNotExistException.class, () -> pap.query().routines().getAdminRoutine("r1"));
        }

    }

}