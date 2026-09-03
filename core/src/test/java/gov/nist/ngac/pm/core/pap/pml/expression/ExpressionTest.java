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

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.pml.TestPMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.compiler.Variable;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.reference.VariableReferenceExpression;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import gov.nist.ngac.pm.core.pap.pml.scope.CompileScope;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ExpressionTest {

    @Test
    void testAllowedTypes() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
            """
            a
            """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
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
        visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        visitorContext.scope().addVariable("a", new Variable("a", ListType.of(STRING_TYPE), false));
        actual = ExpressionVisitor.compile(visitorContext, ctx, ListType.of(STRING_TYPE));
        assertEquals(
            new VariableReferenceExpression<>("a", ListType.of(STRING_TYPE)),
            actual
        );
    }

    @Test
    void testDisallowedTypes() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
            """
            a
            """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        visitorContext.scope().addVariable("a", new Variable("a", STRING_TYPE, false));
        PMLCompilationRuntimeException e = assertThrows(
            PMLCompilationRuntimeException.class,
            () -> ExpressionVisitor.compile(visitorContext, ctx, ListType.of(STRING_TYPE))
        );
        assertEquals(1, e.getErrors().size());
        assertEquals(
            "expected expression type []string, got string",
            e.getErrors().get(0).errorMessage()
        );
    }


    @Test
    void testCompileStringExpression_Literal() throws PMException {
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression<?> expression = ExpressionVisitor.fromString(visitorContext, "\"test\"", STRING_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(new StringLiteralExpression("test"), expression);
    }

    @Test
    void testCompileStringExpression_VarRef() throws PMException {
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        visitorContext.scope().addVariable("test", new Variable("test", STRING_TYPE, true));
        Expression<?> expression = ExpressionVisitor.fromString(visitorContext, "test", STRING_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(new VariableReferenceExpression<>("test", STRING_TYPE), expression);
    }

    @Test
    void testCompileStringExpression_FuncInvoke() throws PMException {
        CompileScope compileScope = new CompileScope(new MemoryPAP());
        PMLOperationSignature signature = new PMLOperationSignature(
            OperationType.FUNCTION,
            "test",
            STRING_TYPE,
            List.of(),
            List.of());
        compileScope.addOperation("test", signature);
        VisitorContext visitorContext = new VisitorContext(compileScope);

        Expression<?> expression = ExpressionVisitor.fromString(visitorContext, "test()", STRING_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(new OperationInvokeExpression<>(signature.getName(), Map.of(), signature.getReturnType()), expression);
    }

    @Test
    void testCompileStringExpression_NonString_Error() throws PMException {
        PMLCompilationRuntimeException e = assertThrows(
            PMLCompilationRuntimeException.class,
            () -> ExpressionVisitor.fromString(new VisitorContext(new CompileScope(new MemoryPAP())),
                "\"test\" == \"test\"",
                STRING_TYPE
            )
        );
        assertEquals(1, e.getErrors().size());

        e = assertThrows(
            PMLCompilationRuntimeException.class,
            () -> ExpressionVisitor.fromString(new VisitorContext(new CompileScope(new MemoryPAP())),
                "[\"a\", \"b\"]",
                STRING_TYPE
            )
        );
        assertEquals(1, e.getErrors().size());
    }
}