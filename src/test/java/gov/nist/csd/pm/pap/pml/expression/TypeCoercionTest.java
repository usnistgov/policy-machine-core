package gov.nist.csd.pm.pap.pml.expression;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.literal.*;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;

import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for type coercion between compatible types when using OBJECT_TYPE.
 * These tests ensure that automatic type conversion works correctly
 * in expressions that involve OBJECT_TYPE.
 */
public class TypeCoercionTest {

    @Mock
    private PAP pap;
    private ExecutionContext executionContext;

    @BeforeEach
    void setUp() throws PMException {
        MockitoAnnotations.openMocks(this);
        executionContext = new ExecutionContext(new TestUserContext("u1"), new MemoryPAP());
    }

    @Test
    void testStringToObjectCoercion() throws PMException {
        // Test implicit coercion from STRING_TYPE to OBJECT_TYPE
        StringLiteralExpression stringExpr = new StringLiteralExpression("test");
        Expression<Object> objectExpr = stringExpr.asType(OBJECT_TYPE);
        
        // Execute and verify
        Object result = objectExpr.execute(executionContext, pap);
        assertEquals("test", result);
        assertTrue(result instanceof String);
        
        // Verify original type is preserved
        assertEquals(STRING_TYPE, stringExpr.getType());
    }

    @Test
    void testNumberToString() throws PMException {
        // PML should allow number to string coercion in concatenation
        String pml = "\"The answer is \" + 42";
        PMLParser.ExpressionContext ctx = TestPMLParser.toCtx(
                pml,
                PMLParser.ExpressionContext.class);
        
        // Compile with expected type STRING
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx, STRING_TYPE);
        
        // Should compile without errors
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        
        // Execute and verify
        Object result = expr.execute(executionContext, pap);
        assertEquals("The answer is 42", result);
    }

    @Test
    void testListTypeCoercion() throws PMException {
        // Test coercion between collection types
        // Create a list of strings
        List<Expression<?>> stringElements = new ArrayList<>();
        stringElements.add(new StringLiteralExpression("a"));
        stringElements.add(new StringLiteralExpression("b"));
        
        // Create as STRING_TYPE array
        ArrayLiteralExpression<String> stringArray = new ArrayLiteralExpression<>(stringElements, STRING_TYPE);
        
        // Coerce to OBJECT_TYPE array
        Expression<List<Object>> objectArray = stringArray.asType(listType(OBJECT_TYPE));
        
        // Execute and verify
        List<Object> result = objectArray.execute(executionContext, pap);
        assertEquals(2, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        
        // Verify original type is preserved
        assertEquals(listType(STRING_TYPE), stringArray.getType());
    }

    @Test
    void testMapTypeCoercion() throws PMException {
        // Test coercion between map types
        // Create a map with string keys and values
        Map<Expression<?>, Expression<?>> entries = new HashMap<>();
        entries.put(new StringLiteralExpression("key1"), new StringLiteralExpression("value1"));
        entries.put(new StringLiteralExpression("key2"), new StringLiteralExpression("value2"));
        
        // Create as STRING_TYPE -> STRING_TYPE map
        MapLiteralExpression<String, String> stringMap = 
                new MapLiteralExpression<>(entries, STRING_TYPE, STRING_TYPE);
        
        // Coerce to STRING_TYPE -> OBJECT_TYPE map
        Expression<Map<String, Object>> objectMap = 
                stringMap.asType(mapType(STRING_TYPE, OBJECT_TYPE));
        
        // Execute and verify
        Map<String, Object> result = objectMap.execute(executionContext, pap);
        assertEquals(2, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        
        // Verify original type is preserved
        assertEquals(mapType(STRING_TYPE, STRING_TYPE), stringMap.getType());
    }

    @Test
    void testNestedCollectionCoercion() throws PMException {
        // Test coercion with nested collections
        
        // Create a deeply nested structure
        // Inner array (string type)
        List<Expression<?>> innerElements = new ArrayList<>();
        innerElements.add(new StringLiteralExpression("inner1"));
        innerElements.add(new StringLiteralExpression("inner2"));
        ArrayLiteralExpression<String> innerArray = 
                new ArrayLiteralExpression<>(innerElements, STRING_TYPE);
        
        // Outer array containing the inner array (typed as list<list<string>>)
        List<Expression<?>> outerElements = new ArrayList<>();
        outerElements.add(innerArray);
        ArrayLiteralExpression<List<String>> outerArray = 
                new ArrayLiteralExpression<>(outerElements, listType(STRING_TYPE));
        
        // Coerce to list<list<object>>
        Expression<List<List<Object>>> coercedArray = 
                outerArray.asType(listType(listType(OBJECT_TYPE)));
        
        // Execute and verify
        List<List<Object>> result = coercedArray.execute(executionContext, pap);
        assertEquals(1, result.size());
        List<Object> innerResult = result.get(0);
        assertEquals(2, innerResult.size());
        assertEquals("inner1", innerResult.get(0));
        assertEquals("inner2", innerResult.get(1));
    }

    @Test
    void testCoercionInPMLCode() throws PMException {
        // Test automatic coercion in PML code
        String pml = """
                {
                    "stringList": ["a", "b", "c"],
                    "mixedList": ["x", 42, true]
                }
                """;
        
        PMLParser.ExpressionContext ctx = TestPMLParser.toCtx(
                pml,
                PMLParser.ExpressionContext.class);
        
        // Compile with expected type of map<string, list<object>>
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx, 
                mapType(STRING_TYPE, listType(OBJECT_TYPE)));
        
        // Should compile without errors
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        
        // Execute and verify
        Map<?, ?> result = (Map<?, ?>) expr.execute(executionContext, pap);
        assertEquals(2, result.size());
        
        // Check stringList
        List<?> stringList = (List<?>) result.get("stringList");
        assertEquals(3, stringList.size());
        assertEquals("a", stringList.get(0));
        assertEquals("b", stringList.get(1));
        assertEquals("c", stringList.get(2));
        
        // Check mixedList
        List<?> mixedList = (List<?>) result.get("mixedList");
        assertEquals(3, mixedList.size());
        assertEquals("x", mixedList.get(0));
        assertEquals(42L, mixedList.get(1));
        assertEquals(true, mixedList.get(2));
    }

    @Test
    void testCoercionWithComplexExpressions() throws PMException {
        // Test coercion in more complex expressions
        String pml = """
                {
                    "result": ["prefix_" + 1, 2 + 3, true && false]
                }
                """;
        
        PMLParser.ExpressionContext ctx = TestPMLParser.toCtx(
                pml,
                PMLParser.ExpressionContext.class);
        
        // Compile with expected type
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx, 
                mapType(STRING_TYPE, listType(OBJECT_TYPE)));
        
        // Should compile without errors
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        
        // Execute and verify
        Map<?, ?> result = (Map<?, ?>) expr.execute(executionContext, pap);
        List<?> resultList = (List<?>) result.get("result");
        
        assertEquals(3, resultList.size());
        assertEquals("prefix_1", resultList.get(0));  // String concatenation
        assertEquals(5L, resultList.get(1));         // Numeric addition
        assertEquals(false, resultList.get(2));     // Boolean operation
    }
    
    @Test
    void testObjectToPrimitiveTypeCoercion() throws PMException {
        // Test that values from OBJECT_TYPE can be coerced to primitive types
        String pml = """
                {
                    "obj1": "string value",
                    "obj2": 42,
                    "obj3": true
                }
                """;
        
        PMLParser.ExpressionContext ctx = TestPMLParser.toCtx(
                pml,
                PMLParser.ExpressionContext.class);
        
        // Compile with map<string, object> type
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx, 
                mapType(STRING_TYPE, OBJECT_TYPE));
        
        // Should compile without errors
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        
        // Execute to get the heterogeneous map
        Map<?, ?> result = (Map<?, ?>) expr.execute(executionContext, pap);
        
        // Now get individual values and verify their types
        String stringValue = (String) result.get("obj1");
        Long numValue = (Long) result.get("obj2");
        Boolean boolValue = (Boolean) result.get("obj3");
        
        assertEquals("string value", stringValue);
        assertEquals(42L, numValue);
        assertEquals(true, boolValue);
    }
} 