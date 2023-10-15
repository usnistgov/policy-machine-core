package gov.nist.csd.pm.policy.pml.expression.reference;

import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.ErrorExpression;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.scope.VariableAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.policy.pml.expression.reference.VariableReference.compileVariableReference;
import static org.junit.jupiter.api.Assertions.*;

class VariableReferenceTest {

    @Nested
    class ReferenceByIDTest {
        @Test
        void testReferenceById() throws VariableAlreadyDefinedInScopeException {
            PMLParser.VariableReferenceExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                    """
                    a
                    """, PMLParser.VariableReferenceExpressionContext.class);
            VisitorContext visitorContext = new VisitorContext();
            visitorContext.scope().addVariable("a", Type.string(), false);
            Expression actual = compileVariableReference(visitorContext, ctx.variableReference());
            assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
            assertEquals(
                    new ReferenceByID("a"),
                    actual
            );
        }

        @Test
        void testUnknownVariable() {
            PMLParser.VariableReferenceExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                    """
                    a
                    """, PMLParser.VariableReferenceExpressionContext.class);
            VisitorContext visitorContext = new VisitorContext();
            Expression e = compileVariableReference(visitorContext, ctx.variableReference());
            assertTrue(e instanceof ErrorExpression);
            assertEquals(1, visitorContext.errorLog().getErrors().size());
            assertEquals(
                    "unknown variable 'a' in scope",
                    visitorContext.errorLog().getErrors().get(0).errorMessage()
            );
        }
    }

    @Nested
    class ReferenceByIndexTest {
        @Test
        void testSuccess() throws VariableAlreadyDefinedInScopeException {
            PMLParser.VariableReferenceExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                    """
                    a.b.c
                    """, PMLParser.VariableReferenceExpressionContext.class);
            VisitorContext visitorContext = new VisitorContext();
            visitorContext.scope().addVariable("a", Type.map(Type.string(), Type.map(Type.string(), Type.string())), false);
            Expression actual = compileVariableReference(visitorContext, ctx.variableReference());
            assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
            assertEquals(
                    new ReferenceByDotIndex(new ReferenceByDotIndex(new ReferenceByID("a"), "b"), "c"),
                    actual
            );

            ctx = PMLContextVisitor.toExpressionCtx(
                    """
                    a["b"]["c"]
                    """, PMLParser.VariableReferenceExpressionContext.class);
            visitorContext = new VisitorContext();
            visitorContext.scope().addVariable("a", Type.map(Type.string(), Type.map(Type.string(), Type.string())), false);
            actual = compileVariableReference(visitorContext, ctx.variableReference());
            assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
            assertEquals(
                    new ReferenceByBracketIndex(new ReferenceByBracketIndex(new ReferenceByID("a"), new StringLiteral("b")), new StringLiteral("c")),
                    actual
            );
        }

        @Test
        void testUnknownVariable() {
            PMLParser.VariableReferenceExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                    """
                    a.b.c
                    """, PMLParser.VariableReferenceExpressionContext.class);
            VisitorContext visitorContext = new VisitorContext();
            Expression e = compileVariableReference(visitorContext, ctx.variableReference());
            assertTrue(e instanceof ErrorExpression);
            assertEquals(1, visitorContext.errorLog().getErrors().size());
            assertEquals(
                    "unknown variable 'a' in scope",
                    visitorContext.errorLog().getErrors().get(0).errorMessage()
            );
        }

        @Test
        void testVarRefNotAMap() throws VariableAlreadyDefinedInScopeException {
            PMLParser.VariableReferenceExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                    """
                    a.b.c
                    """, PMLParser.VariableReferenceExpressionContext.class);
            VisitorContext visitorContext = new VisitorContext();
            visitorContext.scope().addVariable("a", Type.map(Type.string(), Type.string()), false);
            Expression actual = compileVariableReference(visitorContext, ctx.variableReference());
            assertEquals(1, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
            assertEquals(
                    "expected type map but got string",
                    visitorContext.errorLog().getErrors().get(0).errorMessage()
            );
        }
    }


}