package gov.nist.csd.pm.pap.pml.function;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.expression.FunctionInvokeExpression;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunctionSignature;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for OBJECT_TYPE integration with functions in PML.
 * These tests verify that OBJECT_TYPE works correctly with function
 * parameters and return values.
 */
public class ObjectTypeFunctionTest {

    @Mock
    private PAP pap;
    private ExecutionContext executionContext;

    @BeforeEach
    void setUp() throws PMException {
        MockitoAnnotations.openMocks(this);
        executionContext = new ExecutionContext(new TestUserContext("u1"), new MemoryPAP());
    }

    @Test
    void testFunctionWithObjectTypeParameter() throws PMException {
        // Create a function signature with an OBJECT_TYPE parameter
        PMLFunctionSignature functionSignature = new PMLBasicFunctionSignature(
                "testFunction",
                STRING_TYPE,  // Return type
                List.of(
                        new FormalParameter<>("arg", OBJECT_TYPE)
                )
        );

        // Add the function to the scope
        CompileScope scope = new CompileScope();
        scope.addFunction("testFunction", functionSignature);
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
            assertTrue(expr instanceof FunctionInvokeExpression);
            assertEquals(STRING_TYPE, expr.getType());
        }
    }

    @Test
    void testFunctionWithObjectTypeReturnValue() throws PMException {
        // Create a function signature that returns OBJECT_TYPE
        PMLFunctionSignature functionSignature = new PMLBasicFunctionSignature(
                "objectReturningFunction",
                OBJECT_TYPE,  // Return type
                List.of()
        );

        // Add the function to the scope
        CompileScope scope = new CompileScope();
        scope.addFunction("objectReturningFunction", functionSignature);
        VisitorContext visitorContext = new VisitorContext(scope);

        // Compile function call
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression("objectReturningFunction()");

        // Should compile without errors
        Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx, OBJECT_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertTrue(expr instanceof FunctionInvokeExpression);
        assertEquals(OBJECT_TYPE, expr.getType());

        // Test using the function result in different type contexts
        String[] testContexts = {
                "\"prefix_\" + objectReturningFunction()",  // String context (concatenation)
                "objectReturningFunction() == \"expected\"", // Equality comparison
                "[objectReturningFunction()]", // Array context
                "{\"key\": objectReturningFunction()}" // Map value context
        };

        for (String testExpr : testContexts) {
            PMLParser.ExpressionContext testCtx = TestPMLParser.parseExpression(testExpr);

            // Should compile without errors because OBJECT_TYPE is compatible with any expected type
            Expression<?> testExpression = ExpressionVisitor.compile(visitorContext, testCtx);
            assertEquals(0, visitorContext.errorLog().getErrors().size());
        }
    }

    @Test
    void testFunctionWithHeterogeneousCollectionParameters() throws PMException {
        // Create a function signature that takes heterogeneous collections
        PMLFunctionSignature functionSignature = new PMLBasicFunctionSignature(
                "collectionsFunction",
                OBJECT_TYPE,  // Return type
                Arrays.asList(
                        new FormalParameter<>("arrayArg", listType(OBJECT_TYPE)),
                        new FormalParameter<>("mapArg", mapType(STRING_TYPE, OBJECT_TYPE))
                )
        );

        // Add the function to the scope
        CompileScope scope = new CompileScope();
        scope.addFunction("collectionsFunction", functionSignature);
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
        Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx, OBJECT_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertTrue(expr instanceof FunctionInvokeExpression);
        assertEquals(OBJECT_TYPE, expr.getType());
    }

    @Test
    void testFunctionWithNestedObjectTypeParameters() throws PMException {
        // Create a function signature with nested OBJECT_TYPE parameters
        PMLFunctionSignature functionSignature = new PMLBasicFunctionSignature(
                "nestedFunction",
                listType(mapType(STRING_TYPE, OBJECT_TYPE)),  // Return type: list<map<string, object>>
                List.of(
                        new FormalParameter<>("complexArg",
                            mapType(STRING_TYPE, listType(OBJECT_TYPE)))
                )
        );

        // Add the function to the scope
        CompileScope scope = new CompileScope();
        scope.addFunction("nestedFunction", functionSignature);
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
                listType(mapType(STRING_TYPE, OBJECT_TYPE)));
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertTrue(expr instanceof FunctionInvokeExpression);
        assertEquals(listType(mapType(STRING_TYPE, OBJECT_TYPE)), expr.getType());
    }

    @Test
    void testFunctionWithTypeSpecificParameter() throws PMException {
        // Test a function that has mixed parameter types (some specific, some OBJECT_TYPE)
        PMLFunctionSignature functionSignature = new PMLBasicFunctionSignature(
                "mixedParamFunction",
                OBJECT_TYPE,
                Arrays.asList(
                        new FormalParameter<>("stringArg", STRING_TYPE),
                        new FormalParameter<>("objectArg", OBJECT_TYPE)
                )
        );

        // Add the function to the scope
        CompileScope scope = new CompileScope();
        scope.addFunction("mixedParamFunction", functionSignature);
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
            Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx, OBJECT_TYPE);
            assertEquals(0, visitorContext.errorLog().getErrors().size());
        }

        // Invalid call - first parameter must be a string
        String invalidCall = "mixedParamFunction(true, \"string\")";
        PMLParser.ExpressionContext invalidCtx = TestPMLParser.parseExpression(invalidCall);

        // Should have compilation errors
        Exception exception = assertThrows(PMLCompilationRuntimeException.class, () -> {
            ExpressionVisitor.compile(visitorContext, invalidCtx, OBJECT_TYPE);
        });
    }
} 