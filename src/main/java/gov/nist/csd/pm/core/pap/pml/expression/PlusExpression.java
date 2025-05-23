package gov.nist.csd.pm.core.pap.pml.expression;

import static gov.nist.csd.pm.core.pap.function.arg.type.Type.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;

import java.util.Objects;

public class PlusExpression extends Expression<String> {

    private final Expression<String> left;
    private final Expression<String> right;

    public PlusExpression(Expression<String> left, Expression<String> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Type<String> getType() {
        return STRING_TYPE;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return left.toString() + " + " + right.toString();
    }

    @Override
    public String execute(ExecutionContext ctx, PAP pap) throws PMException {
        String leftValue = left.execute(ctx, pap);
        String rightValue = right.execute(ctx, pap);

        return leftValue + rightValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlusExpression that = (PlusExpression) o;
        return Objects.equals(left, that.left) && Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
