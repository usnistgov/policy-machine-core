package gov.nist.csd.pm.core.pap.pml.statement;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.statement.basic.ContinueStatement;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import org.junit.jupiter.api.Test;

class ContinueStatementTest {

    @Test
    void testSuccess() throws PMException {
        String pml = """
                foreach x in ["a", "b", "c"] {
                    if x == "b" {
                        continue
                    }         
                    
                    create PC x         
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
                    
                    create PC x         
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