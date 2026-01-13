package gov.nist.csd.pm.core.pap.pml.expression.literal;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import java.util.List;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import static gov.nist.csd.pm.core.pap.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class ArrayLiteralTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                ["a", "b", "c"]
                """);

        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression<List<String>> expression = ExpressionVisitor.compile(visitorContext, ctx, ListType.of(STRING_TYPE));
	    assertInstanceOf(ArrayLiteralExpression.class, expression);

        assertEquals(
                buildArrayLiteral("a", "b", "c"),
                expression
        );
        assertEquals(
                ListType.of(STRING_TYPE),
                expression.getType()
        );

    }

}