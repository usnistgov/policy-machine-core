package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.Objects;

public class FunctionReturnStmt extends PALStatement {

    private boolean isVoid;
    private Expression expr;

    public FunctionReturnStmt() {
        this.isVoid = true;
    }

    public FunctionReturnStmt(Expression expr) {
        this.expr = expr;
    }

    public boolean isVoid() {
        return isVoid;
    }

    public Expression getExpr() {
        return expr;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        Value value;
        if (isVoid) {
            value = new Value();
        } else {
            value = expr.execute(ctx, policy);
        }

        return Value.returnValue(value);
    }

    @Override
    public String toString() {
        return String.format("return%s;", (isVoid ? "" : String.format(" %s", expr)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionReturnStmt that = (FunctionReturnStmt) o;
        return isVoid == that.isVoid && Objects.equals(expr, that.expr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isVoid, expr);
    }
}
