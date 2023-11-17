package gov.nist.csd.pm.policy.pml.expression;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.scope.Scope;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.BoolValue;
import gov.nist.csd.pm.policy.pml.value.Value;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalExpression that = (LogicalExpression) o;
        return isAnd == that.isAnd && left.equals(that.left) && right.equals(that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, isAnd);
    }

    @Override
    public Type getType(Scope scope) throws PMLScopeException {
        return Type.bool();
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        boolean leftValue = left.execute(ctx, policy).getBooleanValue();
        boolean rightValue = right.execute(ctx, policy).getBooleanValue();

        return new BoolValue(isAnd ? leftValue && rightValue : leftValue || rightValue);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return left.toString() + (isAnd ? " && " : " || ") + right.toString();
    }

}
