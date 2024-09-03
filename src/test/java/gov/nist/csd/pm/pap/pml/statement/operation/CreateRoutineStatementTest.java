package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.routine.Routine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateRoutineStatementTest {

    @Test
    void testOperationSignatureDoesNotThrowExceptionButIsIgnored() throws PMException {
        String pml = "routine routine1(nodeop string a) {\n" +
                "                    check \"assign\" on a\n" +
                "                } {\n" +
                "                \n" +
                "                }";
        MemoryPAP pap = new MemoryPAP();
        assertDoesNotThrow(() -> pap.executePML(new UserContext("u1"), pml));
    }

    @Test
    void testSuccess() throws PMException {
        String pml = "routine routine1(string a) {\n" +
                "    create PC a\n" +
                "    create OA \"oa1\" in [a]\n" +
                "}";
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), pml);
        Routine<?> routine1 = pap.query().routines().getAdminRoutine("routine1");
        assertEquals(pml, routine1.toString());
    }

}