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
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.MapType;
import gov.nist.ngac.pm.core.pap.pml.TestPMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.MapLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.scope.CompileScope;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

public class TypeCoercionTest {

    private PAP pap;
    private ExecutionContext executionContext;

    @BeforeEach
    void setUp() throws PMException {
        executionContext = new ExecutionContext(NodeUserContext.of("u1"), new MemoryPAP());
    }

    @Test
    void testStringToObjectCoercion() throws PMException {
        StringLiteralExpression stringExpr = new StringLiteralExpression("test");
        Expression<Object> objectExpr = stringExpr.asType(ANY_TYPE);
        
        Object result = objectExpr.execute(executionContext, pap);
        assertEquals("test", result);
        assertTrue(result instanceof String);
        
        assertEquals(STRING_TYPE, stringExpr.getType());
    }

    @Test
    void testListTypeCoercion() throws PMException {
        List<Expression<?>> stringElements = new ArrayList<>();
        stringElements.add(new StringLiteralExpression("a"));
        stringElements.add(new StringLiteralExpression("b"));
        
        ArrayLiteralExpression<String> stringArray = new ArrayLiteralExpression<>(stringElements, STRING_TYPE);
        
        Expression<List<Object>> objectArray = stringArray.asType(ListType.of(ANY_TYPE));
        
        List<Object> result = objectArray.execute(executionContext, pap);
        assertEquals(2, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        
        assertEquals(ListType.of(STRING_TYPE), stringArray.getType());
    }

    @Test
    void testMapTypeCoercion() throws PMException {
        Map<Expression<?>, Expression<?>> entries = new HashMap<>();
        entries.put(new StringLiteralExpression("key1"), new StringLiteralExpression("value1"));
        entries.put(new StringLiteralExpression("key2"), new StringLiteralExpression("value2"));
        
        MapLiteralExpression<String, String> stringMap =
                new MapLiteralExpression<>(entries, STRING_TYPE, STRING_TYPE);
        
        Expression<Map<String, Object>> objectMap =
                stringMap.asType(MapType.of(STRING_TYPE, ANY_TYPE));
        
        Map<String, Object> result = objectMap.execute(executionContext, pap);
        assertEquals(2, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        
        assertEquals(MapType.of(STRING_TYPE, STRING_TYPE), stringMap.getType());
    }

    @Test
    void testNestedCollectionCoercion() throws PMException {
        List<Expression<?>> innerElements = new ArrayList<>();
        innerElements.add(new StringLiteralExpression("inner1"));
        innerElements.add(new StringLiteralExpression("inner2"));
        ArrayLiteralExpression<String> innerArray = 
                new ArrayLiteralExpression<>(innerElements, STRING_TYPE);
        
        List<Expression<?>> outerElements = new ArrayList<>();
        outerElements.add(innerArray);
        ArrayLiteralExpression<List<String>> outerArray = 
                new ArrayLiteralExpression<>(outerElements, ListType.of(STRING_TYPE));
        
        Expression<List<List<Object>>> coercedArray =
                outerArray.asType(ListType.of(ListType.of(ANY_TYPE)));
        
        List<List<Object>> result = coercedArray.execute(executionContext, pap);
        assertEquals(1, result.size());
        List<Object> innerResult = result.get(0);
        assertEquals(2, innerResult.size());
        assertEquals("inner1", innerResult.get(0));
        assertEquals("inner2", innerResult.get(1));
    }

    @Test
    void testCoercionInPML() throws PMException {
        String pml = """
                {
                    "stringList": ["a", "b", "c"],
                    "mixedList": ["x", true]
                }
                """;
        
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(pml);
        
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx, 
                MapType.of(STRING_TYPE, ListType.of(ANY_TYPE)));
        
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        
        Map<?, ?> result = (Map<?, ?>) expr.execute(executionContext, pap);
        assertEquals(2, result.size());
        
        List<?> stringList = (List<?>) result.get("stringList");
        assertEquals(3, stringList.size());
        assertEquals("a", stringList.get(0));
        assertEquals("b", stringList.get(1));
        assertEquals("c", stringList.get(2));
        
        List<?> mixedList = (List<?>) result.get("mixedList");
        assertEquals(2, mixedList.size());
        assertEquals("x", mixedList.get(0));
        assertEquals(true, mixedList.get(1));
    }

    @Test
    void testCoercionWithComplexExpressions() throws PMException {
        String pml = """
                {
                    "result": ["test", true && false]
                }
                """;
        
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(pml);
        
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx, 
                MapType.of(STRING_TYPE, ListType.of(ANY_TYPE)));
        
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        
        Map<?, ?> result = (Map<?, ?>) expr.execute(executionContext, pap);
        List<?> resultList = (List<?>) result.get("result");
        
        assertEquals(2, resultList.size());
        assertEquals("test", resultList.get(0));
        assertEquals(false, resultList.get(1));
    }
    
    @Test
    void testObjectToPrimitiveTypeCoercion() throws PMException {
        String pml = """
                {
                    "obj1": "string value",
                    "obj2": true
                }
                """;
        
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(pml);
        
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression<?> expr = ExpressionVisitor.compile(visitorContext, ctx, 
                MapType.of(STRING_TYPE, ANY_TYPE));
        
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        Map<?, ?> result = (Map<?, ?>) expr.execute(executionContext, pap);
        
        String stringValue = (String) result.get("obj1");
        Boolean boolValue = (Boolean) result.get("obj2");
        
        assertEquals("string value", stringValue);
        assertEquals(true, boolValue);
    }
} 