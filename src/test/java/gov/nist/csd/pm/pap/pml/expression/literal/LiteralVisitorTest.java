package gov.nist.csd.pm.pap.pml.expression.literal;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.function.arg.type.Type.BOOLEAN_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.ANY_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.listType;
import static gov.nist.csd.pm.pap.function.arg.type.Type.mapType;
import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.pap.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LiteralVisitorTest {

    @Test
    void testVisitStringLiteralExpression() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                "test"
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression<String> literal = ExpressionVisitor.compile(visitorContext, ctx, STRING_TYPE);

        assertEquals(0, visitorContext.errorLog().getErrors().size());

        assertEquals(
                new StringLiteralExpression("test"),
                literal
        );
        assertEquals(
                STRING_TYPE,
                literal.getType()
        );
    }

    @Test
    void testVisitBoolLiteral() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                true
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression<?> literal = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);

        assertEquals(0, visitorContext.errorLog().getErrors().size());

        assertEquals(
                new BoolLiteralExpression(true),
                literal
        );
        assertEquals(
                BOOLEAN_TYPE,
                literal.getType()
        );
    }

    @Test
    void testVisitArrayLiteral() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                ["a", ["b"]]
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression<?> literal = ExpressionVisitor.compile(visitorContext, ctx, listType(ANY_TYPE));

        assertEquals(0, visitorContext.errorLog().getErrors().size());

        ArrayLiteralExpression<?> arrayLiteral = (ArrayLiteralExpression<?>)literal;
        assertEquals(
            new ArrayLiteralExpression<>(
                List.of(new StringLiteralExpression("a"), new ArrayLiteralExpression<>(List.of(new StringLiteralExpression("b")), STRING_TYPE)),
                ANY_TYPE
            ),
            arrayLiteral
        );
        assertEquals(
                listType(ANY_TYPE),
                literal.getType()
        );

        ctx = TestPMLParser.parseExpression(
                """
                ["a", "b"]
                """);
        visitorContext = new VisitorContext(new CompileScope());
        literal = ExpressionVisitor.compile(visitorContext, ctx, listType(STRING_TYPE));

        assertEquals(0, visitorContext.errorLog().getErrors().size());

        arrayLiteral = (ArrayLiteralExpression<?>)literal;
        assertEquals(
                buildArrayLiteral("a", "b"),
                arrayLiteral
        );
        assertEquals(
                listType(STRING_TYPE),
                literal.getType()
        );
    }

    @Test
    void testVisitArrayLiteralScopeException() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                ["a", b]
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());

        PMLCompilationRuntimeException e = assertThrows(
                PMLCompilationRuntimeException.class,
                () -> ExpressionVisitor.compile(visitorContext, ctx)
        );

        assertEquals(1, e.getErrors().size());
        assertEquals(
                "unknown variable 'b' in scope",
                e.getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testVisitMapLiteralExpression() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                {
                    "a": "a1",
                    "b": "b1"
                }
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression literal = ExpressionVisitor.compile(visitorContext, ctx, mapType(STRING_TYPE, STRING_TYPE));

        assertEquals(0, visitorContext.errorLog().getErrors().size());

        MapLiteralExpression mapLiteral = (MapLiteralExpression)literal;
        assertEquals(
                buildMapLiteral("a", "a1", "b", "b1"),
                mapLiteral
        );
        assertEquals(
                mapType(STRING_TYPE, STRING_TYPE),
                literal.getType()
        );
    }

    @Test
    void testVisitMapLiteralExpressionDifferentValueTypes() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                {
                    "a": "a1",
                    "b": ["b1"]
                }
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression<?> literal = ExpressionVisitor.compile(visitorContext, ctx);

        assertEquals(0, visitorContext.errorLog().getErrors().size());

        MapLiteralExpression<?, ?> mapLiteral = (MapLiteralExpression<?, ?>)literal;
        Map<Expression<?>, Expression<?>> map1 = new HashMap<>();
        map1.put(new StringLiteralExpression("a"), new StringLiteralExpression("a1"));
        map1.put(new StringLiteralExpression("b"), new ArrayLiteralExpression<>(List.of(new StringLiteralExpression("b1")), STRING_TYPE));
        assertEquals(
                new MapLiteralExpression<>(map1, STRING_TYPE, ANY_TYPE),
                mapLiteral
        );
        assertEquals(
                mapType(STRING_TYPE, ANY_TYPE),
                literal.getType()
        );
    }

    @Test
    void testVisitMapLiteralExpressionDifferentKeyTypes() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                {
                    "a": "a1",
                    ["b"]: "b1"
                }
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression<?> literal = ExpressionVisitor.compile(visitorContext, ctx, mapType(ANY_TYPE, STRING_TYPE));

        assertEquals(0, visitorContext.errorLog().getErrors().size());

        MapLiteralExpression<?, ?> mapLiteral = (MapLiteralExpression<?, ?>)literal;
        Map<Expression<?>, Expression<?>> map2 = new HashMap<>();
        map2.put(new StringLiteralExpression("a"), new StringLiteralExpression("a1"));
        map2.put(new ArrayLiteralExpression<>(List.of(new StringLiteralExpression("b")), STRING_TYPE), new StringLiteralExpression("b1"));
        MapLiteralExpression<?, ?> expected = new MapLiteralExpression<>(map2, ANY_TYPE, STRING_TYPE);

        assertEquals(expected, mapLiteral);
        assertEquals(
                mapType(ANY_TYPE, STRING_TYPE),
                literal.getType()
        );
    }

    @Test
    void testEmptyLiterals() throws PMException {
        PMLParser.ExpressionContext stringCtx = TestPMLParser.parseExpression(
                "\"\"");
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression<?> literal = ExpressionVisitor.compile(visitorContext, stringCtx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(
                new StringLiteralExpression(""),
                literal
        );

        PMLParser.ExpressionContext arrayCtx = TestPMLParser.parseExpression(
                "[]");
        visitorContext = new VisitorContext(new CompileScope());
        Expression arrayLiteral = ExpressionVisitor.compile(visitorContext, arrayCtx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(
            new ArrayLiteralExpression<>(List.of(), ANY_TYPE),
                arrayLiteral
        );

        PMLParser.ExpressionContext mapCtx = TestPMLParser.parseExpression(
                "{}");
        visitorContext = new VisitorContext(new CompileScope());
        Expression mapLiteral = ExpressionVisitor.compile(visitorContext, mapCtx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(
                new MapLiteralExpression(Map.of(), ANY_TYPE, ANY_TYPE),
                mapLiteral
        );
    }
}