package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.AdminAccessRights;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.audit.Explain;
import gov.nist.csd.pm.policy.review.AccessReview;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdjudicatorAccessReview implements AccessReview {

    private final UserContext userCtx;
    private final PrivilegeChecker privilegeChecker;

    public AdjudicatorAccessReview(UserContext userCtx, PrivilegeChecker privilegeChecker) {
        this.userCtx = userCtx;
        this.privilegeChecker = privilegeChecker;
    }

    @Override
    public AccessRightSet computePrivileges(UserContext userCtx, String target) throws PMException {
        privilegeChecker.check(this.userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);
        privilegeChecker.check(this.userCtx, target, AdminAccessRights.REVIEW_POLICY);

        return null;
    }

    @Override
    public AccessRightSet computeDeniedPrivileges(UserContext userCtx, String target) throws PMException {
        privilegeChecker.check(this.userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);
        privilegeChecker.check(this.userCtx, target, AdminAccessRights.REVIEW_POLICY);

        return null;
    }

    @Override
    public Map<String, AccessRightSet> computePolicyClassAccessRights(UserContext userCtx, String target)
            throws PMException {
        privilegeChecker.check(this.userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);
        privilegeChecker.check(this.userCtx, target, AdminAccessRights.REVIEW_POLICY);

        return null;
    }

    @Override
    public Map<String, AccessRightSet> buildCapabilityList(UserContext userCtx) throws PMException {
        privilegeChecker.check(this.userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);

        return null;
    }

    @Override
    public Map<String, AccessRightSet> buildACL(String target) throws PMException {
        privilegeChecker.check(this.userCtx, target, AdminAccessRights.REVIEW_POLICY);

        return null;
    }

    @Override
    public Map<String, AccessRightSet> findBorderAttributes(String user) throws PMException {
        privilegeChecker.check(this.userCtx, user, AdminAccessRights.REVIEW_POLICY);

        return null;
    }

    @Override
    public Map<String, AccessRightSet> computeSubgraphPrivileges(UserContext userCtx, String root) throws PMException {
        privilegeChecker.check(this.userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);

        return null;
    }

    @Override
    public Explain explain(UserContext userCtx, String target) throws PMException {
        privilegeChecker.check(this.userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);
        privilegeChecker.check(this.userCtx, target, AdminAccessRights.REVIEW_POLICY);

        return null;
    }

    @Override
    public Set<String> buildPOS(UserContext userCtx) throws PMException {
        privilegeChecker.check(this.userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);

        return null;
    }

    @Override
    public List<String> computeAccessibleChildren(UserContext userCtx, String root) throws PMException {
        privilegeChecker.check(this.userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);
        privilegeChecker.check(this.userCtx, root, AdminAccessRights.REVIEW_POLICY);

        return null;
    }

    @Override
    public List<String> computeAccessibleParents(UserContext userCtx, String root) throws PMException {
        privilegeChecker.check(this.userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);
        privilegeChecker.check(this.userCtx, root, AdminAccessRights.REVIEW_POLICY);

        return null;
    }
}
