package gov.nist.csd.pm.core.pap.function.op.obligation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_OBLIGATION;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_OBLIGATION_WITH_ANY_PATTERN;

public class CreateObligationOp extends ObligationOp {

    public CreateObligationOp() {
        super(
            "create_obligation",
            List.of(AUTHOR_PARAM, NAME_PARAM, RULES_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, ObligationOpArgs args) throws PMException {
        pap.modify().obligations().createObligation(
            args.getAuthorId(),
            args.getName(),
            args.getRules()
        );
        return null;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, ObligationOpArgs args) throws PMException {
        checkObligationRulePrivileges(pap, userCtx, args.getRules(),
                                      CREATE_OBLIGATION, CREATE_OBLIGATION_WITH_ANY_PATTERN);
    }
}
