package gov.nist.csd.pm.core.pap.function.op.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.NodeArg;
import gov.nist.csd.pm.core.pap.function.op.AdminOperation;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeFormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeListFormalParameter;

import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DEASSIGN;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DEASSIGN_FROM;

public class DeassignOp extends AdminOperation<Void> {

    public static final NodeFormalParameter DEASSIGN_ASCENDANT_PARAM =
        new NodeFormalParameter("ascendant", DEASSIGN);

    public static final NodeListFormalParameter DEASSIGN_DESCENDANTS_PARAM =
        new NodeListFormalParameter("descendants", DEASSIGN_FROM);

    public DeassignOp() {
        super(
                "deassign",
                List.of(DEASSIGN_ASCENDANT_PARAM, DEASSIGN_DESCENDANTS_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        NodeArg<?> ascId = args.get(DEASSIGN_ASCENDANT_PARAM);
        List<Long> descIds = args.getIdList(DEASSIGN_DESCENDANTS_PARAM, pap);

        pap.modify().graph().deassign(ascId.getId(pap), descIds);
        return null;
    }
}