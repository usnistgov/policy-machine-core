/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.pml.statement;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.BoolLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.statement.basic.IfStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.CreatePolicyClassStatement;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

class IfStatementTest {

    @Test
    void testSuccess() throws PMException {
        String pml = """
                adminop func1(string s) {
                    if s == "a" {
                        create PC s

                    } else if s == "b" {
                        create PC s
                    
                    } else if s == "c" {
                        create PC s
                    
                    } else {
                        create PC s
                    
                    }
                }
                
                func1(s="a")
                func1(s="b")
                func1(s="c")
                func1(s="d")
                """;
        PAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of("u1"), pml);

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