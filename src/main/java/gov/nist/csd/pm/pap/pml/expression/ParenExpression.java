package gov.nist.csd.pm.pap.pml.expression;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.scope.PMLScopeException;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;

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

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Type getType(Scope<Variable, PMLExecutableSignature> scope) throws PMLScopeException {
        return expression.getType(scope);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format("(%s)", expression.toFormattedString(0));
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        return expression.execute(ctx, pap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParenExpression that = (ParenExpression) o;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(expression);
    }
}
