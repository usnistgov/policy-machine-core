package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.function.FunctionSignature;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.scope.FunctionAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.FunctionInvocationStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class FunctionInvokeStmtVisitorTest {

    @Test
    void testSuccess() throws FunctionAlreadyDefinedInScopeException {
        PMLParser.FunctionInvokeStatementContext ctx = PMLContextVisitor.toCtx(
                """
                func1("a", "b", ["c", "d"])
                """,
                PMLParser.FunctionInvokeStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        visitorCtx.scope().addFunctionSignature(new FunctionSignature("func1", Type.voidType(), List.of(
                new FormalArgument("a", Type.string()),
                new FormalArgument("b", Type.string()),
                new FormalArgument("c", Type.array(Type.string()))
        )));
        PMLStatement stmt = new FunctionInvokeStmtVisitor(visitorCtx)
                .visitFunctionInvokeStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        FunctionInvocationStatement expected = new FunctionInvocationStatement(
                "func1",
                List.of(
                        new StringLiteral("a"),
                        new StringLiteral("b"),
                        buildArrayLiteral("c", "d")
                )
        );
        assertEquals(expected, stmt);
    }

    @Test
    void testFunctionDoesNotExist() {
        PMLParser.FunctionInvokeStatementContext ctx = PMLContextVisitor.toCtx(
                """
                func1("a", "b", ["c", "d"])
                """,
                PMLParser.FunctionInvokeStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        new FunctionInvokeStmtVisitor(visitorCtx)
                .visitFunctionInvokeStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "unknown function 'func1' in scope",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testWrongNumberOfArgs() throws FunctionAlreadyDefinedInScopeException {
        PMLParser.FunctionInvokeStatementContext ctx = PMLContextVisitor.toCtx(
                """
                func1("a", "b")
                """,
                PMLParser.FunctionInvokeStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        visitorCtx.scope().addFunctionSignature(new FunctionSignature("func1", Type.voidType(), List.of(
                new FormalArgument("a", Type.string()),
                new FormalArgument("b", Type.string()),
                new FormalArgument("c", Type.array(Type.string()))
        )));

        new FunctionInvokeStmtVisitor(visitorCtx)
                .visitFunctionInvokeStatement(ctx);

        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "wrong number of args for function call func1: expected 3, got 2",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testWrongArgType() throws FunctionAlreadyDefinedInScopeException {
        PMLParser.FunctionInvokeStatementContext ctx = PMLContextVisitor.toCtx(
                """
                func1("a", "b")
                """,
                PMLParser.FunctionInvokeStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        visitorCtx.scope().addFunctionSignature(new FunctionSignature("func1", Type.voidType(), List.of(
                new FormalArgument("a", Type.string()),
                new FormalArgument("b", Type.bool())
        )));

        new FunctionInvokeStmtVisitor(visitorCtx)
                .visitFunctionInvokeStatement(ctx);

        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "invalid argument type: expected bool, got string at arg 1",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testNoArgs() throws FunctionAlreadyDefinedInScopeException {
        PMLParser.FunctionInvokeStatementContext ctx = PMLContextVisitor.toCtx(
                """
                func1()
                """,
                PMLParser.FunctionInvokeStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        visitorCtx.scope().addFunctionSignature(new FunctionSignature("func1", Type.string(), List.of()));
        PMLStatement stmt = new FunctionInvokeStmtVisitor(visitorCtx)
                .visitFunctionInvokeStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        FunctionInvocationStatement expected = new FunctionInvocationStatement(
                "func1",
                List.of()
        );
        assertEquals(expected, stmt);
    }

}