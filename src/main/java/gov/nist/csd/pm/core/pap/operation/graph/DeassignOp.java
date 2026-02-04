package gov.nist.csd.pm.core.pap.operation.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdListFormalParameter;
import java.util.List;

public class DeassignOp extends AdminOperation<Void> {

    public static final NodeIdFormalParameter DEASSIGN_ASCENDANT_PARAM =
        new NodeIdFormalParameter("ascendant", AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_DELETE);

    public static final NodeIdListFormalParameter DEASSIGN_DESCENDANTS_PARAM =
        new NodeIdListFormalParameter("descendants", AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_DELETE);

    public DeassignOp() {
        super(
            "deassign",
            BasicTypes.VOID_TYPE,
            List.of(DEASSIGN_ASCENDANT_PARAM, DEASSIGN_DESCENDANTS_PARAM)
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