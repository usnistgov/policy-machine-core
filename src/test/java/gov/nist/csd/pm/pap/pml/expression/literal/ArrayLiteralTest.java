package gov.nist.csd.pm.pap.pml.expression.literal;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import org.junit.jupiter.api.Test;
import org.neo4j.codegen.api.ArrayLiteral;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.OBJECT_TYPE;
import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class ArrayLiteralTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.LiteralExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                ["a", "b", "c"]
                """,
                PMLParser.LiteralExpressionContext.class);

        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression<Object> expression = ExpressionVisitor.compile(visitorContext, ctx, OBJECT_TYPE);
	    assertInstanceOf(ArrayLiteral.class, expression);

        ArrayLiteralExpression<?> a = (ArrayLiteralExpression<?>) expression;
        assertEquals(
                buildArrayLiteral("a", "b", "c"),
                a
        );
        assertEquals(
                listType(STRING_TYPE),
                a.getType()
        );

    }

}