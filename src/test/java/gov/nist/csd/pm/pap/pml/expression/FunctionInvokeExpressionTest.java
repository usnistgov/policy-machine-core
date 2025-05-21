package gov.nist.csd.pm.pap.pml.expression;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.type.VoidType;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.pap.pml.expression.reference.VariableReferenceExpression;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.function.operation.PMLStmtsOperation;
import gov.nist.csd.pm.pap.pml.function.operation.CheckAndStatementsBlock;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.scope.FunctionAlreadyDefinedInScopeException;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.statement.basic.ReturnStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.statement.basic.VariableAssignmentStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.CreatePolicyClassStatement;


import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.TestPAP;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.function.arg.type.Type.ANY_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.STRING_TYPE;
import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.*;

class FunctionInvokeExpressionTest {

    private static final FormalParameter<String> a = new FormalParameter<>("a", STRING_TYPE);
    private static final FormalParameter<String> b = new FormalParameter<>("b", STRING_TYPE);

	static PMLStmtsOperation voidFunc = new PMLStmtsOperation("voidFunc", new VoidType(),
            List.of(a, b),
            new CheckAndStatementsBlock(
            new PMLStatementBlock(),
            new PMLStatementBlock(List.of(
                            new CreatePolicyClassStatement(new VariableReferenceExpression<>("a", STRING_TYPE)),
                            new CreatePolicyClassStatement(new VariableReferenceExpression<>("b", STRING_TYPE))
            ))));
    static PMLStmtsOperation stringFunc = new PMLStmtsOperation("stringFunc",
            STRING_TYPE,
            List.of(a, b),
            new CheckAndStatementsBlock(
            new PMLStatementBlock(),
            new PMLStatementBlock(List.of(
                    new VariableAssignmentStatement("x", false, new StringLiteralExpression("test")),
                    new ReturnStatement(new StringLiteralExpression("test_ret"))
            ))));

    private Scope<Variable, PMLFunctionSignature> testScope() throws
                                                              FunctionAlreadyDefinedInScopeException {
        Scope<Variable, PMLFunctionSignature> scope = new Scope<>();

        scope.addFunction(voidFunc.getName(), voidFunc.getSignature());
        scope.addFunction(stringFunc.getName(), stringFunc.getSignature());

        return scope;
    }

    @Test
    void testVoidReturnType() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                voidFunc("a", "b")
                """);


        VisitorContext visitorContext = new VisitorContext(testScope());

        Expression e = ExpressionVisitor.compile(visitorContext, ctx, ANY_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().getErrors().toString());
        assertEquals(
                new FunctionInvokeExpression<>(voidFunc.getSignature(), List.of(
                        new StringLiteralExpression("a"),
                        new StringLiteralExpression("b")
                ), new VoidType()),
                e
        );
        assertEquals(
                new VoidType(),
                e.getType()
        );

        PAP pap = new TestPAP();
        pap.modify().operations().createAdminOperation(voidFunc);
        ExecutionContext executionContext = new ExecutionContext(new UserContext(0), pap);
        Object value = e.execute(executionContext, new MemoryPAP());
        assertNull(value);
    }

    @Test
    void testFunctionNotInScope() throws FunctionAlreadyDefinedInScopeException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());

        testCompilationError(
                """
                voidFunc("a", "b")
                """, visitorCtx, 1,
                "unknown function 'voidFunc' in scope"
        );
    }

    @Test
    void testWrongNumberOfArgs() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(testScope());

        testCompilationError(
                """
                voidFunc("a")
                """, visitorCtx, 1,
                "wrong number of args for function call voidFunc: expected 2, got 1"
        );
    }

    @Test
    void testWrongArgType() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(testScope());

        testCompilationError(
                """
                voidFunc("a", ["b", "c"])
                """, visitorCtx, 1,
                "expected expression type string, got []string"
        );
    }

    @Test
    void testExecuteReturnValue() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                stringFunc("a", "b")
                """);
        VisitorContext visitorContext = new VisitorContext(testScope());

        Expression e = ExpressionVisitor.compile(visitorContext, ctx, ANY_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().getErrors().toString());
        assertEquals(
                STRING_TYPE,
                e.getType()
        );
    }

    @Test
    void testExecuteWithFunctionExecutor() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                stringFunc("a", "b")
                """);
        VisitorContext visitorContext = new VisitorContext(testScope());
        Expression e = ExpressionVisitor.compile(visitorContext, ctx, ANY_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().getErrors().toString());

        PAP pap = new TestPAP();
        pap.modify().operations().createAdminOperation(stringFunc);
        ExecutionContext executionContext =
                new ExecutionContext(
                        new UserContext(0),
                        pap
                );
        Object value = e.execute(executionContext, pap);
        assertEquals(
                "test_ret",
                value
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
        PAP pap = new TestPAP();
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
        PAP pap = new TestPAP();
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
        PAP pap = new TestPAP();
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