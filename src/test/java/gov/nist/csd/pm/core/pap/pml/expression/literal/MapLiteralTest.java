package gov.nist.csd.pm.core.pap.pml.expression.literal;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.arg.type.MapType;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import static gov.nist.csd.pm.core.pap.function.arg.type.Type.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.Type.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.*;

class MapLiteralTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                {
                    "a": ["1", "2"],
                    "b": "c"
                }
                """);

        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression<Map<String, Object>> actual = ExpressionVisitor.compile(visitorContext, ctx, MapType.of(STRING_TYPE,
            ANY_TYPE));
        
        // Create a Map with Java data types
        Map<String, Object> values = new HashMap<>();
        values.put("a", List.of("1", "2"));
        values.put("b", "c");
        
        // Use the helper method to create a properly typed MapLiteralExpression
        Expression<Map<String, Object>> expected = new MapLiteralExpression<>(
            Map.of(
                new StringLiteralExpression("a"), new ArrayLiteralExpression<>(List.of(new StringLiteralExpression("1"), new StringLiteralExpression("2")), STRING_TYPE),
                new StringLiteralExpression("b"), new StringLiteralExpression("c")
            ),
            STRING_TYPE, ANY_TYPE
        );

        assertEquals(
                expected,
                actual
        );
        assertEquals(
                MapType.of(STRING_TYPE, ANY_TYPE),
                actual.getType()
        );

    }

}