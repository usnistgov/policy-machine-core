package gov.nist.csd.pm.pdp.reviewer;

import gov.nist.csd.pm.pdp.adjudicator.AdjudicatorAccessReview;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.audit.Explain;
import gov.nist.csd.pm.policy.review.AccessReview;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PDPAccessReview implements AccessReview {

    private final AdjudicatorAccessReview adjudicator;
    private final AccessReview accessReview;

    public PDPAccessReview(AdjudicatorAccessReview adjudicator, AccessReview accessReview) {
        this.adjudicator = adjudicator;
        this.accessReview = accessReview;
    }

    @Override
    public AccessRightSet computePrivileges(UserContext userCtx, String target) throws PMException {
        adjudicator.computePrivileges(userCtx, target);
        return accessReview.computePrivileges(userCtx, target);
    }

    @Override
    public AccessRightSet computeDeniedPrivileges(UserContext userCtx, String target) throws PMException {
        adjudicator.computeDeniedPrivileges(userCtx, target);
        return accessReview.computeDeniedPrivileges(userCtx, target);
    }

    @Override
    public Map<String, AccessRightSet> computePolicyClassAccessRights(UserContext userCtx, String target)
            throws PMException {
        adjudicator.computePolicyClassAccessRights(userCtx, target);
        return accessReview.computePolicyClassAccessRights(userCtx, target);
    }

    @Override
    public Map<String, AccessRightSet> buildCapabilityList(UserContext userCtx) throws PMException {
        adjudicator.buildCapabilityList(userCtx);
        return accessReview.buildCapabilityList(userCtx);
    }

    @Override
    public Map<String, AccessRightSet> buildACL(String target) throws PMException {
        adjudicator.buildACL(target);
        return accessReview.buildACL(target);
    }

    @Override
    public Map<String, AccessRightSet> findBorderAttributes(String user) throws PMException {
        adjudicator.findBorderAttributes(user);
        return accessReview.findBorderAttributes(user);
    }

    @Override
    public Map<String, AccessRightSet> computeSubgraphPrivileges(UserContext userCtx, String root) throws PMException {
        adjudicator.computeSubgraphPrivileges(userCtx, root);
        return accessReview.computeSubgraphPrivileges(userCtx, root);
    }

    @Override
    public Explain explain(UserContext userCtx, String target) throws PMException {
        adjudicator.explain(userCtx, target);
        return accessReview.explain(userCtx, target);
    }

    @Override
    public Set<String> buildPOS(UserContext userCtx) throws PMException {
        adjudicator.buildPOS(userCtx);
        return accessReview.buildPOS(userCtx);
    }

    @Override
    public List<String> computeAccessibleChildren(UserContext userCtx, String root) throws PMException {
        adjudicator.computeAccessibleChildren(userCtx, root);
        return accessReview.computeAccessibleChildren(userCtx, root);
    }

    @Override
    public List<String> computeAccessibleParents(UserContext userCtx, String root) throws PMException {
        adjudicator.computeAccessibleParents(userCtx, root);
        return accessReview.computeAccessibleParents(userCtx, root);
    }
}
