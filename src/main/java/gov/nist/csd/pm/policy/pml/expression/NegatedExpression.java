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
import gov.nist.csd.pm.policy.pml.value.ComplementedValue;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.util.Objects;

public class NegatedExpression extends Expression {

    public static NegatedExpression compileNegatedExpression(VisitorContext visitorCtx,
                                                             PMLParser.NegateExpressionContext negateExpressionContext) {
        Expression expression = Expression.compile(visitorCtx, negateExpressionContext.expression(), Type.any());

        return new NegatedExpression(expression);
    }

    private Expression expression;

    public NegatedExpression(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Type getType(Scope scope) throws PMLScopeException {
        return expression.getType(scope);
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        Value value = PMLStatement.execute(ctx, policy, expression);

        return new ComplementedValue(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NegatedExpression that = (NegatedExpression) o;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(true, expression);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return "!" + expression.toString();
    }
}
