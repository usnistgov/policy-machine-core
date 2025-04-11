package gov.nist.csd.pm.pap.pml.expression.literal;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.OBJECT_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.mapType;
import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.pap.pml.PMLUtil.buildHeterogeneousMapLiteral;
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
        Expression<Map<String, Object>> actual = ExpressionVisitor.compile(visitorContext, ctx, ArgType.mapType(STRING_TYPE, OBJECT_TYPE));
        
        // Create a Map with Java data types
        Map<String, Object> values = new HashMap<>();
        values.put("a", List.of("1", "2"));
        values.put("b", "c");
        
        // Use the helper method to create a properly typed MapLiteralExpression
        Expression<Map<String, Object>> expected = buildHeterogeneousMapLiteral(values);

        assertEquals(
                expected,
                actual
        );
        assertEquals(
                mapType(STRING_TYPE, OBJECT_TYPE),
                actual.getType()
        );

    }

}