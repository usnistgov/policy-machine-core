/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.pml.compiler.visitor;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.pml.TestPMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.ExpressionContext;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.expression.EqualsExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.expression.LogicalExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.PlusExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.reference.BracketIndexExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.reference.DotIndexExpression;
import gov.nist.ngac.pm.core.pap.pml.scope.CompileScope;
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