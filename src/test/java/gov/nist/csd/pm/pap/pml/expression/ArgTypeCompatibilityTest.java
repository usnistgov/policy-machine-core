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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the ArgType system, focusing on compatibility with OBJECT_TYPE
 * and ensuring all types work correctly with OBJECT_TYPE as the expected type.
 */
public class ArgTypeCompatibilityTest {

    @Mock
    private PAP pap;
    private ExecutionContext executionContext;

    @BeforeEach
    void setUp() throws PMException {
        MockitoAnnotations.openMocks(this);
        executionContext = new ExecutionContext(new TestUserContext("u1"), new MemoryPAP());
    }

    // ==================== Unit Tests for ArgType Compatibility ====================

    @Test
    void testAllTypesCastableToObjectType() {
        // All types should be castable to OBJECT_TYPE
        assertTrue(STRING_TYPE.isCastableTo(OBJECT_TYPE));
        assertTrue(BOOLEAN_TYPE.isCastableTo(OBJECT_TYPE));
        assertTrue(ACCESS_RIGHT_SET_TYPE.isCastableTo(OBJECT_TYPE));
        assertTrue(OPERATION_TYPE.isCastableTo(OBJECT_TYPE));
        assertTrue(ROUTINE_TYPE.isCastableTo(OBJECT_TYPE));
        assertTrue(RULE_TYPE.isCastableTo(OBJECT_TYPE));
        assertTrue(PROHIBITION_SUBJECT_TYPE.isCastableTo(OBJECT_TYPE));
        assertTrue(CONTAINER_CONDITION_TYPE.isCastableTo(OBJECT_TYPE));
        assertTrue(NODE_TYPE_TYPE.isCastableTo(OBJECT_TYPE));
        
        // Collection types should also be castable to OBJECT_TYPE
        assertTrue(listType(STRING_TYPE).isCastableTo(OBJECT_TYPE));
        assertTrue(mapType(STRING_TYPE, BOOLEAN_TYPE).isCastableTo(OBJECT_TYPE));
        
        // OBJECT_TYPE should be castable to itself
        assertTrue(OBJECT_TYPE.isCastableTo(OBJECT_TYPE));

        // Nested collection types should be castable to OBJECT_TYPE
        assertTrue(listType(mapType(STRING_TYPE, BOOLEAN_TYPE)).isCastableTo(OBJECT_TYPE));
        assertTrue(mapType(STRING_TYPE, listType(BOOLEAN_TYPE)).isCastableTo(OBJECT_TYPE));
    }

    @Test
    void testObjectTypeAsSourceType() {
        // OBJECT_TYPE should be castable to any other type in isCastableTo
        // This intentionally tests the first condition in isCastableTo method
        assertTrue(OBJECT_TYPE.isCastableTo(STRING_TYPE));
        assertTrue(OBJECT_TYPE.isCastableTo(BOOLEAN_TYPE));
        assertTrue(OBJECT_TYPE.isCastableTo(listType(STRING_TYPE)));
        assertTrue(OBJECT_TYPE.isCastableTo(mapType(STRING_TYPE, BOOLEAN_TYPE)));
    }

    @Test
    void testObjectTypeCastMethod() {
        // Test that ObjectType.cast accepts any value
        Object stringValue = "test";
        Object booleanValue = true;
        Object listValue = Arrays.asList("a", "b", "c");
        Object mapValue = Map.of("key1", "value1", "key2", "value2");
        
        assertEquals(stringValue, OBJECT_TYPE.cast(stringValue));
        assertEquals(booleanValue, OBJECT_TYPE.cast(booleanValue));
        assertEquals(listValue, OBJECT_TYPE.cast(listValue));
        assertEquals(mapValue, OBJECT_TYPE.cast(mapValue));
        
        // Test null handling
        assertNull(OBJECT_TYPE.cast(null));
    }

    @Test
    void testObjectTypeCastToMethod() {
        // Test the specialized castTo method in ObjectType
        ObjectType objectType = new ObjectType();
        
        String stringValue = "test";
        Boolean booleanValue = true;
        
        // Cast from Object to specific types
        assertEquals(stringValue, objectType.castTo(stringValue, STRING_TYPE));
        assertEquals(booleanValue, objectType.castTo(booleanValue, BOOLEAN_TYPE));
        
        // Test null handling
        assertNull(objectType.castTo(null, STRING_TYPE));
    }

    // ==================== Expression Compilation Tests ====================

    @Test
    void testCompileExpressionsWithObjectTypeExpected() throws PMException {
        // Test compiling various expressions when the expected type is OBJECT_TYPE
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        
        // String literal
        PMLParser.ExpressionContext stringCtx = TestPMLParser.parseExpression("\"test\"");
        Expression<?> stringExpr = ExpressionVisitor.compile(visitorContext, stringCtx, OBJECT_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(STRING_TYPE, stringExpr.getType());
        
        // Boolean literal
        PMLParser.ExpressionContext boolCtx = TestPMLParser.parseExpression("true");
        Expression<?> boolExpr = ExpressionVisitor.compile(visitorContext, boolCtx, OBJECT_TYPE);
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
        Expression<?> arrayExpr = ExpressionVisitor.compile(visitorContext, arrayCtx, listType(OBJECT_TYPE));
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertTrue(arrayExpr instanceof ArrayLiteralExpression);
        assertEquals(listType(OBJECT_TYPE), arrayExpr.getType());
        
        // Map with mixed value types
        PMLParser.ExpressionContext mapCtx = TestPMLParser.parseExpression("""
                {
                    "string": "value",
                    "string2": "value2",
                    "boolean": true
                }
                """);
        Expression<?> mapExpr = ExpressionVisitor.compile(visitorContext, mapCtx, mapType(STRING_TYPE, OBJECT_TYPE));
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertTrue(mapExpr instanceof MapLiteralExpression);
        assertEquals(mapType(STRING_TYPE, OBJECT_TYPE), mapExpr.getType());
    }

    // ==================== Expression Execution Tests ====================

    @Test
    void testExecuteExpressionsWithObjectType() throws PMException {
        // String literal
        StringLiteralExpression stringExpr = new StringLiteralExpression("test");
        Object stringResult = stringExpr.asType(OBJECT_TYPE).execute(executionContext, pap);
        assertEquals("test", stringResult);

        // Boolean literal
        BoolLiteralExpression boolExpr = new BoolLiteralExpression(true);
        Object boolResult = boolExpr.asType(OBJECT_TYPE).execute(executionContext, pap);
        assertEquals(true, boolResult);
    }

    @Test
    void testExecuteHeterogeneousCollections() throws PMException {
        // Create and execute a heterogeneous array expression
        List<Expression<?>> arrayElements = new ArrayList<>();
        arrayElements.add(new StringLiteralExpression("string"));
        arrayElements.add(new StringLiteralExpression("value"));
        arrayElements.add(new BoolLiteralExpression(true));
        
        ArrayLiteralExpression<?> arrayExpr = new ArrayLiteralExpression<>(arrayElements, OBJECT_TYPE);
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
        
        MapLiteralExpression<?, ?> mapExpr = new MapLiteralExpression<>(mapEntries, STRING_TYPE, OBJECT_TYPE);
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
        ArrayLiteralExpression<?> innerArray1 = new ArrayLiteralExpression<>(innerArray1Elements, OBJECT_TYPE);
        
        // Inner array 2
        List<Expression<?>> innerArray2Elements = new ArrayList<>();
        innerArray2Elements.add(new BoolLiteralExpression(true));
        innerArray2Elements.add(new StringLiteralExpression("b"));
        ArrayLiteralExpression<?> innerArray2 = new ArrayLiteralExpression<>(innerArray2Elements, OBJECT_TYPE);
        
        // Inner map
        Map<Expression<?>, Expression<?>> innerMapEntries = new HashMap<>();
        innerMapEntries.put(new StringLiteralExpression("key1"), new StringLiteralExpression("value1"));
        innerMapEntries.put(new StringLiteralExpression("key2"), new BoolLiteralExpression(true));
        MapLiteralExpression<?, ?> innerMap = new MapLiteralExpression<>(innerMapEntries, STRING_TYPE, OBJECT_TYPE);
        
        // Outer map
        Map<Expression<?>, Expression<?>> outerMapEntries = new HashMap<>();
        outerMapEntries.put(new StringLiteralExpression("array1"), innerArray1);
        outerMapEntries.put(new StringLiteralExpression("array2"), innerArray2);
        outerMapEntries.put(new StringLiteralExpression("map"), innerMap);
        outerMapEntries.put(new StringLiteralExpression("primitive"), new BoolLiteralExpression(false));
        
        MapLiteralExpression<?, ?> outerMap = new MapLiteralExpression<>(outerMapEntries, STRING_TYPE, OBJECT_TYPE);
        
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
        ArrayLiteralExpression<?> emptyArrayExpr = new ArrayLiteralExpression<>(emptyArrayElements, OBJECT_TYPE);
        List<?> emptyArrayResult = emptyArrayExpr.execute(executionContext, pap);
        assertTrue(emptyArrayResult.isEmpty());
        
        // Empty map
        Map<Expression<?>, Expression<?>> emptyMapEntries = new HashMap<>();
        MapLiteralExpression<?, ?> emptyMapExpr = new MapLiteralExpression<>(emptyMapEntries, OBJECT_TYPE, OBJECT_TYPE);
        Map<?, ?> emptyMapResult = emptyMapExpr.execute(executionContext, pap);
        assertTrue(emptyMapResult.isEmpty());
    }

    @Test
    void testNullValues() throws PMException {
        // Create a map with a null value (represented as an ObjectLiteralExpression that returns null)
        Expression<?> nullExpr = new Expression<Object>() {
            @Override
            public ArgType<Object> getType() {
                return OBJECT_TYPE;
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
        
        MapLiteralExpression<?, ?> mapExpr = new MapLiteralExpression<>(mapEntries, STRING_TYPE, OBJECT_TYPE);
        Map<?, ?> mapResult = mapExpr.execute(executionContext, pap);
        
        assertEquals(2, mapResult.size());
        assertNull(mapResult.get("nullKey"));
        assertEquals("value", mapResult.get("nonNullKey"));
    }
} 