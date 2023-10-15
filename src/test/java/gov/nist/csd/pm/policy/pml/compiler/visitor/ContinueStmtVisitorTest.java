package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.BreakStatement;
import gov.nist.csd.pm.policy.pml.statement.ContinueStatement;
import gov.nist.csd.pm.policy.pml.statement.ForeachStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class ContinueStmtVisitorTest {

    @Test
    void testSuccess() {
        PMLParser.ForeachStatementContext ctx = PMLContextVisitor.toCtx(
                """
                foreach x in ["a"] {
                    continue
                }
                """,
                PMLParser.ForeachStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        PMLStatement stmt = new ForeachStmtVisitor(visitorCtx).visitForeachStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new ForeachStatement("x", null, buildArrayLiteral("a"), List.of(
                        new ContinueStatement()
                )),
                stmt
        );
    }

    @Test
    void testNotInForLoop() {
        PMLParser.ContinueStatementContext ctx = PMLContextVisitor.toCtx(
                """
                continue
                """,
                PMLParser.ContinueStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        PMLStatement stmt = new ContinueStmtVisitor(visitorCtx).visitContinueStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "continue statement not in foreach",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

}