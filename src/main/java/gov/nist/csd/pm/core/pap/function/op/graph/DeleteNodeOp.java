package gov.nist.csd.pm.core.pap.function.op.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.AdminOperation;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeFormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeListFormalParameter;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.*;

public class DeleteNodeOp extends AdminOperation<Void> {

    public static final NodeFormalParameter DELETE_NODE_NODE_PARAM =
        new NodeFormalParameter("node");
    public static final NodeListFormalParameter DELETE_NODE_DESCENDANTS_PARAM =
        new NodeListFormalParameter("descendants");


    public DeleteNodeOp() {
        super(
                "delete_node",
                List.of(DELETE_NODE_NODE_PARAM, TYPE_PARAM, DELETE_NODE_DESCENDANTS_PARAM)
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        long nodeId = args.get(DELETE_NODE_NODE_PARAM).getId(pap);
        NodeType type = NodeType.toNodeType(args.get(TYPE_PARAM));
        ReqCaps reqCaps = getReqCap(type);

        pap.privilegeChecker().check(userCtx, nodeId, reqCaps.ascReqCap);

        if (type == NodeType.PC) {
            return;
        }

        List<Long> descs = args.getIdList(DELETE_NODE_DESCENDANTS_PARAM, pap);
        for (Long desc : descs) {
            pap.privilegeChecker().check(userCtx, desc, reqCaps.descsReqCap);
        }
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().graph().deleteNode(args.get(DELETE_NODE_NODE_PARAM).getId(pap));
        return null;
    }

    private ReqCaps getReqCap(NodeType type) {
        return switch (type) {
            case OA -> new ReqCaps(DELETE_OBJECT_ATTRIBUTE, DELETE_OBJECT_ATTRIBUTE_FROM);
            case UA -> new ReqCaps(DELETE_USER_ATTRIBUTE, DELETE_USER_ATTRIBUTE_FROM);
            case U -> new ReqCaps(DELETE_USER, DELETE_USER_FROM);
            case O  -> new ReqCaps(DELETE_OBJECT, DELETE_OBJECT_FROM);
            case PC -> new ReqCaps(DELETE_POLICY_CLASS, null);
            default -> throw new IllegalArgumentException("Unsupported node type: " + type);
        };
    }

    private record ReqCaps(String ascReqCap, String descsReqCap) {}
}
