package gov.nist.csd.pm.core.pap.pml.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import org.junit.jupiter.api.Test;

public class PMLOperationTest {

    @Test
    void testFunctionsPersisted() throws PMException {
        String pml = """
            function test() string {
                return "test"
            }
            """;
        MemoryPAP memoryPAP = new MemoryPAP();
        memoryPAP.executePML(new UserContext(0), pml);

        assertTrue(memoryPAP.query().operations().getBasicFunctionNames().contains("test"));

        pml = """
            create PC test()
            """;
        memoryPAP.executePML(new UserContext(0), pml);

        assertTrue(memoryPAP.query().graph().nodeExists("test"));
    }

    @Test
    void testFunctionsCannotCallOperationsOrRoutines() throws PMException {
        String pml = """
            adminop op1(string s) {
                create pc s
            }
            
            function test() {
               op1("test")
            }
            """;
        MemoryPAP memoryPAP = new MemoryPAP();
        PMLCompilationException e = assertThrows(PMLCompilationException.class,
            () -> memoryPAP.executePML(new UserContext(0), pml));
        assertEquals("unknown operation 'op1' in scope",
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
        assertEquals("unknown operation 'rou1' in scope",
            e.getErrors().getFirst().errorMessage());
    }

}
