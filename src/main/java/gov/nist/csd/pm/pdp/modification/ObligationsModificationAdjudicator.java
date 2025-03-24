package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.Obligation;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.pap.executable.op.obligation.CreateObligationOp;
import gov.nist.csd.pm.pap.executable.op.obligation.DeleteObligationOp;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.modification.ObligationsModification;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.executable.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.pap.executable.op.obligation.ObligationOp.AUTHOR_OPERAND;
import static gov.nist.csd.pm.pap.executable.op.obligation.ObligationOp.RULES_OPERAND;

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
        new CreateObligationOp()
                .withOperands(Map.of(AUTHOR_OPERAND, authorId, NAME_OPERAND, name, RULES_OPERAND, rules))
                .execute(pap, userCtx, privilegeChecker);
    }

    @Override
    public void deleteObligation(String name) throws PMException {
        Obligation obligation = pap.query().obligations().getObligation(name);

        new DeleteObligationOp()
                .withOperands(Map.of(
                        AUTHOR_OPERAND, obligation.getAuthorId(),
                        NAME_OPERAND, obligation.getName(),
                        RULES_OPERAND, obligation.getRules()
                ))
                .execute(pap, userCtx, privilegeChecker);
    }
}
