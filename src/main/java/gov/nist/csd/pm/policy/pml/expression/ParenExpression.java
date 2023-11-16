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
import gov.nist.csd.pm.policy.pml.value.Value;

import java.util.Objects;

public class ParenExpression extends Expression{

    public static Expression compileParenExpression(VisitorContext visitorCtx,
                                                    PMLParser.ExpressionContext expressionCtx) {
        return new ParenExpression(Expression.compile(visitorCtx, expressionCtx, Type.any()));
    }

    private final Expression expression;

    public ParenExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Type getType(Scope scope) throws PMLScopeException {
        return expression.getType(scope);
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        return PMLStatement.execute(ctx, policy, expression);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParenExpression that = (ParenExpression) o;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format("(%s)", expression);
    }
}
