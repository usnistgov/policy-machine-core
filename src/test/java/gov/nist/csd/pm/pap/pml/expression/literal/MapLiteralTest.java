package gov.nist.csd.pm.pap.pml.expression.literal;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.OBJECT_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.mapType;
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

        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression<Map<String, Object>> expression = ExpressionVisitor.compile(visitorContext, ctx, ArgType.mapType(STRING_TYPE, OBJECT_TYPE));

        MapLiteralExpression a = (MapLiteralExpression) expression;
        assertEquals(
                new MapLiteralExpression(Map.of(
                        new StringLiteralExpression<>("a", STRING_TYPE), buildArrayLiteral("1", "2"),
                        new StringLiteralExpression<>("b", STRING_TYPE), new StringLiteralExpression<>("c", STRING_TYPE)
                ), mapType(STRING_TYPE, OBJECT_TYPE)),
                a
        );
        assertEquals(
                mapType(STRING_TYPE, OBJECT_TYPE),
                a.getType()
        );

    }

}