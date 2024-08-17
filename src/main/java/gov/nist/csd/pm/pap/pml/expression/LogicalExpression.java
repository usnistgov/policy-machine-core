package gov.nist.csd.pm.pap.pml.expression;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.scope.PMLScopeException;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.BoolValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.Objects;

public class LogicalExpression extends Expression {

    public static Expression compileLogicalExpression(VisitorContext visitorCtx, PMLParser.LogicalExpressionContext logicalExpressionsContext) {
        Expression left = Expression.compile(visitorCtx, logicalExpressionsContext.left, Type.any());
        Expression right = Expression.compile(visitorCtx, logicalExpressionsContext.right, Type.any());

        return new LogicalExpression(left, right, logicalExpressionsContext.LOGICAL_AND() != null);
    }

    private Expression left;
    private Expression right;
    private boolean isAnd;

    public LogicalExpression(Expression left, Expression right, boolean isAnd) {
        this.left = left;
        this.right = right;
        this.isAnd = isAnd;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public boolean isAnd() {
        return isAnd;
    }

    @Override
    public Type getType(Scope<Variable, PMLExecutableSignature> scope) throws PMLScopeException {
        return Type.bool();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return left.toString() +
                (isAnd ? " && " : " || ") +
                right.toString();
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        boolean leftValue = left.execute(ctx, pap).getBooleanValue();
        boolean rightValue = right.execute(ctx, pap).getBooleanValue();

        return new BoolValue(isAnd ? leftValue && rightValue : leftValue || rightValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalExpression that = (LogicalExpression) o;
        return isAnd == that.isAnd && Objects.equals(left, that.left) && Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, isAnd);
    }
}
