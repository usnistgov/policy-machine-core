package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.AssignStatement;
import gov.nist.csd.pm.policy.pml.statement.DeassignStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class DeassignStmtVisitorTest {

    @Test
    void testSuccess() {
        PMLParser.DeassignStatementContext ctx = PMLContextVisitor.toCtx(
                """
                deassign "a" from ["b", "c"]
                """,
                PMLParser.DeassignStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        PMLStatement stmt = new DeassignStmtVisitor(visitorCtx).visitDeassignStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeassignStatement(new StringLiteral("a"), buildArrayLiteral("b", "c")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() {
        PMLParser.DeassignStatementContext ctx = PMLContextVisitor.toCtx(
                """
                deassign "a" from "c"
                """,
                PMLParser.DeassignStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        new DeassignStmtVisitor(visitorCtx).visitDeassignStatement(ctx);
        assertEquals(
                "expected expression type []string, got string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                """
                deassign ["a"] from ["b", "c"]
                """,
                PMLParser.DeassignStatementContext.class);
        visitorCtx = new VisitorContext();
        new DeassignStmtVisitor(visitorCtx).visitDeassignStatement(ctx);
        assertEquals(
                "expected expression type string, got []string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

}