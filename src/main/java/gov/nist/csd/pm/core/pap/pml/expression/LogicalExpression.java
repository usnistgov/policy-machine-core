package gov.nist.csd.pm.core.pap.pml.expression;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.BOOLEAN_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import java.util.Objects;

public class LogicalExpression extends Expression<Boolean> {

    private final Expression<Boolean> left;
    private final Expression<Boolean> right;
    private final boolean isAnd;

    public LogicalExpression(Expression<Boolean> left, Expression<Boolean> right, boolean isAnd) {
        this.left = left;
        this.right = right;
        this.isAnd = isAnd;
    }

    public Expression<Boolean> getLeft() {
        return left;
    }

    public Expression<Boolean> getRight() {
        return right;
    }

    public boolean isAnd() {
        return isAnd;
    }

    @Override
    public Type<Boolean> getType() {
        return BOOLEAN_TYPE;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return left.toString() +
                (isAnd ? " && " : " || ") +
                right.toString();
    }

    @Override
    public Boolean execute(ExecutionContext ctx, PAP pap) throws PMException {
        boolean leftValue = left.execute(ctx, pap);
        boolean rightValue = right.execute(ctx, pap);

        return isAnd ? leftValue && rightValue : leftValue || rightValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalExpression that = (LogicalExpression) o;
        return isAnd == that.isAnd && Objects.equals(left, that.left) && Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, isAnd);
    }
}
