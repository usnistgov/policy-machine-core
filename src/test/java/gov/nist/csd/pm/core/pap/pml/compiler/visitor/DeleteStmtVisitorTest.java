package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.DeleteNodeStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.DeleteObligationStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.DeleteProhibitionStatement;
import org.junit.jupiter.api.Test;

class DeleteStmtVisitorTest {

    @Test
    void testDeleteNode() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                delete if exists node "oa1"
                """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement<?> stmt = new DeleteStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteNodeStatement(new StringLiteralExpression("oa1"), true),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));

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
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement stmt = new DeleteStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteObligationStatement(new StringLiteralExpression("test"), false),
                stmt
        );
    }

    @Test
    void testDeleteProhibition() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                delete prohibition "test"
                """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement stmt = new DeleteStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteProhibitionStatement(new StringLiteralExpression("test"), false),
                stmt
        );
    }
}