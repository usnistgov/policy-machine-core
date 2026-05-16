package gov.nist.csd.pm.core.pdp.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.SelfAccessQuery;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
import gov.nist.csd.pm.core.pdp.adjudication.Adjudicator;
import java.util.List;
import java.util.Map;

public class SelfAccessQueryAdjudicator extends Adjudicator implements SelfAccessQuery {

	public SelfAccessQueryAdjudicator(PAP pap, UserContext userCtx) {
		super(pap, userCtx);
	}

	@Override
	public AccessRightSet computePrivileges(TargetContext targetCtx) throws PMException {
		return pap.query().access().self(userCtx).computePrivileges(targetCtx);
	}

	@Override
	public List<AccessRightSet> computePrivileges(List<TargetContext> targetCtxs) throws PMException {
		return pap.query().access().self(userCtx).computePrivileges(targetCtxs);
	}

	@Override
	public AccessRightSet computeDeniedPrivileges(TargetContext targetCtx) throws PMException {
		return pap.query().access().self(userCtx).computeDeniedPrivileges(targetCtx);
	}

	@Override
	public SubgraphPrivileges computeSubgraphPrivileges(long root) throws PMException {
		return pap.query().access().self(userCtx).computeSubgraphPrivileges(root);
	}

	@Override
	public Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(long root) throws PMException {
		return pap.query().access().self(userCtx).computeAdjacentAscendantPrivileges(root);
	}

	@Override
	public Map<Node, AccessRightSet> computeAdjacentDescendantPrivileges(long root) throws PMException {
		return pap.query().access().self(userCtx).computeAdjacentDescendantPrivileges(root);
	}

	@Override
	public Map<Node, AccessRightSet> computePersonalObjectSystem() throws PMException {
		return pap.query().access().self(userCtx).computePersonalObjectSystem();
	}
}
