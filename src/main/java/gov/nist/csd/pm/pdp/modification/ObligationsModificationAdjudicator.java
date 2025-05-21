package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.op.obligation.ObligationOp.ObligationOpArgs;
import gov.nist.csd.pm.pap.obligation.Obligation;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.pap.function.op.obligation.CreateObligationOp;
import gov.nist.csd.pm.pap.function.op.obligation.DeleteObligationOp;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.modification.ObligationsModification;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;

import java.util.ArrayList;
import java.util.List;

public class ObligationsModificationAdjudicator extends Adjudicator implements ObligationsModification {

    public ObligationsModificationAdjudicator(UserContext userCtx, PAP pap) {
        super(pap, userCtx);
        this.userCtx = userCtx;
        this.pap = pap;
    }

    @Override
    public void createObligation(long authorId, String name, List<Rule> rules) throws PMException {
        CreateObligationOp op = new CreateObligationOp();
        ObligationOpArgs args = new ObligationOpArgs(authorId, name, new ArrayList<>(rules));

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void deleteObligation(String name) throws PMException {
        Obligation obligation = pap.query().obligations().getObligation(name);

        DeleteObligationOp op = new DeleteObligationOp();
        ObligationOpArgs args = new ObligationOpArgs(
                obligation.getAuthorId(),
                obligation.getName(),
                new ArrayList<>(obligation.getRules())
        );

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }
}
