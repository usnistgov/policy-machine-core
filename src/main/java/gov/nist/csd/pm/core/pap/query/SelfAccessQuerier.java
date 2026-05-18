package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SelfAccessQuerier implements SelfAccessQuery{

    private AccessQuerier accessQuerier;
    private UserContext userCtx;

    public SelfAccessQuerier(AccessQuerier accessQuerier, UserContext userCtx) {
        this.accessQuerier = accessQuerier;
        this.userCtx = userCtx;
    }

    @Override
    public AccessRightSet computePrivileges(TargetContext targetCtx) throws PMException {
        return accessQuerier.computePrivileges(userCtx, targetCtx);
    }

    @Override
    public List<AccessRightSet> computePrivileges(List<TargetContext> targetCtxs) throws PMException {
        return accessQuerier.computePrivileges(userCtx, targetCtxs);
    }

    @Override
    public AccessRightSet computeDeniedPrivileges(TargetContext targetCtx) throws PMException {
        return accessQuerier.computeDeniedPrivileges(userCtx, targetCtx);
    }

    @Override
    public SubgraphPrivileges computeSubgraphPrivileges(long root) throws PMException {
        return accessQuerier.computeSubgraphPrivileges(userCtx, root);
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(long root) throws PMException {
        return accessQuerier.computeAdjacentAscendantPrivileges(userCtx, root);
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentDescendantPrivileges(long root) throws PMException {
        return accessQuerier.computeAdjacentDescendantPrivileges(userCtx, root);
    }

    @Override
    public Map<Node, AccessRightSet> computePersonalObjectSystem() throws PMException {
        return accessQuerier.computePersonalObjectSystem(userCtx);
    }

    @Override
    public Map<Long, Set<Long>> computeRequiredAttributeSets(TargetContext targetCtx, AccessRightSet privileges) throws PMException {
        return accessQuerier.computeRequiredAttributeSets(targetCtx, privileges);
    }

    @Override
    public Map<Long, AccessRightSet> computeCapabilityList() throws PMException {
        return accessQuerier.computeCapabilityList(userCtx);
    }
}