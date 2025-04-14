package gov.nist.csd.pm.pap.pml.expression;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.type.*;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.UnexpectedExpressionTypeException;
import gov.nist.csd.pm.pap.pml.expression.literal.*;
import gov.nist.csd.pm.pap.pml.expression.reference.VariableReferenceExpression;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests focused on error handling and edge cases for OBJECT_TYPE.
 * These tests ensure that the type system properly handles errors
 * and edge cases when dealing with OBJECT_TYPE.
 */
public class ObjectTypeErrorHandlingTest {

    @Mock
    private PAP pap;
    private ExecutionContext executionContext;

    @BeforeEach
    void setUp() throws PMException {
        MockitoAnnotations.openMocks(this);
        executionContext = new ExecutionContext(new TestUserContext("u1"), new MemoryPAP());
    }

    @Test
    void testNullValueHandling() {
        // Test that ObjectType.cast properly handles null
        assertNull(OBJECT_TYPE.cast(null));
        
        // Test that collections with null elements work correctly
        ObjectType objectType = new ObjectType();
        
        // Test casting null to various types should return null
        assertNull(objectType.castTo(null, STRING_TYPE));
        assertNull(objectType.castTo(null, BOOLEAN_TYPE));
        assertNull(objectType.castTo(null, listType(STRING_TYPE)));
        assertNull(objectType.castTo(null, mapType(STRING_TYPE, BOOLEAN_TYPE)));
    }
    
    @Test
    void testInvalidCastFailures() {
        // Test cases where cast operations should fail
        
        // Cast Boolean to String should fail in certain contexts
        ObjectType objectType = new ObjectType();
        Boolean boolValue = true;
        
        // Cast String to Boolean should fail
        String stringValue = "not a boolean";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            BOOLEAN_TYPE.cast(stringValue);
        });
        assertTrue(exception.getMessage().contains("Cannot cast"));
    }
    
    @Test
    void testObjectTypeCasting() throws PMException {
        // Test explicit casting behavior with OBJECT_TYPE
        
        // Create expressions of different types
        StringLiteralExpression stringExpr = new StringLiteralExpression("test");
        BoolLiteralExpression boolExpr = new BoolLiteralExpression(true);
        
        // Cast to OBJECT_TYPE
        Expression<Object> objectStringExpr = stringExpr.asType(OBJECT_TYPE);
        Expression<Object> objectBoolExpr = boolExpr.asType(OBJECT_TYPE);
        
        // Execute and verify the results
        assertEquals("test", objectStringExpr.execute(executionContext, pap));
        assertEquals(true, objectBoolExpr.execute(executionContext, pap));
        
        // Test that the original type info is preserved
        assertEquals(STRING_TYPE, stringExpr.getType());
        assertEquals(BOOLEAN_TYPE, boolExpr.getType());
    }
    
    @Test
    void testVariableWithObjectType() throws PMException {
        // Test that variables with OBJECT_TYPE can hold any value
        
        // Create a variable with OBJECT_TYPE
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        visitorContext.scope().addVariable("objVar", new Variable("objVar", OBJECT_TYPE, false));
        
        // Reference the variable with different expected types
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression("objVar");
        
        // Should work with OBJECT_TYPE
        Expression<?> expr1 = ExpressionVisitor.compile(visitorContext, ctx, OBJECT_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(new VariableReferenceExpression<>("objVar", OBJECT_TYPE), expr1);
        
        // Should also work with STRING_TYPE because OBJECT_TYPE is castable to any type
        Expression<?> expr2 = ExpressionVisitor.compile(visitorContext, ctx, STRING_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(new VariableReferenceExpression<>("objVar", OBJECT_TYPE), expr2);
        
        // Should also work with BOOLEAN_TYPE
        Expression<?> expr3 = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(new VariableReferenceExpression<>("objVar", OBJECT_TYPE), expr3);
        
        // Should work with collection types too
        Expression<?> expr4 = ExpressionVisitor.compile(visitorContext, ctx, listType(STRING_TYPE));
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(new VariableReferenceExpression<>("objVar", OBJECT_TYPE), expr4);
    }
    
    @Test
    void testCollectionTypeConsistency() throws PMException {
        // Test that collections maintain type consistency when using OBJECT_TYPE
        
        // Create a list with mixed types
        List<Expression<?>> elements = List.of(
                new StringLiteralExpression("text"),
                new StringLiteralExpression("another"),
                new BoolLiteralExpression(true)
        );
        
        // Create an array expression with OBJECT_TYPE
        ArrayLiteralExpression<?> array = new ArrayLiteralExpression<>(elements, OBJECT_TYPE);
        
        // Execute and verify the results
        List<?> result = array.execute(executionContext, pap);
        
        // The types should be preserved
        assertTrue(result.get(0) instanceof String);
        assertTrue(result.get(1) instanceof String);
        assertTrue(result.get(2) instanceof Boolean);
        
        // Values should be correct
        assertEquals("text", result.get(0));
        assertEquals("another", result.get(1));
        assertEquals(true, result.get(2));
    }
    
    @Test
    void testReferencingCollectionElements() throws PMException {
        // Test that elements from collections with OBJECT_TYPE can be referenced and used
        
        // Create a complex structure: a map with different types of values
        String pml = """
                {
                    "string": "text",
                    "string2": "another",
                    "boolean": true,
                    "array": ["a", "b", "c"],
                    "map": {"key": "value"}
                }
                """;
        
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(pml);
        
        // Compile with expected type of map<string, object>
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression<?> mapExpr = ExpressionVisitor.compile(visitorContext, ctx, mapType(STRING_TYPE, OBJECT_TYPE));
        
        // Execute the map
        Map<?, ?> result = (Map<?, ?>) mapExpr.execute(executionContext, pap);
        
        // Verify structure and types
        assertEquals(5, result.size());
        assertTrue(result.get("string") instanceof String);
        assertTrue(result.get("string2") instanceof String);
        assertTrue(result.get("boolean") instanceof Boolean);
        assertTrue(result.get("array") instanceof List);
        assertTrue(result.get("map") instanceof Map);
        
        // Verify nested elements
        List<?> array = (List<?>) result.get("array");
        assertEquals(3, array.size());
        assertEquals("a", array.get(0));
        
        Map<?, ?> map = (Map<?, ?>) result.get("map");
        assertEquals(1, map.size());
        assertEquals("value", map.get("key"));
    }
    
    @Test
    void testCastingBetweenCollectionTypes() throws PMException {
        // Test casting between collection types with OBJECT_TYPE
        
        // Create a list<string>
        List<Expression<?>> stringElements = List.of(
                new StringLiteralExpression("a"),
                new StringLiteralExpression("b")
        );
        ArrayLiteralExpression<String> stringArray = new ArrayLiteralExpression<>(stringElements, STRING_TYPE);
        
        // Cast to list<object>
        Expression<List<Object>> objectArray = stringArray.asType(listType(OBJECT_TYPE));
        
        // Execute and verify results
        List<Object> result = objectArray.execute(executionContext, pap);
        assertEquals(2, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        
        // Try to cast list<string> to something incompatible
        Exception exception = assertThrows(UnexpectedExpressionTypeException.class, () -> {
            stringArray.asType(mapType(STRING_TYPE, STRING_TYPE));
        });
        assertTrue(exception.getMessage().contains("expected"));
    }
    
    @Test
    void testObjectTypeInTypedContext() throws PMException {
        Map<Expression<?>, Expression<?>> entries = new HashMap<>();
        entries.put(new StringLiteralExpression("string"), new StringLiteralExpression("value"));
        entries.put(new StringLiteralExpression("boolean"), new BoolLiteralExpression(true));
        
        MapLiteralExpression<?, ?> map = new MapLiteralExpression<>(entries, STRING_TYPE, OBJECT_TYPE);
        
        Map<?, ?> result = map.execute(executionContext, pap);
        
        String stringValue = (String) result.get("string");
        Boolean boolValue = (Boolean) result.get("boolean");
        
        assertEquals("value", stringValue);
        assertEquals(true, boolValue);
    }
} 