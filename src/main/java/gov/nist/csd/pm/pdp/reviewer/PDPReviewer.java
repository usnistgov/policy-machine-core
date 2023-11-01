package gov.nist.csd.pm.pdp.reviewer;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.adjudicator.*;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.review.*;

public class PDPReviewer implements PolicyReview {

    private final PDPAccessReview accessReviewer;
    private final PDPGraphReview graphReviewer;
    private final PDPProhibitionsReview prohibitionsReviewer;
    private final PDPObligationsReview obligationsReviewer;

    public PDPReviewer(UserContext userCtx, PrivilegeChecker privilegeChecker, PolicyReview policyReview) {
        this.accessReviewer = new PDPAccessReview(new AdjudicatorAccessReview(userCtx, privilegeChecker), policyReview.access());
        this.graphReviewer = new PDPGraphReview(new AdjudicatorGraphReview(userCtx, privilegeChecker), policyReview.graph());
        this.prohibitionsReviewer = new PDPProhibitionsReview(new AdjudicatorProhibitionsReview(userCtx, privilegeChecker), policyReview.prohibitions());
        this.obligationsReviewer = new PDPObligationsReview(new AdjudicatorObligationsReview(userCtx, privilegeChecker), policyReview.obligations());
    }

    @Override
    public PDPAccessReview access() {
        return accessReviewer;
    }

    @Override
    public PDPGraphReview graph() {
        return graphReviewer;
    }

    @Override
    public PDPProhibitionsReview prohibitions() {
        return prohibitionsReviewer;
    }

    @Override
    public PDPObligationsReview obligations() {
        return obligationsReviewer;
    }
}
