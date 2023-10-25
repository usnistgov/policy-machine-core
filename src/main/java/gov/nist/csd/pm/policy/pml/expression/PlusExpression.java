package gov.nist.csd.pm.policy.pml.expression;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.Scope;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.util.Objects;

public class PlusExpression extends Expression{

    public static Expression compilePlusExpression(VisitorContext visitorCtx, PMLParser.PlusExpressionContext plusExpressionsContext) {
        return new PlusExpression(
                Expression.compile(visitorCtx, plusExpressionsContext.left, Type.string()),
                Expression.compile(visitorCtx, plusExpressionsContext.right, Type.string())
        );
    }

    private Expression left;
    private Expression right;

    public PlusExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public void setLeft(Expression left) {
        this.left = left;
    }

    public Expression getRight() {
        return right;
    }

    public void setRight(Expression right) {
        this.right = right;
    }

    @Override
    public Type getType(Scope scope) throws PMLScopeException {
        return Type.string();
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        String leftStr = left.execute(ctx, policy).getStringValue();
        String rightStr = right.execute(ctx, policy).getStringValue();

        return new StringValue(leftStr + rightStr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlusExpression that = (PlusExpression) o;
        return Objects.equals(left, that.left) && Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return left.toString() + "+" + right.toString();
    }

}
