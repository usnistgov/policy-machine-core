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

package gov.nist.ngac.pm.core.pap.pml.expression.reference;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import java.util.Map;
import java.util.Objects;

/**
 * PML bracket index expression (e.g. "arr[i]" or "map[key]"): indexes into a list or map value by an
 * evaluated key/index expression.
 *
 * @param <T> the type this expression evaluates to
 */
public class BracketIndexExpression<T> extends Expression<T> {
    private final Expression<?> baseExpr;
    private final Expression<?> indexExpr;
    private final Type<T> valueType;

    public BracketIndexExpression(Expression<?> baseExpr, Expression<?> indexExpr, Type<T> valueType) {
        this.baseExpr = baseExpr;
        this.indexExpr = indexExpr;
        this.valueType = valueType;
    }

    @Override
    public Type<T> getType() {
        return valueType;
    }

    @Override
    public T execute(ExecutionContext ctx, PAP pap) throws PMException {
        Object baseValue = baseExpr.execute(ctx, pap);
        Object indexValue = indexExpr.execute(ctx, pap);

        if (baseValue instanceof Map<?, ?> map) {
            return (T) map.get(indexValue);
        }

        return (T) baseValue;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return baseExpr.toFormattedString(indentLevel) + "[" + indexExpr.toFormattedString(indentLevel) + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BracketIndexExpression<?> that = (BracketIndexExpression<?>) o;
        return baseExpr.equals(that.baseExpr) && indexExpr.equals(that.indexExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseExpr, indexExpr);
    }
} 