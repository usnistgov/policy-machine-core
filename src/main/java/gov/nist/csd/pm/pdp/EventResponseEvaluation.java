package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.obligation.Response;
import gov.nist.csd.pm.pap.query.UserContext;

public interface EventResponseEvaluation {

    public void evaluateResponse(UserContext userCtx, Response response);

}
