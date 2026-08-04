package gov.nist.csd.pm.core.pdp.modification;

import static gov.nist.csd.pm.core.pap.operation.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.pap.operation.obligation.CreateObligationOp.AUTHOR_PARAM;
import static gov.nist.csd.pm.core.pap.operation.obligation.CreateObligationOp.EVENT_PATTERN_PARAM;
import static gov.nist.csd.pm.core.pap.operation.obligation.CreateObligationOp.OBLIGATION_RESPONSE_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.modification.ObligationsModification;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.obligation.CreateObligationOp;
import gov.nist.csd.pm.core.pap.operation.obligation.DeleteObligationOp;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.adjudication.Adjudicator;

public class ObligationsModificationAdjudicator extends Adjudicator implements ObligationsModification {

    public ObligationsModificationAdjudicator(UserContext userCtx, PAP pap) {
        super(pap, userCtx);
        this.userCtx = userCtx;
        this.pap = pap;
    }

    @Override
    public void createObligation(Obligation obligation) throws PMException {
        long authorId = obligation.getAuthor().resolveNodeIds(pap.query().graph()).iterator().next();
        CreateObligationOp op = new CreateObligationOp();
        Args args = new Args()
            .put(AUTHOR_PARAM, authorId)
            .put(NAME_PARAM, obligation.getName())
            .put(EVENT_PATTERN_PARAM, obligation.getEventPattern())
            .put(OBLIGATION_RESPONSE_PARAM, obligation.getResponse());

        op.canExecute(pap, userCtx, args);
        op.execute(pap, userCtx, args);
    }

    @Override
    public void deleteObligation(String name) throws PMException {
        Obligation obligation = pap.query().obligations().getObligation(name);

        DeleteObligationOp op = new DeleteObligationOp();
        Args args = new Args()
            .put(NAME_PARAM, obligation.getName())
            .put(EVENT_PATTERN_PARAM, obligation.getEventPattern());

        op.canExecute(pap, userCtx, args);
        op.execute(pap, userCtx, args);
    }
}
