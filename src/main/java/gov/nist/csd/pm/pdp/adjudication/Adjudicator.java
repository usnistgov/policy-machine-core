package gov.nist.csd.pm.pdp.adjudication;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

public abstract class Adjudicator {

    protected PAP pap;
    protected UserContext userCtx;

    public Adjudicator(PAP pap, UserContext userCtx) {
        this.pap = pap;
        this.userCtx = userCtx;
    }
}
