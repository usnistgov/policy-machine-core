package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.Obligation;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.function.op.obligation.CreateObligationOp;
import gov.nist.csd.pm.pap.function.op.obligation.DeleteObligationOp;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.op.obligation.RuleList;
import gov.nist.csd.pm.pap.modification.ObligationsModification;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;

import java.util.List;

public class ObligationsModificationAdjudicator extends Adjudicator implements ObligationsModification {

    private final UserContext userCtx;
    private final PAP pap;

    public ObligationsModificationAdjudicator(UserContext userCtx, PAP pap, PrivilegeChecker privilegeChecker) {
        super(privilegeChecker);
        this.userCtx = userCtx;
        this.pap = pap;
    }

    @Override
    public void createObligation(long authorId, String name, List<Rule> rules) throws PMException {
        CreateObligationOp op = new CreateObligationOp();
        ActualArgs args = op.actualArgs(authorId, name, new RuleList(rules));

        op.canExecute(privilegeChecker, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void deleteObligation(String name) throws PMException {
        Obligation obligation = pap.query().obligations().getObligation(name);

        DeleteObligationOp op = new DeleteObligationOp();
        ActualArgs args = op.actualArgs(
                obligation.getAuthorId(),
                obligation.getName(),
                new RuleList(obligation.getRules())
        );

        op.canExecute(privilegeChecker, userCtx, args);
        op.execute(pap, args);
    }
}
