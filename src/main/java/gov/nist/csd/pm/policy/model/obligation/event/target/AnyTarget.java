package gov.nist.csd.pm.policy.model.obligation.event.target;

import gov.nist.csd.pm.pdp.PolicyReviewer;

public class AnyTarget extends Target{

    @Override
    public boolean matches(String target, PolicyReviewer policyReviewer) {
        return true;
    }
}
