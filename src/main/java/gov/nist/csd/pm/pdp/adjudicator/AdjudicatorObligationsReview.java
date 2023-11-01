package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AdminAccessRights;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.review.ObligationsReview;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminPolicyNode.ADMIN_POLICY_TARGET;

public class AdjudicatorObligationsReview implements ObligationsReview {

    private final UserContext userCtx;
    private final PrivilegeChecker privilegeChecker;

    public AdjudicatorObligationsReview(UserContext userCtx, PrivilegeChecker privilegeChecker) {
        this.userCtx = userCtx;
        this.privilegeChecker = privilegeChecker;
    }

    @Override
    public List<Obligation> getObligationsWithAuthor(UserContext userCtx) throws PMException {
        privilegeChecker.check(this.userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);

        return null;
    }

    @Override
    public Map<String, List<Rule>> getRulesWithEventSubject(String subject) throws PMException {
        privilegeChecker.check(this.userCtx, subject, AdminAccessRights.REVIEW_POLICY);

        return null;
    }

    @Override
    public Map<String, List<Rule>> getRulesWithEventTarget(String target) throws PMException {
        privilegeChecker.check(this.userCtx, target, AdminAccessRights.REVIEW_POLICY);

        return null;
    }

    @Override
    public List<Response> getMatchingEventResponses(EventContext eventCtx) throws PMException {
        privilegeChecker.check(this.userCtx, ADMIN_POLICY_TARGET.nodeName(), AdminAccessRights.REVIEW_POLICY);

        return null;
    }
}
