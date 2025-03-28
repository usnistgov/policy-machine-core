package gov.nist.csd.pm.pap.pml.expression;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.function.basic.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.expression.reference.ReferenceByID;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpressionTest {

    @Test
    void testAllowedTypes() throws PMException {
        PMLParser.VariableReferenceExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                a
                """, PMLParser.VariableReferenceExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        visitorContext.scope().addVariable("a", new Variable("a", Type.string(), false));
        Expression actual = Expression.compile(visitorContext, ctx, Type.string());
        assertEquals(
                new ReferenceByID("a"),
                actual
        );

        ctx = PMLContextVisitor.toExpressionCtx(
                """
                a
                """, PMLParser.VariableReferenceExpressionContext.class);
        visitorContext = new VisitorContext(new CompileScope());
        visitorContext.scope().addVariable("a", new Variable("a", Type.array(Type.string()), false));
        actual = Expression.compile(visitorContext, ctx, Type.array(Type.string()));
        assertEquals(
                new ReferenceByID("a"),
                actual
        );
    }

    @Test
    void testDisallowedTypes() throws PMException {
        PMLParser.VariableReferenceExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                a
                """, PMLParser.VariableReferenceExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        visitorContext.scope().addVariable("a", new Variable("a", Type.string(), false));
        PMLCompilationRuntimeException e = assertThrows(
                PMLCompilationRuntimeException.class,
                () -> Expression.compile(visitorContext, ctx, Type.array(Type.string()))
        );
        assertEquals(1, e.getErrors().size());
        assertEquals(
                "expected expression type(s) [[]string], got string",
                e.getErrors().get(0).errorMessage()
        );
    }


    @Test
    void testCompileStringExpression_Literal() throws PMException {
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression expression = Expression.fromString(visitorContext, "\"test\"", Type.string());
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(new StringLiteral("test"), expression);
    }

    @Test
    void testCompileStringExpression_VarRef() throws PMException {
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        visitorContext.scope().addVariable("test", new Variable("test", Type.string(), true));
        Expression expression = Expression.fromString(visitorContext, "test", Type.string());
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(new ReferenceByID("test"), expression);
    }

    @Test
    void testCompileStringExpression_FuncInvoke() throws PMException {
        CompileScope compileScope = new CompileScope();
        PMLFunctionSignature signature = new PMLFunctionSignature(
                "test",
                Type.string(),
                List.of()
        );
        compileScope.addFunction("test", signature);
        VisitorContext visitorContext = new VisitorContext(compileScope);

        Expression expression = Expression.fromString(visitorContext, "test()", Type.string());
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(new FunctionInvokeExpression(signature.getName(), List.of()), expression);
    }

    @Test
    void testCompileStringExpression_NonString_Error() throws PMException {
        PMLCompilationRuntimeException e = assertThrows(
                PMLCompilationRuntimeException.class,
                () -> Expression.fromString(new VisitorContext(new CompileScope()),
                        "\"test\" == \"test\"",
                        Type.string()
                )
        );
        assertEquals(1, e.getErrors().size());

        e = assertThrows(
                PMLCompilationRuntimeException.class,
                () -> Expression.fromString(new VisitorContext(new CompileScope()),
                        "[\"a\", \"b\"]",
                        Type.string()
                )
        );
        assertEquals(1, e.getErrors().size());
    }

}