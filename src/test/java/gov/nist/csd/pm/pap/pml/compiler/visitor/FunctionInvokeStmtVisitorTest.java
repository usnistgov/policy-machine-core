package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.function.basic.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.expression.FunctionInvokeExpression;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionInvokeStmtVisitorTest {

    private static final PMLFormalArg a = new PMLFormalArg("a", Type.string());
    private static final PMLFormalArg b = new PMLFormalArg("b", Type.string());
    private static final PMLFormalArg c = new PMLFormalArg("c", Type.array(Type.string()));

    PMLFunctionSignature signature = new PMLFunctionSignature(
            "func1",
            Type.string(),
            List.of(a, b, c)
    );

    FunctionInvokeExpression expected = new FunctionInvokeExpression(
            signature.getName(),
            List.of(
                    new StringLiteral("a"),
                    new StringLiteral("b"),
                    buildArrayLiteral("c", "d")
            )
    );

    @Test
    void testSuccess() throws PMException {
        PMLParser.FunctionInvokeStatementContext ctx = PMLContextVisitor.toCtx(
                """
                func1("a", "b", ["c", "d"])
                """,
                PMLParser.FunctionInvokeStatementContext.class);



        CompileScope compileScope = new CompileScope();
        compileScope.addFunction("func1", signature);

        VisitorContext visitorCtx = new VisitorContext(compileScope);

        PMLStatement stmt = new FunctionInvokeStmtVisitor(visitorCtx)
                .visitFunctionInvokeStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        assertEquals(expected, stmt);
    }

    @Test
    void testFunctionDoesNotExist() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());

        testCompilationError(
                """
                func1("a", "b", ["c", "d"])
                """, visitorCtx, 1,
                "unknown function 'func1' in scope"
        );
    }

    @Test
    void testWrongNumberOfArgs() throws PMException {
        CompileScope compileScope = new CompileScope();
        compileScope.addFunction("func1", signature);

        VisitorContext visitorCtx = new VisitorContext(compileScope);

        testCompilationError(
                """
                func1("a", "b")
                """, visitorCtx, 1,
                "wrong number of args for function call func1: expected 3, got 2"
        );
    }

    @Test
    void testWrongArgType() throws PMException {
        CompileScope compileScope = new CompileScope();
        compileScope.addFunction("func1", signature);
        VisitorContext visitorCtx = new VisitorContext(compileScope);

        testCompilationError(
                """
                func1("a", "b", true)
                """, visitorCtx, 1,
                "invalid argument type: expected []string, got bool at arg 2"
        );
    }

    @Test
    void testNoArgs() throws PMException {
        PMLParser.FunctionInvokeStatementContext ctx = PMLContextVisitor.toCtx(
                """
                func1()
                """,
                PMLParser.FunctionInvokeStatementContext.class);

        PMLFunctionSignature signature = new PMLFunctionSignature(
                "func1",
                Type.string(),
                List.of()
        );

        CompileScope compileScope = new CompileScope();
        compileScope.addFunction("func1", signature);

        VisitorContext visitorCtx = new VisitorContext(compileScope);
        PMLStatement stmt = new FunctionInvokeStmtVisitor(visitorCtx)
                .visitFunctionInvokeStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        FunctionInvokeExpression expected = new FunctionInvokeExpression(
                signature.getName(),
                List.of()
        );

        assertEquals(expected, stmt);
    }

}