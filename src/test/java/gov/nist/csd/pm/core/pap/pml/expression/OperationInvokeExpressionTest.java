package gov.nist.csd.pm.core.pap.pml.expression;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.type.VoidType;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.reference.VariableReferenceExpression;
import gov.nist.csd.pm.core.pap.pml.operation.admin.PMLStmtsAdminOperation;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.basic.ReturnStatement;
import gov.nist.csd.pm.core.pap.pml.statement.basic.VariableAssignmentStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreatePolicyClassStatement;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.Test;

class OperationInvokeExpressionTest {

    private static final FormalParameter<String> a = new FormalParameter<>("a", STRING_TYPE);
    private static final FormalParameter<String> b = new FormalParameter<>("b", STRING_TYPE);

	static PMLStmtsAdminOperation voidFunc = new PMLStmtsAdminOperation("voidFunc", new VoidType(),
            List.of(a, b),
            new PMLStatementBlock(List.of(
                            new CreatePolicyClassStatement(new VariableReferenceExpression<>("a", STRING_TYPE)),
                            new CreatePolicyClassStatement(new VariableReferenceExpression<>("b", STRING_TYPE))
            )));
    static PMLStmtsAdminOperation stringFunc = new PMLStmtsAdminOperation("stringFunc",
            STRING_TYPE,
            List.of(a, b),
            new PMLStatementBlock(List.of(
                    new VariableAssignmentStatement("x", false, new StringLiteralExpression("test")),
                    new ReturnStatement(new StringLiteralExpression("test_ret"))
            )));

    private CompileScope testScope() throws PMException {
        CompileScope scope = new CompileScope(new MemoryPAP());

        scope.addOperation(voidFunc.getName(), voidFunc.getSignature());
        scope.addOperation(stringFunc.getName(), stringFunc.getSignature());

        return scope;
    }

    @Test
    void testVoidReturnType() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                voidFunc("a", "b")
                """);


        VisitorContext visitorContext = new VisitorContext(testScope());

        Expression<?> e = ExpressionVisitor.compile(visitorContext, ctx, ANY_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().getErrors().toString());
        assertEquals(
                new OperationInvokeExpression<>(
                    voidFunc.getSignature().getName(),
                    List.of(
                        new StringLiteralExpression("a"),
                        new StringLiteralExpression("b")
                    ),
                    new VoidType()
                ),
                e
        );
        assertEquals(
                new VoidType(),
                e.getType()
        );

        PAP pap = new TestPAP();
        pap.modify().operations().createOperation(voidFunc);
        ExecutionContext executionContext = new ExecutionContext(new UserContext(0), pap);
        Object value = e.execute(executionContext, new MemoryPAP());
        assertNull(value);
    }

    @Test
    void testOperationNotInScope() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));

        testCompilationError(
                """
                voidFunc("a", "b")
                """, visitorCtx, 1,
                "unknown operation 'voidFunc' in scope"
        );
    }

    @Test
    void testWrongNumberOfArgs() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(testScope());

        testCompilationError(
                """
                voidFunc("a")
                """, visitorCtx, 1,
                "wrong number of args for operation call voidFunc: expected 2, got 1"
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
    void testExecuteWithOperationExecutor() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                stringFunc("a", "b")
                """);
        VisitorContext visitorContext = new VisitorContext(testScope());
        Expression e = ExpressionVisitor.compile(visitorContext, ctx, ANY_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().getErrors().toString());

        PAP pap = new TestPAP();
        pap.modify().operations().createOperation(stringFunc);
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
                
                adminop c(string x) string {
                    return "c" + x
                }
                                
                adminop b(string x, string y) {
                    create PC c(x)
                    create PC c(y)
                }
                                
                adminop a(string x) {
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
    void testReassignArgValueInOperationDoesNotUpdateVariableOutsideOfScope() throws PMException {
        String pml = """
                x := "test"
                a(x)
                create pc x
                adminop a(string x) {
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
                adminop a() {
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
    void testScopeIsNotCopiedToOperationInvokeExpression() throws PMException {
        String pml = """
                adminop op1() {
                    x := ""
                    op2()
                }
                
                adminop op2() {
                    x := ""
                }
                
                op1()
                """;
        assertDoesNotThrow(() -> new MemoryPAP().executePML(new UserContext(0), pml));
    }
}