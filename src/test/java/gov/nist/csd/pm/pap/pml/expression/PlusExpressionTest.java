package gov.nist.csd.pm.pap.pml.expression;


import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.scope.CompileGlobalScope;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlusExpressionTest {

    @Test
    void testPlus() throws PMException {
        PMLParser.PlusExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                "\"a\" + \"b\"",
                PMLParser.PlusExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext(new CompileGlobalScope());
        Expression expression = PlusExpression.compilePlusExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        PlusExpression plusExpression = (PlusExpression) expression;
        assertEquals(
                new PlusExpression(new StringLiteral("a"), new StringLiteral("b")),
                plusExpression
        );

        Value value = plusExpression.execute(new ExecutionContext(new UserContext(""), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                new StringValue("ab"),
                value
        );
    }

    @Test
    void testPlus3Expressions() throws PMException {
        PMLParser.PlusExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                "\"a\" + \"b\" + \"c\"",
                PMLParser.PlusExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext(new CompileGlobalScope());
        Expression expression = PlusExpression.compilePlusExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        PlusExpression plusExpression = (PlusExpression) expression;
        assertEquals(
                new PlusExpression(new PlusExpression(new StringLiteral("a"), new StringLiteral("b")), new StringLiteral("c")),
                plusExpression
        );

        Value value = plusExpression.execute(new ExecutionContext(new UserContext(""), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                new StringValue("abc"),
                value
        );
    }

    @Test
    void testNonStringType() throws PMException {
        PMLParser.PlusExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                "\"a\" + \"b\" + [\"c\"]",
                PMLParser.PlusExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext(new CompileGlobalScope());
        PMLCompilationRuntimeException e = assertThrows(
                PMLCompilationRuntimeException.class,
                () -> PlusExpression.compilePlusExpression(visitorContext, ctx)
        );
        assertEquals(1, e.getErrors().size());
        assertEquals(
                "expected expression type(s) [string], got []string",
                e.getErrors().get(0).errorMessage()
        );
    }

}