package gov.nist.csd.pm.core.pap.pml.statement.basic;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.function.arg.type.VoidType;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.statement.result.ReturnResult;
import gov.nist.csd.pm.core.pap.pml.statement.result.StatementResult;
import java.util.Objects;


public class ReturnStatement extends BasicStatement<StatementResult> {

    private Expression<?> expr;

    public ReturnStatement() {
    }

    public ReturnStatement(Expression<?> expr) {
        this.expr = expr;
    }

    public Expression<?> getExpr() {
        return expr;
    }

    public boolean matchesReturnType(Type<?> match) {
        if (expr == null) {
            return match.equals(new VoidType());
        }

        return expr.getType().equals(match);
    }

    @Override
    public StatementResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        if (expr == null) {
            return new ReturnResult(null);
        }

        return new ReturnResult(expr.execute(ctx, pap));
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format("%sreturn%s", indent(indentLevel), (expr == null ? "" : String.format(" %s", expr)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReturnStatement that = (ReturnStatement) o;
        return Objects.equals(expr, that.expr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expr);
    }
}
