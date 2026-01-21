package gov.nist.csd.pm.core.pap.pml;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.MapLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PMLUtil {

    public static Expression<List<String>> buildArrayLiteral(String ... arr) {
        List<Expression<String>> l = new ArrayList<>();
        for (String s : arr) {
            l.add(new StringLiteralExpression(s));
        }

        return ArrayLiteralExpression.of(l, STRING_TYPE);
    }

    public static MapLiteralExpression<String, String> buildMapLiteral(String ... arr) {
        Map<Expression<?>, Expression<?>> mapExpr = new HashMap<>();

        for (int i = 0; i < arr.length; i+=2) {
            Expression<String> expression = new StringLiteralExpression(arr[i]);
            mapExpr.put(expression, new StringLiteralExpression(arr[i+1]));
        }

        return new MapLiteralExpression<>(mapExpr, STRING_TYPE, STRING_TYPE);
    }
}
