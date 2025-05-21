package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.DeleteNodeStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.DeleteObligationStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.DeleteProhibitionStatement;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeleteStmtVisitorTest {

    @Test
    void testDeleteNode() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                delete node "oa1"
                """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());
        PMLStatement stmt = new DeleteStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteNodeStatement(new StringLiteralExpression("oa1")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());

        testCompilationError(
                """
                delete node ["oa1"]
                """, visitorCtx, 1,
                "expected expression type string, got []string"
        );
    }

    @Test
    void testDeleteObligation() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                delete obligation "test"
                """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());
        PMLStatement stmt = new DeleteStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteObligationStatement(new StringLiteralExpression("test")),
                stmt
        );
    }

    @Test
    void testDeleteProhibition() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                delete prohibition "test"
                """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());
        PMLStatement stmt = new DeleteStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteProhibitionStatement(new StringLiteralExpression("test")),
                stmt
        );
    }
}