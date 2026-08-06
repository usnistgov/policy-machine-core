package gov.nist.ngac.pm.core.pap.pml.expression;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import java.util.Objects;

/**
 * PML ! boolean negation expression.
 */
public class NegatedExpression extends Expression<Boolean> {

    private final Expression<Boolean> expression;

    public NegatedExpression(Expression<Boolean> expression) {
        this.expression = expression;
    }

    public Expression<Boolean> getExpression() {
        return expression;
    }

    @Override
    public Type<Boolean> getType() {
        return BOOLEAN_TYPE;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return "!" + expression.toString();
    }

    @Override
    public Boolean execute(ExecutionContext ctx, PAP pap) throws PMException {
        return !expression.execute(ctx, pap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NegatedExpression that = (NegatedExpression) o;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(expression);
    }
}
