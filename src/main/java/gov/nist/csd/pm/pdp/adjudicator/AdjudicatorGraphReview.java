package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AdminAccessRights;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.review.GraphReview;

import java.util.List;

public class AdjudicatorGraphReview implements GraphReview {

    private final UserContext userCtx;
    private final PrivilegeChecker privilegeChecker;

    public AdjudicatorGraphReview(UserContext userCtx, PrivilegeChecker privilegeChecker) {
        this.userCtx = userCtx;
        this.privilegeChecker = privilegeChecker;
    }

    @Override
    public List<String> getAttributeContainers(String node) throws PMException {
        privilegeChecker.check(this.userCtx, node, AdminAccessRights.REVIEW_POLICY);

        return null;
    }

    @Override
    public List<String> getPolicyClassContainers(String node) throws PMException {
        privilegeChecker.check(this.userCtx, node, AdminAccessRights.REVIEW_POLICY);

        return null;
    }

    @Override
    public boolean isContained(String subject, String container) throws PMException {
        privilegeChecker.check(this.userCtx, subject, AdminAccessRights.REVIEW_POLICY);
        privilegeChecker.check(this.userCtx, container, AdminAccessRights.REVIEW_POLICY);

        return false;
    }
}
