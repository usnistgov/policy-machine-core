package gov.nist.csd.pm.pap.pml;

import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.expression.literal.ArrayLiteral;
import gov.nist.csd.pm.pap.pml.expression.literal.MapLiteral;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PMLUtil {

    public static ArrayLiteral buildArrayLiteral(String ... arr) {
        List<Expression> l = new ArrayList<>();
        for (String s : arr) {
            l.add(new StringLiteral(s));
        }

        return new ArrayLiteral(l, Type.string());
    }

    public static MapLiteral buildMapLiteral(String ... arr) {
        Map<Expression, Expression> mapExpr = new HashMap<>();

        for (int i = 0; i < arr.length; i+=2) {
            Expression expression = new StringLiteral(arr[i]);
            mapExpr.put(expression, new StringLiteral(arr[i+1]));
        }

        return new MapLiteral(mapExpr, Type.string(), Type.string());
    }
}
