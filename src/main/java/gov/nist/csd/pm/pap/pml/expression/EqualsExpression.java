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
    public Type getType(Scope<Variable, PMLExecutableSignature> scope) throws PMLScopeException {
        return Type.bool();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return left.toString() +
                (isEquals ? " == " : " != ") +
                right.toString();
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        Value leftValue = left.execute(ctx, pap);
        Value rightValue = right.execute(ctx, pap);

        return new BoolValue(isEquals == (leftValue.equals(rightValue)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EqualsExpression that = (EqualsExpression) o;
        return isEquals == that.isEquals && Objects.equals(left, that.left) && Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, isEquals);
    }
}
