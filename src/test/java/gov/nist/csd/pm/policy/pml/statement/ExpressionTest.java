package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;
import static org.junit.jupiter.api.Assertions.*;

class ExpressionTest {

    @Test
    void testCompareExpression() throws PMException {
        String pml = """
                let x = "hello world"
                let y = "hello world"
                if x == y {
                    create policy class 'pc1'
                }
                
                x = "test"
                if x != y {
                    create policy class 'pc2'
                }
                
                x = ["1", "2"]
                if x != y {
                    create policy class 'pc3'
                }
                
                y = ["1", "2"]
                if x == y {
                    create policy class 'pc4'
                }
                
                x = {"1": "2"}
                if x != y {
                    create policy class 'pc5'
                }
                
                y = {"1": "2"}
                if x == y {
                    create policy class 'pc6'
                }
                
                """;
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(memoryPolicyStore, new UserContext(SUPER_USER), pml);

        assertTrue(memoryPolicyStore.graph().nodeExists("pc1"));
        assertTrue(memoryPolicyStore.graph().nodeExists("pc2"));
        assertTrue(memoryPolicyStore.graph().nodeExists("pc3"));
        assertTrue(memoryPolicyStore.graph().nodeExists("pc4"));
        assertTrue(memoryPolicyStore.graph().nodeExists("pc5"));
        assertTrue(memoryPolicyStore.graph().nodeExists("pc6"));
    }

    @Test
    void testLogicalExpression() throws PMException {
        String pml = """
                let x = "a"
                let y = "a"
                let z = "b"
                
                if x == y || x == z {
                    create pc 'pc1'
                }
                
                if x == y && x == z {
                    create pc 'pc2'
                }
                
                z = "a"
                if x == y && x == z {
                    create pc 'pc3'
                }
                
                x = "a" == "a" || "a" == "x"
                if x {
                    create pc 'pc4'
                }
                """;

        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(memoryPolicyStore, new UserContext(SUPER_USER), pml);

        assertTrue(memoryPolicyStore.graph().nodeExists("pc1"));
        assertFalse(memoryPolicyStore.graph().nodeExists("pc2"));
        assertTrue(memoryPolicyStore.graph().nodeExists("pc3"));
        assertTrue(memoryPolicyStore.graph().nodeExists("pc4"));
    }

}