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

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.type.AnyType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.MapType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.pml.TestPMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.BoolLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.MapLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.scope.CompileScope;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

public class TypeCompatibilityTest {

    private PAP pap;
    private ExecutionContext executionContext;

    @BeforeEach
    void setUp() throws PMException {
        pap = new MemoryPAP();
        executionContext = new ExecutionContext(NodeUserContext.of("u1"), pap);
    }

    @Test
    void testAllTypesCastableToObjectType() {
        assertTrue(STRING_TYPE.isCastableTo(ANY_TYPE));
        assertTrue(BOOLEAN_TYPE.isCastableTo(ANY_TYPE));
        assertTrue(ListType.of(STRING_TYPE).isCastableTo(ANY_TYPE));
        assertTrue(MapType.of(STRING_TYPE, BOOLEAN_TYPE).isCastableTo(ANY_TYPE));
        
        assertTrue(ANY_TYPE.isCastableTo(ANY_TYPE));

        assertTrue(ListType.of(MapType.of(STRING_TYPE, BOOLEAN_TYPE)).isCastableTo(ANY_TYPE));
        assertTrue(MapType.of(STRING_TYPE, ListType.of(BOOLEAN_TYPE)).isCastableTo(ANY_TYPE));
    }

    @Test
    void testObjectTypeAsSourceType() {
        assertTrue(ANY_TYPE.isCastableTo(STRING_TYPE));
        assertTrue(ANY_TYPE.isCastableTo(BOOLEAN_TYPE));
        assertTrue(ANY_TYPE.isCastableTo(ListType.of(STRING_TYPE)));
        assertTrue(ANY_TYPE.isCastableTo(MapType.of(STRING_TYPE, BOOLEAN_TYPE)));
    }

    @Test
    void testObjectTypeCastMethod() {
        Object stringValue = "test";
        Object booleanValue = true;
        Object listValue = Arrays.asList("a", "b", "c");
        Object mapValue = Map.of("key1", "value1", "key2", "value2");
        
        assertEquals(stringValue, ANY_TYPE.cast(stringValue));
        assertEquals(booleanValue, ANY_TYPE.cast(booleanValue));
        assertEquals(listValue, ANY_TYPE.cast(listValue));
        assertEquals(mapValue, ANY_TYPE.cast(mapValue));
        
        assertNull(ANY_TYPE.cast(null));
    }

    @Test
    void testObjectTypeCastToMethod() {
        AnyType anyType = new AnyType();
        
        String stringValue = "test";
        Boolean booleanValue = true;
        
        assertEquals(stringValue, anyType.castTo(stringValue, STRING_TYPE));
        assertEquals(booleanValue, anyType.castTo(booleanValue, BOOLEAN_TYPE));
        
        assertNull(anyType.castTo(null, STRING_TYPE));
    }

    @Test
    void testCompileExpressionsWithObjectTypeExpected() throws PMException {
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        
        PMLParser.ExpressionContext stringCtx = TestPMLParser.parseExpression("\"test\"");
        Expression<?> stringExpr = ExpressionVisitor.compile(visitorContext, stringCtx, ANY_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(STRING_TYPE, stringExpr.getType());
        
        PMLParser.ExpressionContext boolCtx = TestPMLParser.parseExpression("true");
        Expression<?> boolExpr = ExpressionVisitor.compile(visitorContext, boolCtx, ANY_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertEquals(BOOLEAN_TYPE, boolExpr.getType());
    }

    @Test
    void testCompileHeterogeneousCollections() throws PMException {
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        
        PMLParser.ExpressionContext arrayCtx = TestPMLParser.parseExpression("""
                ["a", "value", true]
                """);
        Expression<?> arrayExpr = ExpressionVisitor.compile(visitorContext, arrayCtx, ListType.of(ANY_TYPE));
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertTrue(arrayExpr instanceof ArrayLiteralExpression);
        assertEquals(ListType.of(ANY_TYPE), arrayExpr.getType());
        
        PMLParser.ExpressionContext mapCtx = TestPMLParser.parseExpression("""
                {
                    "string": "value",
                    "string2": "value2",
                    "boolean": true
                }
                """);
        Expression<?> mapExpr = ExpressionVisitor.compile(visitorContext, mapCtx, MapType.of(STRING_TYPE, ANY_TYPE));
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        assertTrue(mapExpr instanceof MapLiteralExpression);
        assertEquals(MapType.of(STRING_TYPE, ANY_TYPE), mapExpr.getType());
    }

    @Test
    void testExecuteExpressionsWithObjectType() throws PMException {
        StringLiteralExpression stringExpr = new StringLiteralExpression("test");
        Object stringResult = stringExpr.asType(ANY_TYPE).execute(executionContext, pap);
        assertEquals("test", stringResult);

        BoolLiteralExpression boolExpr = new BoolLiteralExpression(true);
        Object boolResult = boolExpr.asType(ANY_TYPE).execute(executionContext, pap);
        assertEquals(true, boolResult);
    }

    @Test
    void testExecuteHeterogeneousCollections() throws PMException {
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

    @Test
    void testNestedHeterogeneousStructures() throws PMException {
        List<Expression<?>> innerArray1Elements = new ArrayList<>();
        innerArray1Elements.add(new StringLiteralExpression("a"));
        innerArray1Elements.add(new BoolLiteralExpression(true));
        ArrayLiteralExpression<?> innerArray1 = new ArrayLiteralExpression<>(innerArray1Elements, ANY_TYPE);
        
        List<Expression<?>> innerArray2Elements = new ArrayList<>();
        innerArray2Elements.add(new BoolLiteralExpression(true));
        innerArray2Elements.add(new StringLiteralExpression("b"));
        ArrayLiteralExpression<?> innerArray2 = new ArrayLiteralExpression<>(innerArray2Elements, ANY_TYPE);
        
        Map<Expression<?>, Expression<?>> innerMapEntries = new HashMap<>();
        innerMapEntries.put(new StringLiteralExpression("key1"), new StringLiteralExpression("value1"));
        innerMapEntries.put(new StringLiteralExpression("key2"), new BoolLiteralExpression(true));
        MapLiteralExpression<?, ?> innerMap = new MapLiteralExpression<>(innerMapEntries, STRING_TYPE, ANY_TYPE);
        
        Map<Expression<?>, Expression<?>> outerMapEntries = new HashMap<>();
        outerMapEntries.put(new StringLiteralExpression("array1"), innerArray1);
        outerMapEntries.put(new StringLiteralExpression("array2"), innerArray2);
        outerMapEntries.put(new StringLiteralExpression("map"), innerMap);
        outerMapEntries.put(new StringLiteralExpression("primitive"), new BoolLiteralExpression(false));
        
        MapLiteralExpression<?, ?> outerMap = new MapLiteralExpression<>(outerMapEntries, STRING_TYPE, ANY_TYPE);
        
        Map<?, ?> result = outerMap.execute(executionContext, pap);
        
        assertEquals(4, result.size());
        
        List<?> array1Result = (List<?>) result.get("array1");
        assertEquals(2, array1Result.size());
        assertEquals("a", array1Result.get(0));
        assertEquals(true, array1Result.get(1));
        
        List<?> array2Result = (List<?>) result.get("array2");
        assertEquals(2, array2Result.size());
        assertEquals(true, array2Result.get(0));
        assertEquals("b", array2Result.get(1));
        
        Map<?, ?> mapResult = (Map<?, ?>) result.get("map");
        assertEquals(2, mapResult.size());
        assertEquals("value1", mapResult.get("key1"));
        assertEquals(true, mapResult.get("key2"));
        
        assertEquals(false, result.get("primitive"));
    }

    @Test
    void testEmptyCollections() throws PMException {
        List<Expression<?>> emptyArrayElements = new ArrayList<>();
        ArrayLiteralExpression<?> emptyArrayExpr = new ArrayLiteralExpression<>(emptyArrayElements, ANY_TYPE);
        List<?> emptyArrayResult = emptyArrayExpr.execute(executionContext, pap);
        assertTrue(emptyArrayResult.isEmpty());
        
        Map<Expression<?>, Expression<?>> emptyMapEntries = new HashMap<>();
        MapLiteralExpression<?, ?> emptyMapExpr = new MapLiteralExpression<>(emptyMapEntries, ANY_TYPE, ANY_TYPE);
        Map<?, ?> emptyMapResult = emptyMapExpr.execute(executionContext, pap);
        assertTrue(emptyMapResult.isEmpty());
    }

    @Test
    void testNullValues() throws PMException {
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