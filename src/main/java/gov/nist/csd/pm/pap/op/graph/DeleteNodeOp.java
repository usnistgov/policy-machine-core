package gov.nist.csd.pm.pap.op.graph;


import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.*;

public class DeleteNodeOp extends GraphOp<Void> {

    public DeleteNodeOp() {
        super(
                "delete_node",
                List.of(NODE_OPERAND, TYPE_OPERAND, DESCENDANTS_OPERAND),
                List.of(NODE_OPERAND, DESCENDANTS_OPERAND)
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        long nodeId =  (long) operands.get(NODE_OPERAND);
        NodeType type = privilegeChecker.getNodeType(nodeId);
        ReqCaps reqCaps = getReqCap(type);

        // check ascendant privs
        privilegeChecker.check(userCtx, nodeId, reqCaps.ascReqCap);

        // if pc no descendants are present
        if (type == NodeType.PC) {
            return;
        }

        // check for privs on each descendant
        Collection<Long> descs = (Collection<Long>) operands.get(DESCENDANTS_OPERAND);
        for (Long desc : descs) {
            privilegeChecker.check(userCtx, desc, reqCaps.descsReqCap);
        }
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        pap.modify().graph().deleteNode((long) operands.get(NODE_OPERAND));

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

    public static class EventCtx extends EventContext {

        public EventCtx(String user, String process, String nodeName, List<String> descendantNames) {
            super(user, process, "delete_node", Map.of(
                    NODE_OPERAND, nodeName,
                    DESCENDANTS_OPERAND, descendantNames
            ));
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return super.equals(o);
        }
    }
}
