package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.AssociateStatement;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.core.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.core.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AssociateStmtVisitorTest {

    VisitorContext visitorCtx = new VisitorContext(new CompileScope());

    AssociateStmtVisitorTest() throws PMException {
    }

    @Test
    void testSuccess() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                associate "a" and "b" with ["c", "d"]
                """);
        PMLStatement<?> stmt = new AssociateStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new AssociateStatement(new StringLiteralExpression("a"), new StringLiteralExpression("b"), buildArrayLiteral("c", "d")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() throws PMException {
        testCompilationError(
                """
                associate ["a"] and "b" with ["c", "d"]
                """, visitorCtx, 1,
                "expected expression type string, got []string"
        );

        testCompilationError(
                """
                associate "a" and ["b"] with ["c", "d"]
                """, visitorCtx, 1,
                "expected expression type string, got []string"
        );

        testCompilationError(
                """
                associate "a" and "b" with "c"
                """, visitorCtx, 1,
                "expected expression type []string, got string"
        );
    }

}