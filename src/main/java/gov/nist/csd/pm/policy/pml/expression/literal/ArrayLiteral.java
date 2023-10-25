package gov.nist.csd.pm.policy.pml.expression.literal;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.Scope;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.ArrayValue;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ArrayLiteral extends Literal {

    private final List<Expression> array;
    private final Type type;

    public ArrayLiteral(Type elementType) {
        this.array = new ArrayList<>();
        this.type = Type.array(elementType);
    }

    public ArrayLiteral(Type elementType, Expression ... exprs) {
        this.type = Type.array(elementType);
        this.array = new ArrayList<>(List.of(exprs));
    }

    public ArrayLiteral(Expression[] array, Type elementType) {
        this.array = Arrays.asList(array);
        this.type = Type.array(elementType);
    }

    public ArrayLiteral(List<Expression> array, Type elementType) {
        this.array = array;
        this.type = Type.array(elementType);
    }

    @Override
    public Type getType(Scope scope) throws PMLScopeException {
        return type;
    }

    public void add(Expression expression) {
        this.array.add(expression);
    }

    public Expression[] getArray() {
        return this.array.toArray(new Expression[]{});
    }

    public List<Expression> getList() {
        return this.array;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayLiteral that = (ArrayLiteral) o;
        return Objects.equals(array, that.array)
                && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(array, type);
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        List<Value> values = new ArrayList<>();
        for (Expression expr : array) {
            values.add(expr.execute(ctx, policy));
        }

        return new ArrayValue(values, type.getArrayElementType());
    }

    @Override
    public String toFormattedString(int indentLevel) {
        StringBuilder s = new StringBuilder();
        for (Expression expression : array) {
            if (s.length() > 0) {
                s.append(", ");
            }

            s.append(expression.toString());
        }
        return String.format("[%s]", s);
    }
}
