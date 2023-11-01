package gov.nist.csd.pm.policy.model.obligation.event.target;

import gov.nist.csd.pm.policy.review.GraphReview;

public class AnyTarget extends Target{

    @Override
    public boolean matches(String target, GraphReview graphReview) {
        return true;
    }
}
