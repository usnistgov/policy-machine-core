package gov.nist.csd.pm.pap.pml.expression;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;


import gov.nist.csd.pm.pap.query.model.context.UserContext;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlusExpressionTest {

    @Test
    void testPlus() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                "a" + "b"
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression<?> expression = ExpressionVisitor.compile(visitorContext, ctx, STRING_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        PlusExpression plusExpression = (PlusExpression) expression;
        assertEquals(
                new PlusExpression(new StringLiteralExpression("a"), new StringLiteralExpression("b")),
                plusExpression
        );

        Object value = plusExpression.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                "ab",
                value
        );
    }

    @Test
    void testPlus3Expressions() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                "a" + "b" + "c"
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, STRING_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        PlusExpression plusExpression = (PlusExpression) expression;
        assertEquals(
                new PlusExpression(new PlusExpression(new StringLiteralExpression("a"), new StringLiteralExpression("b")), new StringLiteralExpression("c")),
                plusExpression
        );

        Object value = plusExpression.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                "abc",
                value
        );
    }

    @Test
    void testNonStringType() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                "a" + "b" + ["c"]
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        PMLCompilationRuntimeException e = assertThrows(
                PMLCompilationRuntimeException.class,
                () -> ExpressionVisitor.compile(visitorContext, ctx, STRING_TYPE)
        );
        assertEquals(1, e.getErrors().size());
        assertEquals(
                "Cannot cast from []string to string",
                e.getErrors().get(0).errorMessage()
        );
    }

}