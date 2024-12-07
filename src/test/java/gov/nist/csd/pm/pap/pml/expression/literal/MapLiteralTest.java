package gov.nist.csd.pm.pap.pml.expression.literal;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.scope.CompileGlobalScope;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class MapLiteralTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.LiteralExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                {
                    "a": ["1", "2"],
                    "b": "c"
                }
                """,
                PMLParser.LiteralExpressionContext.class);

        VisitorContext visitorContext = new VisitorContext(new CompileGlobalScope());
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