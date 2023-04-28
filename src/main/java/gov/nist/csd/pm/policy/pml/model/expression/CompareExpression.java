package gov.nist.csd.pm.policy.pml.model.expression;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;

import java.io.Serializable;
import java.util.Objects;

public class CompareExpression extends PMLStatement implements Serializable {

    private Expression left;
    private Expression right;
    private boolean isEquals;

    public CompareExpression(Expression left, Expression right, boolean isEquals) {
        this.left = left;
        this.right = right;
        this.isEquals = isEquals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompareExpression that = (CompareExpression) o;
        return isEquals == that.isEquals && left.equals(that.left) && right.equals(that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, isEquals);
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        return new Value(
                isEquals == left.execute(ctx, policy).equals(right.execute(ctx, policy))
        );
    }

    @Override
    public String toString() {
        return left.toString() + (isEquals ? " == " : " != ") + right.toString();
    }
}
