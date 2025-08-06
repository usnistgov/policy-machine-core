package gov.nist.csd.pm.core.pap.pml.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import org.junit.jupiter.api.Test;

public class PMLFunctionTest {

    @Test
    void testFunctionsPersistedAsOperations() throws PMException {
        String pml = """
            function test() string {
                return "test"
            }
            """;
        MemoryPAP memoryPAP = new MemoryPAP();
        memoryPAP.executePML(new UserContext(0), pml);

        assertTrue(memoryPAP.query().operations().getAdminOperationNames().contains("test"));

        pml = """
            create PC test()
            """;
        memoryPAP.executePML(new UserContext(0), pml);

        assertTrue(memoryPAP.query().graph().nodeExists("test"));
    }

    @Test
    void testFunctionsCannotCallOperationsOrRoutines() throws PMException {
        String pml = """
            operation op1(string s) {
                create pc s
            }
            
            function test() {
               op1("test")
            }
            """;
        MemoryPAP memoryPAP = new MemoryPAP();
        PMLCompilationException e = assertThrows(PMLCompilationException.class,
            () -> memoryPAP.executePML(new UserContext(0), pml));
        assertEquals("only PML basic functions (defined as 'function') allowed in basic statement block",
            e.getErrors().getFirst().errorMessage());

        String pml2 = """
            routine rou1(string s) {
                create pc s
            }
            
            function test() {
               rou1("test")
            }
            """;
        MemoryPAP memoryPAP2 = new MemoryPAP();
        e = assertThrows(PMLCompilationException.class,
            () -> memoryPAP2.executePML(new UserContext(0), pml2));
        assertEquals("only PML basic functions (defined as 'function') allowed in basic statement block",
            e.getErrors().getFirst().errorMessage());
    }

}
