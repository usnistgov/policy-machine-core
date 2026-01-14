package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import org.junit.jupiter.api.Test;

class RoutineDefinitionStatementTest {

    @Test
    void testSuccess() throws PMException {
        String pml = """
                routine routine1(string a) {
                    create PC a
                    create OA "oa1" in [a]
                }""";
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), pml);
        Routine<?> routine1 = pap.query().routines().getAdminRoutine("routine1");
        assertEquals(pml, routine1.toString());
    }

}