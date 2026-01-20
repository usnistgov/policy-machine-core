package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.pap.operation.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.pap.operation.graph.CreateObjectAttributeOp.CREATE_OA_DESCENDANTS_PARAM;
import static gov.nist.csd.pm.core.pap.operation.graph.CreateObjectOp.CREATE_O_DESCENDANTS_PARAM;
import static gov.nist.csd.pm.core.pap.operation.graph.CreateUserAttributeOp.CREATE_UA_DESCENDANTS_PARAM;
import static gov.nist.csd.pm.core.pap.operation.graph.CreateUserOp.CREATE_U_DESCENDANTS_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.graph.CreateObjectAttributeOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreateObjectOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreateUserAttributeOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreateUserOp;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdListFormalParameter;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import java.util.ArrayList;
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

        List<Long> descIds = new ArrayList<>();
        for (String parentName : inList) {
            descIds.add(pap.query().graph().getNodeByName(parentName).getId());
        }

        return new Args()
            .put(NAME_PARAM, name)
            .put(getDescendantsFormalParam(nodeType), descIds);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format("create %s %s in %s", nodeType.toString(), nameExpr, inExpr);
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

    private static Operation<Long> getOpFromType(NodeType type) {
        return switch (type) {
            case OA -> new CreateObjectAttributeOp();
            case O -> new CreateObjectOp();
            case UA -> new CreateUserAttributeOp();
            default -> new CreateUserOp();
        };
    }

    private static NodeIdListFormalParameter getDescendantsFormalParam(NodeType type) {
        return switch (type) {
            case OA -> CREATE_OA_DESCENDANTS_PARAM;
            case O -> CREATE_O_DESCENDANTS_PARAM;
            case UA -> CREATE_UA_DESCENDANTS_PARAM;
            default -> CREATE_U_DESCENDANTS_PARAM;
        };
    }

} 