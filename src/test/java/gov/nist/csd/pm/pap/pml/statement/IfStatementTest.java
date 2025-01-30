package gov.nist.csd.pm.pap.pml.statement;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.statement.operation.CreatePolicyStatement;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.pml.expression.literal.BoolLiteral;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.util.TestMemoryPAP.id;
import static org.junit.jupiter.api.Assertions.*;

class IfStatementTest {

    @Test
    void testSuccess() throws PMException {
        String pml = """
                operation func1(string s) {
                    if s == "a" {
                        create policy class s

                    } else if s == "b" {
                        create policy class s
                    
                    } else if s == "c" {
                        create policy class s
                    
                    } else {
                        create policy class s
                    
                    }
                }
                
                func1("a")
                func1("b")
                func1("c")
                func1("d")
                """;
        PAP pap = new MemoryPAP();
        pap.executePML(new TestUserContext("u1", pap), pml);

        assertTrue(pap.query().graph().nodeExists("a"));
        assertTrue(pap.query().graph().nodeExists("b"));
        assertTrue(pap.query().graph().nodeExists("c"));
        assertTrue(pap.query().graph().nodeExists("d"));
    }

    @Test
    void testToFormattedStringVoidReturn() {
        IfStatement stmt = new IfStatement(
                new IfStatement.ConditionalBlock(
                        new BoolLiteral(true),
                        new PMLStatementBlock(
                                new CreatePolicyStatement(new StringLiteral("a"))
                        )
                ),
                List.of(
                        new IfStatement.ConditionalBlock(
                                new BoolLiteral(true),
                                new PMLStatementBlock(
                                        new CreatePolicyStatement(new StringLiteral("b"))
                                )
                        ),
                        new IfStatement.ConditionalBlock(
                                new BoolLiteral(true),
                                new PMLStatementBlock(
                                        new CreatePolicyStatement(new StringLiteral("c"))
                                )
                        )
                ),
                new PMLStatementBlock(
                        new CreatePolicyStatement(new StringLiteral("d"))
                )
        );

        assertEquals("""
                             if true {
                                 create PC "a"
                             } else if true {
                                 create PC "b"                          
                             } else if true {
                                 create PC "c"
                             } else {
                                 create PC "d"                             
                             }""",
                     stmt.toFormattedString(0));

        assertEquals("""
                                 if true {
                                     create PC "a"
                                 } else if true {
                                     create PC "b"                          
                                 } else if true {
                                     create PC "c"
                                 } else {
                                     create PC "d"                             
                                 }
                             """,
                     stmt.toFormattedString(1) + "\n");
    }
}