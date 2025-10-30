package gov.nist.csd.pm.core.pap.function.op.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DISSOCIATE;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DISSOCIATE_FROM;

public class DissociateOp extends GraphOp<Void> {

    public DissociateOp() {
        super(
                "dissociate",
                List.of(UA_PARAM, TARGET_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        Long uaId = args.get(UA_PARAM);
        Long targetId = args.get(TARGET_PARAM);

        pap.modify().graph().dissociate(uaId, targetId);
        return null;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        Long uaId = args.get(UA_PARAM);
        Long targetId = args.get(TARGET_PARAM);

        pap.privilegeChecker().check(userCtx, uaId, DISSOCIATE);
        pap.privilegeChecker().check(userCtx, targetId, DISSOCIATE_FROM);
    }
}
