package gov.nist.csd.pm.core.pap.pml.expression;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.type.AnyType;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.arg.type.MapType;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.UnexpectedExpressionTypeException;
import gov.nist.csd.pm.core.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.BoolLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.MapLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.reference.VariableReferenceExpression;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AnyTypeErrorHandlingTest {

    private PAP pap;
    private ExecutionContext executionContext;

    @BeforeEach
    void setUp() throws PMException {
        pap = new MemoryPAP();
        executionContext = new ExecutionContext(new TestUserContext("u1"), new MemoryPAP());
    }

    @Test
    void testNullValueHandling() {
        assertNull(ANY_TYPE.cast(null));
        
        AnyType anyType = new AnyType();
        
        assertNull(anyType.castTo(null, STRING_TYPE));
        assertNull(anyType.castTo(null, BOOLEAN_TYPE));
        assertNull(anyType.castTo(null, ListType.of(STRING_TYPE)));
        assertNull(anyType.castTo(null, MapType.of(STRING_TYPE, BOOLEAN_TYPE)));
    }
    
    @Test
    void testInvalidCastFailures() {
        String stringValue = "not a boolean";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            BOOLEAN_TYPE.cast(stringValue);
        });
        assertTrue(exception.getMessage().contains("Cannot cast"));
    }
    
    @Test
    void testObjectTypeCasting() throws PMException {
        StringLiteralExpression stringExpr = new StringLiteralExpression("test");
        BoolLiteralExpression boolExpr = new BoolLiteralExpression(true);
        
        Expression<Object> objectStringExpr = stringExpr.asType(ANY_TYPE);
        Expression<Object> objectBoolExpr = boolExpr.asType(ANY_TYPE);
        
        assertEquals("test", objectStringExpr.execute(executionContext, pap));
        assertEquals(true, objectBoolExpr.execute(executionContext, pap));
        
        assertEquals(STRING_TYPE, stringExpr.getType());
        assertEquals(BOOLEAN_TYPE, boolExpr.getType());
    }
    
    @Test
    void testVariableWithObjectType() throws PMException {
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        visitorContext.scope().addVariable("objVar", new Variable("objVar", ANY_TYPE, false));
        
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression("objVar");
        
        Expression<?> expr1 = ExpressionVisitor.compile(visitorContext, ctx, ANY_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(new VariableReferenceExpression<>("objVar", ANY_TYPE), expr1);
        
        Expression<?> expr2 = ExpressionVisitor.compile(visitorContext, ctx, STRING_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(STRING_TYPE, expr2.getType());
        
        Expression<?> expr3 = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(BOOLEAN_TYPE, expr3.getType());
        
        Expression<?> expr4 = ExpressionVisitor.compile(visitorContext, ctx, ListType.of(STRING_TYPE));
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(ListType.of(STRING_TYPE), expr4.getType());
    }
    
    @Test
    void testCollectionTypeConsistency() throws PMException {
        List<Expression<?>> elements = List.of(
                new StringLiteralExpression("text"),
                new StringLiteralExpression("another"),
                new BoolLiteralExpression(true)
        );
        
        ArrayLiteralExpression<?> array = new ArrayLiteralExpression<>(elements, ANY_TYPE);
        
        List<?> result = array.execute(executionContext, pap);
        
        assertTrue(result.get(0) instanceof String);
        assertTrue(result.get(1) instanceof String);
        assertTrue(result.get(2) instanceof Boolean);
        
        assertEquals("text", result.get(0));
        assertEquals("another", result.get(1));
        assertEquals(true, result.get(2));
    }
    
    @Test
    void testReferencingCollectionElements() throws PMException {
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
        
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression<?> mapExpr = ExpressionVisitor.compile(visitorContext, ctx, MapType.of(STRING_TYPE, ANY_TYPE));
        
        Map<?, ?> result = (Map<?, ?>) mapExpr.execute(executionContext, pap);
        
        assertEquals(5, result.size());
        assertTrue(result.get("string") instanceof String);
        assertTrue(result.get("string2") instanceof String);
        assertTrue(result.get("boolean") instanceof Boolean);
        assertTrue(result.get("array") instanceof List);
        assertTrue(result.get("map") instanceof Map);
        
        List<?> array = (List<?>) result.get("array");
        assertEquals(3, array.size());
        assertEquals("a", array.get(0));
        
        Map<?, ?> map = (Map<?, ?>) result.get("map");
        assertEquals(1, map.size());
        assertEquals("value", map.get("key"));
    }
    
    @Test
    void testCastingBetweenCollectionTypes() throws PMException {
        List<Expression<?>> stringElements = List.of(
                new StringLiteralExpression("a"),
                new StringLiteralExpression("b")
        );
        ArrayLiteralExpression<String> stringArray = new ArrayLiteralExpression<>(stringElements, STRING_TYPE);
        
        Expression<List<Object>> objectArray = stringArray.asType(ListType.of(ANY_TYPE));
        
        List<Object> result = objectArray.execute(executionContext, pap);
        assertEquals(2, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        
        // Try to cast list<string> to something incompatible
        Exception exception = assertThrows(UnexpectedExpressionTypeException.class, () -> {
            stringArray.asType(MapType.of(STRING_TYPE, STRING_TYPE));
        });
        assertTrue(exception.getMessage().contains("expected"));
    }
    
    @Test
    void testObjectTypeInTypedContext() throws PMException {
        Map<Expression<?>, Expression<?>> entries = new HashMap<>();
        entries.put(new StringLiteralExpression("string"), new StringLiteralExpression("value"));
        entries.put(new StringLiteralExpression("boolean"), new BoolLiteralExpression(true));
        
        MapLiteralExpression<?, ?> map = new MapLiteralExpression<>(entries, STRING_TYPE, ANY_TYPE);
        
        Map<?, ?> result = map.execute(executionContext, pap);
        
        String stringValue = (String) result.get("string");
        Boolean boolValue = (Boolean) result.get("boolean");
        
        assertEquals("value", stringValue);
        assertEquals(true, boolValue);
    }
} 