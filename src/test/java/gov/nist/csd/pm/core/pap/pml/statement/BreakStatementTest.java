package gov.nist.csd.pm.core.pap.pml.statement;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.statement.basic.BreakStatement;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import org.junit.jupiter.api.Test;

class BreakStatementTest {

    @Test
    void testSuccess() throws PMException {
        String pml = """
                foreach x in ["a", "b", "c"] {
                    create PC x
                    
                    if x == "b" {
                        break
                    }                  
                }
                """;
        PAP pap = new TestPAP();
        pap.executePML(new UserContext(0), pml);

        assertTrue(pap.query().graph().nodeExists("a"));
        assertTrue(pap.query().graph().nodeExists("b"));
        assertFalse(pap.query().graph().nodeExists("c"));
    }

    @Test
    void testMultipleLevels() throws PMException {
        String pml = """
                foreach x in ["a", "b", "c"] {
                    create PC x
                    
                    if x == "b" {
                        if x == "b" {
                            break
                        }
                    }                 
                }
                """;
        PAP pap = new TestPAP();
        pap.executePML(new UserContext(0), pml);

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