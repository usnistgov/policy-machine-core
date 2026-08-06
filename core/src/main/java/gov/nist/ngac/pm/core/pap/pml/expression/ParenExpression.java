package gov.nist.ngac.pm.core.pap.pml.expression;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import java.util.Objects;

/**
 * A parenthesized PML expression, evaluating to its wrapped expression's value unchanged.
 */
public class ParenExpression<T> extends Expression<T> {

    private final Expression<T> expression;

    public ParenExpression(Expression<T> expression) {
        this.expression = expression;
    }

    public Expression<T> getExpression() {
        return expression;
    }

    @Override
    public Type<T> getType() {
        return expression.getType();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format("(%s)", expression.toFormattedString(0));
    }

    @Override
    public T execute(ExecutionContext ctx, PAP pap) throws PMException {
        return expression.execute(ctx, pap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParenExpression<?> that)) {
            return false;
        }
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(expression);
    }
}
