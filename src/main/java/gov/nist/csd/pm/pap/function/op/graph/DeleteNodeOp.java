package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.Collection;
import java.util.List;

import static gov.nist.csd.pm.pap.AdminAccessRights.*;

public class DeleteNodeOp extends GraphOp<Void> {

    public DeleteNodeOp() {
        super(
                "delete_node",
                List.of(NODE_ARG, TYPE_ARG, DESCENDANTS_ARG)
        );
    }
    
    public Args actualArgs(long nodeId, NodeType type, LongArrayList descendants) {
        Args args = new Args();
        args.put(NODE_ARG, nodeId);
        args.put(TYPE_ARG, type);
        args.put(DESCENDANTS_ARG, descendants);
        return args;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Args args) throws PMException {
        long nodeId = args.get(NODE_ARG);
        NodeType type = args.get(TYPE_ARG);
        ReqCaps reqCaps = getReqCap(type);

        // check ascendant privs
        privilegeChecker.check(userCtx, nodeId, reqCaps.ascReqCap);

        // if pc no descendants are present
        if (type == NodeType.PC) {
            return;
        }

        // check for privs on each descendant
        Collection<Long> descs = args.get(DESCENDANTS_ARG);
        for (Long desc : descs) {
            privilegeChecker.check(userCtx, desc, reqCaps.descsReqCap);
        }
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().graph().deleteNode(args.get(NODE_ARG));
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
