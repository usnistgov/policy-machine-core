package gov.nist.csd.pm.core.pap.operation.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdListFormalParameter;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public class DeleteNodeOp extends AdminOperation<Void> {

    public static final NodeIdFormalParameter DELETE_NODE_NODE_ID_PARAM =
        new NodeIdFormalParameter("id");
    public static final NodeIdListFormalParameter DELETE_NODE_DESCENDANTS_PARAM =
        new NodeIdListFormalParameter("descendants");


    public DeleteNodeOp() {
        super(
            "delete_node",
            BasicTypes.VOID_TYPE,
            List.of(DELETE_NODE_NODE_ID_PARAM, TYPE_PARAM, DELETE_NODE_DESCENDANTS_PARAM)
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        long nodeId = args.get(DELETE_NODE_NODE_ID_PARAM);
        NodeType type = NodeType.toNodeType(args.get(TYPE_PARAM));
        ReqCaps reqCaps = getReqCap(type);

        pap.privilegeChecker().check(userCtx, nodeId, reqCaps.ascReqCap.toString());

        if (type == NodeType.PC) {
            return;
        }

        List<Long> descs = args.get(DELETE_NODE_DESCENDANTS_PARAM);
        for (Long desc : descs) {
            pap.privilegeChecker().check(userCtx, desc, reqCaps.descsReqCap.toString());
        }
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().graph().deleteNode(args.get(DELETE_NODE_NODE_ID_PARAM));
        return null;
    }

    private ReqCaps getReqCap(NodeType type) {
        return switch (type) {
            case OA -> new ReqCaps(AdminAccessRight.ADMIN_GRAPH_NODE_OA_DELETE, AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_DELETE);
            case UA -> new ReqCaps(AdminAccessRight.ADMIN_GRAPH_NODE_UA_DELETE, AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_DELETE);
            case U -> new ReqCaps(AdminAccessRight.ADMIN_GRAPH_NODE_U_DELETE, AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_DELETE);
            case O  -> new ReqCaps(AdminAccessRight.ADMIN_GRAPH_NODE_O_DELETE, AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_DELETE);
            case PC -> new ReqCaps(AdminAccessRight.ADMIN_GRAPH_NODE_PC_DELETE, null);
            default -> throw new IllegalArgumentException("Unsupported node type: " + type);
        };
    }

    private record ReqCaps(AdminAccessRight ascReqCap, AdminAccessRight descsReqCap) {}
}
