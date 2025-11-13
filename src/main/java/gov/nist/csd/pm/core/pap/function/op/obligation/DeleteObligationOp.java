package gov.nist.csd.pm.core.pap.function.op.obligation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DELETE_OBLIGATION;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DELETE_OBLIGATION_WITH_ANY_PATTERN;

public class DeleteObligationOp extends ObligationOp {

    public DeleteObligationOp() {
        super(
            "delete_obligation",
            List.of(NAME_PARAM, RULES_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().obligations().deleteObligation(args.get(NAME_PARAM));
        return null;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        checkObligationRulePrivileges(pap, userCtx, args.get(RULES_PARAM), DELETE_OBLIGATION, DELETE_OBLIGATION_WITH_ANY_PATTERN);
    }
}
