package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class ContinueStatementTest {

    @Test
    void testSuccess() throws PMException {
        String pml = "foreach x in [\"a\", \"b\", \"c\"] {\n" +
                "                    if x == \"b\" {\n" +
                "                        continue\n" +
                "                    }         \n" +
                "                    \n" +
                "                    create policy class x         \n" +
                "                }";
        MemoryPolicyStore store = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(store, new UserContext(""), pml);

        assertTrue(store.graph().nodeExists("a"));
        assertFalse(store.graph().nodeExists("b"));
        assertTrue(store.graph().nodeExists("c"));
    }

    @Test
    void testMultipleLevels() throws PMException {
        String pml = "foreach x in [\"a\", \"b\", \"c\"] {\n" +
                "                    if x == \"b\" {\n" +
                "                        if x == \"b\" {\n" +
                "                            continue\n" +
                "                        }   \n" +
                "                    }         \n" +
                "                    \n" +
                "                    create policy class x         \n" +
                "                }";
        MemoryPolicyStore store = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(store, new UserContext(""), pml);

        assertTrue(store.graph().nodeExists("a"));
        assertFalse(store.graph().nodeExists("b"));
        assertTrue(store.graph().nodeExists("c"));
    }

    @Test
    void testToFormattedString() {
        ContinueStatement stmt = new ContinueStatement();

        assertEquals(
                "continue",
                stmt.toFormattedString(0)
        );
        assertEquals(
                "    continue",
                stmt.toFormattedString(1)
        );
    }

}