package gov.nist.csd.pm.policy.pml.expression;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.function.FunctionSignature;
import gov.nist.csd.pm.policy.pml.function.builtin.Equals;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.BoolValue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static gov.nist.csd.pm.policy.pml.statement.PMLStatement.execute;
import static org.junit.jupiter.api.Assertions.*;

class ParenExpressionTest {

    private static GlobalScope<Variable, FunctionSignature> compileGlobalScope;
    private static GlobalScope<Value, FunctionDefinitionStatement> executeGlobalScope;

    @BeforeAll
    static void setup() throws PMException {
        compileGlobalScope = GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore())
                                        .withPersistedFunctions(Map.of("equals", new Equals().getSignature()));
        executeGlobalScope = GlobalScope.withValuesAndDefinitions(new MemoryPolicyStore())
                                        .withPersistedFunctions(Map.of("equals", new Equals()));
    }

    @Test
    void testParenExpression() throws PMException {
        PMLParser.ExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                true && (true || false)
                """, PMLParser.ExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext(compileGlobalScope);
        Expression e = Expression.compile(visitorContext, ctx, Type.bool());
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        Value actual = execute(new ExecutionContext(new UserContext(""), executeGlobalScope), new MemoryPolicyStore(), e);
        assertEquals(
                new BoolValue(true),
                actual
        );

        ctx = PMLContextVisitor.toExpressionCtx(
                """
                (false || false) && (true || false)
                """, PMLParser.ExpressionContext.class);
        visitorContext = new VisitorContext(compileGlobalScope);
        e = Expression.compile(visitorContext, ctx, Type.bool());
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        actual = execute(new ExecutionContext(new UserContext(""), executeGlobalScope), new MemoryPolicyStore(), e);
        assertEquals(
                new BoolValue(false),
                actual
        );

        ctx = PMLContextVisitor.toExpressionCtx(
                """
                (false || false) || (true || false)
                """, PMLParser.ExpressionContext.class);
        visitorContext = new VisitorContext(compileGlobalScope);
        e = Expression.compile(visitorContext, ctx, Type.bool());
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        actual = execute(new ExecutionContext(new UserContext(""), executeGlobalScope), new MemoryPolicyStore(), e);
        assertEquals(
                new BoolValue(true),
                actual
        );

        ctx = PMLContextVisitor.toExpressionCtx(
                """
                !(false || false) && (true || false)
                """, PMLParser.ExpressionContext.class);
        visitorContext = new VisitorContext(compileGlobalScope);
        e = Expression.compile(visitorContext, ctx, Type.bool());
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        actual = execute(new ExecutionContext(new UserContext(""), executeGlobalScope), new MemoryPolicyStore(), e);
        assertEquals(
                new BoolValue(true),
                actual
        );

        ctx = PMLContextVisitor.toExpressionCtx(
                """
                !(false || false) && (false || false || true)
                """, PMLParser.ExpressionContext.class);
        visitorContext = new VisitorContext(compileGlobalScope);
        e = Expression.compile(visitorContext, ctx, Type.bool());
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        actual = execute(new ExecutionContext(new UserContext(""), executeGlobalScope), new MemoryPolicyStore(), e);
        assertEquals(
                new BoolValue(true),
                actual
        );

        ctx = PMLContextVisitor.toExpressionCtx(
                """
                !(false || false) && (false || false || true) && false
                """, PMLParser.ExpressionContext.class);
        visitorContext = new VisitorContext(compileGlobalScope);
        e = Expression.compile(visitorContext, ctx, Type.bool());
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        actual = execute(new ExecutionContext(new UserContext(""), executeGlobalScope), new MemoryPolicyStore(), e);
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
        VisitorContext visitorContext = new VisitorContext(compileGlobalScope);
        Expression expression = ParenExpression.compileParenExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        MemoryPolicyStore store = new MemoryPolicyStore();
        ExecutionContext executionContext = new ExecutionContext(new UserContext(""), executeGlobalScope);
        Value actual = execute(executionContext, store, expression);
        assertEquals(
                new BoolValue(false),
                actual
        );
    }

}