package gov.nist.csd.pm.policy.pml.expression;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.BoolValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParenExpressionTest {

    @Test
    void testParenExpression() throws PMException {
        PMLParser.ExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                true && (true || false)
                """, PMLParser.ExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext();
        Expression e = Expression.compile(visitorContext, ctx, Type.bool());
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        Value actual = e.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(true),
                actual
        );

        ctx = PMLContextVisitor.toExpressionCtx(
                """
                (false || false) && (true || false)
                """, PMLParser.ExpressionContext.class);
        visitorContext = new VisitorContext();
        e = Expression.compile(visitorContext, ctx, Type.bool());
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        actual = e.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(false),
                actual
        );

        ctx = PMLContextVisitor.toExpressionCtx(
                """
                (false || false) || (true || false)
                """, PMLParser.ExpressionContext.class);
        visitorContext = new VisitorContext();
        e = Expression.compile(visitorContext, ctx, Type.bool());
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        actual = e.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(true),
                actual
        );

        ctx = PMLContextVisitor.toExpressionCtx(
                """
                !(false || false) && (true || false)
                """, PMLParser.ExpressionContext.class);
        visitorContext = new VisitorContext();
        e = Expression.compile(visitorContext, ctx, Type.bool());
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        actual = e.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(true),
                actual
        );

        ctx = PMLContextVisitor.toExpressionCtx(
                """
                !(false || false) && (false || false || true)
                """, PMLParser.ExpressionContext.class);
        visitorContext = new VisitorContext();
        e = Expression.compile(visitorContext, ctx, Type.bool());
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        actual = e.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(true),
                actual
        );

        ctx = PMLContextVisitor.toExpressionCtx(
                """
                !(false || false) && (false || false || true) && false
                """, PMLParser.ExpressionContext.class);
        visitorContext = new VisitorContext();
        e = Expression.compile(visitorContext, ctx, Type.bool());
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        actual = e.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(false),
                actual
        );
    }

    @Test
    void testComplexParen() throws PMException {
        PMLParser.ParenExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                ((true || (true && false)) && (false || (false && true)))
                """,
                PMLParser.ParenExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext();
        Expression expression = ParenExpression.compileParenExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        MemoryPolicyStore store = new MemoryPolicyStore();
        ExecutionContext executionContext = new ExecutionContext(new UserContext(""));
        Value actual = expression.execute(executionContext, store);
        assertEquals(
                new BoolValue(false),
                actual
        );
    }

}