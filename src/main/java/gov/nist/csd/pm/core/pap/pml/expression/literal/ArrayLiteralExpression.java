package gov.nist.csd.pm.core.pap.pml.expression.literal;


import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArrayLiteralExpression<E> extends Expression<List<E>> {

    public static <E> ArrayLiteralExpression<E> of(List<Expression<E>> expressions, Type<E> elementType) {
        return new ArrayLiteralExpression<>(new ArrayList<>(expressions), elementType);
    }

    private final List<Expression<?>> compiledExpressions;
    private final ListType<E> type;

    public ArrayLiteralExpression(List<Expression<?>> compiledExpressions, Type<E> elementType) {
        this.compiledExpressions = new ArrayList<>(compiledExpressions);
        this.type = ListType.of(Objects.requireNonNull(elementType));
    }

    @Override
    public ListType<E> getType() {
        return type;
    }

    @Override
    public List<E> execute(ExecutionContext ctx, PAP pap) throws PMException {
        List<E> results = new ArrayList<>();
        
        for (Expression<?> elementExpr : compiledExpressions) {
            Object value = elementExpr.execute(ctx, pap);
            results.add(type.getElementType().cast(value));
        }

        return results;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        StringBuilder s = new StringBuilder();
        for (Expression<?> expression : compiledExpressions) {
            if (!s.isEmpty()) {
                s.append(", ");
            }

            s.append(expression.toString());
        }
        return String.format("[%s]", s);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArrayLiteralExpression<?> that)) {
            return false;
        }
        return Objects.equals(compiledExpressions, that.compiledExpressions) && Objects.equals(type,
            that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compiledExpressions, type);
    }
}