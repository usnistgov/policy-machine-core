package gov.nist.csd.pm.pap.pml.expression.literal;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;

import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.scope.PMLScopeException;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ArrayValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ArrayLiteral extends Literal {

    private final List<Expression> array;
    private final Type type;

    public ArrayLiteral(Expression[] array, Type elementType) {
        this.array = Arrays.asList(array);
        this.type = Type.array(elementType);
    }

    public ArrayLiteral(List<Expression> array, Type elementType) {
        this.array = array;
        this.type = Type.array(elementType);
    }

    @Override
    public Type getType(Scope<Variable, PMLFunctionSignature> scope) throws PMLScopeException {
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

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        List<Value> values = new ArrayList<>();
        for (Expression expr : array) {
            values.add(expr.execute(ctx, pap));
        }

        return new ArrayValue(values, type.getArrayElementType());
    }
}
