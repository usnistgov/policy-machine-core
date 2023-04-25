package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.Objects;

public class VarStatement extends PMLStatement {

    private final String varName;
    private final Expression expression;

    private final boolean isConst;

    public VarStatement(String varName, Expression expression, boolean isConst) {
        this.varName = varName;
        this.expression = expression;
        this.isConst = isConst;
    }

    public String getVarName() {
        return varName;
    }

    public Expression getExpression() {
        return expression;
    }

    public boolean isConst() {
        return isConst;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        ctx.scope().putValue(varName, expression.execute(ctx, policy));

        return new Value();
    }

    @Override
    public String toString() {
        String s = "";
        if (isConst) {
            s = "const ";
        } else {
            s = "let ";
        }

        return String.format("%s%s = %s", s, varName, expression);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VarStatement letStmt = (VarStatement) o;
        return Objects.equals(varName, letStmt.varName) && Objects.equals(expression, letStmt.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varName, expression);
    }
}
