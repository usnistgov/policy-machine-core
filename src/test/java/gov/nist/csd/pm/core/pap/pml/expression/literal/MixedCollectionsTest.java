package gov.nist.csd.pm.core.pap.pml.expression.literal;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.arg.type.MapType;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Tests specifically focused on mixed collections in the PML language.
 * These tests ensure that collections with ANY_TYPE as element or value type
 * can properly handle mixed types of data.
 */
public class MixedCollectionsTest {

    private PAP pap = new MemoryPAP();
    private ExecutionContext executionContext = new ExecutionContext(new TestUserContext("u1"), pap);

    public MixedCollectionsTest() throws PMException {
    }

    @Test
    void testMixedArrayCreation() throws PMException {
        // Create array with mixed types using direct construction
        List<Expression<?>> elements = List.of(
                new StringLiteralExpression("text"),
                new StringLiteralExpression("second"),
                new BoolLiteralExpression(true)
        );
        
        ArrayLiteralExpression<?> array = new ArrayLiteralExpression<>(elements, ANY_TYPE);
        assertEquals(ListType.of(ANY_TYPE), array.getType());
        
        // Execute the array and verify results
        List<?> result = array.execute(executionContext, pap);
        assertEquals(3, result.size());
        assertEquals("text", result.get(0));
        assertEquals("second", result.get(1));
        assertEquals(true, result.get(2));
    }
    
    @Test
    void testMixedArrayFromPML() throws PMException {
        // Parse and compile mixed array from PML code
        String pml = """
                ["text", "second", true]
                """;
        
        // Use TestPMLParser instead of PMLContextVisitor
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(pml);
        
        // Compile with expected type of list<object>
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression<?> expression = ExpressionVisitor.compile(visitorContext, ctx, ListType.of(ANY_TYPE));
        
        // Verify no compilation errors
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertTrue(expression instanceof ArrayLiteralExpression);
        assertEquals(ListType.of(ANY_TYPE), expression.getType());
        
        // Execute and verify results
        List<?> result = (List<?>) expression.execute(executionContext, pap);
        assertEquals(3, result.size());
        assertEquals("text", result.get(0));
        assertEquals("second", result.get(1));
        assertEquals(true, result.get(2));
    }
    
    @Test
    void testMixedMapCreation() throws PMException {
        // Create map with mixed value types
        Map<Expression<?>, Expression<?>> entries = new HashMap<>();
        entries.put(new StringLiteralExpression("string"), new StringLiteralExpression("value"));
        entries.put(new StringLiteralExpression("string2"), new StringLiteralExpression("value2"));
        entries.put(new StringLiteralExpression("boolean"), new BoolLiteralExpression(true));
        
        MapLiteralExpression<?, ?> map = new MapLiteralExpression<>(entries, STRING_TYPE, ANY_TYPE);
        assertEquals(MapType.of(STRING_TYPE, ANY_TYPE), map.getType());
        
        // Execute the map and verify results
        Map<?, ?> result = map.execute(executionContext, pap);
        assertEquals(3, result.size());
        assertEquals("value", result.get("string"));
        assertEquals("value2", result.get("string2"));
        assertEquals(true, result.get("boolean"));
    }
    
    @Test
    void testMixedMapFromPML() throws PMException {
        // Parse and compile mixed map from PML code
        String pml = """
                {
                    "string": "value",
                    "string2": "value2",
                    "boolean": true
                }
                """;
        
        // Use TestPMLParser instead of PMLContextVisitor
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(pml);
        
        // Compile with expected type of map<string, object>
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression<?> expression = ExpressionVisitor.compile(visitorContext, ctx, MapType.of(STRING_TYPE, ANY_TYPE));
        
        // Verify no compilation errors
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertTrue(expression instanceof MapLiteralExpression);
        assertEquals(MapType.of(STRING_TYPE, ANY_TYPE), expression.getType());
        
        // Execute and verify results
        Map<?, ?> result = (Map<?, ?>) expression.execute(executionContext, pap);
        assertEquals(3, result.size());
        assertEquals("value", result.get("string"));
        assertEquals("value2", result.get("string2"));
        assertEquals(true, result.get("boolean"));
    }
    
    @Test
    void testMixedMapKeysFromPML() throws PMException {
        // Parse and compile map with mixed keys from PML code
        String pml = """
                {
                    "string_key": "value1",
                    "string_key2": "value2",
                    true: "value3"
                }
                """;
        
        // Use TestPMLParser instead of PMLContextVisitor
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(pml);
        
        // Compile with expected type of map<object, string>
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression<?> expression = ExpressionVisitor.compile(visitorContext, ctx, MapType.of(ANY_TYPE, STRING_TYPE));
        
        // Verify no compilation errors
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertTrue(expression instanceof MapLiteralExpression);
        assertEquals(MapType.of(ANY_TYPE, STRING_TYPE), expression.getType());
        
        // Execute and verify results
        Map<?, ?> result = (Map<?, ?>) expression.execute(executionContext, pap);
        assertEquals(3, result.size());
        assertEquals("value1", result.get("string_key"));
        assertEquals("value2", result.get("string_key2"));
        assertEquals("value3", result.get(true));
    }
    
    @Test
    void testNestedMixedCollections() throws PMException {
        // Parse and compile a complex nested structure with mixed types
        String pml = """
                {
                    "array": ["string", "second", true],
                    "map": {
                        "key1": "value1",
                        "key2": "value2"
                    },
                    "nested": [
                        {
                            "inner_key": "inner_value"
                        },
                        ["a", "b", "c"]
                    ]
                }
                """;
        
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(pml);
        
        // Compile with expected type of map<string, object>
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression<?> expression = ExpressionVisitor.compile(visitorContext, ctx, MapType.of(STRING_TYPE, ANY_TYPE));
        
        // Verify no compilation errors
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertTrue(expression instanceof MapLiteralExpression);
        
        // Execute and verify results
        Map<?, ?> result = (Map<?, ?>) expression.execute(executionContext, pap);
        assertEquals(3, result.size());
        
        // Check array
        List<?> array = (List<?>) result.get("array");
        assertEquals(3, array.size());
        assertEquals("string", array.get(0));
        assertEquals("second", array.get(1));
        assertEquals(true, array.get(2));
        
        // Check map
        Map<?, ?> map = (Map<?, ?>) result.get("map");
        assertEquals(2, map.size());
        assertEquals("value1", map.get("key1"));
        assertEquals("value2", map.get("key2"));
        
        // Check nested
        List<?> nested = (List<?>) result.get("nested");
        assertEquals(2, nested.size());
        
        Map<?, ?> innerMap = (Map<?, ?>) nested.get(0);
        assertEquals(1, innerMap.size());
        assertEquals("inner_value", innerMap.get("inner_key"));
        
        List<?> innerArray = (List<?>) nested.get(1);
        assertEquals(3, innerArray.size());
        assertEquals("a", innerArray.get(0));
        assertEquals("b", innerArray.get(1));
        assertEquals("c", innerArray.get(2));
    }
    
    @Test
    void testDeeplyNestedStructure() throws PMException {
        // Parse and compile a deeply nested structure
        String pml = """
                {
                    "level1": {
                        "level2": {
                            "level3": {
                                "array": ["one", "two", true],
                                "map": {
                                    "key": "value"
                                }
                            }
                        }
                    }
                }
                """;
        
        // Use TestPMLParser instead of PMLContextVisitor
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(pml);
        
        // Compile with expected type of map<string, object>
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression<?> expression = ExpressionVisitor.compile(visitorContext, ctx, MapType.of(STRING_TYPE, ANY_TYPE));
        
        // Verify no compilation errors
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        
        // Execute and verify results
        Map<?, ?> level1 = (Map<?, ?>) expression.execute(executionContext, pap);
        Map<?, ?> level2 = (Map<?, ?>) level1.get("level1");
        Map<?, ?> level3 = (Map<?, ?>) level2.get("level2");
        Map<?, ?> content = (Map<?, ?>) level3.get("level3");
        
        List<?> array = (List<?>) content.get("array");
        assertEquals(3, array.size());
        assertEquals("one", array.get(0));
        assertEquals("two", array.get(1));
        assertEquals(true, array.get(2));
        
        Map<?, ?> map = (Map<?, ?>) content.get("map");
        assertEquals(1, map.size());
        assertEquals("value", map.get("key"));
    }
    
    @Test
    void testEmptyCollections() throws PMException {
        // Parse and compile empty collections
        String emptyArrayCode = "[]";
        String emptyMapCode = "{}";
        
        // Empty array - use TestPMLParser instead of PMLContextVisitor
        PMLParser.ExpressionContext arrayCtx = TestPMLParser.parseExpression(emptyArrayCode);
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression<?> arrayExpr = ExpressionVisitor.compile(visitorContext, arrayCtx, ListType.of(ANY_TYPE));
        List<?> arrayResult = (List<?>) arrayExpr.execute(executionContext, pap);
        assertTrue(arrayResult.isEmpty());
        
        // Empty map - use TestPMLParser instead of PMLContextVisitor
        PMLParser.ExpressionContext mapCtx = TestPMLParser.parseExpression(emptyMapCode);
        visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression<?> mapExpr = ExpressionVisitor.compile(visitorContext, mapCtx, MapType.of(ANY_TYPE, ANY_TYPE));
        Map<?, ?> mapResult = (Map<?, ?>) mapExpr.execute(executionContext, pap);
        assertTrue(mapResult.isEmpty());
    }
    
    @Test
    void testCollectionTypeCasting() throws PMException {
        // Test casting a list<string> to list<object>
        List<Expression<?>> stringElements = List.of(
                new StringLiteralExpression("a"),
                new StringLiteralExpression("b"),
                new StringLiteralExpression("c")
        );
        
        ArrayLiteralExpression<String> stringArray = new ArrayLiteralExpression<>(stringElements, STRING_TYPE);
        Expression<List<Object>> objectArray = stringArray.asType(ListType.of(ANY_TYPE));
        
        List<Object> result = objectArray.execute(executionContext, pap);
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c", result.get(2));
        
        // Test casting a map<string, string> to map<string, object>
        Map<Expression<?>, Expression<?>> stringEntries = new HashMap<>();
        stringEntries.put(new StringLiteralExpression("key1"), new StringLiteralExpression("value1"));
        stringEntries.put(new StringLiteralExpression("key2"), new StringLiteralExpression("value2"));
        
        MapLiteralExpression<String, String> stringMap = new MapLiteralExpression<>(stringEntries, STRING_TYPE, STRING_TYPE);
        Expression<Map<String, Object>> objectMap = stringMap.asType(MapType.of(STRING_TYPE, ANY_TYPE));
        
        Map<String, Object> mapResult = objectMap.execute(executionContext, pap);
        assertEquals(2, mapResult.size());
        assertEquals("value1", mapResult.get("key1"));
        assertEquals("value2", mapResult.get("key2"));
    }
} 