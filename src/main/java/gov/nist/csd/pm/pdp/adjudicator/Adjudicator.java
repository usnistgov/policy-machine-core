package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.reviewer.PolicyReviewer;
import gov.nist.csd.pm.policy.author.*;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.review.PolicyReview;

public class Adjudicator extends PolicyAuthor {

    private final UserContext userCtx;
    private final PAP pap;
    private final PolicyReviewer policyReviewer;

    public Adjudicator(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer) {
        this.userCtx = userCtx;
        this.pap = pap;
        this.policyReviewer = policyReviewer;
    }

    @Override
    public GraphAuthor graph() {
        return new Graph(userCtx, pap, policyReviewer);
    }

    @Override
    public ProhibitionsAuthor prohibitions() {
        return new Prohibitions(userCtx, pap, policyReviewer);
    }

    @Override
    public ObligationsAuthor obligations() {
        return new Obligations(userCtx, pap, policyReviewer);
    }

    @Override
    public PALAuthor pal() {
        return new PAL(userCtx, pap, policyReviewer);
    }
}
