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
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapabilityFunc;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
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
            List.of(DELETE_NODE_NODE_ID_PARAM, TYPE_PARAM, DELETE_NODE_DESCENDANTS_PARAM),
            new RequiredCapabilityFunc((policyQuery, userCtx, args) -> {
                long nodeId = args.get(DELETE_NODE_NODE_ID_PARAM);
                NodeType type = NodeType.toNodeType(args.get(TYPE_PARAM));

                boolean hasPrivs = policyQuery.access()
                    .computePrivileges(userCtx, new TargetContext(nodeId))
                    .contains(AdminAccessRight.ADMIN_GRAPH_NODE_DELETE.toString());

                if (!hasPrivs) {
                    return false;
                } else if (type == NodeType.PC) {
                    return true;
                }

                List<Long> descs = args.get(DELETE_NODE_DESCENDANTS_PARAM);
                for (Long desc : descs) {
                    hasPrivs = policyQuery.access()
                        .computePrivileges(userCtx, new TargetContext(desc))
                        .contains(AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_DELETE.toString());

                    if(!hasPrivs) {
                        return false;
                    }
                }

                return true;
            })
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().graph().deleteNode(args.get(DELETE_NODE_NODE_ID_PARAM));
        return null;
    }
}
