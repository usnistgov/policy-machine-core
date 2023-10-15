package gov.nist.csd.pm.policy.pml.expression;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.BoolLiteral;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.BoolValue;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.policy.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.*;

class EqualsExpressionTest {

    @Test
    void testEqualsString() throws PMException {
        PMLParser.EqualsExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                "a" == "a"
                """,
                PMLParser.EqualsExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext();
        Expression expression = EqualsExpression.compileEqualsExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(new StringLiteral("a"), new StringLiteral("a"), true),
                equalsExpression
        );

        Value value = equalsExpression.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(true),
                value
        );
    }

    @Test
    void testNotEqualsString() throws PMException {
        PMLParser.EqualsExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                "a" != "a"
                """,
                PMLParser.EqualsExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext();
        Expression expression = EqualsExpression.compileEqualsExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(new StringLiteral("a"), new StringLiteral("a"), false),
                equalsExpression
        );

        Value value = equalsExpression.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(false),
                value
        );
    }

    @Test
    void testEqualsArray() throws PMException {
        PMLParser.EqualsExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                ["a", "b"] == ["a", "b"]
                """,
                PMLParser.EqualsExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext();
        Expression expression = EqualsExpression.compileEqualsExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(buildArrayLiteral("a", "b"), buildArrayLiteral("a", "b"), true),
                equalsExpression
        );

        Value value = equalsExpression.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(true),
                value
        );

        ctx = PMLContextVisitor.toExpressionCtx(
                """
                ["a", "b"] == ["b", "a"]
                """,
                PMLParser.EqualsExpressionContext.class);
        visitorContext = new VisitorContext();
        expression = EqualsExpression.compileEqualsExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(buildArrayLiteral("a", "b"), buildArrayLiteral("b", "a"), true),
                equalsExpression
        );

        value = equalsExpression.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(false),
                value
        );
    }

    @Test
    void testNotEqualsArray() throws PMException {
        PMLParser.EqualsExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                ["a", "b"] != ["a", "b"]
                """,
                PMLParser.EqualsExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext();
        Expression expression = EqualsExpression.compileEqualsExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(buildArrayLiteral("a", "b"), buildArrayLiteral("a", "b"), false),
                equalsExpression
        );

        Value value = equalsExpression.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(false),
                value
        );
    }

    @Test
    void testEqualsBool() throws PMException {
        PMLParser.EqualsExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                true == true
                """,
                PMLParser.EqualsExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext();
        Expression expression = EqualsExpression.compileEqualsExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(new BoolLiteral(true), new BoolLiteral(true), true),
                equalsExpression
        );

        Value value = equalsExpression.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(true),
                value
        );

        ctx = PMLContextVisitor.toExpressionCtx(
                """
                true == false
                """,
                PMLParser.EqualsExpressionContext.class);
        visitorContext = new VisitorContext();
        expression = EqualsExpression.compileEqualsExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(new BoolLiteral(true), new BoolLiteral(false), true),
                equalsExpression
        );

        value = equalsExpression.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(false),
                value
        );
    }

    @Test
    void testNotEqualsBool() throws PMException {
        PMLParser.EqualsExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                true != true
                """,
                PMLParser.EqualsExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext();
        Expression expression = EqualsExpression.compileEqualsExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(new BoolLiteral(true), new BoolLiteral(true), false),
                equalsExpression
        );

        Value value = equalsExpression.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(false),
                value
        );
    }

    @Test
    void testEqualsMap() throws PMException {
        PMLParser.EqualsExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                {"a": "a", "b": "b"} == {"a": "a", "b": "b"}
                """,
                PMLParser.EqualsExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext();
        Expression expression = EqualsExpression.compileEqualsExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(buildMapLiteral("a", "a", "b", "b"), buildMapLiteral("a", "a", "b", "b"), true),
                equalsExpression
        );

        Value value = equalsExpression.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(true),
                value
        );

        ctx = PMLContextVisitor.toExpressionCtx(
                """
                {"a": "a", "b": "b"} == {"a": "a", "b": "c"}
                """,
                PMLParser.EqualsExpressionContext.class);
        visitorContext = new VisitorContext();
        expression = EqualsExpression.compileEqualsExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(buildMapLiteral("a", "a", "b", "b"), buildMapLiteral("a", "a", "b", "c"), true),
                equalsExpression
        );

        value = equalsExpression.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(false),
                value
        );
    }

    @Test
    void testNotEqualsMap() throws PMException {
        PMLParser.EqualsExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                {"a": "a", "b": "b"} != {"a": "a", "b": "b"}
                """,
                PMLParser.EqualsExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext();
        Expression expression = EqualsExpression.compileEqualsExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(buildMapLiteral("a", "a", "b", "b"), buildMapLiteral("a", "a", "b", "b"), false),
                equalsExpression
        );

        Value value = equalsExpression.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(false),
                value
        );
    }

    @Test
    void testEqualsWithParens() throws PMException {
        PMLParser.EqualsExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                ("a" + "b") == ("a" + "b")
                """,
                PMLParser.EqualsExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext();
        Expression expression = EqualsExpression.compileEqualsExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(
                        new ParenExpression(
                                new PlusExpression(new StringLiteral("a"), new StringLiteral("b"))
                        ),
                        new ParenExpression(
                                new PlusExpression(new StringLiteral("a"), new StringLiteral("b"))
                        ),
                        true
                ),
                equalsExpression
        );

        Value value = equalsExpression.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(true),
                value
        );

        ctx = PMLContextVisitor.toExpressionCtx(
                """
                ("a" + "b") == ("a" + "c")
                """,
                PMLParser.EqualsExpressionContext.class);
        visitorContext = new VisitorContext();
        expression = EqualsExpression.compileEqualsExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(
                        new ParenExpression(
                                new PlusExpression(new StringLiteral("a"), new StringLiteral("b"))
                        ),
                        new ParenExpression(
                                new PlusExpression(new StringLiteral("a"), new StringLiteral("c"))
                        ),
                        true
                ),
                equalsExpression
        );

        value = equalsExpression.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(false),
                value
        );
    }

    @Test
    void testNotEqualsDifferentTypes() throws PMException {
        PMLParser.EqualsExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                ("a" + "b") == (true)
                """,
                PMLParser.EqualsExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext();
        Expression expression = EqualsExpression.compileEqualsExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(
                        new ParenExpression(
                                new PlusExpression(new StringLiteral("a"), new StringLiteral("b"))
                        ),
                        new ParenExpression(
                                new BoolLiteral(true)
                        ),
                        true
                ),
                equalsExpression
        );

        Value value = equalsExpression.execute(new ExecutionContext(new UserContext("")), new MemoryPolicyStore());
        assertEquals(
                new BoolValue(false),
                value
        );

    }

}