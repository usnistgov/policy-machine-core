package gov.nist.csd.pm.pap.pml.expression.literal;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.BOOLEAN_TYPE;
import static org.junit.jupiter.api.Assertions.*;

class BoolLiteralTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.LiteralExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                true
                """,
                PMLParser.LiteralExpressionContext.class);

        Scope<Variable, PMLFunctionSignature> globalScope = new CompileScope();

        VisitorContext visitorContext = new VisitorContext(globalScope);
        Expression<Boolean> expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
	    assertInstanceOf(BoolLiteralExpression.class, expression);

        BoolLiteralExpression a = (BoolLiteralExpression) expression;
        assertEquals(
               new BoolLiteralExpression(true, BOOLEAN_TYPE),
                a
        );
        assertEquals(
                BOOLEAN_TYPE,
                a.getType()
        );

    }

}