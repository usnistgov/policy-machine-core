package gov.nist.ngac.pm.core.pap.pml.statement.basic;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.statement.result.VoidResult;
import java.util.Objects;

/**
 * PML "=" or "+=" variable assignment statement; "+=" is only supported for string variables and casts
 * the existing value to a String before concatenating.
 */
public class VariableAssignmentStatement extends BasicStatement<VoidResult> {

    private final String id;
    private final boolean isPlus;
    private final Expression<?> expression;

    public VariableAssignmentStatement(String id, boolean isPlus, Expression<?> expression) {
        this.id = id;
        this.isPlus = isPlus;
        this.expression = expression;
    }

    public String getId() {
        return id;
    }

    public boolean isPlus() {
        return isPlus;
    }

    public Expression<?> getExpression() {
        return expression;
    }

    @Override
    public VoidResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        Object value = expression.execute(ctx, pap);

        // if statement uses '+=' add the existing value to the new value
        if (isPlus) {
            String variable = (String) ctx.scope().getVariable(id);

            value = variable + value;
        }

        ctx.scope().updateVariable(id, value);

        return new VoidResult();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VariableAssignmentStatement that = (VariableAssignmentStatement) o;
        return isPlus == that.isPlus && Objects.equals(id, that.id) && Objects.equals(
                expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isPlus, expression);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + id + (isPlus ? " +": " ") + "= " + expression;
    }
} 