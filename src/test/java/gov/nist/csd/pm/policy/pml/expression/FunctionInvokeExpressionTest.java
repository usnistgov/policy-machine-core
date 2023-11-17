package gov.nist.csd.pm.policy.pml.expression;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.expression.reference.ReferenceByID;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.statement.CreatePolicyStatement;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.FunctionReturnStatement;
import gov.nist.csd.pm.policy.pml.statement.VariableAssignmentStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import gov.nist.csd.pm.policy.pml.value.VoidValue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FunctionInvokeExpressionTest {

    FunctionDefinitionStatement voidFunc = new FunctionDefinitionStatement.Builder("voidFunc")
            .returns(Type.voidType())
            .args(
                    new FormalArgument("a", Type.string()),
                    new FormalArgument("b", Type.string())
            )
            .body(
                    new CreatePolicyStatement(new ReferenceByID("a")),
                    new CreatePolicyStatement(new ReferenceByID("b"))
            )
            .build();

    @Test
    void testVoidReturnType() throws PMException {
        PMLParser.FunctionInvokeExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                voidFunc("a", "b")
                """, PMLParser.FunctionInvokeExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore())
                                                                      .withPersistedFunctions(Map.of(voidFunc.getSignature().getFunctionName(), voidFunc.getSignature())));

        Expression e = FunctionInvokeExpression.compileFunctionInvokeExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().getErrors().toString());
        assertEquals(
                new FunctionInvokeExpression("voidFunc", Type.voidType(), List.of(
                        new StringLiteral("a"),
                        new StringLiteral("b")
                )),
                e
        );
        assertEquals(
                Type.voidType(),
                e.getType(visitorContext.scope())
        );

        ExecutionContext executionContext = new ExecutionContext(new UserContext(""), GlobalScope.withValuesAndDefinitions(new MemoryPolicyStore())
                                                                                                 .withPersistedFunctions(Map.of(voidFunc.getSignature().getFunctionName(), voidFunc)));
        Value value = e.execute(executionContext, new MemoryPolicyStore());
        assertEquals(
                new VoidValue(),
                value
        );

        assertEquals(
                Type.voidType(),
                value.getType()
        );
    }

    @Test
    void testFunctionNotInScope() throws PMException {
        PMLParser.FunctionInvokeExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                voidFunc("a", "b")
                """, PMLParser.FunctionInvokeExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        FunctionInvokeExpression.compileFunctionInvokeExpression(visitorContext, ctx);
        assertEquals(1, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().getErrors().toString());
        assertEquals(
                "unknown function 'voidFunc' in scope",
                visitorContext.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testWrongNumberOfArgs() throws PMException {
        PMLParser.FunctionInvokeExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                voidFunc("a")
                """, PMLParser.FunctionInvokeExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore())
                                                                      .withPersistedFunctions(Map.of(voidFunc.getSignature().getFunctionName(), voidFunc.getSignature())));
        FunctionInvokeExpression.compileFunctionInvokeExpression(visitorContext, ctx);
        assertEquals(1, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().getErrors().toString());
        assertEquals(
                "wrong number of args for function call voidFunc: expected 2, got 1",
                visitorContext.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testWrongArgType() throws PMException {
        PMLParser.FunctionInvokeExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                voidFunc("a", ["b", "c"])
                """, PMLParser.FunctionInvokeExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore())
                                                                      .withPersistedFunctions(Map.of(voidFunc.getSignature().getFunctionName(), voidFunc.getSignature())));

        Expression e = FunctionInvokeExpression.compileFunctionInvokeExpression(visitorContext, ctx);
        assertEquals(1, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().getErrors().toString());
        assertEquals(
                "expected expression type string, got []string",
                visitorContext.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testExecuteReturnValue() throws PMException {
        FunctionDefinitionStatement stringFunc = new FunctionDefinitionStatement.Builder("stringFunc")
                .returns(Type.string())
                .args(
                        new FormalArgument("a", Type.string()),
                        new FormalArgument("b", Type.string())
                )
                .body(
                        new VariableAssignmentStatement("x", false, new StringLiteral("test")),
                        new FunctionReturnStatement(new StringLiteral("test_ret"))
                )
                .build();

        PMLParser.FunctionInvokeExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                stringFunc("a", "b")
                """, PMLParser.FunctionInvokeExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore())
                                                                      .withPersistedFunctions(Map.of(stringFunc.getSignature().getFunctionName(), stringFunc.getSignature())));

        Expression e = FunctionInvokeExpression.compileFunctionInvokeExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().getErrors().toString());
        assertEquals(
                Type.string(),
                e.getType(visitorContext.scope())
        );
    }

    @Test
    void testExecuteWithFunctionExecutor() throws PMException {
        FunctionDefinitionStatement stringFunc = new FunctionDefinitionStatement.Builder("stringFunc")
                .returns(Type.string())
                .args(
                        new FormalArgument("a", Type.string()),
                        new FormalArgument("b", Type.string())
                )
                .executor((ctx, policy) -> {
                    return new StringValue("test");
                })
                .build();
        PMLParser.FunctionInvokeExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                stringFunc("a", "b")
                """, PMLParser.FunctionInvokeExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext(
                GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore())
                           .withPersistedFunctions(Map.of(stringFunc.getSignature().getFunctionName(), stringFunc.getSignature()))
        );
        Expression e = FunctionInvokeExpression.compileFunctionInvokeExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().getErrors().toString());

        MemoryPolicyStore store = new MemoryPolicyStore();
        ExecutionContext executionContext =
                new ExecutionContext(
                        new UserContext(""),
                        GlobalScope.withValuesAndDefinitions(new MemoryPolicyStore())
                                   .withPersistedFunctions(Map.of(stringFunc.getSignature().getFunctionName(), stringFunc))
                );
        Value value = e.execute(executionContext, store);
        assertEquals(
                new StringValue("test"),
                value
        );
        assertEquals(
                Type.string(),
                value.getType()
        );
    }

    @Test
    void testChainMethodCall() throws PMException {
        String pml = """
                a("123")
                
                function c(string x) string {
                    return "c" + x
                }
                                
                function b(string x, string y) {
                    create policy class c(x)
                    create policy class c(y)
                }
                                
                function a(string x) {
                    x = "x"
                    y := "y"
                                
                    b(x, y)
                }
                """;
        MemoryPolicyStore store = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(store, new UserContext(), pml);
        assertTrue(store.graph().nodeExists("cx"));
        assertTrue(store.graph().nodeExists("cy"));
    }
}