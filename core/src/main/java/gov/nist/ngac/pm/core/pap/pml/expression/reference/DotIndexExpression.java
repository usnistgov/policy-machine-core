package gov.nist.ngac.pm.core.pap.pml.expression.reference;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import java.util.Map;
import java.util.Objects;

/**
 * PML dot-index expression (e.g. "ctx.args.name"): looks up a fixed string key on a map-typed value.
 */
public class DotIndexExpression<T> extends Expression<T> {
    private final Expression<?> baseExpr;
    private final String key;
    private final Type<T> valueType;

    public DotIndexExpression(Expression<?> baseExpr, String key, Type<T> valueType) {
        this.baseExpr = baseExpr;
        this.key = key;
        this.valueType = valueType;
    }

    @Override
    public Type<T> getType() {
        return valueType;
    }

    @Override
    public T execute(ExecutionContext ctx, PAP pap) throws PMException {
        Object baseValue = baseExpr.execute(ctx, pap);
        if (baseValue instanceof Map<?, ?> map) {
            return (T) map.get(key);
        }

        return (T) baseValue;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return baseExpr.toFormattedString(indentLevel) + "." + key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DotIndexExpression<?> that = (DotIndexExpression<?>) o;
        return baseExpr.equals(that.baseExpr) && key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseExpr, key);
    }
} 