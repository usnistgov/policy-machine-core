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
}
