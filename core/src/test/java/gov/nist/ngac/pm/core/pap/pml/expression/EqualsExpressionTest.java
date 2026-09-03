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

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.ngac.pm.core.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.ngac.pm.core.pap.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.pml.TestPMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.BoolLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.scope.CompileScope;
import gov.nist.ngac.pm.core.util.TestPAP;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

class EqualsExpressionTest {

    @Test
    void testEqualsString() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                "a" == "a"
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(new StringLiteralExpression("a"), new StringLiteralExpression("a"), true),
                equalsExpression
        );

        MemoryPAP pap = new TestPAP();

        Object value = equalsExpression.execute(new ExecutionContext(NodeUserContext.of(0), pap), pap);
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
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(new StringLiteralExpression("a"), new StringLiteralExpression("a"), false),
                equalsExpression
        );

        Object value = equalsExpression.execute(new ExecutionContext(NodeUserContext.of(0), new MemoryPAP()), new MemoryPAP());
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
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(buildArrayLiteral("a", "b"), buildArrayLiteral("a", "b"), true),
                equalsExpression
        );

        Object value = equalsExpression.execute(new ExecutionContext(NodeUserContext.of(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                true,
                value
        );

        ctx = TestPMLParser.parseExpression(
                """
                ["a", "b"] == ["b", "a"]
                """);
        visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(buildArrayLiteral("a", "b"), buildArrayLiteral("b", "a"), true),
                equalsExpression
        );

        value = equalsExpression.execute(new ExecutionContext(NodeUserContext.of(0), new MemoryPAP()), new MemoryPAP());
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
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(buildArrayLiteral("a", "b"), buildArrayLiteral("a", "b"), false),
                equalsExpression
        );

        Object value = equalsExpression.execute(new ExecutionContext(NodeUserContext.of(0), new MemoryPAP()), new MemoryPAP());
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
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(new BoolLiteralExpression(true), new BoolLiteralExpression(true), true),
                equalsExpression
        );

        Object value = equalsExpression.execute(new ExecutionContext(NodeUserContext.of(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                true,
                value
        );

        ctx = TestPMLParser.parseExpression(
                """
                true == false
                """);
        visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(new BoolLiteralExpression(true), new BoolLiteralExpression(false), true),
                equalsExpression
        );

        value = equalsExpression.execute(new ExecutionContext(NodeUserContext.of(0), new MemoryPAP()), new MemoryPAP());
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
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(new BoolLiteralExpression(true), new BoolLiteralExpression(true), false),
                equalsExpression
        );

        Object value = equalsExpression.execute(new ExecutionContext(NodeUserContext.of(0), new MemoryPAP()), new MemoryPAP());
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
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(buildMapLiteral("a", "a", "b", "b"), buildMapLiteral("a", "a", "b", "b"), true),
                equalsExpression
        );

        Object value = equalsExpression.execute(new ExecutionContext(NodeUserContext.of(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                true,
                value
        );

        ctx = TestPMLParser.parseExpression(
                """
                {"a": "a", "b": "b"} == {"a": "a", "b": "c"}
                """);
        visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(buildMapLiteral("a", "a", "b", "b"), buildMapLiteral("a", "a", "b", "c"), true),
                equalsExpression
        );

        value = equalsExpression.execute(new ExecutionContext(NodeUserContext.of(0), new MemoryPAP()), new MemoryPAP());
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
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        EqualsExpression equalsExpression = (EqualsExpression) expression;
        assertEquals(
                new EqualsExpression(buildMapLiteral("a", "a", "b", "b"), buildMapLiteral("a", "a", "b", "b"), false),
                equalsExpression
        );

        Object value = equalsExpression.execute(new ExecutionContext(NodeUserContext.of(0), new MemoryPAP()), new MemoryPAP());
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
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
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

        Object value = equalsExpression.execute(new ExecutionContext(NodeUserContext.of(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                true,
                value
        );

        ctx = TestPMLParser.parseExpression(
                """
                ("a" + "b") == ("a" + "c")
                """);
        visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
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

        value = equalsExpression.execute(new ExecutionContext(NodeUserContext.of(0), new MemoryPAP()), new MemoryPAP());
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
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
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

        Object value = equalsExpression.execute(new ExecutionContext(NodeUserContext.of(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                false,
                value
        );

    }

}