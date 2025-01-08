package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.expression.FunctionInvokeExpression;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.scope.CompileGlobalScope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionInvokeStmtVisitorTest {

    PMLExecutableSignature signature = new PMLExecutableSignature(
            "func1",
            Type.string(),
            List.of("a", "b", "c"),
            Map.of(
                    "a", Type.string(),
                    "b", Type.string(),
                    "c", Type.array(Type.string())
            )
    );

    FunctionInvokeExpression expected = new FunctionInvokeExpression(
            signature,
            Map.of(
                    "a", new StringLiteral("a"),
                    "b", new StringLiteral("b"),
                    "c", buildArrayLiteral("c", "d")
            )
    );

    @Test
    void testSuccess() throws PMException {
        PMLParser.FunctionInvokeStatementContext ctx = PMLContextVisitor.toCtx(
                """
                func1("a", "b", ["c", "d"])
                """,
                PMLParser.FunctionInvokeStatementContext.class);



        CompileGlobalScope compileGlobalScope = new CompileGlobalScope();
        compileGlobalScope.addExecutable("func1", signature);

        VisitorContext visitorCtx = new VisitorContext(compileGlobalScope);

        PMLStatement stmt = new FunctionInvokeStmtVisitor(visitorCtx)
                .visitFunctionInvokeStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        assertEquals(expected, stmt);
    }

    @Test
    void testFunctionDoesNotExist() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileGlobalScope());

        testCompilationError(
                """
                func1("a", "b", ["c", "d"])
                """, visitorCtx, 1,
                "unknown function 'func1' in scope"
        );
    }

    @Test
    void testWrongNumberOfArgs() throws PMException {
        CompileGlobalScope compileGlobalScope = new CompileGlobalScope();
        compileGlobalScope.addExecutable("func1", signature);

        VisitorContext visitorCtx = new VisitorContext(compileGlobalScope);

        testCompilationError(
                """
                func1("a", "b")
                """, visitorCtx, 1,
                "wrong number of args for function call func1: expected 3, got 2"
        );
    }

    @Test
    void testWrongArgType() throws PMException {
        CompileGlobalScope compileGlobalScope = new CompileGlobalScope();
        compileGlobalScope.addExecutable("func1", signature);
        VisitorContext visitorCtx = new VisitorContext(compileGlobalScope);

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

        PMLExecutableSignature signature = new PMLExecutableSignature(
                "func1",
                Type.string(),
                List.of(),
                Map.of()
        );

        CompileGlobalScope compileGlobalScope = new CompileGlobalScope();
        compileGlobalScope.addExecutable("func1", signature);

        VisitorContext visitorCtx = new VisitorContext(compileGlobalScope);
        PMLStatement stmt = new FunctionInvokeStmtVisitor(visitorCtx)
                .visitFunctionInvokeStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        FunctionInvokeExpression expected = new FunctionInvokeExpression(
                signature,
                Map.of()
        );

        assertEquals(expected, stmt);
    }

}