package gov.nist.csd.pm.core.pap.pml.expression.literal;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.BOOLEAN_TYPE;
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

class BoolLiteralTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                true
                """);

        CompileScope globalScope = new CompileScope(new MemoryPAP());

        VisitorContext visitorContext = new VisitorContext(globalScope);
        Expression<Boolean> expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
	    assertInstanceOf(BoolLiteralExpression.class, expression);

        BoolLiteralExpression a = (BoolLiteralExpression) expression;
        assertEquals(
               new BoolLiteralExpression(true),
                a
        );
        assertEquals(
                BOOLEAN_TYPE,
                a.getType()
        );

    }

}