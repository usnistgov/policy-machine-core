/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.pml.compiler.visitor;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.ngac.pm.core.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.ngac.pm.core.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.pml.TestPMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.expression.OperationInvokeExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import gov.nist.ngac.pm.core.pap.pml.scope.CompileScope;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatement;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class OperationInvokeStmtVisitorTest {

    private static final FormalParameter<String> a = new FormalParameter<>("a", STRING_TYPE);
    private static final FormalParameter<String> b = new FormalParameter<>("b", STRING_TYPE);
    private static final FormalParameter<List<String>> c = new FormalParameter<>("c", ListType.of(STRING_TYPE));

    PMLOperationSignature signature = new PMLOperationSignature(
        OperationType.FUNCTION,
        "func1",
        STRING_TYPE,
        List.of(a, b, c),
        List.of());

    OperationInvokeExpression<String> expected = new OperationInvokeExpression(
        signature.getName(),
        Map.of(
            "a", new StringLiteralExpression("a"),
            "b", new StringLiteralExpression("b"),
            "c", buildArrayLiteral("c", "d")
        ),
        STRING_TYPE
    );

    @Test
    void testSuccess() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
            """
            func1(a="a", b="b", c=["c", "d"])
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
            func1(a="a", b="b")
            """, visitorCtx, 1,
            "required formal parameters: [a, b, c], got: [a, b]"
        );
    }

    @Test
    void testWrongArgType() throws PMException {
        CompileScope compileScope = new CompileScope(new MemoryPAP());
        compileScope.addOperation("func1", signature);
        VisitorContext visitorCtx = new VisitorContext(compileScope);
        testCompilationError(
            """
            func1(a="a", b="b", c=true)
            """, visitorCtx, 1,
            "expected expression type []string, got bool"
        );
    }

    @Test
    void testOptionalParamCanBeOmitted() throws PMException {
        FormalParameter<String> required = new FormalParameter<>("a", STRING_TYPE, true);
        FormalParameter<String> optional = new FormalParameter<>("b", STRING_TYPE, false);
        PMLOperationSignature sig = new PMLOperationSignature(
            OperationType.FUNCTION, "func2", STRING_TYPE,
            List.of(required, optional),
            List.of());

        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            func2(a="a")
            """);

        CompileScope compileScope = new CompileScope(new MemoryPAP());
        compileScope.addOperation("func2", sig);
        VisitorContext visitorCtx = new VisitorContext(compileScope);

        PMLStatement<?> stmt = new OperationInvokeStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
            new OperationInvokeExpression<>("func2",
                Map.of("a", new StringLiteralExpression("a")),
                STRING_TYPE),
            stmt
        );
    }

    @Test
    void testOptionalParamCanBeProvided() throws PMException {
        FormalParameter<String> required = new FormalParameter<>("a", STRING_TYPE, true);
        FormalParameter<String> optional = new FormalParameter<>("b", STRING_TYPE, false);
        PMLOperationSignature sig = new PMLOperationSignature(
            OperationType.FUNCTION, "func2", STRING_TYPE,
            List.of(required, optional),
            List.of());

        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            func2(a="a", b="b")
            """);

        CompileScope compileScope = new CompileScope(new MemoryPAP());
        compileScope.addOperation("func2", sig);
        VisitorContext visitorCtx = new VisitorContext(compileScope);

        PMLStatement<?> stmt = new OperationInvokeStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
            new OperationInvokeExpression<>("func2",
                Map.of(
                    "a", new StringLiteralExpression("a"),
                    "b", new StringLiteralExpression("b")),
                STRING_TYPE),
            stmt
        );
    }

    @Test
    void testRequiredParamMissingWhenOptionalPresent() throws PMException {
        FormalParameter<String> required = new FormalParameter<>("a", STRING_TYPE, true);
        FormalParameter<String> optional = new FormalParameter<>("b", STRING_TYPE, false);
        PMLOperationSignature sig = new PMLOperationSignature(
            OperationType.FUNCTION, "func2", STRING_TYPE,
            List.of(required, optional),
            List.of());

        CompileScope compileScope = new CompileScope(new MemoryPAP());
        compileScope.addOperation("func2", sig);
        VisitorContext visitorCtx = new VisitorContext(compileScope);

        testCompilationError("""
            func2(b="b")
            """, visitorCtx, 1,
            "required formal parameters: [a], got: [b]"
        );
    }

    @Test
    void testUnknownParamName() throws PMException {
        CompileScope compileScope = new CompileScope(new MemoryPAP());
        compileScope.addOperation("func1", signature);
        VisitorContext visitorCtx = new VisitorContext(compileScope);

        testCompilationError(
            """
            func1(a="a", b="b", c=["c", "d"], unknown="x")
            """, visitorCtx, 1,
            "unknown parameter 'unknown' for operation 'func1'"
        );
    }

    @Test
    void testDuplicateArgName() throws PMException {
        CompileScope compileScope = new CompileScope(new MemoryPAP());
        compileScope.addOperation("func1", signature);
        VisitorContext visitorCtx = new VisitorContext(compileScope);

        testCompilationError(
            """
            func1(a="first", a="second", b="b", c=["c", "d"])
            """, visitorCtx, 1,
            "duplicate argument 'a' for operation 'func1'"
        );
    }

    @Test
    void testMultipleArgTypeErrors() throws PMException {
        CompileScope compileScope = new CompileScope(new MemoryPAP());
        compileScope.addOperation("func1", signature);
        VisitorContext visitorCtx = new VisitorContext(compileScope);

        testCompilationError(
            """
            func1(a=true, b=true, c=["c", "d"])
            """, visitorCtx, 2,
            "expected expression type string, got bool",
            "expected expression type string, got bool"
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
            List.of(),
            List.of());

        CompileScope compileScope = new CompileScope(new MemoryPAP());
        compileScope.addOperation("func1", signature);

        VisitorContext visitorCtx = new VisitorContext(compileScope);
        PMLStatement<?> stmt = new OperationInvokeStmtVisitor(visitorCtx)
            .visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        OperationInvokeExpression<?> expected = new OperationInvokeExpression(
            signature.getName(),
            Map.of(),
            signature.getReturnType()
        );

        assertEquals(expected, stmt);
    }

}