package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.function.FunctionSignature;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.scope.FunctionAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.scope.Scope;
import gov.nist.csd.pm.policy.pml.statement.FunctionInvocationStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class FunctionInvokeStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.FunctionInvokeStatementContext ctx = PMLContextVisitor.toCtx(
                """
                func1("a", "b", ["c", "d"])
                """,
                PMLParser.FunctionInvokeStatementContext.class);

        VisitorContext visitorCtx = new VisitorContext(
                GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore())
                           .withPersistedFunctions(
                                   Map.of(
                                           "func1",
                                           new FunctionSignature(
                                                   "func1",
                                                   Type.string(),
                                                   List.of(
                                                           new FormalArgument("a", Type.string()),
                                                           new FormalArgument("b", Type.string()),
                                                           new FormalArgument("c", Type.array(Type.string()))
                                                   )
                                           )
                                   ))
        );

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
    void testFunctionDoesNotExist() throws PMException {
        PMLParser.FunctionInvokeStatementContext ctx = PMLContextVisitor.toCtx(
                """
                func1("a", "b", ["c", "d"])
                """,
                PMLParser.FunctionInvokeStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        new FunctionInvokeStmtVisitor(visitorCtx)
                .visitFunctionInvokeStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "unknown function 'func1' in scope",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testWrongNumberOfArgs() throws PMException {
        PMLParser.FunctionInvokeStatementContext ctx = PMLContextVisitor.toCtx(
                """
                func1("a", "b")
                """,
                PMLParser.FunctionInvokeStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(
                GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore())
                           .withPersistedFunctions(Map.of(
                                   "func1",
                                   new FunctionSignature(
                                           "func1",
                                           Type.string(),
                                           List.of(
                                                   new FormalArgument("a", Type.string()),
                                                   new FormalArgument("b", Type.string()),
                                                   new FormalArgument("c", Type.array(Type.string()))
                                           )
                                   )
                           ))
        );

        new FunctionInvokeStmtVisitor(visitorCtx)
                .visitFunctionInvokeStatement(ctx);

        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "wrong number of args for function call func1: expected 3, got 2",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testWrongArgType() throws PMException {
        PMLParser.FunctionInvokeStatementContext ctx = PMLContextVisitor.toCtx(
                """
                func1("a", "b")
                """,
                PMLParser.FunctionInvokeStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(
                GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore())
                           .withPersistedFunctions(Map.of(
                                   "func1",
                                   new FunctionSignature(
                                           "func1",
                                           Type.string(),
                                           List.of(
                                                   new FormalArgument("a", Type.string()),
                                                   new FormalArgument("b", Type.bool())
                                           )
                                   )
                           ))
        );

        new FunctionInvokeStmtVisitor(visitorCtx)
                .visitFunctionInvokeStatement(ctx);

        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "invalid argument type: expected bool, got string at arg 1",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testNoArgs() throws PMException {
        PMLParser.FunctionInvokeStatementContext ctx = PMLContextVisitor.toCtx(
                """
                func1()
                """,
                PMLParser.FunctionInvokeStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(
                GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore())
                           .withPersistedFunctions(Map.of(
                                   "func1",
                                   new FunctionSignature(
                                           "func1",
                                           Type.string(),
                                           List.of()
                                   )
                           ))
        );
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