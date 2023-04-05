package gov.nist.csd.pm.policy.author.pal.model.expression;

import gov.nist.csd.pm.policy.author.pal.statement.Expression;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ArrayLiteral implements Serializable {

    private final List<Expression> array;
    private final Type type;

    public ArrayLiteral(Type elementType) {
        this.array = new ArrayList<>();
        this.type = Type.array(elementType);
    }

    public ArrayLiteral(Expression[] array, Type elementType) {
        this.array = Arrays.asList(array);
        this.type = Type.array(elementType);
    }

    public void add(Expression expression) {
        this.array.add(expression);
    }

    public Expression[] getArray() {
        return this.array.toArray(new Expression[]{});
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
    public String toString() {
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
