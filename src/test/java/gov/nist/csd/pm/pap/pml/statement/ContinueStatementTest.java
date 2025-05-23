package gov.nist.csd.pm.pap.pml.statement;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.statement.basic.ContinueStatement;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.TestPAP;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContinueStatementTest {

    @Test
    void testSuccess() throws PMException {
        String pml = """
                foreach x in ["a", "b", "c"] {
                    if x == "b" {
                        continue
                    }         
                    
                    create policy class x         
                }
                """;
        PAP pap = new TestPAP();
        pap.executePML(new UserContext(0), pml);

        assertTrue(pap.query().graph().nodeExists("a"));
        assertFalse(pap.query().graph().nodeExists("b"));
        assertTrue(pap.query().graph().nodeExists("c"));
    }

    @Test
    void testMultipleLevels() throws PMException {
        String pml = """
                foreach x in ["a", "b", "c"] {
                    if x == "b" {
                        if x == "b" {
                            continue
                        }   
                    }         
                    
                    create policy class x         
                }
                """;
        PAP pap = new TestPAP();
        pap.executePML(new UserContext(0), pml);

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