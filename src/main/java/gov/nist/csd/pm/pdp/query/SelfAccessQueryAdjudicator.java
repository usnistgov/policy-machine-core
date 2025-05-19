package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.query.model.subgraph.SubgraphPrivileges;

import gov.nist.csd.pm.pdp.adjudication.Adjudicator;
import java.util.List;
import java.util.Map;

public class SelfAccessQueryAdjudicator extends Adjudicator {

	public SelfAccessQueryAdjudicator(PAP pap, UserContext userCtx) {
		super(pap, userCtx);
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

	public SubgraphPrivileges computeSubgraphPrivileges(long root) throws PMException {
		return pap.query().access().computeSubgraphPrivileges(userCtx, root);
	}

	public Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(long root) throws PMException {
		return pap.query().access().computeAdjacentAscendantPrivileges(userCtx, root);
	}

	public Map<Node, AccessRightSet> computeAdjacentDescendantPrivileges(long root) throws PMException {
		return pap.query().access().computeAdjacentDescendantPrivileges(userCtx, root);
	}

	public Map<Node, AccessRightSet> computePersonalObjectSystem() throws PMException {
		return pap.query().access().computePersonalObjectSystem(userCtx);
	}
}
