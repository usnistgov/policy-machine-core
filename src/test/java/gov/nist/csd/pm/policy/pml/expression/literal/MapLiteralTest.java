package gov.nist.csd.pm.policy.pml.expression.literal;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class MapLiteralTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.LiteralExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                "{\n" +
                        "                    \"a\": [\"1\", \"2\"],\n" +
                        "                    \"b\": \"c\"\n" +
                        "                }",
                PMLParser.LiteralExpressionContext.class);

        VisitorContext visitorContext = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
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