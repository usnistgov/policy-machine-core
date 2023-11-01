package gov.nist.csd.pm.pdp.reviewer;

import gov.nist.csd.pm.policy.review.*;

public class ReviewAdjudicator implements PolicyReview {
    @Override
    public AccessReview access() {
        return null;
    }

    @Override
    public GraphReview graph() {
        return null;
    }

    @Override
    public ProhibitionsReview prohibitions() {
        return null;
    }

    @Override
    public ObligationsReview obligations() {
        return null;
    }
}
