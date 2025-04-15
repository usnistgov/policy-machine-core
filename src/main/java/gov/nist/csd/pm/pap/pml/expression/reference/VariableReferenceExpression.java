package gov.nist.csd.pm.pap.pml.expression.reference;

import gov.nist.csd.pm.pap.function.arg.type.Type;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.expression.Expression;

import java.util.Objects;

public class VariableReferenceExpression<T> extends Expression<T> {
    private final String varName;
    private final Type<T> type;

    public VariableReferenceExpression(String varName, Type<T> type) {
        this.varName = varName;
        this.type = type;
    }

    @Override
    public Type<T> getType() {
        return type;
    }

    @Override
    public T execute(ExecutionContext ctx, PAP pap) throws PMException {
        return (T) ctx.scope().getVariable(varName);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return varName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableReferenceExpression<?> that = (VariableReferenceExpression<?>) o;
        return varName.equals(that.varName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varName);
    }
} 