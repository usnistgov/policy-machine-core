package gov.nist.csd.pm.pap.pml.expression.literal;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.scope.CompileGlobalScope;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class ArrayLiteralTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.LiteralExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                "[\"a\", \"b\", \"c\"]",
                PMLParser.LiteralExpressionContext.class);

        VisitorContext visitorContext = new VisitorContext(new CompileGlobalScope());
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