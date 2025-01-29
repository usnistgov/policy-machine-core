package gov.nist.csd.pm.pap.pml.expression;

import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLStmtsOperation;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLStmtsOperationBody;
import gov.nist.csd.pm.pap.pml.scope.CompileGlobalScope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.statement.operation.CreatePolicyStatement;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.expression.reference.ReferenceByID;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.statement.FunctionReturnStatement;
import gov.nist.csd.pm.pap.pml.statement.VariableAssignmentStatement;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.VoidValue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.*;

class FunctionInvokeExpressionTest {

    static CompileGlobalScope scope = new CompileGlobalScope();

    static PMLStmtsOperation voidFunc = new PMLStmtsOperation("voidFunc", Type.voidType(),
            List.of("a", "b"),
            List.of(),
            Map.of("a", Type.string(), "b", Type.string()),
            new PMLStmtsOperationBody(
            new PMLStatementBlock(),
            new PMLStatementBlock(List.of(
                            new CreatePolicyStatement(new ReferenceByID("a")),
                            new CreatePolicyStatement(new ReferenceByID("b"))
            ))));
    static PMLStmtsOperation stringFunc = new PMLStmtsOperation("stringFunc",
            Type.string(),
            List.of("a", "b"),
            List.of(),
            Map.of(
                    "a", Type.string(),
                    "b", Type.string()
            ),
            new PMLStmtsOperationBody(
            new PMLStatementBlock(),
            new PMLStatementBlock(List.of(
                    new VariableAssignmentStatement("x", false, new StringLiteral("test")),
                    new FunctionReturnStatement(new StringLiteral("test_ret"))
            ))));

    static {
        scope.addExecutable(voidFunc.getName(), voidFunc.getSignature());
        scope.addExecutable(stringFunc.getName(), stringFunc.getSignature());
    }

    @Test
    void testVoidReturnType() throws PMException {
        PMLParser.FunctionInvokeExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                voidFunc("a", "b")
                """, PMLParser.FunctionInvokeExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext(scope);

        Expression e = FunctionInvokeExpression.compileFunctionInvokeExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().getErrors().toString());
        assertEquals(
                new FunctionInvokeExpression(voidFunc.getSignature(), Map.of(
                        "a", new StringLiteral("a"),
                        "b", new StringLiteral("b")
                )),
                e
        );
        assertEquals(
                Type.voidType(),
                e.getType(visitorContext.scope())
        );

        PAP pap = new MemoryPAP();
        pap.modify().operations().createAdminOperation(voidFunc);
        ExecutionContext executionContext = new ExecutionContext(new UserContext(0), pap);
        Value value = e.execute(executionContext, new MemoryPAP());
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
        VisitorContext visitorCtx = new VisitorContext(new CompileGlobalScope());

        testCompilationError(
                """
                voidFunc("a", "b")
                """, visitorCtx, 1,
                "unknown function 'voidFunc' in scope"
        );
    }

    @Test
    void testWrongNumberOfArgs() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(scope);

        testCompilationError(
                """
                voidFunc("a")
                """, visitorCtx, 1,
                "wrong number of args for function call voidFunc: expected 2, got 1"
        );
    }

    @Test
    void testWrongArgType() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(scope);

        testCompilationError(
                """
                voidFunc("a", ["b", "c"])
                """, visitorCtx, 1,
                "invalid argument type: expected string, got []string at arg 1"
        );
    }

    @Test
    void testExecuteReturnValue() throws PMException {
        PMLParser.FunctionInvokeExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                stringFunc("a", "b")
                """, PMLParser.FunctionInvokeExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext(scope);

        Expression e = FunctionInvokeExpression.compileFunctionInvokeExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().getErrors().toString());
        assertEquals(
                Type.string(),
                e.getType(visitorContext.scope())
        );
    }

    @Test
    void testExecuteWithFunctionExecutor() throws PMException {
        PMLParser.FunctionInvokeExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                stringFunc("a", "b")
                """, PMLParser.FunctionInvokeExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext(scope);
        Expression e = FunctionInvokeExpression.compileFunctionInvokeExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().getErrors().toString());

        PAP pap = new MemoryPAP();
        pap.modify().operations().createAdminOperation(stringFunc);
        ExecutionContext executionContext =
                new ExecutionContext(
                        new UserContext(0),
                        pap
                );
        Value value = e.execute(executionContext, pap);
        assertEquals(
                new StringValue("test_ret"),
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
                
                operation c(string x) string {
                    return "c" + x
                }
                                
                operation b(string x, string y) {
                    create policy class c(x)
                    create policy class c(y)
                }
                                
                operation a(string x) {
                    x = "x"
                    y := "y"
                                
                    b(x, y)
                }
                """;
        PAP pap = new MemoryPAP();
        pap.executePML(new UserContext(0), pml);
        assertTrue(pap.query().graph().nodeExists("cx"));
        assertTrue(pap.query().graph().nodeExists("cy"));
    }

    @Test
    void testReassignArgValueInFunctionDoesNotUpdateVariableOutsideOfScope() throws PMException {
        String pml = """
                x := "test"
                a(x)
                create pc x
                operation a(string x) {
                    x = "x"
                }
                """;
        PAP pap = new MemoryPAP();
        pap.executePML(new UserContext(0), pml);
        assertFalse(pap.query().graph().nodeExists("x"));
        assertTrue(pap.query().graph().nodeExists("test"));
    }

    @Test
    void testReturnInIf() throws PMException {
        String pml = """            
                operation a() {
                    if true {
                        return
                    }
                    
                    create pc "pc1"                               
                }
                
                a()
                """;
        PAP pap = new MemoryPAP();
        pap.executePML(new UserContext(0), pml);
        assertFalse(pap.query().graph().nodeExists("pc1"));
    }

    @Test
    void testScopeIsNotCopiedToFunctionInvokeExpression() throws PMException {
        String pml = """
                operation op1() {
                    x := ""
                    op2()
                }
                
                operation op2() {
                    x := ""
                }
                
                op1()
                """;
        assertDoesNotThrow(() -> new MemoryPAP().executePML(new UserContext(0), pml));
    }
}