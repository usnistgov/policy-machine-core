package gov.nist.csd.pm.pap.pml.expression.literal;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.*;

class StringLiteralTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                "test"
                """);

        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression<String> expression = ExpressionVisitor.compile(visitorContext, ctx, STRING_TYPE);
	    assertInstanceOf(StringLiteralExpression.class, expression);

        StringLiteralExpression a = (StringLiteralExpression) expression;
        assertEquals(
                new StringLiteralExpression("test"),
                a
        );
        assertEquals(
                STRING_TYPE,
                a.getType()
        );
    }

}