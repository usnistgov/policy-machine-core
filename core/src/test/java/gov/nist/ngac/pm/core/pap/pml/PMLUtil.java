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

package gov.nist.ngac.pm.core.pap.pml;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.MapLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.StringLiteralExpression;
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
