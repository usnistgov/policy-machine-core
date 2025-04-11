package gov.nist.csd.pm.pap.pml.expression.reference;

import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.scope.UnknownVariableInScopeException;

import java.util.Objects;

public class VariableReferenceExpression<T> extends Expression<T> {
    private final String varName;
    private final ArgType<T> type;

    public VariableReferenceExpression(String varName, ArgType<T> type) {
        this.varName = varName;
        this.type = type;
    }

    @Override
    public ArgType<T> getType() {
        return type;
    }

    @Override
    public T execute(ExecutionContext ctx, PAP pap) throws PMException {
        try {
            return (T) ctx.scope().getVariable(varName);
        } catch (UnknownVariableInScopeException e) {
            throw new PMException("Unknown variable '" + varName + "' in scope");
        }
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