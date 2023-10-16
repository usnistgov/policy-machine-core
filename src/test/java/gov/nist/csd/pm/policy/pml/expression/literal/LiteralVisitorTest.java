package gov.nist.csd.pm.policy.pml.expression.literal;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.policy.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LiteralVisitorTest {

    @Test
    void testVisitStringLiteral() throws PMException {
        PMLParser.StringLiteralContext ctx = PMLContextVisitor.toLiteralCtx(
                "\"test\"",
                PMLParser.StringLiteralContext.class);
        VisitorContext visitorContext = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
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
                "true",
                PMLParser.BoolLiteralContext.class);
        VisitorContext visitorContext = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
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
                "[\"a\", [\"b\"]]",
                PMLParser.ArrayLiteralContext.class);
        VisitorContext visitorContext = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        Expression literal = new LiteralVisitor(visitorContext)
                .visitArrayLiteral(ctx);

        assertEquals(0, visitorContext.errorLog().getErrors().size());

        ArrayLiteral arrayLiteral = (ArrayLiteral)literal;
        assertEquals(
                new ArrayLiteral(Type.any(), new StringLiteral("a"), new ArrayLiteral(Type.string(), new StringLiteral("b"))),
                arrayLiteral
        );
        assertEquals(
                Type.array(Type.any()),
                literal.getType(visitorContext.scope())
        );

        ctx = PMLContextVisitor.toLiteralCtx(
                "[\"a\", \"b\"]",
                PMLParser.ArrayLiteralContext.class);
        visitorContext = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
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
                "[\"a\", b]",
                PMLParser.ArrayLiteralContext.class);
        VisitorContext visitorContext = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        new LiteralVisitor(visitorContext)
                .visitArrayLiteral(ctx);

        assertEquals(1, visitorContext.errorLog().getErrors().size());
        assertEquals(
                "unknown variable 'b' in scope",
                visitorContext.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testVisitMapLiteral() throws PMException {
        PMLParser.MapLiteralContext ctx = PMLContextVisitor.toLiteralCtx(
                "{\n" +
                        "                    \"a\": \"a1\",\n" +
                        "                    \"b\": \"b1\"\n" +
                        "                }",
                PMLParser.MapLiteralContext.class);
        VisitorContext visitorContext = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
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
                "{\n" +
                        "                    \"a\": \"a1\",\n" +
                        "                    \"b\": [\"b1\"]\n" +
                        "                }",
                PMLParser.MapLiteralContext.class);
        VisitorContext visitorContext = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        Expression literal = new LiteralVisitor(visitorContext)
                .visitMapLiteral(ctx);

        assertEquals(0, visitorContext.errorLog().getErrors().size());

        MapLiteral mapLiteral = (MapLiteral)literal;
        assertEquals(
                new MapLiteral(Map.of(
                        new StringLiteral("a"), new StringLiteral("a1"),
                        new StringLiteral("b"), new ArrayLiteral(Type.string(), new StringLiteral("b1"))
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
                "{\n" +
                        "                    \"a\": \"a1\",\n" +
                        "                    [\"b\"]: \"b1\"\n" +
                        "                }",
                PMLParser.MapLiteralContext.class);
        VisitorContext visitorContext = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        Expression literal = new LiteralVisitor(visitorContext)
                .visitMapLiteral(ctx);

        assertEquals(0, visitorContext.errorLog().getErrors().size());

        MapLiteral mapLiteral = (MapLiteral)literal;
        MapLiteral expected = new MapLiteral(new HashMap<>(Map.of(
                new StringLiteral("a"), new StringLiteral("a1"),
                new ArrayLiteral(Type.string(), new StringLiteral("b")), new StringLiteral("b1")
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
        VisitorContext visitorContext = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
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
        visitorContext = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        Expression arrayLiteral = new LiteralVisitor(visitorContext)
                .visitArrayLiteral(arrayCtx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(
                new ArrayLiteral(Type.any()),
                arrayLiteral
        );

        PMLParser.MapLiteralContext mapCtx = PMLContextVisitor.toLiteralCtx(
                "{}",
                PMLParser.MapLiteralContext.class);
        visitorContext = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        Expression mapLiteral = new LiteralVisitor(visitorContext)
                .visitMapLiteral(mapCtx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(
                new MapLiteral(Type.any(), Type.any()),
                mapLiteral
        );
    }
}