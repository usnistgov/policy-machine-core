package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.PMLUtil;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.DissociateStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DissociateStmtVisitorTest {

    @Test
    void testSuccess() {
        PMLParser.DissociateStatementContext ctx = PMLContextVisitor.toCtx(
                """
                dissociate "a" and ["b"]
                """,
                PMLParser.DissociateStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        PMLStatement stmt = new DissociateStmtVisitor(visitorCtx).visitDissociateStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DissociateStatement(new StringLiteral("a"), buildArrayLiteral("b")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() {
        PMLParser.DissociateStatementContext ctx = PMLContextVisitor.toCtx(
                """
                dissociate ["a"] and "b"
                """,
                PMLParser.DissociateStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        new DissociateStmtVisitor(visitorCtx).visitDissociateStatement(ctx);
        assertEquals(2, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type string, got []string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
        assertEquals(
                "expected expression type []string, got string",
                visitorCtx.errorLog().getErrors().get(1).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                """
                dissociate "a" and "b"
                """,
                PMLParser.DissociateStatementContext.class);
        visitorCtx = new VisitorContext();
        new DissociateStmtVisitor(visitorCtx).visitDissociateStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type []string, got string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

}