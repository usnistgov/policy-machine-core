package gov.nist.csd.pm.pap.pml.expression;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;

import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.scope.PMLScopeException;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.Objects;

public class PlusExpression extends Expression{

    public static Expression compilePlusExpression(VisitorContext visitorCtx, PMLParser.PlusExpressionContext plusExpressionsContext) {
        return new PlusExpression(
                Expression.compile(visitorCtx, plusExpressionsContext.left, Type.string()),
                Expression.compile(visitorCtx, plusExpressionsContext.right, Type.string())
        );
    }

    private final Expression left;
    private final Expression right;

    public PlusExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Type getType(Scope<Variable, PMLFunctionSignature> scope) throws PMLScopeException {
        return Type.string();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return left.toString() + " + " + right.toString();
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        String leftValue = left.execute(ctx, pap).getStringValue();
        String rightValue = right.execute(ctx, pap).getStringValue();

        return new StringValue(leftValue + rightValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlusExpression that = (PlusExpression) o;
        return Objects.equals(left, that.left) && Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
