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

package gov.nist.ngac.pm.core.pap.pml.expression;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.operation.arg.type.MapType;
import gov.nist.ngac.pm.core.pap.pml.TestPMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.compiler.Variable;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.expression.reference.BracketIndexExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.reference.DotIndexExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.reference.VariableReferenceExpression;
import gov.nist.ngac.pm.core.pap.pml.scope.CompileScope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExpressionPrecedenceTest {

    private VisitorContext visitorContext;

    @BeforeEach
    void setup() throws Exception {
        visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
    }

    private void addBoolVar(String name) throws Exception {
        visitorContext.scope().addVariable(name, new Variable(name, BOOLEAN_TYPE, false));
    }

    private void addStringVar(String name) throws Exception {
        visitorContext.scope().addVariable(name, new Variable(name, STRING_TYPE, false));
    }

    private void addBoolMapVar(String name) throws Exception {
        visitorContext.scope().addVariable(name,
                new Variable(name, MapType.of(STRING_TYPE, BOOLEAN_TYPE), false));
    }

    private void addStringMapVar(String name) throws Exception {
        visitorContext.scope().addVariable(name,
                new Variable(name, MapType.of(STRING_TYPE, STRING_TYPE), false));
    }

    private Expression<?> compile(String expr) {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(expr);
        return ExpressionVisitor.compile(visitorContext, ctx, ANY_TYPE);
    }

    @Test
    void testDotIndexHigherThanNegate() throws Exception {
        addBoolMapVar("a");

        Expression<?> result = compile("!a.b");

        NegatedExpression negate = assertInstanceOf(NegatedExpression.class, result);
        assertInstanceOf(DotIndexExpression.class, negate.getExpression());
    }

    @Test
    void testBracketIndexHigherThanNegate() throws Exception {
        addBoolMapVar("a");

        Expression<?> result = compile("!a[\"k\"]");

        NegatedExpression negate = assertInstanceOf(NegatedExpression.class, result);
        assertInstanceOf(BracketIndexExpression.class, negate.getExpression());
    }

    @Test
    void testNegateHigherThanLogicalAnd() throws Exception {
        addBoolVar("a");
        addBoolVar("b");

        Expression<?> result = compile("!a && b");

        LogicalExpression logical = assertInstanceOf(LogicalExpression.class, result);
        assertTrue(logical.isAnd());
        assertInstanceOf(NegatedExpression.class, logical.getLeft());
        assertEquals(new VariableReferenceExpression<>("b", BOOLEAN_TYPE), logical.getRight());
    }

    @Test
    void testNegateHigherThanLogicalOr() throws Exception {
        addBoolVar("a");
        addBoolVar("b");

        Expression<?> result = compile("!a || b");

        LogicalExpression logical = assertInstanceOf(LogicalExpression.class, result);
        assertFalse(logical.isAnd());
        assertInstanceOf(NegatedExpression.class, logical.getLeft());
        assertEquals(new VariableReferenceExpression<>("b", BOOLEAN_TYPE), logical.getRight());
    }

    @Test
    void testLogicalAndHigherThanLogicalOr() throws Exception {
        addBoolVar("a");
        addBoolVar("b");
        addBoolVar("c");

        Expression<?> result = compile("a || b && c");

        LogicalExpression or = assertInstanceOf(LogicalExpression.class, result);
        assertFalse(or.isAnd());
        assertEquals(new VariableReferenceExpression<>("a", BOOLEAN_TYPE), or.getLeft());

        LogicalExpression and = assertInstanceOf(LogicalExpression.class, or.getRight());
        assertTrue(and.isAnd());
        assertEquals(new VariableReferenceExpression<>("b", BOOLEAN_TYPE), and.getLeft());
        assertEquals(new VariableReferenceExpression<>("c", BOOLEAN_TYPE), and.getRight());
    }

    @Test
    void testEqualsHigherThanLogicalAnd() throws Exception {
        addBoolVar("a");
        addStringVar("b");
        addStringVar("c");

        Expression<?> result = compile("a && b == c");

        LogicalExpression and = assertInstanceOf(LogicalExpression.class, result);
        assertTrue(and.isAnd());
        assertEquals(new VariableReferenceExpression<>("a", BOOLEAN_TYPE), and.getLeft());
        assertInstanceOf(EqualsExpression.class, and.getRight());
    }

    @Test
    void testPlusHigherThanEquals() throws Exception {
        addStringVar("a");
        addStringVar("b");
        addStringVar("c");

        Expression<?> result = compile("a == b + c");

        EqualsExpression eq = assertInstanceOf(EqualsExpression.class, result);
        assertEquals(
                new EqualsExpression(
                        new VariableReferenceExpression<>("a", STRING_TYPE),
                        new PlusExpression(
                                new VariableReferenceExpression<>("b", STRING_TYPE),
                                new VariableReferenceExpression<>("c", STRING_TYPE)
                        ),
                        true
                ),
                eq
        );
    }

    @Test
    void testIndexHigherThanPlus() throws Exception {
        addStringMapVar("a");
        addStringMapVar("b");

        Expression<?> result = compile("a.x + b.y");

        PlusExpression plus = assertInstanceOf(PlusExpression.class, result);
        assertEquals(
                new PlusExpression(
                        new DotIndexExpression<>(
                                new VariableReferenceExpression<>("a", ANY_TYPE),
                                "x", STRING_TYPE
                        ),
                        new DotIndexExpression<>(
                                new VariableReferenceExpression<>("b", ANY_TYPE),
                                "y", STRING_TYPE
                        )
                ),
                plus
        );
    }

    @Test
    void testFullPrecedenceChain() throws Exception {
        addBoolVar("a");
        addBoolVar("b");
        addStringVar("c");
        addStringVar("d");
        addStringVar("e");

        Expression<?> result = compile("a || b && c == d + e");

        LogicalExpression or = assertInstanceOf(LogicalExpression.class, result);
        assertFalse(or.isAnd());
        assertEquals(new VariableReferenceExpression<>("a", BOOLEAN_TYPE), or.getLeft());

        LogicalExpression and = assertInstanceOf(LogicalExpression.class, or.getRight());
        assertTrue(and.isAnd());
        assertEquals(new VariableReferenceExpression<>("b", BOOLEAN_TYPE), and.getLeft());

        EqualsExpression eq = assertInstanceOf(EqualsExpression.class, and.getRight());

        assertEquals(
                new EqualsExpression(
                        new VariableReferenceExpression<>("c", STRING_TYPE),
                        new PlusExpression(
                                new VariableReferenceExpression<>("d", STRING_TYPE),
                                new VariableReferenceExpression<>("e", STRING_TYPE)
                        ),
                        true
                ),
                eq
        );
    }
}
