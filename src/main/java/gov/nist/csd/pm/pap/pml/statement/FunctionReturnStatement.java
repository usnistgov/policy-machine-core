package gov.nist.csd.pm.pap.pml.statement;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.scope.PMLScopeException;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ReturnValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;

import java.util.Objects;


public class FunctionReturnStatement extends ControlStatement {

    private Expression expr;

    public FunctionReturnStatement() {
    }

    public FunctionReturnStatement(Expression expr) {
        this.expr = expr;
    }

    public Expression getExpr() {
        return expr;
    }

    public boolean matchesReturnType(Type match, Scope<Variable, PMLExecutableSignature> scope) throws PMLScopeException {
        if (expr == null) {
            return match.equals(Type.voidType());
        }

        return expr.getType(scope).equals(match);
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        if (expr == null) {
            return new ReturnValue(new VoidValue());
        }

        return new ReturnValue(expr.execute(ctx, pap));
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format("%sreturn%s", indent(indentLevel), (expr == null ? "" : String.format(" %s", expr)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionReturnStatement that = (FunctionReturnStatement) o;
        return Objects.equals(expr, that.expr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expr);
    }
}
