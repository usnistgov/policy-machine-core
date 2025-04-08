package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
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
        PMLParser.DeleteStatementContext ctx = PMLContextVisitor.toCtx(
                """
                delete node "oa1"
                """,
                PMLParser.DeleteStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());
        PMLStatement stmt = new DeleteStmtVisitor(visitorCtx).visitDeleteStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteNodeStatement(new StringLiteral("oa1")),
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
                "expected expression type(s) [string], got []string"
        );
    }

    @Test
    void testDeleteObligation() throws PMException {
        PMLParser.DeleteStatementContext ctx = PMLContextVisitor.toCtx(
                """
                delete obligation "test"
                """,
                PMLParser.DeleteStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());
        PMLStatement stmt = new DeleteStmtVisitor(visitorCtx).visitDeleteStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteObligationStatement(new StringLiteral("test")),
                stmt
        );
    }

    @Test
    void testDeleteProhibition() throws PMException {
        PMLParser.DeleteStatementContext ctx = PMLContextVisitor.toCtx(
                """
                delete prohibition "test"
                """,
                PMLParser.DeleteStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());
        PMLStatement stmt = new DeleteStmtVisitor(visitorCtx).visitDeleteStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteProhibitionStatement(new StringLiteral("test")),
                stmt
        );
    }
}