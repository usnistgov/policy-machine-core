package gov.nist.csd.pm.policy.pml.expression;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.scope.Scope;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.util.Objects;

public class AnyExpression extends Expression {

    private Expression e;

    public AnyExpression(Expression e) {
        this.e = e;
    }

    @Override
    public Type getType(Scope scope) throws PMLScopeException {
        return e.getType(scope);
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        return PMLStatement.execute(ctx, policy, e);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnyExpression that = (AnyExpression) o;
        return Objects.equals(e, that.e);
    }

    @Override
    public int hashCode() {
        return Objects.hash(e);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return e.toFormattedString(indentLevel);
    }
}
