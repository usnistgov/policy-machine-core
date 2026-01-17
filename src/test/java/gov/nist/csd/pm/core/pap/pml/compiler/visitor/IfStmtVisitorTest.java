package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.literal.BoolLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.basic.IfStatement;
import gov.nist.csd.pm.core.pap.pml.statement.basic.ShortDeclarationStatement;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.Test;

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
        pap.executePML(new UserContext(0), pml);
        assertFalse(pap.query().graph().nodeExists("pc1"));
    }

}