package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.common.routine.Routine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateRoutineStatementTest {

    @Test
    void testOperationSignatureDoesNotThrowExceptionButIsIgnored() throws PMException {
        String pml = """
                routine routine1(nodeop string a) {
                    check "assign" on a
                } {
                
                }
                """;
        MemoryPAP pap = new MemoryPAP();
        assertDoesNotThrow(() -> pap.executePML(new UserContext("u1"), pml));
    }

    @Test
    void testSuccess() throws PMException {
        String pml = """
                routine routine1(string a) {
                    create PC a
                    create OA "oa1" in [a]
                }""";
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), pml);
        Routine<?> routine1 = pap.query().routines().getAdminRoutine("routine1");
        assertEquals(pml, routine1.toString());
    }

}