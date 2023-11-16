package gov.nist.csd.pm.policy.pml.expression.reference;

import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.scope.Scope;
import gov.nist.csd.pm.policy.pml.type.Type;

import java.util.Objects;

public abstract class ReferenceByIndex extends VariableReference{

    protected VariableReference varRef;
    protected Expression index;

    public ReferenceByIndex(VariableReference varRef, Expression index) {
        this.varRef = varRef;
        this.index = index;
    }

    public VariableReference getVarRef() {
        return varRef;
    }

    public void setVarRef(VariableReference varRef) {
        this.varRef = varRef;
    }

    public Expression getIndex() {
        return index;
    }

    public void setIndex(Expression index) {
        this.index = index;
    }

    @Override
    public Type getType(Scope scope) throws PMLScopeException {
        return varRef.getType(scope).getMapValueType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReferenceByIndex that = (ReferenceByIndex) o;
        return Objects.equals(varRef, that.varRef) && Objects.equals(index, that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varRef, index);
    }
}
