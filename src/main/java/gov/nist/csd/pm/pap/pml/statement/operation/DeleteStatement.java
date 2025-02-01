package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.pap.pml.expression.Expression;

import java.util.Objects;

public abstract class DeleteStatement extends OperationStatement {

    private Type type;
    private Expression expression;

    public DeleteStatement(Operation<Void> op, Type type, Expression expression) {
        super(op);
        this.type = type;
        this.expression = expression;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        String typeStr = "";
        switch (type) {
            case PROHIBITION -> typeStr = "prohibition";
            case OBLIGATION -> typeStr = "obligation";
            case NODE -> typeStr = "node";
        }

        return indent(indentLevel) + String.format("delete %s %s", typeStr, expression);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeleteStatement that)) return false;
        return type == that.type && Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, expression);
    }

    public enum Type {
        NODE,
        PROHIBITION,
        OBLIGATION
    }
}
