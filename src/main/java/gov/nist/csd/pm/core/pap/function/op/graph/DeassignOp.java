package gov.nist.csd.pm.core.pap.function.op.graph;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DEASSIGN;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DEASSIGN_FROM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.AdminOperation;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeIdListFormalParameter;
import java.util.List;

public class DeassignOp extends AdminOperation<Void> {

    public static final NodeIdFormalParameter DEASSIGN_ASCENDANT_PARAM =
        new NodeIdFormalParameter("ascendant", DEASSIGN);

    public static final NodeIdListFormalParameter DEASSIGN_DESCENDANTS_PARAM =
        new NodeIdListFormalParameter("descendants", DEASSIGN_FROM);

    public DeassignOp() {
        super(
                "deassign",
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