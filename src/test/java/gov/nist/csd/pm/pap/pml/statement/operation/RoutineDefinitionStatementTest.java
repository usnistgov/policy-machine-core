package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.routine.Routine;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RoutineDefinitionStatementTest {

    @Test
    void testOperationSignatureDoesNotThrowExceptionButIsIgnored() throws PMException {
        String pml = """
                routine routine1(@node string a) {
                    check "assign" on a
                } {
                
                }
                """;
        MemoryPAP pap = new TestPAP();
        assertDoesNotThrow(() -> pap.executePML(new TestUserContext("u1"), pml));
    }

    @Test
    void testSuccess() throws PMException {
        String pml = """
                routine routine1(string a) {
                    create PC a
                    create OA "oa1" in [a]
                }""";
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), pml);
        Routine<?, ?> routine1 = pap.query().routines().getAdminRoutine("routine1");
        assertEquals(pml, routine1.toString());
    }

}