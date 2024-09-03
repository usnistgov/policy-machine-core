package gov.nist.csd.pm.pap.pml.expression.reference;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.scope.PMLScopeException;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.Map;
import java.util.Objects;

public class ReferenceByDotIndex extends ReferenceByIndex{

    private VariableReference varRef;
    private String index;

    public ReferenceByDotIndex(VariableReference varRef, String index) {
        this.varRef = varRef;
        this.index = index;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return varRef + "." + index;
    }

    @Override
    public Type getType(Scope<Variable, PMLExecutableSignature> scope) throws PMLScopeException {
        return varRef.getType(scope).getMapValueType();
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        Value value = varRef.execute(ctx, pap);
        if (!value.getType().isMap()) {
            return value;
        }

        Map<Value, Value> mapValue = value.getMapValue();

        StringValue indexValue = new StringValue(index);
        if (!mapValue.containsKey(indexValue)) {
            throw new NullPointerException("map index " + index + " does not exist");
        } else {
            return mapValue.get(indexValue);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReferenceByDotIndex)) return false;
        ReferenceByDotIndex that = (ReferenceByDotIndex) o;
        return Objects.equals(varRef, that.varRef) && Objects.equals(index, that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varRef, index);
    }
}
