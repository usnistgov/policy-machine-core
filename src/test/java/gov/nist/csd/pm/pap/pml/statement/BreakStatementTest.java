package gov.nist.csd.pm.pap.pml.statement;


import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.UserContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BreakStatementTest {

    @Test
    void testSuccess() throws PMException {
        String pml = "                foreach x in [\"a\", \"b\", \"c\"] {\n" +
                "                    create policy class x\n" +
                "                    \n" +
                "                    if x == \"b\" {\n" +
                "                        break\n" +
                "                    }                  \n" +
                "                }";
        PAP pap = new MemoryPAP();
        pap.executePML(new UserContext(""), pml);

        assertTrue(pap.query().graph().nodeExists("a"));
        assertTrue(pap.query().graph().nodeExists("b"));
        assertFalse(pap.query().graph().nodeExists("c"));
    }

    @Test
    void testMultipleLevels() throws PMException {
        String pml = "                foreach x in [\"a\", \"b\", \"c\"] {\n" +
                "                    create policy class x\n" +
                "                    \n" +
                "                    if x == \"b\" {\n" +
                "                        if x == \"b\" {\n" +
                "                            break\n" +
                "                        }\n" +
                "                    }                 \n" +
                "                }";
        PAP pap = new MemoryPAP();
        pap.executePML(new UserContext(""), pml);

        assertTrue(pap.query().graph().nodeExists("a"));
        assertTrue(pap.query().graph().nodeExists("b"));
        assertFalse(pap.query().graph().nodeExists("c"));
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