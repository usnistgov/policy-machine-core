package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.BreakStatement;
import gov.nist.csd.pm.policy.pml.statement.CreateNonPCStatement;
import gov.nist.csd.pm.policy.pml.statement.ForeachStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.policy.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.*;

class CreateNonPCStmtVisitorTest {

    @Test
    void testSuccess() {
        PMLParser.CreateNonPCStatementContext ctx = PMLContextVisitor.toCtx(
                """
                create user attribute "ua1" with properties {"k": "v"} assign to ["a"]
                """,
                PMLParser.CreateNonPCStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        PMLStatement stmt = new CreateNonPCStmtVisitor(visitorCtx).visitCreateNonPCStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new CreateNonPCStatement(new StringLiteral("ua1"), NodeType.UA, buildArrayLiteral("a"), buildMapLiteral("k", "v")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() {
        PMLParser.CreateNonPCStatementContext ctx = PMLContextVisitor.toCtx(
                """
                create user attribute ["ua1"] with properties {"k": "v"} assign to ["a"]
                """,
                PMLParser.CreateNonPCStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        new CreateNonPCStmtVisitor(visitorCtx).visitCreateNonPCStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type string, got []string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                """
                create user attribute "ua1" with properties ["k", "v"] assign to ["a"]
                """,
                PMLParser.CreateNonPCStatementContext.class);
        visitorCtx = new VisitorContext();
        new CreateNonPCStmtVisitor(visitorCtx).visitCreateNonPCStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type map[string]string, got []string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                """
                create user attribute "ua1" with properties {"k": "v"} assign to "a"
                """,
                PMLParser.CreateNonPCStatementContext.class);
        visitorCtx = new VisitorContext();
        new CreateNonPCStmtVisitor(visitorCtx).visitCreateNonPCStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type []string, got string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

}