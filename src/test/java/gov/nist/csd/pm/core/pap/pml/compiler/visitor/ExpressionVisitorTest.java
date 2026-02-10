package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.ExpressionContext;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.EqualsExpression;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.expression.LogicalExpression;
import gov.nist.csd.pm.core.pap.pml.expression.PlusExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.reference.BracketIndexExpression;
import gov.nist.csd.pm.core.pap.pml.expression.reference.DotIndexExpression;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import org.junit.jupiter.api.Test;

class ExpressionVisitorTest {

    @Test
    void testCompileWithObjectAsExpectedReturnType() throws PMException {
        ExpressionContext ctx = TestPMLParser.parseExpression("""
                "test"
                """);
        Expression<Object> actual = ExpressionVisitor.compile(new VisitorContext(new CompileScope(new MemoryPAP())), ctx, ANY_TYPE);
        assertEquals(STRING_TYPE, actual.getType());
        assertEquals(StringLiteralExpression.class, actual.getClass());
    }

    @Test
    void testCompileWithNoDefinedExpectedType() throws PMException {
        ExpressionContext ctx = TestPMLParser.parseExpression("""
                "test"
                """);
        Expression<?> actual = ExpressionVisitor.compile(new VisitorContext(new CompileScope(new MemoryPAP())), ctx);
        assertEquals(STRING_TYPE, actual.getType());
        assertEquals(StringLiteralExpression.class, actual.getClass());
    }

    @Test
    void testIndexExpressionOnMapLiteral() throws PMException {
        ExpressionContext ctx = TestPMLParser.parseExpression("""
                {"name": "test"}.name
                """);
        Expression<?> actual = ExpressionVisitor.compile(new VisitorContext(new CompileScope(new MemoryPAP())), ctx);
        assertEquals(STRING_TYPE, actual.getType());
        assertEquals(DotIndexExpression.class, actual.getClass());
    }

    @Test
    void testBracketIndexExpressionOnMapLiteral() throws PMException {
        ExpressionContext ctx = TestPMLParser.parseExpression("""
                {"name": "test"}["name"]
                """);
        Expression<?> actual = ExpressionVisitor.compile(new VisitorContext(new CompileScope(new MemoryPAP())), ctx);
        assertEquals(STRING_TYPE, actual.getType());
        assertEquals(BracketIndexExpression.class, actual.getClass());
    }

    @Test
    void testChainedDotIndexExpression() throws PMException {
        ExpressionContext ctx = TestPMLParser.parseExpression("""
                {"outer": {"inner": "value"}}.outer.inner
                """);
        Expression<?> actual = ExpressionVisitor.compile(new VisitorContext(new CompileScope(new MemoryPAP())), ctx);
        assertEquals(STRING_TYPE, actual.getType());
        assertInstanceOf(DotIndexExpression.class, actual);
    }

    @Test
    void testMixedBracketAndDotIndexExpression() throws PMException {
        ExpressionContext ctx = TestPMLParser.parseExpression("""
                {"outer": {"inner": "value"}}["outer"].inner
                """);
        Expression<?> actual = ExpressionVisitor.compile(new VisitorContext(new CompileScope(new MemoryPAP())), ctx);
        assertEquals(STRING_TYPE, actual.getType());
        assertInstanceOf(DotIndexExpression.class, actual);
    }

    @Test
    void testIndexExpressionHasHigherPrecedenceThanPlus() throws PMException {
        ExpressionContext ctx = TestPMLParser.parseExpression("""
                "prefix" + {"name": "test"}.name
                """);
        Expression<?> actual = ExpressionVisitor.compile(new VisitorContext(new CompileScope(new MemoryPAP())), ctx);
        assertEquals(STRING_TYPE, actual.getType());
        assertInstanceOf(PlusExpression.class, actual);
    }

    @Test
    void testIndexExpressionHasHigherPrecedenceThanEquals() throws PMException {
        ExpressionContext ctx = TestPMLParser.parseExpression("""
                {"name": "test"}.name == "test"
                """);
        Expression<?> actual = ExpressionVisitor.compile(new VisitorContext(new CompileScope(new MemoryPAP())), ctx);
        assertEquals(BOOLEAN_TYPE, actual.getType());
        assertInstanceOf(EqualsExpression.class, actual);
    }

    @Test
    void testIndexExpressionHasHigherPrecedenceThanLogical() throws PMException {
        ExpressionContext ctx = TestPMLParser.parseExpression("""
                {"a": true}.a && {"b": false}.b
                """);
        Expression<?> actual = ExpressionVisitor.compile(new VisitorContext(new CompileScope(new MemoryPAP())), ctx);
        assertEquals(BOOLEAN_TYPE, actual.getType());
        assertInstanceOf(LogicalExpression.class, actual);
    }

    @Test
    void testIndexOnParenthesizedExpression() throws PMException {
        ExpressionContext ctx = TestPMLParser.parseExpression("""
                ({"name": "test"}).name
                """);
        Expression<?> actual = ExpressionVisitor.compile(new VisitorContext(new CompileScope(new MemoryPAP())), ctx);
        assertEquals(STRING_TYPE, actual.getType());
        assertInstanceOf(DotIndexExpression.class, actual);
    }

    @Test
    void testPlusOnLeftOfIndexExpression() throws PMException {
        ExpressionContext ctx = TestPMLParser.parseExpression("""
                {"prefix": "hello"}.prefix + " world"
                """);
        Expression<?> actual = ExpressionVisitor.compile(new VisitorContext(new CompileScope(new MemoryPAP())), ctx);
        assertEquals(STRING_TYPE, actual.getType());
        assertInstanceOf(PlusExpression.class, actual);
    }

}