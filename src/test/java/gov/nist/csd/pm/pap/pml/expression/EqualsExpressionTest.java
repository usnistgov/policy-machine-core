package gov.nist.csd.pm.pap.pml.expression;

import com.google.protobuf.BoolValue;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.literal.BoolLiteralExpression;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;

import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.TestPAP;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.BOOLEAN_TYPE;
import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.pap.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EqualsExpressionTest {

    @Test
    void testEqualsString() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                "a" == "a"
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(new StringLiteralExpression("a"), new StringLiteralExpression("a"), true),
                equalsExpression
        );

        MemoryPAP pap = new TestPAP();

        Object value = equalsExpression.execute(new ExecutionContext(new UserContext(0), pap), pap);
        assertEquals(
                true,
                value
        );
    }

    @Test
    void testNotEqualsString() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                "a" != "a"
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(new StringLiteralExpression("a"), new StringLiteralExpression("a"), false),
                equalsExpression
        );

        Object value = equalsExpression.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                false,
                value
        );
    }

    @Test
    void testEqualsArray() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                ["a", "b"] == ["a", "b"]
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(buildArrayLiteral("a", "b"), buildArrayLiteral("a", "b"), true),
                equalsExpression
        );

        Object value = equalsExpression.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                true,
                value
        );

        ctx = TestPMLParser.parseExpression(
                """
                ["a", "b"] == ["b", "a"]
                """);
        visitorContext = new VisitorContext(new CompileScope());
        expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(buildArrayLiteral("a", "b"), buildArrayLiteral("b", "a"), true),
                equalsExpression
        );

        value = equalsExpression.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                false,
                value
        );
    }

    @Test
    void testNotEqualsArray() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                ["a", "b"] != ["a", "b"]
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(buildArrayLiteral("a", "b"), buildArrayLiteral("a", "b"), false),
                equalsExpression
        );

        Object value = equalsExpression.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                false,
                value
        );
    }

    @Test
    void testEqualsBool() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                true == true
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(new BoolLiteralExpression(true), new BoolLiteralExpression(true), true),
                equalsExpression
        );

        Object value = equalsExpression.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                true,
                value
        );

        ctx = TestPMLParser.parseExpression(
                """
                true == false
                """);
        visitorContext = new VisitorContext(new CompileScope());
        expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(new BoolLiteralExpression(true), new BoolLiteralExpression(false), true),
                equalsExpression
        );

        value = equalsExpression.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                false,
                value
        );
    }

    @Test
    void testNotEqualsBool() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                true != true
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(new BoolLiteralExpression(true), new BoolLiteralExpression(true), false),
                equalsExpression
        );

        Object value = equalsExpression.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                false,
                value
        );
    }

    @Test
    void testEqualsMap() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                {"a": "a", "b": "b"} == {"a": "a", "b": "b"}
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(buildMapLiteral("a", "a", "b", "b"), buildMapLiteral("a", "a", "b", "b"), true),
                equalsExpression
        );

        Object value = equalsExpression.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                true,
                value
        );

        ctx = TestPMLParser.parseExpression(
                """
                {"a": "a", "b": "b"} == {"a": "a", "b": "c"}
                """);
        visitorContext = new VisitorContext(new CompileScope());
        expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(buildMapLiteral("a", "a", "b", "b"), buildMapLiteral("a", "a", "b", "c"), true),
                equalsExpression
        );

        value = equalsExpression.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                false,
                value
        );
    }

    @Test
    void testNotEqualsMap() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                {"a": "a", "b": "b"} != {"a": "a", "b": "b"}
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(buildMapLiteral("a", "a", "b", "b"), buildMapLiteral("a", "a", "b", "b"), false),
                equalsExpression
        );

        Object value = equalsExpression.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                false,
                value
        );
    }

    @Test
    void testEqualsWithParens() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                ("a" + "b") == ("a" + "b")
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(
                        new ParenExpression(
                                new PlusExpression(new StringLiteralExpression("a"), new StringLiteralExpression("b"))
                        ),
                        new ParenExpression(
                                new PlusExpression(new StringLiteralExpression("a"), new StringLiteralExpression("b"))
                        ),
                        true
                ),
                equalsExpression
        );

        Object value = equalsExpression.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                true,
                value
        );

        ctx = TestPMLParser.parseExpression(
                """
                ("a" + "b") == ("a" + "c")
                """);
        visitorContext = new VisitorContext(new CompileScope());
        expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(
                        new ParenExpression(
                                new PlusExpression(new StringLiteralExpression("a"), new StringLiteralExpression("b"))
                        ),
                        new ParenExpression(
                                new PlusExpression(new StringLiteralExpression("a"), new StringLiteralExpression("c"))
                        ),
                        true
                ),
                equalsExpression
        );

        value = equalsExpression.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                false,
                value
        );
    }

    @Test
    void testNotEqualsDifferentTypes() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                ("a" + "b") == (true)
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(
                        new ParenExpression(
                                new PlusExpression(new StringLiteralExpression("a"), new StringLiteralExpression("b"))
                        ),
                        new ParenExpression(
                                new BoolLiteralExpression(true)
                        ),
                        true
                ),
                equalsExpression
        );

        Object value = equalsExpression.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                false,
                value
        );

    }

}