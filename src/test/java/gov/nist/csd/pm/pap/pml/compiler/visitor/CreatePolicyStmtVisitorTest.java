package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.CreatePolicyClassStatement;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreatePolicyStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.CreatePolicyStatementContext ctx = TestPMLParser.toCtx(
                """
                create policy class "test"
                """,
                PMLParser.CreatePolicyStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());
        PMLStatement stmt = new CreatePolicyStmtVisitor(visitorCtx).visitCreatePolicyStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new CreatePolicyClassStatement(new StringLiteralExpression("test")),
                stmt
        );
    }


    @Test
    void testSuccessWithProperties() throws PMException {
        PMLParser.CreatePolicyStatementContext ctx = TestPMLParser.toCtx(
                """
                create policy class "test" 
                """,
                PMLParser.CreatePolicyStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());
        PMLStatement stmt = new CreatePolicyStmtVisitor(visitorCtx).visitCreatePolicyStatement(ctx);
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
                create policy class ["test"]
                """, visitorCtx, 1,
                "expected expression type(s) [string], got []string"
        );
    }
}