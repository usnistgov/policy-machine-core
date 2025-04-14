package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.FunctionInvokeExpression;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunctionSignature;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;

import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.listType;
import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionInvokeStmtVisitorTest {

    private static final FormalParameter<String> a = new FormalParameter<>("a", STRING_TYPE);
    private static final FormalParameter<String> b = new FormalParameter<>("b", STRING_TYPE);
    private static final FormalParameter<List<String>> c = new FormalParameter<>("c", listType(STRING_TYPE));

    PMLFunctionSignature signature = new PMLBasicFunctionSignature(
        "func1",
        STRING_TYPE,
        List.of(a, b, c)
    );

    FunctionInvokeExpression expected = new FunctionInvokeExpression(
        signature,
        List.of(
            new StringLiteralExpression("a"),
            new StringLiteralExpression("b"),
            buildArrayLiteral("c", "d")
        ),
        STRING_TYPE
    );

    @Test
    void testSuccess() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
            """
            func1("a", "b", ["c", "d"])
            """);

        CompileScope compileScope = new CompileScope();
        compileScope.addFunction("func1", signature);

        VisitorContext visitorCtx = new VisitorContext(compileScope);

        PMLStatement stmt = new FunctionInvokeStmtVisitor(visitorCtx)
            .visit(ctx);
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
            "expected expression type []string, got bool"
        );
    }

    @Test
    void testNoArgs() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
            """
            func1()
            """);

        PMLFunctionSignature signature = new PMLBasicFunctionSignature(
            "func1",
            STRING_TYPE,
            List.of()
        );

        CompileScope compileScope = new CompileScope();
        compileScope.addFunction("func1", signature);

        VisitorContext visitorCtx = new VisitorContext(compileScope);
        PMLStatement stmt = new FunctionInvokeStmtVisitor(visitorCtx)
            .visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        FunctionInvokeExpression expected = new FunctionInvokeExpression(
            signature,
            List.of(),
            signature.getReturnType()
        );

        assertEquals(expected, stmt);
    }

}