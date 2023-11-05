package gov.nist.csd.pm.policy.pml.expression;

import gov.nist.csd.pm.policy.pml.PMLCompiler;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.expression.reference.ReferenceByID;
import gov.nist.csd.pm.policy.pml.function.FunctionSignature;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.exception.PMLCompilationException;
import gov.nist.csd.pm.policy.pml.model.scope.FunctionAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.VariableAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionTest {

    @Test
    void testAllowedTypes() throws VariableAlreadyDefinedInScopeException {
        PMLParser.VariableReferenceExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                a
                """, PMLParser.VariableReferenceExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext();
        visitorContext.scope().addVariable("a", Type.string(), false);
        Expression actual = Expression.compile(visitorContext, ctx, Type.string());
        assertEquals(
                new ReferenceByID("a"),
                actual
        );

        ctx = PMLContextVisitor.toExpressionCtx(
                """
                a
                """, PMLParser.VariableReferenceExpressionContext.class);
        visitorContext = new VisitorContext();
        visitorContext.scope().addVariable("a", Type.array(Type.string()), false);
        actual = Expression.compile(visitorContext, ctx, Type.array(Type.string()));
        assertEquals(
                new ReferenceByID("a"),
                actual
        );
    }

    @Test
    void testDisallowedTypes() throws VariableAlreadyDefinedInScopeException {
        PMLParser.VariableReferenceExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                a
                """, PMLParser.VariableReferenceExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext();
        visitorContext.scope().addVariable("a", Type.string(), false);
        Expression e = Expression.compile(visitorContext, ctx, Type.array(Type.string()));
        assertTrue(e instanceof ErrorExpression);
        assertEquals(1, visitorContext.errorLog().getErrors().size());
        assertEquals(
                "expected expression type []string, got string",
                visitorContext.errorLog().getErrors().get(0).errorMessage()
        );

    }


    @Test
    void testCompileStringExpression_Literal() {
        VisitorContext visitorContext = new VisitorContext();
        Expression expression = Expression.fromString(visitorContext, "\"test\"", Type.string());
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(new StringLiteral("test"), expression);
    }

    @Test
    void testCompileStringExpression_VarRef() throws VariableAlreadyDefinedInScopeException {
        VisitorContext visitorContext = new VisitorContext();
        visitorContext.scope().addVariable("test", Type.string(), true);
        Expression expression = Expression.fromString(visitorContext, "test", Type.string());
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(new ReferenceByID("test"), expression);
    }

    @Test
    void testCompileStringExpression_FuncInvoke() throws PMLCompilationException,
                                                         FunctionAlreadyDefinedInScopeException {
        VisitorContext visitorContext = new VisitorContext();
        visitorContext.scope().addFunctionSignature(new FunctionSignature("test", Type.string(), List.of()));
        Expression expression = Expression.fromString(visitorContext, "test()", Type.string());
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(new FunctionInvokeExpression("test", Type.string(), List.of()), expression);
    }

    @Test
    void testCompileStringExpression_NonString_Error() {
        VisitorContext visitorContext = new VisitorContext();
        Expression.fromString(visitorContext, "\"test\" == \"test\"", Type.string());
        assertEquals(1, visitorContext.errorLog().getErrors().size());

        visitorContext = new VisitorContext();
        Expression.fromString(visitorContext, "[\"a\", \"b\"]", Type.string());
        assertEquals(1, visitorContext.errorLog().getErrors().size());
    }

}