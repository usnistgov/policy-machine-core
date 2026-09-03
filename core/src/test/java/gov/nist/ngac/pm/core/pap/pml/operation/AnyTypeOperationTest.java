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

package gov.nist.ngac.pm.core.pap.pml.operation;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.MapType;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.pml.TestPMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.expression.OperationInvokeExpression;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import gov.nist.ngac.pm.core.pap.pml.scope.CompileScope;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class AnyTypeOperationTest {

    @Test
    void testOpWithObjectTypeParameter() throws PMException {
        PMLOperationSignature functionSignature = new PMLOperationSignature(
            OperationType.FUNCTION,
            "testOp",
            STRING_TYPE,
            List.of(
                new FormalParameter<>("a", ANY_TYPE)
            ),
            List.of());

        CompileScope scope = new CompileScope(new MemoryPAP());
        scope.addOperation("testOp", functionSignature);
        VisitorContext visitorContext = new VisitorContext(scope);

        String[] testCalls = {
            "testOp(a=\"string value\")",
            "testOp(a=\"value2\")",
            "testOp(a=true)",
            "testOp(a=[\"a\", \"b\", \"c\"])",
            "testOp(a={\"key\": \"value\"})"
        };

        for (String call : testCalls) {
            PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(call);

            Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx, STRING_TYPE);
            assertEquals(0, visitorContext.errorLog().getErrors().size());
            assertTrue(expr instanceof OperationInvokeExpression);
            assertEquals(STRING_TYPE, expr.getType());
        }
    }

    @Test
    void testOpWithObjectTypeReturnValue() throws PMException {
        PMLOperationSignature functionSignature = new PMLOperationSignature(
            OperationType.FUNCTION,
            "returningOp",
            ANY_TYPE,  
            List.of(),
            List.of());

        CompileScope scope = new CompileScope(new MemoryPAP());
        scope.addOperation("returningOp", functionSignature);
        VisitorContext visitorContext = new VisitorContext(scope);

        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression("returningOp()");

        Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx, ANY_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertTrue(expr instanceof OperationInvokeExpression);
        assertEquals(ANY_TYPE, expr.getType());

        String[] testContexts = {
            "\"prefix_\" + returningOp()", 
            "returningOp() == \"expected\"", 
            "[returningOp()]", 
            "{\"key\": returningOp()}" 
        };

        for (String testExpr : testContexts) {
            PMLParser.ExpressionContext testCtx = TestPMLParser.parseExpression(testExpr);

            Expression<?> testExpression = ExpressionVisitor.compile(visitorContext, testCtx);
            assertEquals(0, visitorContext.errorLog().getErrors().size());
        }
    }

    @Test
    void testOpWithHeterogeneousCollectionParameters() throws PMException {
        PMLOperationSignature functionSignature = new PMLOperationSignature(
            OperationType.FUNCTION,
            "listMapFunction",
            ANY_TYPE,
            Arrays.asList(
                new FormalParameter<>("a", ListType.of(ANY_TYPE)),
                new FormalParameter<>("b", MapType.of(STRING_TYPE, ANY_TYPE))
            ),
            List.of());

        CompileScope scope = new CompileScope(new MemoryPAP());
        scope.addOperation("listMapFunction", functionSignature);
        VisitorContext visitorContext = new VisitorContext(scope);

        String functionCall = """
                listMapFunction(
                    a=["string", "value", true],
                    b={
                        "string": "value",
                        "string2": "value2",
                        "boolean": true,
                        "array": ["a", "b", "c"]
                    }
                )
                """;

        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(functionCall);

        Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx, ANY_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertTrue(expr instanceof OperationInvokeExpression);
        assertEquals(ANY_TYPE, expr.getType());
    }

    @Test
    void testOpWithNestedObjectTypeParameters() throws PMException {
        PMLOperationSignature functionSignature = new PMLOperationSignature(
            OperationType.FUNCTION,
            "nestedFunction",
            ListType.of(MapType.of(STRING_TYPE, ANY_TYPE)),
            List.of(
                new FormalParameter<>("a",
                    MapType.of(STRING_TYPE, ListType.of(ANY_TYPE)))
            ),
            List.of());

        CompileScope scope = new CompileScope(new MemoryPAP());
        scope.addOperation("nestedFunction", functionSignature);
        VisitorContext visitorContext = new VisitorContext(scope);

        String functionCall = """
                nestedFunction(a={
                    "array1": ["one", "two", true],
                    "array2": [{
                        "nested": "value"
                    }, "value2", ["a", "b", "c"]]
                })
                """;

        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(functionCall);

        Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx,
            ListType.of(MapType.of(STRING_TYPE, ANY_TYPE)));
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertTrue(expr instanceof OperationInvokeExpression);
        assertEquals(ListType.of(MapType.of(STRING_TYPE, ANY_TYPE)), expr.getType());
    }

    @Test
    void testOpWithTypeSpecificParameter() throws PMException {
        PMLOperationSignature operationSignature = new PMLOperationSignature(
            OperationType.FUNCTION,
            "anyParamFunction",
            ANY_TYPE,
            Arrays.asList(
                new FormalParameter<>("a", STRING_TYPE),
                new FormalParameter<>("b", ANY_TYPE)
            ),
            List.of());

        CompileScope scope = new CompileScope(new MemoryPAP());
        scope.addOperation("anyParamFunction", operationSignature);
        VisitorContext visitorContext = new VisitorContext(scope);

        String[] validCalls = {
            "anyParamFunction(a=\"string\", b=\"value\")",
            "anyParamFunction(a=\"string\", b=true)",
            "anyParamFunction(a=\"string\", b=[\"a\", \"b\", \"c\"])",
            "anyParamFunction(a=\"string\", b={\"key\": \"value\"})"
        };

        for (String call : validCalls) {
            PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(call);

            Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx, ANY_TYPE);
            assertEquals(0, visitorContext.errorLog().getErrors().size());
        }

        String invalidCall = "anyParamFunction(true, \"string\")";
        PMLParser.ExpressionContext invalidCtx = TestPMLParser.parseExpression(invalidCall);

        assertThrows(PMLCompilationRuntimeException.class, () -> {
            ExpressionVisitor.compile(visitorContext, invalidCtx, ANY_TYPE);
        });
    }
} 