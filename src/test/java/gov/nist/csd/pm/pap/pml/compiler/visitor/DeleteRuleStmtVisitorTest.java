package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.DeleteRuleStatement;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeleteRuleStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                delete rule "rule1" from obligation "obl1"
                """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());
        PMLStatement stmt = new DeleteRuleStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteRuleStatement(new StringLiteralExpression("rule1"), new StringLiteralExpression("obl1")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());

        testCompilationError(
                """
                delete rule ["rule1"] from obligation "obl1"
                """, visitorCtx, 1,
                "expected expression type string, got []string"
        );

        testCompilationError(
                """
                delete rule "rule1" from obligation ["obl1"]
                """, visitorCtx, 1,
                "expected expression type string, got []string"
        );
    }

}