package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.AdminAccessRights;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.AccessQuery;
import gov.nist.csd.pm.pap.query.model.subgraph.SubgraphPrivileges;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.query.model.explain.Explain;
import gov.nist.csd.pm.pdp.Adjudicator;

import java.util.Collection;
import java.util.Map;

public class AccessQueryAdjudicator extends Adjudicator implements AccessQuery {

    private final UserContext userCtx;
    private final PAP pap;
    private final PrivilegeChecker privilegeChecker;

    public AccessQueryAdjudicator(UserContext userCtx, PAP pap, PrivilegeChecker privilegeChecker) {
        super(privilegeChecker);
        this.userCtx = userCtx;
        this.pap = pap;
        this.privilegeChecker = privilegeChecker;
    }

    @Override
    public AccessRightSet computePrivileges(UserContext userCtx, String target) throws PMException {
        privilegeChecker.check(userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);
        privilegeChecker.check(userCtx, target, AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().computePrivileges(userCtx, target);
    }

    @Override
    public AccessRightSet computeDeniedPrivileges(UserContext userCtx, String target) throws PMException {
        privilegeChecker.check(userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);
        privilegeChecker.check(userCtx, target, AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().computeDeniedPrivileges(userCtx, target);
    }

    @Override
    public Map<String, AccessRightSet> computePolicyClassAccessRights(UserContext userCtx, String target)
            throws PMException {
        privilegeChecker.check(userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);
        privilegeChecker.check(userCtx, target, AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().computePolicyClassAccessRights(userCtx, target);
    }

    @Override
    public Map<String, AccessRightSet> computeCapabilityList(UserContext userCtx) throws PMException {
        privilegeChecker.check(userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().computeCapabilityList(userCtx);
    }

    @Override
    public Map<String, AccessRightSet> computeACL(String target) throws PMException {
        privilegeChecker.check(userCtx, target, AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().computeACL(target);
    }

    @Override
    public Map<String, AccessRightSet> computeDestinationAttributes(String user) throws PMException {
        privilegeChecker.check(userCtx, user, AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().computeDestinationAttributes(user);
    }

    @Override
    public SubgraphPrivileges computeSubgraphPrivileges(UserContext userCtx, String root) throws PMException {
        privilegeChecker.check(userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().computeSubgraphPrivileges(userCtx, root);
    }

    @Override
    public Map<String, AccessRightSet> computeAdjacentAscendantPrivileges(UserContext userCtx, String root) throws PMException {
        privilegeChecker.check(userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().computeAdjacentAscendantPrivileges(userCtx, root);
    }

    @Override
    public Explain explain(UserContext userCtx, String target) throws PMException {
        privilegeChecker.check(userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);
        privilegeChecker.check(userCtx, target, AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().explain(userCtx, target);
    }

    @Override
    public Collection<String> computePersonalObjectSystem(UserContext userCtx) throws PMException {
        privilegeChecker.check(userCtx, userCtx.getUser(), AdminAccessRights.REVIEW_POLICY);

        return pap.query().access().computePersonalObjectSystem(userCtx);
    }
}
