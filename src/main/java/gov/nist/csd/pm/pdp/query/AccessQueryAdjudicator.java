package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.AdminAccessRights;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.AccessQuerier;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.query.explain.Explain;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class AccessQueryAdjudicator extends AccessQuerier {

    private final UserContext userCtx;
    private final PAP pap;

    public AccessQueryAdjudicator(UserContext userCtx, PAP pap) {
        super(pap.query());
        this.userCtx = userCtx;
        this.pap = pap;
    }

    @Override
    public AccessRightSet computePrivileges(UserContext userCtx, String target) throws PMException {
        PrivilegeChecker.check(pap, userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);
        PrivilegeChecker.check(pap, userCtx, target, AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().computePrivileges(userCtx, target);
    }

    @Override
    public AccessRightSet computeDeniedPrivileges(UserContext userCtx, String target) throws PMException {
        PrivilegeChecker.check(pap, userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);
        PrivilegeChecker.check(pap, userCtx, target, AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().computeDeniedPrivileges(userCtx, target);
    }

    @Override
    public Map<String, AccessRightSet> computePolicyClassAccessRights(UserContext userCtx, String target)
            throws PMException {
        PrivilegeChecker.check(pap, userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);
        PrivilegeChecker.check(pap, userCtx, target, AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().computePolicyClassAccessRights(userCtx, target);
    }

    @Override
    public Map<String, AccessRightSet> computeCapabilityList(UserContext userCtx) throws PMException {
        PrivilegeChecker.check(pap, userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().computeCapabilityList(userCtx);
    }

    @Override
    public Map<String, AccessRightSet> computeACL(String target) throws PMException {
        PrivilegeChecker.check(pap, userCtx, target, AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().computeACL(target);
    }

    @Override
    public Map<String, AccessRightSet> computeDestinationAttributes(String user) throws PMException {
        PrivilegeChecker.check(pap, userCtx, user, AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().computeDestinationAttributes(user);
    }

    @Override
    public Map<String, AccessRightSet> computeAscendantPrivileges(UserContext userCtx, String root) throws PMException {
        PrivilegeChecker.check(pap, userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().computeAscendantPrivileges(userCtx, root);
    }

    @Override
    public Explain explain(UserContext userCtx, String target) throws PMException {
        PrivilegeChecker.check(pap, userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);
        PrivilegeChecker.check(pap, userCtx, target, AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().explain(userCtx, target);
    }

    @Override
    public Set<String> computePersonalObjectSystem(UserContext userCtx) throws PMException {
        PrivilegeChecker.check(pap, userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().computePersonalObjectSystem(userCtx);
    }

    @Override
    public Collection<String> computeAccessibleAscendants(UserContext userCtx, String root) throws PMException {
        PrivilegeChecker.check(pap, userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);
        PrivilegeChecker.check(pap, userCtx, root, AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().computeAccessibleAscendants(userCtx, root);
    }

    @Override
    public Collection<String> computeAccessibleDescendants(UserContext userCtx, String root) throws PMException {
        PrivilegeChecker.check(pap, userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);
        PrivilegeChecker.check(pap, userCtx, root, AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().computeAccessibleDescendants(userCtx, root);
    }
}
