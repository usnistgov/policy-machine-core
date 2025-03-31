package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.function.op.graph.*;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.value.Value;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateNonPCStatement extends OperationStatement<Operation<Long>> {

    private final NodeType nodeType;
    private final Expression nameExpr;
    private final Expression inExpr;

    public CreateNonPCStatement(Expression nameExpr, NodeType nodeType, Expression inExpr) {
        super(getOpFromType(nodeType));
        this.nodeType = nodeType;
        this.nameExpr = nameExpr;
        this.inExpr = inExpr;
    }

    @Override
    public Args prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
        String name = nameExpr.execute(ctx, pap).getStringValue();
        
        List<Value> inList = inExpr.execute(ctx, pap).getArrayValue();
        List<String> parentNames = new ArrayList<>();
        for (Value value : inList) {
            parentNames.add(value.getStringValue());
        }

        // convert parent node names to IDs
        LongArrayList parentIds = new LongArrayList();
        for (String parentName : parentNames) {
            parentIds.add(pap.query().graph().getNodeByName(parentName).getId());
        }

        return ((CreateNodeOp) op).actualArgs(name, parentIds);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        String nodeTypeStr;
        if (nodeType == NodeType.OA) {
            nodeTypeStr = "OA";
        } else if (nodeType == NodeType.UA) {
            nodeTypeStr = "UA";
        } else if (nodeType == NodeType.O) {
            nodeTypeStr = "O";
        } else if (nodeType == NodeType.U) {
            nodeTypeStr = "U";
        } else {
            nodeTypeStr = nodeType.toString();
        }

        return indent(indentLevel) + String.format("create %s %s in %s", nodeTypeStr, nameExpr, inExpr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateNonPCStatement that)) return false;
        return nodeType == that.nodeType && 
               Objects.equals(nameExpr, that.nameExpr) && 
               Objects.equals(inExpr, that.inExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeType, nameExpr, inExpr);
    }

    private static CreateNodeOp getOpFromType(NodeType type) {
        return switch (type) {
            case OA -> new CreateObjectAttributeOp();
            case O -> new CreateObjectOp();
            case UA -> new CreateUserAttributeOp();
            default -> new CreateUserOp();
        };
    }
} 