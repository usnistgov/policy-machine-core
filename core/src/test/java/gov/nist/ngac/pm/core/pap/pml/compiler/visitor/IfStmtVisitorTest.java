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

package gov.nist.ngac.pm.core.pap.pml.compiler.visitor;

import static gov.nist.ngac.pm.core.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.pml.TestPMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.BoolLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.scope.CompileScope;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.ngac.pm.core.pap.pml.statement.basic.IfStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.basic.ShortDeclarationStatement;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

class IfStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                if true {
                    x := "a"
                } else if false {
                    x := "b"
                } else {
                    x := "c"
                }
                """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement<?> stmt = new IfStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new IfStatement(
                        new IfStatement.ConditionalBlock(new BoolLiteralExpression(true), new PMLStatementBlock(List.of(new ShortDeclarationStatement("x", new StringLiteralExpression("a"))))),
                        List.of(new IfStatement.ConditionalBlock(new BoolLiteralExpression(false), new PMLStatementBlock(List.of(new ShortDeclarationStatement("x", new StringLiteralExpression("b")))))),
                        new PMLStatementBlock(List.of(new ShortDeclarationStatement("x", new StringLiteralExpression("c"))))
                ),
                stmt
        );
    }

    @Test
    void testConditionExpressionsNotBool() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));

        testCompilationError(
                """
                if "a" {
                    x := "a"
                } else if "b" {
                    x := "b"
                } else {
                    x := "c"
                }
                """, visitorCtx, 1,
                "expected expression type bool, got string"
                );
    }

    @Test
    void testMultipleBodyStmtErrors() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        testCompilationError(
            """
            if true {
                badOp1()
                badOp2()
            }
            """, visitorCtx, 2,
            "unknown operation 'badOp1' in scope",
            "unknown operation 'badOp2' in scope"
        );
    }

    @Test
    void testErrorsAcrossBranches() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        testCompilationError(
            """
            if true {
                badOp1()
            } else {
                badOp2()
            }
            """, visitorCtx, 2,
            "unknown operation 'badOp1' in scope",
            "unknown operation 'badOp2' in scope"
        );
    }

    @Test
    void testReturnVoidInIf() throws PMException {
        String pml = """
                adminop f1() {
                    if true {
                        return
                    }
                    
                    create PC "pc1"
                }
                
                f1()
                """;
        PAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of(0), pml);
        assertFalse(pap.query().graph().nodeExists("pc1"));
    }

}