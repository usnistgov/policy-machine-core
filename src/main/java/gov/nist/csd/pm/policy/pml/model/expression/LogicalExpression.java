package gov.nist.csd.pm.policy.pml.model.expression;

import com.mysql.cj.log.Log;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;

import java.io.Serializable;
import java.util.Objects;

public class LogicalExpression extends PMLStatement implements Serializable {

    private Expression left;
    private Expression right;
    private boolean isAnd;

    public LogicalExpression(Expression left, Expression right, boolean isAnd) {
        this.left = left;
        this.right = right;
        this.isAnd = isAnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalExpression that = (LogicalExpression) o;
        return isAnd == that.isAnd && left.equals(that.left) && right.equals(that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, isAnd);
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        boolean leftValue = left.execute(ctx, policy).getBooleanValue();
        boolean rightValue = right.execute(ctx, policy).getBooleanValue();

        return new Value(isAnd ? leftValue && rightValue : leftValue || rightValue);
    }

    @Override
    public String toString() {
        return left.toString() + (isAnd ? " && " : " || ") + right.toString();
    }

}
