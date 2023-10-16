package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.statement.BreakStatement;
import gov.nist.csd.pm.policy.pml.statement.ForeachStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class BreakStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.ForeachStatementContext ctx = PMLContextVisitor.toCtx(
                "foreach x in [\"a\"] {\n" +
                        "                    break\n" +
                        "                }",
                PMLParser.ForeachStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        PMLStatement stmt = new ForeachStmtVisitor(visitorCtx).visitForeachStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new ForeachStatement("x", null, buildArrayLiteral("a"), List.of(
                        new BreakStatement()
                )),
                stmt
        );
    }

    @Test
    void testNotInForLoop() throws PMException {
        PMLParser.BreakStatementContext ctx = PMLContextVisitor.toCtx(
                "break",
                PMLParser.BreakStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        PMLStatement stmt = new BreakStmtVisitor(visitorCtx).visitBreakStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "break statement not in foreach",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

}