package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class BreakStatementTest {

    @Test
    void testSuccess() throws PMException {
        String pml = """
                foreach x in ["a", "b", "c"] {
                    create policy class x
                    
                    if x == "b" {
                        break
                    }                  
                }
                """;
        MemoryPolicyStore store = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(store, new UserContext(""), pml);

        assertTrue(store.graph().nodeExists("a"));
        assertTrue(store.graph().nodeExists("b"));
        assertFalse(store.graph().nodeExists("c"));
    }

    @Test
    void testMultipleLevels() throws PMException {
        String pml = """
                foreach x in ["a", "b", "c"] {
                    create policy class x
                    
                    if x == "b" {
                        if x == "b" {
                            break
                        }
                    }                 
                }
                """;
        MemoryPolicyStore store = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(store, new UserContext(""), pml);

        assertTrue(store.graph().nodeExists("a"));
        assertTrue(store.graph().nodeExists("b"));
        assertFalse(store.graph().nodeExists("c"));
    }

    @Test
    void testToFormattedString() {
        BreakStatement stmt = new BreakStatement();

        assertEquals(
                "break",
                stmt.toFormattedString(0)
        );
        assertEquals(
                "    break",
                stmt.toFormattedString(1)
        );
    }

}