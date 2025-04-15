package gov.nist.csd.pm.pap.pml.expression;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.type.*;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.literal.*;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static gov.nist.csd.pm.pap.function.arg.type.Type.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the ArgType system, focusing on compatibility with ANY_TYPE
 * and ensuring all types work correctly with ANY_TYPE as the expected type.
 */
public class TypeCompatibilityTest {

    private PAP pap;
    private ExecutionContext executionContext;

    @BeforeEach
    void setUp() throws PMException {
        executionContext = new ExecutionContext(new TestUserContext("u1"), new MemoryPAP());
    }

    // ==================== Unit Tests for ArgType Compatibility ====================

    @Test
    void testAllTypesCastableToObjectType() {
        // All types should be castable to ANY_TYPE
        assertTrue(STRING_TYPE.isCastableTo(ANY_TYPE));
        assertTrue(BOOLEAN_TYPE.isCastableTo(ANY_TYPE));
        assertTrue(ACCESS_RIGHT_SET_TYPE.isCastableTo(ANY_TYPE));
        assertTrue(OPERATION_TYPE.isCastableTo(ANY_TYPE));
        assertTrue(ROUTINE_TYPE.isCastableTo(ANY_TYPE));
        assertTrue(RULE_TYPE.isCastableTo(ANY_TYPE));
        assertTrue(PROHIBITION_SUBJECT_TYPE.isCastableTo(ANY_TYPE));
        assertTrue(CONTAINER_CONDITION_TYPE.isCastableTo(ANY_TYPE));
        assertTrue(NODE_TYPE_TYPE.isCastableTo(ANY_TYPE));
        
        // Collection types should also be castable to ANY_TYPE
        assertTrue(listType(STRING_TYPE).isCastableTo(ANY_TYPE));
        assertTrue(mapType(STRING_TYPE, BOOLEAN_TYPE).isCastableTo(ANY_TYPE));
        
        // ANY_TYPE should be castable to itself
        assertTrue(ANY_TYPE.isCastableTo(ANY_TYPE));

        // Nested collection types should be castable to ANY_TYPE
        assertTrue(listType(mapType(STRING_TYPE, BOOLEAN_TYPE)).isCastableTo(ANY_TYPE));
        assertTrue(mapType(STRING_TYPE, listType(BOOLEAN_TYPE)).isCastableTo(ANY_TYPE));
    }

    @Test
    void testObjectTypeAsSourceType() {
        // ANY_TYPE should be castable to any other type in isCastableTo
        // This intentionally tests the first condition in isCastableTo method
        assertTrue(ANY_TYPE.isCastableTo(STRING_TYPE));
        assertTrue(ANY_TYPE.isCastableTo(BOOLEAN_TYPE));
        assertTrue(ANY_TYPE.isCastableTo(listType(STRING_TYPE)));
        assertTrue(ANY_TYPE.isCastableTo(mapType(STRING_TYPE, BOOLEAN_TYPE)));
    }

    @Test
    void testObjectTypeCastMethod() {
        // Test that ObjectType.cast accepts any value
        Object stringValue = "test";
        Object booleanValue = true;
        Object listValue = Arrays.asList("a", "b", "c");
        Object mapValue = Map.of("key1", "value1", "key2", "value2");
        
        assertEquals(stringValue, ANY_TYPE.cast(stringValue));
        assertEquals(booleanValue, ANY_TYPE.cast(booleanValue));
        assertEquals(listValue, ANY_TYPE.cast(listValue));
        assertEquals(mapValue, ANY_TYPE.cast(mapValue));
        
        // Test null handling
        assertNull(ANY_TYPE.cast(null));
    }

    @Test
    void testObjectTypeCastToMethod() {
        // Test the specialized castTo method in ObjectType
        AnyType anyType = new AnyType();
        
        String stringValue = "test";
        Boolean booleanValue = true;
        
        // Cast from Object to specific types
        assertEquals(stringValue, anyType.castTo(stringValue, STRING_TYPE));
        assertEquals(booleanValue, anyType.castTo(booleanValue, BOOLEAN_TYPE));
        
        // Test null handling
        assertNull(anyType.castTo(null, STRING_TYPE));
    }

    // ==================== Expression Compilation Tests ====================

    @Test
    void testCompileExpressionsWithObjectTypeExpected() throws PMException {
        // Test compiling various expressions when the expected type is ANY_TYPE
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        
        // String literal
        PMLParser.ExpressionContext stringCtx = TestPMLParser.parseExpression("\"test\"");
        Expression<?> stringExpr = ExpressionVisitor.compile(visitorContext, stringCtx, ANY_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(STRING_TYPE, stringExpr.getType());
        
        // Boolean literal
        PMLParser.ExpressionContext boolCtx = TestPMLParser.parseExpression("true");
        Expression<?> boolExpr = ExpressionVisitor.compile(visitorContext, boolCtx, ANY_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(BOOLEAN_TYPE, boolExpr.getType());
    }

    @Test
    void testCompileHeterogeneousCollections() throws PMException {
        // Test compiling heterogeneous collections (arrays and maps)
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        
        // Array with mixed types
        PMLParser.ExpressionContext arrayCtx = TestPMLParser.parseExpression("""
                ["a", "value", true]
                """);
        Expression<?> arrayExpr = ExpressionVisitor.compile(visitorContext, arrayCtx, listType(ANY_TYPE));
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertTrue(arrayExpr instanceof ArrayLiteralExpression);
        assertEquals(listType(ANY_TYPE), arrayExpr.getType());
        
        // Map with mixed value types
        PMLParser.ExpressionContext mapCtx = TestPMLParser.parseExpression("""
                {
                    "string": "value",
                    "string2": "value2",
                    "boolean": true
                }
                """);
        Expression<?> mapExpr = ExpressionVisitor.compile(visitorContext, mapCtx, mapType(STRING_TYPE, ANY_TYPE));
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertTrue(mapExpr instanceof MapLiteralExpression);
        assertEquals(mapType(STRING_TYPE, ANY_TYPE), mapExpr.getType());
    }

    // ==================== Expression Execution Tests ====================

    @Test
    void testExecuteExpressionsWithObjectType() throws PMException {
        // String literal
        StringLiteralExpression stringExpr = new StringLiteralExpression("test");
        Object stringResult = stringExpr.asType(ANY_TYPE).execute(executionContext, pap);
        assertEquals("test", stringResult);

        // Boolean literal
        BoolLiteralExpression boolExpr = new BoolLiteralExpression(true);
        Object boolResult = boolExpr.asType(ANY_TYPE).execute(executionContext, pap);
        assertEquals(true, boolResult);
    }

    @Test
    void testExecuteHeterogeneousCollections() throws PMException {
        // Create and execute a heterogeneous array expression
        List<Expression<?>> arrayElements = new ArrayList<>();
        arrayElements.add(new StringLiteralExpression("string"));
        arrayElements.add(new StringLiteralExpression("value"));
        arrayElements.add(new BoolLiteralExpression(true));
        
        ArrayLiteralExpression<?> arrayExpr = new ArrayLiteralExpression<>(arrayElements, ANY_TYPE);
        List<?> arrayResult = arrayExpr.execute(executionContext, pap);
        
        assertEquals(3, arrayResult.size());
        assertEquals("string", arrayResult.get(0));
        assertEquals("value", arrayResult.get(1));
        assertEquals(true, arrayResult.get(2));
        
        // Create and execute a heterogeneous map expression
        Map<Expression<?>, Expression<?>> mapEntries = new HashMap<>();
        mapEntries.put(new StringLiteralExpression("string"), new StringLiteralExpression("value"));
        mapEntries.put(new StringLiteralExpression("string2"), new StringLiteralExpression("value2"));
        mapEntries.put(new StringLiteralExpression("boolean"), new BoolLiteralExpression(true));
        
        MapLiteralExpression<?, ?> mapExpr = new MapLiteralExpression<>(mapEntries, STRING_TYPE, ANY_TYPE);
        Map<?, ?> mapResult = mapExpr.execute(executionContext, pap);
        
        assertEquals(3, mapResult.size());
        assertEquals("value", mapResult.get("string"));
        assertEquals("value2", mapResult.get("string2"));
        assertEquals(true, mapResult.get("boolean"));
    }

    // ==================== Integration Tests ====================

    @Test
    void testNestedHeterogeneousStructures() throws PMException {
        // Create a complex nested structure with heterogeneous types
        // Map containing arrays and other maps with mixed types
        
        // Inner array 1
        List<Expression<?>> innerArray1Elements = new ArrayList<>();
        innerArray1Elements.add(new StringLiteralExpression("a"));
        innerArray1Elements.add(new BoolLiteralExpression(true));
        ArrayLiteralExpression<?> innerArray1 = new ArrayLiteralExpression<>(innerArray1Elements, ANY_TYPE);
        
        // Inner array 2
        List<Expression<?>> innerArray2Elements = new ArrayList<>();
        innerArray2Elements.add(new BoolLiteralExpression(true));
        innerArray2Elements.add(new StringLiteralExpression("b"));
        ArrayLiteralExpression<?> innerArray2 = new ArrayLiteralExpression<>(innerArray2Elements, ANY_TYPE);
        
        // Inner map
        Map<Expression<?>, Expression<?>> innerMapEntries = new HashMap<>();
        innerMapEntries.put(new StringLiteralExpression("key1"), new StringLiteralExpression("value1"));
        innerMapEntries.put(new StringLiteralExpression("key2"), new BoolLiteralExpression(true));
        MapLiteralExpression<?, ?> innerMap = new MapLiteralExpression<>(innerMapEntries, STRING_TYPE, ANY_TYPE);
        
        // Outer map
        Map<Expression<?>, Expression<?>> outerMapEntries = new HashMap<>();
        outerMapEntries.put(new StringLiteralExpression("array1"), innerArray1);
        outerMapEntries.put(new StringLiteralExpression("array2"), innerArray2);
        outerMapEntries.put(new StringLiteralExpression("map"), innerMap);
        outerMapEntries.put(new StringLiteralExpression("primitive"), new BoolLiteralExpression(false));
        
        MapLiteralExpression<?, ?> outerMap = new MapLiteralExpression<>(outerMapEntries, STRING_TYPE, ANY_TYPE);
        
        // Execute the complex structure
        Map<?, ?> result = outerMap.execute(executionContext, pap);
        
        // Verify the complex structure was correctly executed
        assertEquals(4, result.size());
        
        // Verify array1
        List<?> array1Result = (List<?>) result.get("array1");
        assertEquals(2, array1Result.size());
        assertEquals("a", array1Result.get(0));
        assertEquals(true, array1Result.get(1));
        
        // Verify array2
        List<?> array2Result = (List<?>) result.get("array2");
        assertEquals(2, array2Result.size());
        assertEquals(true, array2Result.get(0));
        assertEquals("b", array2Result.get(1));
        
        // Verify inner map
        Map<?, ?> mapResult = (Map<?, ?>) result.get("map");
        assertEquals(2, mapResult.size());
        assertEquals("value1", mapResult.get("key1"));
        assertEquals(true, mapResult.get("key2"));
        
        // Verify primitive
        assertEquals(false, result.get("primitive"));
    }

    // ==================== Edge Case Tests ====================

    @Test
    void testEmptyCollections() throws PMException {
        // Empty array
        List<Expression<?>> emptyArrayElements = new ArrayList<>();
        ArrayLiteralExpression<?> emptyArrayExpr = new ArrayLiteralExpression<>(emptyArrayElements, ANY_TYPE);
        List<?> emptyArrayResult = emptyArrayExpr.execute(executionContext, pap);
        assertTrue(emptyArrayResult.isEmpty());
        
        // Empty map
        Map<Expression<?>, Expression<?>> emptyMapEntries = new HashMap<>();
        MapLiteralExpression<?, ?> emptyMapExpr = new MapLiteralExpression<>(emptyMapEntries, ANY_TYPE, ANY_TYPE);
        Map<?, ?> emptyMapResult = emptyMapExpr.execute(executionContext, pap);
        assertTrue(emptyMapResult.isEmpty());
    }

    @Test
    void testNullValues() throws PMException {
        // Create a map with a null value (represented as an ObjectLiteralExpression that returns null)
        Expression<?> nullExpr = new Expression<Object>() {
            @Override
            public Type<Object> getType() {
                return ANY_TYPE;
            }

            @Override
            public Object execute(ExecutionContext ctx, PAP pap) {
                return null;
            }

            @Override
            public int hashCode() {
                return Objects.hash(getType());
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                return true;
            }

            @Override
            public String toFormattedString(int indentLevel) {
                return "null";
            }
        };
        
        Map<Expression<?>, Expression<?>> mapEntries = new HashMap<>();
        mapEntries.put(new StringLiteralExpression("nullKey"), nullExpr);
        mapEntries.put(new StringLiteralExpression("nonNullKey"), new StringLiteralExpression("value"));
        
        MapLiteralExpression<?, ?> mapExpr = new MapLiteralExpression<>(mapEntries, STRING_TYPE, ANY_TYPE);
        Map<?, ?> mapResult = mapExpr.execute(executionContext, pap);
        
        assertEquals(2, mapResult.size());
        assertNull(mapResult.get("nullKey"));
        assertEquals("value", mapResult.get("nonNullKey"));
    }
} 