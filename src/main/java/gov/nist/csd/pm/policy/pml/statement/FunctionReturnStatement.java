package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.Scope;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.VoidValue;

import java.util.Objects;


public class FunctionReturnStatement extends PMLStatement {

    private Expression expr;

    public FunctionReturnStatement() {
    }

    public FunctionReturnStatement(Expression expr) {
        this.expr = expr;
    }

    public Expression getExpr() {
        return expr;
    }

    public boolean matchesReturnType(Type match, Scope scope) throws PMLScopeException {
        if (expr == null) {
            return match.equals(Type.voidType());
        }

        return expr.getType(scope).equals(match);
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        if (expr == null) {
            return new VoidValue();
        }

        return expr.execute(ctx, policy);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format("%sreturn%s", indent(indentLevel), (expr == null ? "" : String.format(" %s", expr)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionReturnStatement that = (FunctionReturnStatement) o;
        return Objects.equals(expr, that.expr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expr);
    }
}
