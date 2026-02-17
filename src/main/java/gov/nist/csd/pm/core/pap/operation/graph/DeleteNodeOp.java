package gov.nist.csd.pm.core.pap.operation.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdListFormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilegeOnParameter;
import java.util.List;

public class DeleteNodeOp extends AdminOperation<Void> {

    public static final NodeIdFormalParameter DELETE_NODE_NODE_ID_PARAM =
        new NodeIdFormalParameter("id");

    public DeleteNodeOp() {
        super(
            "delete_node",
            BasicTypes.VOID_TYPE,
            List.of(DELETE_NODE_NODE_ID_PARAM),
            new RequiredCapability(
                new RequiredPrivilegeOnParameter(
                    DELETE_NODE_NODE_ID_PARAM, AdminAccessRight.ADMIN_GRAPH_NODE_DELETE
                )
            )
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().graph().deleteNode(args.get(DELETE_NODE_NODE_ID_PARAM));
        return null;
    }
}
