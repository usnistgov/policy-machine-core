package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.op.graph.*;
import gov.nist.csd.pm.pap.op.obligation.DeleteObligationOp;
import gov.nist.csd.pm.pap.op.prohibition.DeleteProhibitionOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;

import java.util.Map;
import java.util.Objects;

import static gov.nist.csd.pm.pap.op.Operation.NAME_OPERAND;

public class DeleteStatement extends OperationStatement {

    private Type type;
    private Expression expression;

    public DeleteStatement(Type type, Expression expression) {
        super(getOpFromType(type));
        this.type = type;
        this.expression = expression;
    }

    @Override
    public Map<String, Object> prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
        String name = expression.execute(ctx, pap).getStringValue();

        return Map.of(NAME_OPERAND, name);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        String typeStr = "";
        switch (type) {
            case PROHIBITION -> typeStr = "prohibition";
            case OBLIGATION -> typeStr = "obligation";
            case POLICY_CLASS -> typeStr = "PC";
            case OBJECT_ATTRIBUTE -> typeStr = "OA";
            case USER_ATTRIBUTE -> typeStr = "UA";
            case OBJECT -> typeStr = "O";
            case USER -> typeStr = "U";
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
        POLICY_CLASS,
        OBJECT_ATTRIBUTE,
        USER_ATTRIBUTE,
        OBJECT,
        USER,
        PROHIBITION,
        OBLIGATION
    }

    private static Operation<Void> getOpFromType(Type type) {
        return switch (type) {
            case POLICY_CLASS -> new DeletePolicyClassOp();
            case OBJECT_ATTRIBUTE -> new DeleteObjectAttributeOp();
            case USER_ATTRIBUTE -> new DeleteUserAttributeOp();
            case OBJECT -> new DeleteObjectOp();
            case USER -> new DeleteUserOp();
            case PROHIBITION -> new DeleteProhibitionOp();
            case OBLIGATION -> new DeleteObligationOp();
        };
    }
}
