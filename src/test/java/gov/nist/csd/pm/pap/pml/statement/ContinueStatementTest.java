package gov.nist.csd.pm.pap.pml.statement;


import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.UserContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContinueStatementTest {

    @Test
    void testSuccess() throws PMException {
        String pml = "                foreach x in [\"a\", \"b\", \"c\"] {\n" +
                "                    if x == \"b\" {\n" +
                "                        continue\n" +
                "                    }         \n" +
                "                    \n" +
                "                    create policy class x         \n" +
                "                }";
        PAP pap = new MemoryPAP();
        pap.executePML(new UserContext(""), pml);

        assertTrue(pap.query().graph().nodeExists("a"));
        assertFalse(pap.query().graph().nodeExists("b"));
        assertTrue(pap.query().graph().nodeExists("c"));
    }

    @Test
    void testMultipleLevels() throws PMException {
        String pml = "                foreach x in [\"a\", \"b\", \"c\"] {\n" +
                "                    if x == \"b\" {\n" +
                "                        if x == \"b\" {\n" +
                "                            continue\n" +
                "                        }   \n" +
                "                    }         \n" +
                "                    \n" +
                "                    create policy class x         \n" +
                "                }";
        PAP pap = new MemoryPAP();
        pap.executePML(new UserContext(""), pml);

        assertTrue(pap.query().graph().nodeExists("a"));
        assertFalse(pap.query().graph().nodeExists("b"));
        assertTrue(pap.query().graph().nodeExists("c"));
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