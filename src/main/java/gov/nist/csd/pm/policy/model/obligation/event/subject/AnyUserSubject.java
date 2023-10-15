package gov.nist.csd.pm.policy.model.obligation.event.subject;

import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.util.ArrayList;

public class AnyUserSubject extends Subject {
    public AnyUserSubject() {
        super(new ArrayList<>());
    }

    @Override
    public boolean matches(UserContext userCtx, PolicyReviewer policyReviewer) throws PMException {
        return true;
    }
}
