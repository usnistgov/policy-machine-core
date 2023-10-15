package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.statement.SetNodePropertiesStatement;
import gov.nist.csd.pm.policy.pml.statement.SetResourceAccessRightsStatement;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.policy.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.*;

class SetResourceAccessRightsStmtVisitorTest {

    @Test
    void testSuccess() {
        PMLParser.SetResourceAccessRightsStatementContext ctx = PMLContextVisitor.toCtx(
                """
                set resource access rights ["a", "b"]
                """,
                PMLParser.SetResourceAccessRightsStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        PMLStatement stmt = new SetResourceAccessRightsStmtVisitor(visitorCtx)
                .visitSetResourceAccessRightsStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new SetResourceAccessRightsStatement(buildArrayLiteral("a", "b")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() {
        PMLParser.SetResourceAccessRightsStatementContext ctx = PMLContextVisitor.toCtx(
                """
                set resource access rights "a"
                """,
                PMLParser.SetResourceAccessRightsStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        new SetResourceAccessRightsStmtVisitor(visitorCtx)
                .visitSetResourceAccessRightsStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type []string, got string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

}