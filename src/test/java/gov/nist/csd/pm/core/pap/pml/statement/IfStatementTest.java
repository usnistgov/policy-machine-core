package gov.nist.csd.pm.core.pap.pml.statement;


import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.expression.literal.BoolLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.statement.basic.IfStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreatePolicyClassStatement;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        PAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), pml);

        assertTrue(pap.query().graph().nodeExists("a"));
        assertTrue(pap.query().graph().nodeExists("b"));
        assertTrue(pap.query().graph().nodeExists("c"));
        assertTrue(pap.query().graph().nodeExists("d"));
    }

    @Test
    void testToFormattedStringVoidReturn() {
        IfStatement stmt = new IfStatement(
                new IfStatement.ConditionalBlock(
                        new BoolLiteralExpression(true),
                        new PMLStatementBlock(
                                new CreatePolicyClassStatement(new StringLiteralExpression("a"))
                        )
                ),
                List.of(
                        new IfStatement.ConditionalBlock(
                                new BoolLiteralExpression(true),
                                new PMLStatementBlock(
                                        new CreatePolicyClassStatement(new StringLiteralExpression("b"))
                                )
                        ),
                        new IfStatement.ConditionalBlock(
                                new BoolLiteralExpression(true),
                                new PMLStatementBlock(
                                        new CreatePolicyClassStatement(new StringLiteralExpression("c"))
                                )
                        )
                ),
                new PMLStatementBlock(
                        new CreatePolicyClassStatement(new StringLiteralExpression("d"))
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