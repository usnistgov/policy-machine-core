package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.BoolLiteral;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.IfStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.statement.ShortDeclarationStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IfStmtVisitorTest {

    @Test
    void testSuccess() {
        PMLParser.IfStatementContext ctx = PMLContextVisitor.toCtx(
                """
                if true {
                    x := "a"
                } else if false {
                    x := "b"
                } else {
                    x := "c"
                }
                """,
                PMLParser.IfStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        PMLStatement stmt = new IfStmtVisitor(visitorCtx)
                .visitIfStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new IfStatement(
                        new IfStatement.ConditionalBlock(new BoolLiteral(true), List.of(new ShortDeclarationStatement("x", new StringLiteral("a")))),
                        List.of(new IfStatement.ConditionalBlock(new BoolLiteral(false), List.of(new ShortDeclarationStatement("x", new StringLiteral("b"))))),
                        List.of(new ShortDeclarationStatement("x", new StringLiteral("c")))
                ),
                stmt
        );
    }

    @Test
    void testConditionExpressionsNotBool() {
        PMLParser.IfStatementContext ctx = PMLContextVisitor.toCtx(
                """
                if "a" {
                    x := "a"
                } else if "b" {
                    x := "b"
                } else {
                    x := "c"
                }
                """,
                PMLParser.IfStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        PMLStatement stmt = new IfStmtVisitor(visitorCtx)
                .visitIfStatement(ctx);
        assertEquals(2, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type bool, got string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
        assertEquals(
                "expected expression type bool, got string",
                visitorCtx.errorLog().getErrors().get(1).errorMessage()
        );
    }

}