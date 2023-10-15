package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.scope.VariableAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.statement.DeleteStatement;
import gov.nist.csd.pm.policy.pml.statement.ForeachStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.policy.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.*;

class ForeachStmtVisitorTest {

    @Test
    void testSuccess() {
        PMLParser.ForeachStatementContext ctx = PMLContextVisitor.toCtx(
                """
                foreach x in ["a", "b"] {}
                """,
                PMLParser.ForeachStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
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
        visitorCtx = new VisitorContext();
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
                """
                foreach x in "a" {}
                """,
                PMLParser.ForeachStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        new ForeachStmtVisitor(visitorCtx).visitForeachStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type []any, got string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                """
                foreach x in {"a": "b"} {}
                """,
                PMLParser.ForeachStatementContext.class);
        visitorCtx = new VisitorContext();
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
                """
                foreach x, y in ["a"] {}
                """,
                PMLParser.ForeachStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
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
                """
                foreach x in arr {}
                """,
                PMLParser.ForeachStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
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
                """
                foreach x in ["a"] {}
                """,
                PMLParser.ForeachStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        visitorCtx.scope().addVariable("x", Type.string(), false);
        new ForeachStmtVisitor(visitorCtx).visitForeachStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "variable 'x' already defined in scope",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                """
                foreach x, y in {"a": "b"} {}
                """,
                PMLParser.ForeachStatementContext.class);
        visitorCtx = new VisitorContext();
        visitorCtx.scope().addVariable("y", Type.string(), false);
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
                """
                foreach x in {"a": "b"} {}
                """,
                PMLParser.ForeachStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
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
                """
                foreach x, y in ["a": "b"] {}
                """,
                PMLParser.ForeachStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        new ForeachStmtVisitor(visitorCtx).visitForeachStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type map[any]any, got []string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

}