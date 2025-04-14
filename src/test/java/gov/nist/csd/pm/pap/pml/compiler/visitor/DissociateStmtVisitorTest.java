package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.DissociateStatement;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DissociateStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                dissociate "a" and "b"
                """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());
        PMLStatement<?> stmt = new DissociateStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DissociateStatement(new StringLiteralExpression("a"), new StringLiteralExpression("b")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());

        testCompilationError(
                """
                dissociate ["a"] and "b"
                """, visitorCtx, 1,
                "expected expression type string, got []string"
                );

        testCompilationError(
                """
                dissociate "a" and ["b"]
                """, visitorCtx, 1,
                "expected expression type string, got []string"
        );
    }

}