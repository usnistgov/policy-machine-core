package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.pap.function.op.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.graph.GraphOp.DESCENDANTS_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.graph.*;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.List;
import java.util.Objects;

public class CreateNonPCStatement extends OperationStatement {

    private final NodeType nodeType;
    private final Expression<String> nameExpr;
    private final Expression<List<String>> inExpr;

    public CreateNonPCStatement(Expression<String> nameExpr, NodeType nodeType, Expression<List<String>> inExpr) {
        super(getOpFromType(nodeType));
        this.nodeType = nodeType;
        this.nameExpr = nameExpr;
        this.inExpr = inExpr;
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String name = nameExpr.execute(ctx, pap);
        List<String> inList = inExpr.execute(ctx, pap);

        // convert desc node names to IDs
        LongArrayList descIds = new LongArrayList();
        for (String parentName : inList) {
            descIds.add(pap.query().graph().getNodeByName(parentName).getId());
        }

        return new Args()
            .put(NAME_PARAM, name)
            .put(DESCENDANTS_PARAM, descIds);
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