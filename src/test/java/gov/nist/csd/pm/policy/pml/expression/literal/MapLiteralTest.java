package gov.nist.csd.pm.policy.pml.expression.literal;

import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class MapLiteralTest {

    @Test
    void testSuccess() {
        PMLParser.LiteralExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                {
                    "a": ["1", "2"],
                    "b": "c"
                }
                """,
                PMLParser.LiteralExpressionContext.class);

        VisitorContext visitorContext = new VisitorContext();
        Expression expression = Literal.compileLiteral(visitorContext, ctx);
        assertTrue(expression instanceof MapLiteral);

        MapLiteral a = (MapLiteral) expression;
        assertEquals(
                new MapLiteral(Map.of(
                        new StringLiteral("a"), buildArrayLiteral("1", "2"),
                        new StringLiteral("b"), new StringLiteral("c")
                ), Type.string(), Type.any()),
                a
        );
        assertEquals(
                Type.map(Type.string(), Type.any()),
                a.getType()
        );

    }

}