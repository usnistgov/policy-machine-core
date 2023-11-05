package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PMLVisitorTest {

    @Test
    void testConstantAndFunctionSignatureCompilationHappensBeforeOtherStatements() throws PMException {
        String pml = """
                test2()
                
                const b = "b"

                function test2() {
                    create pc b
                    create pc c
                    
                    test1()
                }               
                
                const c = "c"
                
                function test1() {
                    create pc "a"
                }
                
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(memoryPolicyStore, new UserContext("u1"), pml);

        assertTrue(memoryPolicyStore.graph().nodeExists("a"));
        assertTrue(memoryPolicyStore.graph().nodeExists("b"));
        assertTrue(memoryPolicyStore.graph().nodeExists("c"));
    }

}