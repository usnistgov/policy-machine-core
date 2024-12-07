package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.pap.modification.ObligationsModification;
import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.obligation.Obligation;
import gov.nist.csd.pm.common.event.EventPublisher;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.common.op.obligation.CreateObligationOp;
import gov.nist.csd.pm.common.op.obligation.DeleteObligationOp;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.pdp.Adjudicator;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.common.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.common.op.obligation.ObligationOp.AUTHOR_OPERAND;
import static gov.nist.csd.pm.common.op.obligation.ObligationOp.RULES_OPERAND;

public class ObligationsModificationAdjudicator extends Adjudicator implements ObligationsModification {

    private final UserContext userCtx;
    private final PAP pap;
    private final EventPublisher eventPublisher;

    public ObligationsModificationAdjudicator(UserContext userCtx, PAP pap, EventPublisher eventPublisher, PrivilegeChecker privilegeChecker) {
        super(privilegeChecker);
        this.userCtx = userCtx;
        this.pap = pap;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void createObligation(String author, String name, List<Rule> rules) throws PMException {
        EventContext event = new CreateObligationOp()
                .withOperands(Map.of(AUTHOR_OPERAND, author, NAME_OPERAND, name, RULES_OPERAND, rules))
                .execute(pap, userCtx, privilegeChecker);

        eventPublisher.publishEvent(event);
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

        eventPublisher.publishEvent(event);
    }
}
