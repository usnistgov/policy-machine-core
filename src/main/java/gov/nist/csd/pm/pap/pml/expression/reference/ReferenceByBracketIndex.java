package gov.nist.csd.pm.pap.pml.expression.reference;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.scope.PMLScopeException;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.Map;
import java.util.Objects;

public class ReferenceByBracketIndex extends ReferenceByIndex{

    private VariableReference varRef;
    private Expression index;

    public ReferenceByBracketIndex(VariableReference varRef, Expression index) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReferenceByBracketIndex)) return false;
        ReferenceByBracketIndex that = (ReferenceByBracketIndex) o;
        return Objects.equals(varRef, that.varRef) && Objects.equals(index, that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varRef, index);
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        Value indexValue = index.execute(ctx, pap);
        Value value = varRef.execute(ctx, pap);
        if (!value.getType().isMap()) {
            return value;
        }

        Map<Value, Value> mapValue = value.getMapValue();
        return mapValue.get(indexValue);
    }
}
