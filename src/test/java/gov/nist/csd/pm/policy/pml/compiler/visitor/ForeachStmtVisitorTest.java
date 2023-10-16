package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.function.FunctionSignature;
import gov.nist.csd.pm.policy.pml.function.builtin.Equals;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.scope.VariableAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.statement.ForeachStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.policy.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.*;

class ForeachStmtVisitorTest {

    private static GlobalScope<Variable, FunctionSignature> testGlobalScope;

    @BeforeAll
    static void setup() throws PMException {
        testGlobalScope = GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore())
                                     .withPersistedFunctions(Map.of("equals", new Equals().getSignature()));
    }

    @Test
    void testSuccess() {
        PMLParser.ForeachStatementContext ctx = PMLContextVisitor.toCtx(
                "foreach x in [\"a\", \"b\"] {}",
                PMLParser.ForeachStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
        PMLStatement stmt = new ForeachStmtVisitor(visitorCtx).visitForeachStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new ForeachStatement("x", null, buildArrayLiteral("a", "b"), List.of()),
                stmt
        );

        ctx = PMLContextVisitor.toCtx(
                "foreach x, y in {\"a\": \"b\"} {}",
                PMLParser.ForeachStatementContext.class);
        visitorCtx = new VisitorContext(testGlobalScope);
        stmt = new ForeachStmtVisitor(visitorCtx).visitForeachStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new ForeachStatement("x", "y", buildMapLiteral("a", "b"), List.of()),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() {
        PMLParser.ForeachStatementContext ctx = PMLContextVisitor.toCtx(
                "foreach x in \"a\" {}",
                PMLParser.ForeachStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
        new ForeachStmtVisitor(visitorCtx).visitForeachStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type []any, got string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                "foreach x in {\"a\": \"b\"} {}",
                PMLParser.ForeachStatementContext.class);
        visitorCtx = new VisitorContext(testGlobalScope);
        new ForeachStmtVisitor(visitorCtx).visitForeachStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type []any, got map[string]string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testKeyValueOnArray() {
        PMLParser.ForeachStatementContext ctx = PMLContextVisitor.toCtx(
                "foreach x, y in [\"a\"] {}",
                PMLParser.ForeachStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
        new ForeachStmtVisitor(visitorCtx).visitForeachStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type map[any]any, got []string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testIterVarDoesNotExists() throws VariableAlreadyDefinedInScopeException {
        PMLParser.ForeachStatementContext ctx = PMLContextVisitor.toCtx(
                "foreach x in arr {}",
                PMLParser.ForeachStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
        new ForeachStmtVisitor(visitorCtx).visitForeachStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "unknown variable 'arr' in scope",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testKeyValueVarsAlreadyExist() throws VariableAlreadyDefinedInScopeException {
        PMLParser.ForeachStatementContext ctx = PMLContextVisitor.toCtx(
                "foreach x in [\"a\"] {}",
                PMLParser.ForeachStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
        visitorCtx.scope().addVariable("x", new Variable("x", Type.string(), false));
        new ForeachStmtVisitor(visitorCtx).visitForeachStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "variable 'x' already defined in scope",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                "foreach x, y in {\"a\": \"b\"} {}",
                PMLParser.ForeachStatementContext.class);
        visitorCtx = new VisitorContext(testGlobalScope);
        visitorCtx.scope().addVariable("y", new Variable("y", Type.string(), false));
        new ForeachStmtVisitor(visitorCtx).visitForeachStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "variable 'y' already defined in scope",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testKeyOnlyOnMapReturnsError() throws VariableAlreadyDefinedInScopeException {
        PMLParser.ForeachStatementContext ctx = PMLContextVisitor.toCtx(
                "foreach x in {\"a\": \"b\"} {}",
                PMLParser.ForeachStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
        new ForeachStmtVisitor(visitorCtx).visitForeachStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type []any, got map[string]string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testKeyValueOnArrayReturnsError() throws VariableAlreadyDefinedInScopeException {
        PMLParser.ForeachStatementContext ctx = PMLContextVisitor.toCtx(
                "foreach x, y in [\"a\": \"b\"] {}",
                PMLParser.ForeachStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
        new ForeachStmtVisitor(visitorCtx).visitForeachStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type map[any]any, got []string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

}