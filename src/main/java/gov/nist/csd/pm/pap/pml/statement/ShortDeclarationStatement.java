package gov.nist.csd.pm.pap.pml.statement;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;

import java.util.Objects;


public class ShortDeclarationStatement extends ControlStatement {

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
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        ctx.scope().addVariable(id, expression.execute(ctx, pap));

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
