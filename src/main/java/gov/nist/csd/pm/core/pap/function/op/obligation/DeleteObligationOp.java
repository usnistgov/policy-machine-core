package gov.nist.csd.pm.core.pap.function.op.obligation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DELETE_OBLIGATION;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DELETE_OBLIGATION_WITH_ANY_PATTERN;

public class DeleteObligationOp extends ObligationOp {

    public DeleteObligationOp() {
        super(
            "delete_obligation",
            List.of(NAME_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, ObligationOpArgs args) throws PMException {
        pap.modify().obligations().deleteObligation(args.getName());
        return null;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, ObligationOpArgs args) throws PMException {
        checkObligationRulePrivileges(pap, userCtx, args.getRules(), DELETE_OBLIGATION, DELETE_OBLIGATION_WITH_ANY_PATTERN);
    }
}
