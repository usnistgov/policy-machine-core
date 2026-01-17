package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.Operation;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.statement.result.VoidResult;
import java.util.Objects;

public abstract class DeleteStatement extends OperationStatement {

    protected Type type;
    protected Expression<String> nameExpression;
    protected boolean ifExists;

    public DeleteStatement(Operation<?> op, Type type, Expression<String> nameExpression, boolean ifExists) {
        super(op);
        this.type = type;
        this.nameExpression = nameExpression;
        this.ifExists = ifExists;
    }

    public abstract boolean exists(PAP pap, String name) throws PMException;

    @Override
    public VoidResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        String name = nameExpression.execute(ctx, pap);

        // if the statement includes "if exists" and the entity does not exist, return early
        if (ifExists && !exists(pap, name)) {
            return new VoidResult();
        }

        return super.execute(ctx, pap);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Expression<String> getNameExpression() {
        return nameExpression;
    }

    public void setNameExpression(Expression<String> nameExpression) {
        this.nameExpression = nameExpression;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public void setIfExists(boolean ifExists) {
        this.ifExists = ifExists;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        String typeStr = "";
        switch (type) {
            case PROHIBITION -> typeStr = "prohibition";
            case OBLIGATION -> typeStr = "obligation";
            case ADMIN_OP -> typeStr = "adminop";
            case RESOURCE_OP -> typeStr = "resourceop";
            case NODE -> typeStr = "node";
        }
        typeStr = (ifExists ? "if exists " : "") + typeStr;

        return indent(indentLevel) + String.format("delete %s %s", typeStr, nameExpression);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeleteStatement that)) {
            return false;
        }
        return ifExists == that.ifExists && type == that.type && Objects.equals(nameExpression, that.nameExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, nameExpression, ifExists);
    }

    public enum Type {
        NODE,
        PROHIBITION,
        OBLIGATION,
        ADMIN_OP,
        RESOURCE_OP
    }
}
