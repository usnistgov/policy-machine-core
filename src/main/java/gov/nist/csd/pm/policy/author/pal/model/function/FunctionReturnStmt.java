package gov.nist.csd.pm.policy.author.pal.model.function;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

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
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        Value value;
        if (isVoid) {
            value = new Value();
        } else {
            value = expr.execute(ctx, policyAuthor);
        }

        return Value.returnValue(value);
    }

    @Override
    public String toString(int indent) {
        return format(indent, "return%s", (isVoid ? "" : String.format(" %s", expr)));
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
