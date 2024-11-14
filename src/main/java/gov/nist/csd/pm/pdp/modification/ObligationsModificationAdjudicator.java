package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.pap.modification.ObligationsModification;
import gov.nist.csd.pm.pap.obligation.EventContext;
import gov.nist.csd.pm.pap.obligation.Obligation;
import gov.nist.csd.pm.epp.EventEmitter;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.op.obligation.CreateObligationOp;
import gov.nist.csd.pm.pap.op.obligation.DeleteObligationOp;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.pdp.Adjudicator;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.pap.op.obligation.ObligationOp.AUTHOR_OPERAND;
import static gov.nist.csd.pm.pap.op.obligation.ObligationOp.RULES_OPERAND;

public class ObligationsModificationAdjudicator extends Adjudicator implements ObligationsModification {

    private final UserContext userCtx;
    private final PAP pap;
    private final EventEmitter eventEmitter;

    public ObligationsModificationAdjudicator(UserContext userCtx, PAP pap, EventEmitter eventEmitter, PrivilegeChecker privilegeChecker) {
        super(privilegeChecker);
        this.userCtx = userCtx;
        this.pap = pap;
        this.eventEmitter = eventEmitter;
    }

    @Override
    public void createObligation(String author, String name, List<Rule> rules) throws PMException {
        EventContext event = new CreateObligationOp()
                .withOperands(Map.of(AUTHOR_OPERAND, author, NAME_OPERAND, name, RULES_OPERAND, rules))
                .execute(pap, userCtx, privilegeChecker);

        eventEmitter.emitEvent(event);
    }

    @Override
    public void deleteObligation(String name) throws PMException {
        Obligation obligation = pap.query().obligations().getObligation(name);

        EventContext event = new DeleteObligationOp()
                .withOperands(Map.of(
                        AUTHOR_OPERAND, obligation.getAuthor(),
                        NAME_OPERAND, obligation.getName(),
                        RULES_OPERAND, obligation.getRules()
                ))
                .execute(pap, userCtx, privilegeChecker);

        eventEmitter.emitEvent(event);
    }
}
