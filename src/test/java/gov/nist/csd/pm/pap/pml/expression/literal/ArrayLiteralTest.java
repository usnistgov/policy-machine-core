package gov.nist.csd.pm.pap.pml.expression.literal;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import java.util.List;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.OBJECT_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.listType;
import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class ArrayLiteralTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                ["a", "b", "c"]
                """);

        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression<List<String>> expression = ExpressionVisitor.compile(visitorContext, ctx, listType(STRING_TYPE));
	    assertInstanceOf(ArrayLiteralExpression.class, expression);

        assertEquals(
                buildArrayLiteral("a", "b", "c"),
                expression
        );
        assertEquals(
                listType(STRING_TYPE),
                expression.getType()
        );

    }

}