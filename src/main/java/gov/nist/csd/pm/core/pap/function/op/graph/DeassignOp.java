package gov.nist.csd.pm.core.pap.function.op.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DEASSIGN;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DEASSIGN_FROM;

public class DeassignOp extends GraphOp<Void> {

    public DeassignOp() {
        super(
                "deassign",
                List.of(ASCENDANT_PARAM, DESCENDANTS_PARAM)
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        Long ascId = args.get(ASCENDANT_PARAM);
        List<Long> descIds = args.get(DESCENDANTS_PARAM);

        pap.privilegeChecker().check(userCtx, ascId, DEASSIGN);
        pap.privilegeChecker().check(userCtx, descIds, DEASSIGN_FROM);
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        Long ascId = args.get(ASCENDANT_PARAM);
        List<Long> descIds = args.get(DESCENDANTS_PARAM);

        pap.modify().graph().deassign(ascId, descIds);
        return null;
    }
}