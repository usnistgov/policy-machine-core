package gov.nist.csd.pm.core.pap.pml.expression.reference;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import java.util.Map;
import java.util.Objects;

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