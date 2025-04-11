package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.arg.IdNodeFormalParameter;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.*;

public class DeleteNodeOp extends GraphOp<Void, DeleteNodeOp.DeleteNodeOpArgs> {

    public static final IdNodeFormalParameter NODE_ARG = new IdNodeFormalParameter("node");

    public DeleteNodeOp() {
        super(
                "delete_node",
                List.of(NODE_ARG, TYPE_ARG, DESCENDANTS_ARG)
        );
    }

    public static class DeleteNodeOpArgs extends Args {
        private final long nodeId;
        private final NodeType type;
        private final List<Long> descendantIds;

        public DeleteNodeOpArgs(long nodeId, NodeType type, List<Long> descendantIds) {
            this.nodeId = nodeId;
            this.type = type;
            this.descendantIds = descendantIds;
        }

        public long getNodeId() {
            return nodeId;
        }

        public NodeType getType() {
            return type;
        }

        public List<Long> getDescendantIds() {
            return descendantIds;
        }
    }

    @Override
    protected DeleteNodeOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        Long nodeId = prepareArg(NODE_ARG, argsMap);
        NodeType type = prepareArg(TYPE_ARG, argsMap);
        List<Long> descIds = prepareArg(DESCENDANTS_ARG, argsMap);
        return new DeleteNodeOpArgs(nodeId, type, descIds);
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, DeleteNodeOpArgs args) throws PMException {
        long nodeId = args.getNodeId();
        NodeType type = args.getType();
        ReqCaps reqCaps = getReqCap(type);

        privilegeChecker.check(userCtx, nodeId, reqCaps.ascReqCap);

        if (type == NodeType.PC) {
            return;
        }

        List<Long> descs = args.getDescendantIds();
        for (Long desc : descs) {
            privilegeChecker.check(userCtx, desc, reqCaps.descsReqCap);
        }
    }

    @Override
    public Void execute(PAP pap, DeleteNodeOpArgs args) throws PMException {
        pap.modify().graph().deleteNode(args.getNodeId());
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
