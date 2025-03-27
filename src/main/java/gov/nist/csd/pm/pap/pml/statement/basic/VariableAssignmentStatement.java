package gov.nist.csd.pm.pap.pml.statement.basic;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;

import java.util.Objects;

public class VariableAssignmentStatement extends BasicStatement {

    private final String id;
    private final boolean isPlus;
    private final Expression expression;

    public VariableAssignmentStatement(String id, boolean isPlus, Expression expression) {
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

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        Value value = expression.execute(ctx, pap);

        // if statement uses '+=' add the existing value to the new value
        if (isPlus) {
            String strValue = ctx.scope().getVariable(id).getStringValue();
            String exprValue = expression.execute(ctx, pap).getStringValue();

            value = new StringValue(strValue + exprValue);
        }

        ctx.scope().updateVariable(id, value);

        return new VoidValue();
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