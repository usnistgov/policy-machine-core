package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.scope.CompileGlobalScope;
import gov.nist.csd.pm.pap.pml.statement.operation.DeleteStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeleteStmtVisitorTest {

    @Test
    void testDeleteNode() throws PMException {
        PMLParser.DeleteStatementContext ctx = PMLContextVisitor.toCtx(
                "delete object attribute \"oa1\"",
                PMLParser.DeleteStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(new CompileGlobalScope());
        PMLStatement stmt = new DeleteStmtVisitor(visitorCtx).visitDeleteStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteStatement(DeleteStatement.Type.OBJECT_ATTRIBUTE, new StringLiteral("oa1")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileGlobalScope());

        testCompilationError(
                "delete object attribute [\"oa1\"]", visitorCtx, 1,
                "expected expression type(s) [string], got []string"
        );
    }

    @Test
    void testDeleteObligation() throws PMException {
        PMLParser.DeleteStatementContext ctx = PMLContextVisitor.toCtx(
                "delete obligation \"test\"",
                PMLParser.DeleteStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(new CompileGlobalScope());
        PMLStatement stmt = new DeleteStmtVisitor(visitorCtx).visitDeleteStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteStatement(DeleteStatement.Type.OBLIGATION, new StringLiteral("test")),
                stmt
        );
    }

    @Test
    void testDeleteProhibition() throws PMException {
        PMLParser.DeleteStatementContext ctx = PMLContextVisitor.toCtx(
                "delete prohibition \"test\"",
                PMLParser.DeleteStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(new CompileGlobalScope());
        PMLStatement stmt = new DeleteStmtVisitor(visitorCtx).visitDeleteStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteStatement(DeleteStatement.Type.PROHIBITION, new StringLiteral("test")),
                stmt
        );
    }
}