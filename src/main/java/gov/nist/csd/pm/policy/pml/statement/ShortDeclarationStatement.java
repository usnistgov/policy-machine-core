package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.VoidValue;

import java.util.Objects;


public class ShortDeclarationStatement extends PMLStatement{

    private String id;
    private Expression expression;

    public ShortDeclarationStatement(String id, Expression expression) {
        this.id = id;
        this.expression = expression;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        ctx.scope().addValue(id, expression.execute(ctx, policy));

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
        ShortDeclarationStatement that = (ShortDeclarationStatement) o;
        return Objects.equals(id, that.id) && Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, expression);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + id + " := " + expression.toFormattedString(indentLevel);
    }
}
