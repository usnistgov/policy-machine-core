package gov.nist.csd.pm.pap.pml.expression;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.pap.pml.expression.reference.VariableReferenceExpression;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunctionSignature;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;

import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.listType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpressionTest {

    @Test
    void testAllowedTypes() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                a
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        visitorContext.scope().addVariable("a", new Variable("a", STRING_TYPE, false));
        Expression<?> actual = ExpressionVisitor.compile(visitorContext, ctx, STRING_TYPE);
        assertEquals(
                new VariableReferenceExpression<>("a", STRING_TYPE),
                actual
        );

        ctx = TestPMLParser.parseExpression(
                """
                a
                """);
        visitorContext = new VisitorContext(new CompileScope());
        visitorContext.scope().addVariable("a", new Variable("a", listType(STRING_TYPE), false));
        actual = ExpressionVisitor.compile(visitorContext, ctx, listType(STRING_TYPE));
        assertEquals(
                new VariableReferenceExpression<>("a", listType(STRING_TYPE)),
                actual
        );
    }

    @Test
    void testDisallowedTypes() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                a
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        visitorContext.scope().addVariable("a", new Variable("a", STRING_TYPE, false));
        PMLCompilationRuntimeException e = assertThrows(
                PMLCompilationRuntimeException.class,
                () -> ExpressionVisitor.compile(visitorContext, ctx, listType(STRING_TYPE))
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
        Expression<?> expression = ExpressionVisitor.fromString(visitorContext, "\"test\"", STRING_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(new StringLiteralExpression("test"), expression);
    }

    @Test
    void testCompileStringExpression_VarRef() throws PMException {
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        visitorContext.scope().addVariable("test", new Variable("test", STRING_TYPE, true));
        Expression<?> expression = ExpressionVisitor.fromString(visitorContext, "test", STRING_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(new VariableReferenceExpression<>("test", STRING_TYPE), expression);
    }

    @Test
    void testCompileStringExpression_FuncInvoke() throws PMException {
        CompileScope compileScope = new CompileScope();
        PMLFunctionSignature signature = new PMLBasicFunctionSignature(
                "test",
                STRING_TYPE,
                List.of()
        );
        compileScope.addFunction("test", signature);
        VisitorContext visitorContext = new VisitorContext(compileScope);

        Expression<?> expression = ExpressionVisitor.fromString(visitorContext, "test()", STRING_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(new FunctionInvokeExpression(signature, List.of(), signature.getReturnType()), expression);
    }

    @Test
    void testCompileStringExpression_NonString_Error() throws PMException {
        PMLCompilationRuntimeException e = assertThrows(
                PMLCompilationRuntimeException.class,
                () -> ExpressionVisitor.fromString(new VisitorContext(new CompileScope()),
                        "\"test\" == \"test\"",
                        STRING_TYPE
                )
        );
        assertEquals(1, e.getErrors().size());

        e = assertThrows(
                PMLCompilationRuntimeException.class,
                () -> ExpressionVisitor.fromString(new VisitorContext(new CompileScope()),
                        "[\"a\", \"b\"]",
                        STRING_TYPE
                )
        );
        assertEquals(1, e.getErrors().size());
    }

}