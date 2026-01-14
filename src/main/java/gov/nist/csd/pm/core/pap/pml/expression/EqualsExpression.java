package gov.nist.csd.pm.core.pap.pml.expression;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.BOOLEAN_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import java.util.Objects;

public class EqualsExpression extends Expression<Boolean> {

    private final Expression<?> left;
    private final Expression<?> right;
    private final boolean isEquals;

    public EqualsExpression(Expression<?> left, Expression<?> right, boolean isEquals) {
        this.left = left;
        this.right = right;
        this.isEquals = isEquals;
    }

    @Override
    public Type<Boolean> getType() {
        return BOOLEAN_TYPE;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return left.toString() +
                (isEquals ? " == " : " != ") +
                right.toString();
    }

    @Override
    public Boolean execute(ExecutionContext ctx, PAP pap) throws PMException {
        Object leftValue = left.execute(ctx, pap);
        Object rightValue = right.execute(ctx, pap);

        return isEquals == (leftValue.equals(rightValue));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EqualsExpression that)) {
            return false;
        }
        return isEquals == that.isEquals && Objects.equals(left, that.left) && Objects.equals(right,
            that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, isEquals);
    }
}
