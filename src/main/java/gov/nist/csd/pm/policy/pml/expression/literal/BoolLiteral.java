package gov.nist.csd.pm.policy.pml.expression.literal;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.Scope;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.BoolValue;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.util.Objects;

public class BoolLiteral extends Literal {

    private boolean value;

    public BoolLiteral(boolean value) {
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        return new BoolValue(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoolLiteral that = (BoolLiteral) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return value ? "true" : "false";
    }

    @Override
    public Type getType(Scope scope) throws PMLScopeException {
        return Type.bool();
    }
}
