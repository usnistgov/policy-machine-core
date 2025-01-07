package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.common.op.graph.CreateObjectAttributeOp;
import gov.nist.csd.pm.common.op.graph.CreateObjectOp;
import gov.nist.csd.pm.common.op.graph.CreateUserAttributeOp;
import gov.nist.csd.pm.common.op.graph.CreateUserOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.*;

import static gov.nist.csd.pm.common.op.graph.CreateNodeOp.*;


public class CreateNonPCStatement extends OperationStatement {
    private Expression name;
    private NodeType type;
    private Expression assignTo;

    public CreateNonPCStatement(Expression name, NodeType type, Expression assignTo) {
        super(getOpFromType(type));
        this.name = name;
        this.type = type;
        this.assignTo = assignTo;
    }

    @Override
    public Map<String, Object> prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
        Value nameValue = name.execute(ctx, pap);
        Value assignToValue = assignTo.execute(ctx, pap);

        List<String> descendants = new ArrayList<>();
        List<Value> arrayValue = assignToValue.getArrayValue();
        for (Value descValue : arrayValue) {
            descendants.add(descValue.getStringValue());
        }

        return Map.of(
                NAME_OPERAND, nameValue.getStringValue(),
                DESCENDANTS_OPERAND, descendants
        );
    }
    
    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format(
                "create %s %s in %s",
                type.toString(),
                name,
                assignTo
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateNonPCStatement that)) return false;
        return Objects.equals(name, that.name) && type == that.type && Objects.equals(assignTo, that.assignTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, assignTo);
    }

    private static Operation<Void> getOpFromType(NodeType type) {
        return switch (type) {
            case OA -> new CreateObjectAttributeOp();
            case O -> new CreateObjectOp();
            case UA -> new CreateUserAttributeOp();
            default -> new CreateUserOp();
        };
    }
}
