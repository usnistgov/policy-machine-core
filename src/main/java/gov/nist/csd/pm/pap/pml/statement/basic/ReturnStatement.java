package gov.nist.csd.pm.pap.pml.statement.basic;

import com.sun.jdi.VoidValue;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.function.arg.type.VoidType;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.scope.PMLScopeException;

import gov.nist.csd.pm.pap.pml.statement.result.ReturnResult;
import gov.nist.csd.pm.pap.pml.statement.result.StatementResult;
import gov.nist.csd.pm.pap.pml.statement.result.VoidResult;
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

    public boolean matchesReturnType(ArgType<?> match) {
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
