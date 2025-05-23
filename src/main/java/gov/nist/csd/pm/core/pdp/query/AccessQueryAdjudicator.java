package gov.nist.csd.pm.core.pdp.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.AccessQuery;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.query.model.explain.Explain;
import gov.nist.csd.pm.core.pdp.adjudication.Adjudicator;

import java.util.List;
import java.util.Map;

public class AccessQueryAdjudicator extends Adjudicator implements AccessQuery {

    public AccessQueryAdjudicator(PAP pap, UserContext userCtx) {
        super(pap, userCtx);
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
    public Map<Long, AccessRightSet> computeCapabilityList(UserContext userCtx) throws PMException {
        return pap.query().access().computeCapabilityList(userCtx);
    }

    @Override
    public Map<Long, AccessRightSet> computeACL(TargetContext targetCtx) throws PMException {
        return pap.query().access().computeACL(targetCtx);
    }

    @Override
    public Map<Long, AccessRightSet> computeDestinationAttributes(UserContext userCtx) throws PMException {
        return pap.query().access().computeDestinationAttributes(userCtx);
    }

    @Override
    public SubgraphPrivileges computeSubgraphPrivileges(UserContext userCtx, long root) throws PMException {
        return pap.query().access().computeSubgraphPrivileges(userCtx, root);
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(UserContext userCtx, long root) throws PMException {
        return pap.query().access().computeAdjacentAscendantPrivileges(userCtx, root);
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentDescendantPrivileges(UserContext userCtx, long root) throws PMException {
        return pap.query().access().computeAdjacentDescendantPrivileges(userCtx, root);
    }

    @Override
    public Explain explain(UserContext userCtx, TargetContext targetCtx) throws PMException {
        return pap.query().access().explain(userCtx, targetCtx);
    }

    @Override
    public Map<Node, AccessRightSet> computePersonalObjectSystem(UserContext userCtx) throws PMException {
	    return pap.query().access().computePersonalObjectSystem(userCtx);
    }
}
