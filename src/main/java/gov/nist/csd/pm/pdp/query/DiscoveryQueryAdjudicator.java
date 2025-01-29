package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.query.model.explain.Explain;
import gov.nist.csd.pm.pap.query.model.subgraph.SubgraphPrivileges;

import java.util.List;
import java.util.Map;

public class DiscoveryQueryAdjudicator {

	private final PAP pap;
	private final UserContext userCtx;

	public DiscoveryQueryAdjudicator(PAP pap, UserContext userCtx) {
		this.pap = pap;
		this.userCtx = userCtx;
	}

	public AccessRightSet computePrivileges(TargetContext targetCtx) throws PMException {
		return pap.query().access().computePrivileges(userCtx, targetCtx);
	}

	public List<AccessRightSet> computePrivileges(List<TargetContext> targetCtxs) throws PMException {
		return pap.query().access().computePrivileges(userCtx, targetCtxs);
	}

	public AccessRightSet computeDeniedPrivileges(TargetContext targetCtx) throws PMException {
		return pap.query().access().computeDeniedPrivileges(userCtx, targetCtx);
	}

	public Map<Long, AccessRightSet> computeCapabilityList(UserContext userCtx) throws PMException {
		return pap.query().access().computeCapabilityList(userCtx);
	}

	public Map<Long, AccessRightSet> computeACL(TargetContext targetCtx) throws PMException {
		return pap.query().access().computeACL(targetCtx);
	}

	public Map<Long, AccessRightSet> computeDestinationAttributes(UserContext userCtx) throws PMException {
		return pap.query().access().computeDestinationAttributes(userCtx);
	}

	public SubgraphPrivileges computeSubgraphPrivileges(long root) throws PMException {
		return pap.query().access().computeSubgraphPrivileges(userCtx, root);
	}

	public Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(long root) throws PMException {
		return pap.query().access().computeAdjacentAscendantPrivileges(userCtx, root);
	}

	public Map<Node, AccessRightSet> computeAdjacentDescendantPrivileges(long root) throws PMException {
		return pap.query().access().computeAdjacentDescendantPrivileges(userCtx, root);
	}

	public Explain explain(TargetContext targetCtx) throws PMException {
		return pap.query().access().explain(userCtx, targetCtx);
	}

	public Map<Node, AccessRightSet> computePersonalObjectSystem(UserContext userCtx) throws PMException {
		return pap.query().access().computePersonalObjectSystem(userCtx);
	}
}
