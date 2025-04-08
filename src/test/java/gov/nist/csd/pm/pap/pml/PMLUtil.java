package gov.nist.csd.pm.pap.pml;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.listType;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.mapType;

import gov.nist.csd.pm.common.exception.ArgTypeNotCastableException;
import gov.nist.csd.pm.pap.pml.expression.Expression;

import gov.nist.csd.pm.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.csd.pm.pap.pml.expression.literal.MapLiteralExpression;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.print.DocFlavor.STRING;
import org.neo4j.codegen.api.ArrayLiteral;
import org.neo4j.cypher.internal.expressions.StringLiteral;

public class PMLUtil {

    public static ArrayLiteralExpression<List<String>> buildArrayLiteral(String ... arr) {
        List<Expression<?>> l = new ArrayList<>();
        try {
            for (String s : arr) {
                l.add(new StringLiteralExpression<>(s, STRING_TYPE));
            }

            return new ArrayLiteralExpression<>(l, listType(STRING_TYPE));
        } catch (ArgTypeNotCastableException e) {
            throw new RuntimeException(e);
        }
    }

    public static MapLiteralExpression<Map<String, String>> buildMapLiteral(String ... arr) {
        Map<Expression<?>, Expression<?>> mapExpr = new HashMap<>();

        try {
            for (int i = 0; i < arr.length; i+=2) {
                Expression<?> expression = new StringLiteralExpression<>(arr[i], STRING_TYPE);
                mapExpr.put(expression, new StringLiteralExpression<>(arr[i+1], STRING_TYPE));
            }

            return new MapLiteralExpression<>(mapExpr, mapType(STRING_TYPE, STRING_TYPE));
        } catch (ArgTypeNotCastableException e) {
            throw new RuntimeException(e);
        }
    }
}
