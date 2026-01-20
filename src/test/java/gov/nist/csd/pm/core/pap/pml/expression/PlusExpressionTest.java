package gov.nist.csd.pm.core.pap.pml.expression;


import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import org.junit.jupiter.api.Test;

class PlusExpressionTest {

    @Test
    void testPlus() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                "a" + "b"
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
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
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
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
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLCompilationRuntimeException e = assertThrows(
                PMLCompilationRuntimeException.class,
                () -> ExpressionVisitor.compile(visitorContext, ctx, STRING_TYPE)
        );
        assertEquals(1, e.getErrors().size());
        assertEquals(
                "expected expression type string, got []string",
                e.getErrors().get(0).errorMessage()
        );
    }

}