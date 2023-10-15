package gov.nist.csd.pm.policy.pml.expression.reference;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.exception.PMLExecutionException;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.Scope;
import gov.nist.csd.pm.policy.pml.model.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.util.Objects;

public class ReferenceByID extends VariableReference{

    private final String id;

    public ReferenceByID(String id) {
        this.id = id;
    }

    @Override
    public Type getType(Scope scope) throws PMLScopeException {
        return scope.getVariable(id).type();
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        try {
            return ctx.scope().getValue(id);
        } catch (UnknownVariableInScopeException e) {
            throw new PMLExecutionException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReferenceByID that = (ReferenceByID) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return id;
    }
}
