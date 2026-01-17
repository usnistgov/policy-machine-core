package gov.nist.csd.pm.core.pap.pml.expression.literal;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import org.junit.jupiter.api.Test;

class StringLiteralTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                "test"
                """);

        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
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