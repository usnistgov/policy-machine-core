package gov.nist.csd.pm.pap.pml;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.OBJECT_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.listType;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.mapType;


import gov.nist.csd.pm.pap.pml.exception.ArgTypeNotCastableException;
import gov.nist.csd.pm.pap.pml.expression.Expression;

import gov.nist.csd.pm.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.csd.pm.pap.pml.expression.literal.MapLiteralExpression;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.pap.pml.expression.literal.BoolLiteralExpression;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PMLUtil {

    public static Expression<List<String>> buildArrayLiteral(String ... arr) {
        List<Expression<String>> l = new ArrayList<>();
        try {
            for (String s : arr) {
                l.add(new StringLiteralExpression(s));
            }

            return ArrayLiteralExpression.of(l, STRING_TYPE);
        } catch (ArgTypeNotCastableException e) {
            throw new RuntimeException(e);
        }
    }

    public static MapLiteralExpression<String, String> buildMapLiteral(String ... arr) {
        Map<Expression<?>, Expression<?>> mapExpr = new HashMap<>();

        try {
            for (int i = 0; i < arr.length; i+=2) {
                Expression<String> expression = new StringLiteralExpression(arr[i]);
                mapExpr.put(expression, new StringLiteralExpression(arr[i+1]));
            }

            return new MapLiteralExpression<>(mapExpr, STRING_TYPE, STRING_TYPE);
        } catch (ArgTypeNotCastableException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Creates a MapLiteralExpression with String keys and heterogeneous Object values
     * 
     * @param map A map with String keys and values of different types
     * @return A MapLiteralExpression representing the heterogeneous map
     */
    public static MapLiteralExpression<String, Object> buildHeterogeneousMapLiteral(Map<String, Object> map) {
        Map<Expression<?>, Expression<?>> expressionMap = new HashMap<>();
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Expression<String> keyExpr = new StringLiteralExpression(entry.getKey());
            Expression<?> valueExpr = createExpressionForValue(entry.getValue());
            expressionMap.put(keyExpr, valueExpr);
        }
        
        return new MapLiteralExpression<>(expressionMap, STRING_TYPE, OBJECT_TYPE);
    }
    
    /**
     * Creates an appropriate Expression for different value types
     */
    private static Expression<?> createExpressionForValue(Object value) {
        if (value instanceof String) {
            return new StringLiteralExpression((String) value);
        } else if (value instanceof Boolean) {
            return new BoolLiteralExpression((Boolean) value);
        } else if (value instanceof List) {
            // Handle list values - requires converting each element to an Expression
            List<Expression<?>> elementExpressions = new ArrayList<>();
            for (Object element : (List<?>) value) {
                elementExpressions.add(createExpressionForValue(element));
            }
            return new ArrayLiteralExpression<>(elementExpressions, OBJECT_TYPE);
        } else if (value instanceof Map) {
            // Handle nested maps
            Map<Expression<?>, Expression<?>> nestedExprMap = new HashMap<>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                Expression<?> keyExpr = createExpressionForValue(entry.getKey());
                Expression<?> valueExpr = createExpressionForValue(entry.getValue());
                nestedExprMap.put(keyExpr, valueExpr);
            }
            return new MapLiteralExpression<>(nestedExprMap, OBJECT_TYPE, OBJECT_TYPE);
        }
        
        // For other types, you could add more conversions as needed
        // For example, for numbers, custom objects, etc.
        throw new IllegalArgumentException("Unsupported value type: " + value.getClass().getName());
    }
}
