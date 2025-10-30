package gov.nist.csd.pm.core.pap.function.op.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ASSIGN;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ASSIGN_TO;

public class AssignOp extends GraphOp<Void> {

    public AssignOp() {
        super(
                "assign",
                List.of(ASCENDANT_PARAM, DESCENDANTS_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        Long ascId = args.get(ASCENDANT_PARAM);
        List<Long> descIds = args.get(DESCENDANTS_PARAM);

        pap.modify().graph().assign(ascId, descIds);
        return null;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        Long ascId = args.get(ASCENDANT_PARAM);
        List<Long> descIds = args.get(DESCENDANTS_PARAM);

        pap.privilegeChecker().check(userCtx, ascId, ASSIGN);
        pap.privilegeChecker().check(userCtx, descIds, ASSIGN_TO);
    }
}

