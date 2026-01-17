package gov.nist.csd.pm.core.pdp.modification;

import static gov.nist.csd.pm.core.pap.function.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.obligation.ObligationOp.AUTHOR_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.obligation.ObligationOp.EVENT_PATTERN_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.obligation.ObligationOp.OBLIGATION_RESPONSE_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.obligation.CreateObligationOp;
import gov.nist.csd.pm.core.pap.function.op.obligation.DeleteObligationOp;
import gov.nist.csd.pm.core.pap.modification.ObligationsModification;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.adjudication.Adjudicator;

public class ObligationsModificationAdjudicator extends Adjudicator implements ObligationsModification {

    public ObligationsModificationAdjudicator(UserContext userCtx, PAP pap) {
        super(pap, userCtx);
        this.userCtx = userCtx;
        this.pap = pap;
    }

    @Override
    public void createObligation(long authorId,
                                 String name,
                                 EventPattern eventPattern,
                                 ObligationResponse response) throws PMException {
        CreateObligationOp op = new CreateObligationOp();
        Args args = new Args()
            .put(AUTHOR_PARAM, authorId)
            .put(NAME_PARAM, name)
            .put(EVENT_PATTERN_PARAM, eventPattern)
            .put(OBLIGATION_RESPONSE_PARAM, response);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void deleteObligation(String name) throws PMException {
        Obligation obligation = pap.query().obligations().getObligation(name);

        DeleteObligationOp op = new DeleteObligationOp();
        Args args = new Args()
            .put(NAME_PARAM, obligation.getName())
            .put(EVENT_PATTERN_PARAM, obligation.getEventPattern());

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }
}
