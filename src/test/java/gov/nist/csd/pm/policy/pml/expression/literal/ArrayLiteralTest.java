package gov.nist.csd.pm.policy.pml.expression.literal;

import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class ArrayLiteralTest {

    @Test
    void testSuccess() {
        PMLParser.LiteralExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                ["a", "b", "c"]
                """,
                PMLParser.LiteralExpressionContext.class);

        VisitorContext visitorContext = new VisitorContext();
        Expression expression = Literal.compileLiteral(visitorContext, ctx);
        assertTrue(expression instanceof ArrayLiteral);

        ArrayLiteral a = (ArrayLiteral) expression;
        assertEquals(
                buildArrayLiteral("a", "b", "c"),
                a
        );
        assertEquals(
                Type.array(Type.string()),
                a.getType()
        );

    }

}