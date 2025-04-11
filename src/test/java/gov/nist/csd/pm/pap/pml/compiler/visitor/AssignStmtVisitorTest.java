package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.AssignStatement;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AssignStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.AssignStatementContext ctx = TestPMLParser.toCtx(
                """
                assign "a" to ["b", "c"]
                """,
                PMLParser.AssignStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());
        PMLStatement stmt = new AssignStmtVisitor(visitorCtx).visitAssignStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new AssignStatement(new StringLiteralExpression("a"), buildArrayLiteral("b", "c")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());

        testCompilationError(
                """
                assign "a" to "b"
                """, visitorCtx, 1,
                "expected expression type(s) [[]string], got string"
        );

        testCompilationError(
                """
                assign ["a"] to "b"
                """, visitorCtx, 1,
                "expected expression type(s) [string], got []string"
        );
    }

}