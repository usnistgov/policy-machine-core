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

public class EqualsExpression extends Expression {

    public static Expression compileEqualsExpression(VisitorContext visitorCtx, PMLParser.EqualsExpressionContext equalsExpressionContext) {
        Expression left = Expression.compile(visitorCtx, equalsExpressionContext.left, Type.any());
        Expression right = Expression.compile(visitorCtx, equalsExpressionContext.right, Type.any());

        return new EqualsExpression(left, right, equalsExpressionContext.EQUALS() != null);
    }

    private Expression left;
    private Expression right;
    private boolean isEquals;

    public EqualsExpression(Expression left, Expression right, boolean isEquals) {
        this.left = left;
        this.right = right;
        this.isEquals = isEquals;
    }

    @Override
    public Type getType(Scope scope) throws PMLScopeException {
        return Type.bool();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EqualsExpression that = (EqualsExpression) o;
        return isEquals == that.isEquals && left.equals(that.left) && right.equals(that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, isEquals);
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        return new BoolValue(
                isEquals == left.execute(ctx, policy).equals(right.execute(ctx, policy))
        );
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return left.toString() + (isEquals ? " == " : " != ") + right.toString();
    }
}
