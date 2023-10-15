package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.DeassignStatement;
import gov.nist.csd.pm.policy.pml.statement.DeleteStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class DeleteStmtVisitorTest {

    @Test
    void testSuccess() {
        PMLParser.DeleteStatementContext ctx = PMLContextVisitor.toCtx(
                """
                delete object attribute "oa1"
                """,
                PMLParser.DeleteStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        PMLStatement stmt = new DeleteStmtVisitor(visitorCtx).visitDeleteStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteStatement(DeleteStatement.Type.OBJECT_ATTRIBUTE, new StringLiteral("oa1")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() {
        PMLParser.DeleteStatementContext ctx = PMLContextVisitor.toCtx(
                """
                delete object attribute ["oa1"]
                """,
                PMLParser.DeleteStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        new DeleteStmtVisitor(visitorCtx).visitDeleteStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type string, got []string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }
}