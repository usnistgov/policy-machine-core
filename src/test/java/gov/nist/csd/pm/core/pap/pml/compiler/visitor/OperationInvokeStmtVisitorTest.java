package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.core.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.OperationInvokeExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import java.util.List;
import org.junit.jupiter.api.Test;

class OperationInvokeStmtVisitorTest {

    private static final FormalParameter<String> a = new FormalParameter<>("a", STRING_TYPE);
    private static final FormalParameter<String> b = new FormalParameter<>("b", STRING_TYPE);
    private static final FormalParameter<List<String>> c = new FormalParameter<>("c", ListType.of(STRING_TYPE));

    PMLOperationSignature signature = new PMLOperationSignature(
        OperationType.FUNCTION,
        "func1",
        STRING_TYPE,
        List.of(a, b, c)
    );

    OperationInvokeExpression<String> expected = new OperationInvokeExpression(
        signature.getName(),
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

        CompileScope compileScope = new CompileScope(new MemoryPAP());
        compileScope.addOperation("func1", signature);

        VisitorContext visitorCtx = new VisitorContext(compileScope);

        PMLStatement stmt = new OperationInvokeStmtVisitor(visitorCtx)
            .visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        assertEquals(expected, stmt);
    }

    @Test
    void testOperationDoesNotExist() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));

        testCompilationError(
            """
            func1("a", "b", ["c", "d"])
            """, visitorCtx, 1,
            "unknown operation 'func1' in scope"
        );
    }

    @Test
    void testWrongNumberOfArgs() throws PMException {
        CompileScope compileScope = new CompileScope(new MemoryPAP());
        compileScope.addOperation("func1", signature);

        VisitorContext visitorCtx = new VisitorContext(compileScope);

        testCompilationError(
            """
            func1("a", "b")
            """, visitorCtx, 1,
            "wrong number of args for operation call func1: expected 3, got 2"
        );
    }

    @Test
    void testWrongArgType() throws PMException {
        CompileScope compileScope = new CompileScope(new MemoryPAP());
        compileScope.addOperation("func1", signature);
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

        PMLOperationSignature signature = new PMLOperationSignature(
            OperationType.FUNCTION,
            "func1",
            STRING_TYPE,
            List.of()
        );

        CompileScope compileScope = new CompileScope(new MemoryPAP());
        compileScope.addOperation("func1", signature);

        VisitorContext visitorCtx = new VisitorContext(compileScope);
        PMLStatement<?> stmt = new OperationInvokeStmtVisitor(visitorCtx)
            .visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        OperationInvokeExpression<?> expected = new OperationInvokeExpression(
            signature.getName(),
            List.of(),
            signature.getReturnType()
        );

        assertEquals(expected, stmt);
    }

}