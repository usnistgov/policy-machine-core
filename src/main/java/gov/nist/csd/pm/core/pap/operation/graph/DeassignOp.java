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

public class DeassignOp extends AdminOperation<Void> {

    public static final NodeIdFormalParameter DEASSIGN_ASCENDANT_PARAM =
        new NodeIdFormalParameter("ascendant");

    public static final NodeIdListFormalParameter DEASSIGN_DESCENDANTS_PARAM =
        new NodeIdListFormalParameter("descendants");

    public DeassignOp() {
        super(
            "deassign",
            BasicTypes.VOID_TYPE,
            List.of(DEASSIGN_ASCENDANT_PARAM, DEASSIGN_DESCENDANTS_PARAM),
            new RequiredCapability(
                new RequiredPrivilegeOnParameter(DEASSIGN_ASCENDANT_PARAM, AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_DELETE),
                new RequiredPrivilegeOnParameter(DEASSIGN_DESCENDANTS_PARAM, AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_DELETE)
            )
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        long ascId = args.get(DEASSIGN_ASCENDANT_PARAM);
        List<Long> descIds = args.get(DEASSIGN_DESCENDANTS_PARAM);

        pap.modify().graph().deassign(ascId, descIds);
        return null;
    }
}