package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.executable.operation.builtin.Equals;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.scope.VariableAlreadyDefinedInScopeException;
import gov.nist.csd.pm.pap.pml.statement.basic.ForeachStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.pap.pml.PMLUtil.buildMapLiteral;
import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ForeachStmtVisitorTest {

    private static Scope<Variable, PMLExecutableSignature> testScope;

    @BeforeAll
    static void setup() throws PMException {
        testScope = new CompileScope();
    }

    @Test
    void testSuccess() {
        PMLParser.ForeachStatementContext ctx = PMLContextVisitor.toCtx(
                """
                foreach x in ["a", "b"] {}
                """,
                PMLParser.ForeachStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(testScope);
        PMLStatement stmt = new ForeachStmtVisitor(visitorCtx).visitForeachStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new ForeachStatement("x", null, buildArrayLiteral("a", "b"), List.of()),
                stmt
        );

        ctx = PMLContextVisitor.toCtx(
                """
                foreach x, y in {"a": "b"} {}
                """,
                PMLParser.ForeachStatementContext.class);
        visitorCtx = new VisitorContext(testScope);
        stmt = new ForeachStmtVisitor(visitorCtx).visitForeachStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new ForeachStatement("x", "y", buildMapLiteral("a", "b"), List.of()),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() {
        VisitorContext visitorCtx = new VisitorContext(testScope);

        testCompilationError(
                """
                foreach x in "a" {}
                """, visitorCtx, 1,
                "expected expression type(s) [[]any], got string"

        );

        testCompilationError(
                """
                foreach x in {"a": "b"} {}
                """, visitorCtx, 1,
                "expected expression type(s) [[]any], got map[string]string"

        );
    }

    @Test
    void testKeyValueOnArray() {
        VisitorContext visitorCtx = new VisitorContext(testScope);

        testCompilationError(
                """
                foreach x, y in ["a"] {}
                """, visitorCtx, 1,
                "expected expression type(s) [map[any]any], got []string"

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
        visitorCtx.scope().addVariable("x", new Variable("x", Type.string(), false));

        testCompilationError(
                """
                foreach x in ["a"] {}
                """, visitorCtx, 1,
                "variable 'x' already defined in scope"

        );

        visitorCtx = new VisitorContext(testScope.copy());
        visitorCtx.scope().addVariable("y", new Variable("y", Type.string(), false));

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
                "expected expression type(s) [[]any], got map[string]string"

        );
    }

    @Test
    void testKeyValueOnArrayReturnsError() throws VariableAlreadyDefinedInScopeException {
        VisitorContext visitorCtx = new VisitorContext(testScope);

        testCompilationError(
                """
                foreach x, y in ["a": "b"] {}
                """, visitorCtx, 1,
                "expected expression type(s) [map[any]any], got []string"

        );
    }

}