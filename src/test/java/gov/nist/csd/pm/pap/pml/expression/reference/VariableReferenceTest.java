package gov.nist.csd.pm.pap.pml.expression.reference;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.pml.expression.reference.VariableReference.compileVariableReference;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VariableReferenceTest {

    @Nested
    class ReferenceByIDTest {
        @Test
        void testReferenceById() throws PMException {
            PMLParser.VariableReferenceExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                    """
                    a
                    """, PMLParser.VariableReferenceExpressionContext.class);
            VisitorContext visitorContext = new VisitorContext(new CompileScope());
            visitorContext.scope().addVariable("a", new Variable("a", Type.string(), false));
            Expression actual = compileVariableReference(visitorContext, ctx.variableReference());
            assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
            assertEquals(
                    new ReferenceByID("a"),
                    actual
            );
        }

        @Test
        void testUnknownVariable() throws PMException {
            PMLParser.VariableReferenceExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                    """
                    a
                    """, PMLParser.VariableReferenceExpressionContext.class);
            VisitorContext visitorContext = new VisitorContext(new CompileScope());
            PMLCompilationRuntimeException e = assertThrows(
                    PMLCompilationRuntimeException.class,
                    () -> compileVariableReference(visitorContext, ctx.variableReference())
            );
            assertEquals(1, e.getErrors().size());
            assertEquals(
                    "unknown variable 'a' in scope",
                    e.getErrors().get(0).errorMessage()
            );
        }
    }

    @Nested
    class ReferenceByIndexTest {
        @Test
        void testSuccess() throws PMException {
            PMLParser.VariableReferenceExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                    """
                    a.b.c
                    """, PMLParser.VariableReferenceExpressionContext.class);
            VisitorContext visitorContext = new VisitorContext(new CompileScope());
            visitorContext.scope().addVariable("a", new Variable("a", Type.map(Type.string(), Type.map(Type.string(), Type.string())), false));
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
            visitorContext = new VisitorContext(new CompileScope());
            visitorContext.scope().addVariable("a", new Variable("a", Type.map(Type.string(), Type.map(Type.string(), Type.string())), false));
            actual = compileVariableReference(visitorContext, ctx.variableReference());
            assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
            assertEquals(
                    new ReferenceByBracketIndex(new ReferenceByBracketIndex(new ReferenceByID("a"), new StringLiteral("b")), new StringLiteral("c")),
                    actual
            );
        }

        @Test
        void testUnknownVariable() throws PMException {
            PMLParser.VariableReferenceExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                    """
                    a.b.c
                    """, PMLParser.VariableReferenceExpressionContext.class);
            VisitorContext visitorContext = new VisitorContext(new CompileScope());
            PMLCompilationRuntimeException e = assertThrows(
                    PMLCompilationRuntimeException.class,
                    () -> compileVariableReference(visitorContext, ctx.variableReference())
            );
            assertEquals(1, e.getErrors().size());
            assertEquals(
                    "unknown variable 'a' in scope",
                    e.getErrors().get(0).errorMessage()
            );
        }

        @Test
        void testVarRefNotAMap() throws PMException {
            PMLParser.VariableReferenceExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                    """
                    a.b.c
                    """, PMLParser.VariableReferenceExpressionContext.class);
            VisitorContext visitorContext = new VisitorContext(new CompileScope());
            visitorContext.scope().addVariable("a", new Variable("a", Type.map(Type.string(), Type.string()), false));
            PMLCompilationRuntimeException e = assertThrows(
                    PMLCompilationRuntimeException.class,
                    () -> compileVariableReference(visitorContext, ctx.variableReference())
            );
            assertEquals(1, e.getErrors().size(), visitorContext.errorLog().toString());
            assertEquals(
                    "expected type map but got string",
                    e.getErrors().get(0).errorMessage()
            );
        }
    }


}