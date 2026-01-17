package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.core.pap.pml.PMLUtil.buildMapLiteral;
import static gov.nist.csd.pm.core.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.pml.scope.Scope;
import gov.nist.csd.pm.core.pap.pml.scope.VariableAlreadyDefinedInScopeException;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.basic.ForeachStatement;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ForeachStmtVisitorTest {

    private static CompileScope testScope;

    @BeforeAll
    static void setup() throws PMException {
        testScope = new CompileScope(new MemoryPAP());
    }

    @Test
    void testSuccess() {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                foreach x in ["a", "b"] {}
                """);
        VisitorContext visitorCtx = new VisitorContext(testScope);
        PMLStatement<?> stmt = new ForeachStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new ForeachStatement("x", null, buildArrayLiteral("a", "b"), List.of()).toString(),
                stmt.toString()
        );

        ctx = TestPMLParser.parseStatement(
                """
                foreach x, y in {"a": "b"} {}
                """);
        visitorCtx = new VisitorContext(testScope);
        stmt = new ForeachStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new ForeachStatement("x", "y", buildMapLiteral("a", "b"), List.of()).toString(),
                stmt.toString()
        );
    }

    @Test
    void testInvalidExpressions() {
        VisitorContext visitorCtx = new VisitorContext(testScope);

        testCompilationError(
                """
                foreach x in "a" {}
                """, visitorCtx, 1,
                "expected expression type []any, got string"

        );

        testCompilationError(
                """
                foreach x in {"a": "b"} {}
                """, visitorCtx, 1,
                "expected expression type []any, got map[string]string"

        );
    }

    @Test
    void testKeyValueOnArray() {
        VisitorContext visitorCtx = new VisitorContext(testScope);

        testCompilationError(
                """
                foreach x, y in ["a"] {}
                """, visitorCtx, 1,
                "expected expression type map[any]any, got []string"

        );
    }

    @Test
    void testIterVarDoesNotExists() throws VariableAlreadyDefinedInScopeException {
        VisitorContext visitorCtx = new VisitorContext(testScope);

        testCompilationError(
                """
                foreach x in arr {}
                """, visitorCtx, 1,
                "unknown variable 'arr' in scope"

        );
    }

    @Test
    void testKeyValueVarsAlreadyExist() throws VariableAlreadyDefinedInScopeException {
        VisitorContext visitorCtx = new VisitorContext(testScope.copy());
        visitorCtx.scope().addVariable("x", new Variable("x", STRING_TYPE, false));

        testCompilationError(
                """
                foreach x in ["a"] {}
                """, visitorCtx, 1,
                "variable 'x' already defined in scope"

        );

        visitorCtx = new VisitorContext(testScope.copy());
        visitorCtx.scope().addVariable("y", new Variable("y", STRING_TYPE, false));

        testCompilationError(
                """
                foreach x, y in {"a": "b"} {}
                """, visitorCtx, 1,
                "variable 'y' already defined in scope"

        );
    }

    @Test
    void testKeyOnlyOnMapReturnsError() throws VariableAlreadyDefinedInScopeException {
        VisitorContext visitorCtx = new VisitorContext(testScope);

        testCompilationError(
                """
                foreach x in {"a": "b"} {}
                """, visitorCtx, 1,
                "expected expression type []any, got map[string]string"

        );
    }

    @Test
    void testKeyValueOnArrayReturnsError() throws VariableAlreadyDefinedInScopeException {
        VisitorContext visitorCtx = new VisitorContext(testScope);

        testCompilationError(
                """
                foreach x, y in ["a", "b"] {}
                """, visitorCtx, 1,
                "expected expression type map[any]any, got []string"

        );
    }

}