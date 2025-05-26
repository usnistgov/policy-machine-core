package gov.nist.csd.pm.core.pap.pml.expression.literal;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.pml.scope.Scope;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.core.pap.function.arg.type.Type.BOOLEAN_TYPE;
import static org.junit.jupiter.api.Assertions.*;

class BoolLiteralTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                true
                """);

        Scope<Variable, PMLFunctionSignature> globalScope = new CompileScope();

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