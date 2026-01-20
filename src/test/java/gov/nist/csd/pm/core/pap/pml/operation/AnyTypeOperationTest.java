package gov.nist.csd.pm.core.pap.pml.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.arg.type.MapType;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.expression.OperationInvokeExpression;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for ANY_TYPE integration with functions in PML.
 * These tests verify that ANY_TYPE works correctly with function
 * parameters and return values.
 */
public class AnyTypeOperationTest {

    private ExecutionContext executionContext;

    @BeforeEach
    void setUp() throws PMException {
        executionContext = new ExecutionContext(new TestUserContext("u1"), new MemoryPAP());
    }

    @Test
    void testFunctionWithObjectTypeParameter() throws PMException {
        // Create a function signature with an ANY_TYPE parameter
        PMLOperationSignature functionSignature = new PMLOperationSignature(
            OperationType.FUNCTION,
            "testFunction",
            STRING_TYPE,  // Return type
            List.of(
                new FormalParameter<>("arg", ANY_TYPE)
            )
        );

        // Add the function to the scope
        CompileScope scope = new CompileScope(new MemoryPAP());
        scope.addOperation("testFunction", functionSignature);
        VisitorContext visitorContext = new VisitorContext(scope);

        // Test with different argument types
        String[] testCalls = {
            "testFunction(\"string value\")",
            "testFunction(\"value2\")",
            "testFunction(true)",
            "testFunction([\"a\", \"b\", \"c\"])",
            "testFunction({\"key\": \"value\"})"
        };

        for (String call : testCalls) {
            PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(call);

            // Should compile without errors
            Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx, STRING_TYPE);
            assertEquals(0, visitorContext.errorLog().getErrors().size());
            assertTrue(expr instanceof OperationInvokeExpression);
            assertEquals(STRING_TYPE, expr.getType());
        }
    }

    @Test
    void testFunctionWithObjectTypeReturnValue() throws PMException {
        // Create a function signature that returns ANY_TYPE
        PMLOperationSignature functionSignature = new PMLOperationSignature(
            OperationType.FUNCTION,
            "objectReturningFunction",
            ANY_TYPE,  // Return type
            List.of()
        );

        // Add the function to the scope
        CompileScope scope = new CompileScope(new MemoryPAP());
        scope.addOperation("objectReturningFunction", functionSignature);
        VisitorContext visitorContext = new VisitorContext(scope);

        // Compile function call
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression("objectReturningFunction()");

        // Should compile without errors
        Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx, ANY_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertTrue(expr instanceof OperationInvokeExpression);
        assertEquals(ANY_TYPE, expr.getType());

        // Test using the function result in different type contexts
        String[] testContexts = {
            "\"prefix_\" + objectReturningFunction()",  // String context (concatenation)
            "objectReturningFunction() == \"expected\"", // Equality comparison
            "[objectReturningFunction()]", // Array context
            "{\"key\": objectReturningFunction()}" // Map value context
        };

        for (String testExpr : testContexts) {
            PMLParser.ExpressionContext testCtx = TestPMLParser.parseExpression(testExpr);

            // Should compile without errors because ANY_TYPE is compatible with any expected type
            Expression<?> testExpression = ExpressionVisitor.compile(visitorContext, testCtx);
            assertEquals(0, visitorContext.errorLog().getErrors().size());
        }
    }

    @Test
    void testFunctionWithHeterogeneousCollectionParameters() throws PMException {
        // Create a function signature that takes heterogeneous collections
        PMLOperationSignature functionSignature = new PMLOperationSignature(
            OperationType.FUNCTION,
            "collectionsFunction",
            ANY_TYPE,  // Return type
            Arrays.asList(
                new FormalParameter<>("arrayArg", ListType.of(ANY_TYPE)),
                new FormalParameter<>("mapArg", MapType.of(STRING_TYPE, ANY_TYPE))
            )
        );

        // Add the function to the scope
        CompileScope scope = new CompileScope(new MemoryPAP());
        scope.addOperation("collectionsFunction", functionSignature);
        VisitorContext visitorContext = new VisitorContext(scope);

        // Compile function call with heterogeneous collections
        String functionCall = """
                collectionsFunction(
                    ["string", "value", true],
                    {
                        "string": "value",
                        "string2": "value2",
                        "boolean": true,
                        "array": ["a", "b", "c"]
                    }
                )
                """;

        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(functionCall);

        // Should compile without errors
        Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx, ANY_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertTrue(expr instanceof OperationInvokeExpression);
        assertEquals(ANY_TYPE, expr.getType());
    }

    @Test
    void testFunctionWithNestedObjectTypeParameters() throws PMException {
        // Create a function signature with nested ANY_TYPE parameters
        PMLOperationSignature functionSignature = new PMLOperationSignature(
            OperationType.FUNCTION,
            "nestedFunction",
            ListType.of(MapType.of(STRING_TYPE, ANY_TYPE)),  // Return type: list<map<string, object>>
            List.of(
                new FormalParameter<>("complexArg",
                    MapType.of(STRING_TYPE, ListType.of(ANY_TYPE)))
            )
        );

        // Add the function to the scope
        CompileScope scope = new CompileScope(new MemoryPAP());
        scope.addOperation("nestedFunction", functionSignature);
        VisitorContext visitorContext = new VisitorContext(scope);

        // Compile function call with a complex nested structure
        String functionCall = """
                nestedFunction({
                    "array1": ["one", "two", true],
                    "array2": [{
                        "nested": "value"
                    }, "value2", ["a", "b", "c"]]
                })
                """;

        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(functionCall);

        // Should compile without errors
        Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx,
            ListType.of(MapType.of(STRING_TYPE, ANY_TYPE)));
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertTrue(expr instanceof OperationInvokeExpression);
        assertEquals(ListType.of(MapType.of(STRING_TYPE, ANY_TYPE)), expr.getType());
    }

    @Test
    void testFunctionWithTypeSpecificParameter() throws PMException {
        // Test a function that has mixed parameter types (some specific, some ANY_TYPE)
        PMLOperationSignature functionSignature = new PMLOperationSignature(
            OperationType.FUNCTION,
            "mixedParamFunction",
            ANY_TYPE,
            Arrays.asList(
                new FormalParameter<>("stringArg", STRING_TYPE),
                new FormalParameter<>("objectArg", ANY_TYPE)
            )
        );

        // Add the function to the scope
        CompileScope scope = new CompileScope(new MemoryPAP());
        scope.addOperation("mixedParamFunction", functionSignature);
        VisitorContext visitorContext = new VisitorContext(scope);

        // Valid calls - first parameter must be a string, second can be anything
        String[] validCalls = {
            "mixedParamFunction(\"string\", \"value\")",
            "mixedParamFunction(\"string\", true)",
            "mixedParamFunction(\"string\", [\"a\", \"b\", \"c\"])",
            "mixedParamFunction(\"string\", {\"key\": \"value\"})"
        };

        for (String call : validCalls) {
            PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(call);

            // Should compile without errors
            Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx, ANY_TYPE);
            assertEquals(0, visitorContext.errorLog().getErrors().size());
        }

        // Invalid call - first parameter must be a string
        String invalidCall = "mixedParamFunction(true, \"string\")";
        PMLParser.ExpressionContext invalidCtx = TestPMLParser.parseExpression(invalidCall);

        // Should have compilation errors
        Exception exception = assertThrows(PMLCompilationRuntimeException.class, () -> {
            ExpressionVisitor.compile(visitorContext, invalidCtx, ANY_TYPE);
        });
    }
} 