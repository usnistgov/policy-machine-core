package gov.nist.csd.pm.policy.pml.expression;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.expression.reference.ReferenceByID;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.scope.FunctionAlreadyDefinedInScopeException;
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
        VisitorContext visitorContext = new VisitorContext();
        visitorContext.scope().addFunctionSignature(voidFunc.signature());
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

        ExecutionContext executionContext = new ExecutionContext(new UserContext(""));
        executionContext.scope().addFunctionSignature(voidFunc.signature());
        executionContext.scope().addFunction(voidFunc);
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
    void testFunctionNotInScope() {
        PMLParser.FunctionInvokeExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                voidFunc("a", "b")
                """, PMLParser.FunctionInvokeExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext();
        Expression e = FunctionInvokeExpression.compileFunctionInvokeExpression(visitorContext, ctx);
        assertEquals(1, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().getErrors().toString());
        assertEquals(
                "unknown function 'voidFunc' in scope",
                visitorContext.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testWrongNumberOfArgs() throws FunctionAlreadyDefinedInScopeException {
        PMLParser.FunctionInvokeExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                voidFunc("a")
                """, PMLParser.FunctionInvokeExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext();
        visitorContext.scope().addFunctionSignature(voidFunc.signature());
        Expression e = FunctionInvokeExpression.compileFunctionInvokeExpression(visitorContext, ctx);
        assertEquals(1, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().getErrors().toString());
        assertEquals(
                "wrong number of args for function call voidFunc: expected 2, got 1",
                visitorContext.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testWrongArgType() throws FunctionAlreadyDefinedInScopeException {
        PMLParser.FunctionInvokeExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                voidFunc("a", ["b", "c"])
                """, PMLParser.FunctionInvokeExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext();
        visitorContext.scope().addFunctionSignature(voidFunc.signature());
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
        VisitorContext visitorContext = new VisitorContext();
        visitorContext.scope().addFunctionSignature(stringFunc.signature());
        Expression e = FunctionInvokeExpression.compileFunctionInvokeExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().getErrors().toString());
        assertEquals(
                Type.string(),
                e.getType(visitorContext.scope())
        );

        MemoryPolicyStore store = new MemoryPolicyStore();
        ExecutionContext executionContext = new ExecutionContext(new UserContext(""));
        executionContext.scope().addFunctionSignature(stringFunc.signature());
        executionContext.scope().addFunction(stringFunc);
        executionContext.scope().addValue("x", new StringValue("x"));
        Value value = e.execute(executionContext, store);
        assertEquals(
                new StringValue("test_ret"),
                value
        );
        assertEquals(
                Type.string(),
                value.getType()
        );
        assertEquals(
                new StringValue("test"),
                executionContext.scope().getValue("x")
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
        VisitorContext visitorContext = new VisitorContext();
        visitorContext.scope().addFunctionSignature(stringFunc.signature());
        Expression e = FunctionInvokeExpression.compileFunctionInvokeExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().getErrors().toString());

        MemoryPolicyStore store = new MemoryPolicyStore();
        ExecutionContext executionContext = new ExecutionContext(new UserContext(""));
        executionContext.scope().addFunctionSignature(stringFunc.signature());
        executionContext.scope().addFunction(stringFunc);
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
}