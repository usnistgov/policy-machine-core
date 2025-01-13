package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.AccessQuery;
import gov.nist.csd.pm.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.pap.query.model.subgraph.SubgraphPrivileges;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.query.model.explain.Explain;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;

import java.util.List;
import java.util.Map;

public class AccessQueryAdjudicator extends Adjudicator implements AccessQuery {

    private final UserContext adjUserContext;
    private final PAP pap;
    private final PrivilegeChecker privilegeChecker;

    public AccessQueryAdjudicator(UserContext adjUserContext, PAP pap, PrivilegeChecker privilegeChecker) {
        super(privilegeChecker);
        this.adjUserContext = adjUserContext;
        this.pap = pap;
        this.privilegeChecker = privilegeChecker;
    }

    @Override
    public AccessRightSet computePrivileges(UserContext userCtx, TargetContext targetCtx) throws PMException {
        return pap.query().access().computePrivileges(userCtx, targetCtx);
    }

    @Override
    public List<AccessRightSet> computePrivileges(UserContext userCtx, List<TargetContext> targetCtxs) throws PMException {
        return pap.query().access().computePrivileges(userCtx, targetCtxs);
    }

    @Override
    public AccessRightSet computeDeniedPrivileges(UserContext userCtx, TargetContext targetCtx) throws PMException {
        return pap.query().access().computeDeniedPrivileges(userCtx, targetCtx);
    }

    @Override
    public Map<String, AccessRightSet> computeCapabilityList(UserContext userCtx) throws PMException {
        return pap.query().access().computeCapabilityList(userCtx);
    }

    @Override
    public Map<String, AccessRightSet> computeACL(TargetContext targetCtx) throws PMException {
        return pap.query().access().computeACL(targetCtx);
    }

    @Override
    public Map<String, AccessRightSet> computeDestinationAttributes(UserContext userCtx) throws PMException {
        return pap.query().access().computeDestinationAttributes(userCtx);
    }

    @Override
    public SubgraphPrivileges computeSubgraphPrivileges(UserContext userCtx, String root) throws PMException {
        return pap.query().access().computeSubgraphPrivileges(userCtx, root);
    }

    @Override
    public Map<String, AccessRightSet> computeAdjacentAscendantPrivileges(UserContext userCtx, String root) throws PMException {
        return pap.query().access().computeAdjacentAscendantPrivileges(userCtx, root);
    }

    @Override
    public Map<String, AccessRightSet> computeAdjacentDescendantPrivileges(UserContext userCtx, String root) throws PMException {
        return pap.query().access().computeAdjacentDescendantPrivileges(userCtx, root);
    }

    @Override
    public Explain explain(UserContext userCtx, TargetContext targetCtx) throws PMException {
        return pap.query().access().explain(userCtx, targetCtx);
    }

    @Override
    public Map<String, AccessRightSet> computePersonalObjectSystem(UserContext userCtx) throws PMException {
	    return pap.query().access().computePersonalObjectSystem(userCtx);
    }
}
