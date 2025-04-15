package gov.nist.csd.pm.pap.pml.expression;

import static gov.nist.csd.pm.pap.function.arg.type.Type.ANY_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.BOOLEAN_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.UnexpectedExpressionTypeException;
import gov.nist.csd.pm.pap.pml.expression.literal.BoolLiteralExpression;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import org.junit.jupiter.api.Test;

class NegatedExpressionTest {

    @Test
    void testNegatedExpression() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                !true
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression<?> expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        NegatedExpression negatedExpression = (NegatedExpression) expression;
        assertEquals(
                new NegatedExpression(new BoolLiteralExpression(true)),
                negatedExpression
        );

        Object value = negatedExpression.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                false,
                value
        );
    }

    @Test
    void testAsType() throws PMException {
        NegatedExpression negatedExpression = new NegatedExpression(new BoolLiteralExpression(true));
        
        // Test asType with BOOLEAN_TYPE
        Expression<Boolean> asBooleanType = negatedExpression.asType(BOOLEAN_TYPE);
        assertEquals(negatedExpression, asBooleanType);
        
        // Test asType with ANY_TYPE
        Expression<Object> asObjectType = negatedExpression.asType(ANY_TYPE);
        assertEquals(negatedExpression, asObjectType);
        
        // Test asType with incompatible type (should throw exception)
        assertThrows(UnexpectedExpressionTypeException.class, () -> 
            negatedExpression.asType(STRING_TYPE)
        );
    }
} 