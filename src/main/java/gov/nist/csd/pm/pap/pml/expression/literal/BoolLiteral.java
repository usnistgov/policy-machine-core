package gov.nist.csd.pm.pap.pml.expression.literal;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.scope.PMLScopeException;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.BoolValue;
import gov.nist.csd.pm.pap.pml.value.Value;

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

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        return new BoolValue(value);
    }
}
