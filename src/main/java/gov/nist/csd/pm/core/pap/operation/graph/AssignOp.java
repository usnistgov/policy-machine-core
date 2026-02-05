package gov.nist.csd.pm.core.pap.operation.graph;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdListFormalParameter;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import java.util.List;

public class AssignOp extends AdminOperation<Void> {

    public static final NodeIdFormalParameter ASSIGN_ASCENDANT_PARAM = new NodeIdFormalParameter("ascendant");
    public static final NodeIdListFormalParameter ASSIGN_DESCENDANTS_PARAM = new NodeIdListFormalParameter("descendants");

    public AssignOp() {
        super(
            "assign",
            VOID_TYPE,
            List.of(ASSIGN_ASCENDANT_PARAM, ASSIGN_DESCENDANTS_PARAM),
            new RequiredCapability(
                ASSIGN_ASCENDANT_PARAM, new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_CREATE),
                ASSIGN_DESCENDANTS_PARAM, new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_CREATE)
            )
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        Long ascId = args.get(ASSIGN_ASCENDANT_PARAM);
        List<Long> descIds = args.get(ASSIGN_DESCENDANTS_PARAM);

        pap.modify().graph().assign(ascId, descIds);
        return null;
    }
}

