package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreatePolicyClassStatement;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.core.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreatePolicyStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                create PC "test"
                """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());
        PMLStatement stmt = new CreatePolicyStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new CreatePolicyClassStatement(new StringLiteralExpression("test")),
                stmt
        );
    }


    @Test
    void testSuccessWithProperties() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                create PC "test" 
                """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());
        PMLStatement stmt = new CreatePolicyStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new CreatePolicyClassStatement(new StringLiteralExpression("test")),
                stmt
        );
    }

    @Test
    void testInvalidNameExpression() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());

        testCompilationError(
                """
                create PC ["test"]
                """, visitorCtx, 1,
                "expected expression type string, got []string"
        );
    }
}