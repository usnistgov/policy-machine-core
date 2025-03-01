package gov.nist.csd.pm.pap.pml.expression.literal;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.pap.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LiteralVisitorTest {

    @Test
    void testVisitStringLiteral() throws PMException {
        PMLParser.StringLiteralContext ctx = PMLContextVisitor.toLiteralCtx(
                """
                "test"
                """,
                PMLParser.StringLiteralContext.class);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        StringLiteral literal = new LiteralVisitor(visitorContext)
                .visitStringLiteral(ctx);

        assertEquals(0, visitorContext.errorLog().getErrors().size());

        assertEquals(
                new StringLiteral("test"),
                literal
        );
        assertEquals(
                Type.string(),
                literal.getType(visitorContext.scope())
        );
    }

    @Test
    void testVisitBoolLiteral() throws PMException {
        PMLParser.BoolLiteralContext ctx = PMLContextVisitor.toLiteralCtx(
                """
                true
                """,
                PMLParser.BoolLiteralContext.class);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        BoolLiteral literal = new LiteralVisitor(visitorContext)
                .visitBoolLiteral(ctx);

        assertEquals(0, visitorContext.errorLog().getErrors().size());

        assertEquals(
                new BoolLiteral(true),
                literal
        );
        assertEquals(
                Type.bool(),
                literal.getType(visitorContext.scope())
        );
    }

    @Test
    void testVisitArrayLiteral() throws PMException {
        PMLParser.ArrayLiteralContext ctx = PMLContextVisitor.toLiteralCtx(
                """
                ["a", ["b"]]
                """,
                PMLParser.ArrayLiteralContext.class);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression literal = new LiteralVisitor(visitorContext)
                .visitArrayLiteral(ctx);

        assertEquals(0, visitorContext.errorLog().getErrors().size());

        ArrayLiteral arrayLiteral = (ArrayLiteral)literal;
        assertEquals(
                new ArrayLiteral(List.of(new StringLiteral("a"), new ArrayLiteral(List.of(new StringLiteral("b")), Type.string())), Type.any()),
                arrayLiteral
        );
        assertEquals(
                Type.array(Type.any()),
                literal.getType(visitorContext.scope())
        );

        ctx = PMLContextVisitor.toLiteralCtx(
                """
                ["a", "b"]
                """,
                PMLParser.ArrayLiteralContext.class);
        visitorContext = new VisitorContext(new CompileScope());
        literal = new LiteralVisitor(visitorContext)
                .visitArrayLiteral(ctx);

        assertEquals(0, visitorContext.errorLog().getErrors().size());

        arrayLiteral = (ArrayLiteral)literal;
        assertEquals(
                buildArrayLiteral("a", "b"),
                arrayLiteral
        );
        assertEquals(
                Type.array(Type.string()),
                literal.getType(visitorContext.scope())
        );
    }

    @Test
    void testVisitArrayLiteralScopeException() throws PMException {
        PMLParser.ArrayLiteralContext ctx = PMLContextVisitor.toLiteralCtx(
                """
                ["a", b]
                """,
                PMLParser.ArrayLiteralContext.class);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());

        PMLCompilationRuntimeException e = assertThrows(
                PMLCompilationRuntimeException.class,
                () -> new LiteralVisitor(visitorContext)
                        .visitArrayLiteral(ctx)
        );

        assertEquals(1, e.getErrors().size());
        assertEquals(
                "unknown variable 'b' in scope",
                e.getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testVisitMapLiteral() throws PMException {
        PMLParser.MapLiteralContext ctx = PMLContextVisitor.toLiteralCtx(
                """
                {
                    "a": "a1",
                    "b": "b1"
                }
                """,
                PMLParser.MapLiteralContext.class);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression literal = new LiteralVisitor(visitorContext)
                .visitMapLiteral(ctx);

        assertEquals(0, visitorContext.errorLog().getErrors().size());

        MapLiteral mapLiteral = (MapLiteral)literal;
        assertEquals(
                buildMapLiteral("a", "a1", "b", "b1"),
                mapLiteral
        );
        assertEquals(
                Type.map(Type.string(), Type.string()),
                literal.getType(visitorContext.scope())
        );
    }

    @Test
    void testVisitMapLiteralDifferentValueTypes() throws PMException {
        PMLParser.MapLiteralContext ctx = PMLContextVisitor.toLiteralCtx(
                """
                {
                    "a": "a1",
                    "b": ["b1"]
                }
                """,
                PMLParser.MapLiteralContext.class);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression literal = new LiteralVisitor(visitorContext)
                .visitMapLiteral(ctx);

        assertEquals(0, visitorContext.errorLog().getErrors().size());

        MapLiteral mapLiteral = (MapLiteral)literal;
        assertEquals(
                new MapLiteral(Map.of(
                        new StringLiteral("a"), new StringLiteral("a1"),
                        new StringLiteral("b"), new ArrayLiteral(List.of(new StringLiteral("b1")), Type.string())
                ), Type.string(), Type.any()),
                mapLiteral
        );
        assertEquals(
                Type.map(Type.string(), Type.any()),
                literal.getType(visitorContext.scope())
        );
    }

    @Test
    void testVisitMapLiteralDifferentKeyTypes() throws PMException {
        PMLParser.MapLiteralContext ctx = PMLContextVisitor.toLiteralCtx(
                """
                {
                    "a": "a1",
                    ["b"]: "b1"
                }
                """,
                PMLParser.MapLiteralContext.class);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression literal = new LiteralVisitor(visitorContext)
                .visitMapLiteral(ctx);

        assertEquals(0, visitorContext.errorLog().getErrors().size());

        MapLiteral mapLiteral = (MapLiteral)literal;
        MapLiteral expected = new MapLiteral(new HashMap<>(Map.of(
                new StringLiteral("a"), new StringLiteral("a1"),
                new ArrayLiteral(List.of(new StringLiteral("b")), Type.string()), new StringLiteral("b1")
        )), Type.any(), Type.string());

        assertEquals(expected, mapLiteral);
        assertEquals(
                Type.map(Type.any(), Type.string()),
                literal.getType(visitorContext.scope())
        );
    }

    @Test
    void testEmptyLiterals() throws PMException {
        PMLParser.StringLiteralContext stringCtx = PMLContextVisitor.toLiteralCtx(
                "\"\"",
                PMLParser.StringLiteralContext.class);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        StringLiteral literal = new LiteralVisitor(visitorContext)
                .visitStringLiteral(stringCtx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(
                new StringLiteral(""),
                literal
        );

        PMLParser.ArrayLiteralContext arrayCtx = PMLContextVisitor.toLiteralCtx(
                "[]",
                PMLParser.ArrayLiteralContext.class);
        visitorContext = new VisitorContext(new CompileScope());
        Expression arrayLiteral = new LiteralVisitor(visitorContext)
                .visitArrayLiteral(arrayCtx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(
                new ArrayLiteral(List.of(), Type.any()),
                arrayLiteral
        );

        PMLParser.MapLiteralContext mapCtx = PMLContextVisitor.toLiteralCtx(
                "{}",
                PMLParser.MapLiteralContext.class);
        visitorContext = new VisitorContext(new CompileScope());
        Expression mapLiteral = new LiteralVisitor(visitorContext)
                .visitMapLiteral(mapCtx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(
                new MapLiteral(Map.of(), Type.any(), Type.any()),
                mapLiteral
        );
    }
}