package gov.nist.csd.pm.core.pap.operation.param;

import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;

public abstract sealed class NodeFormalParameter<T> extends FormalParameter<T>
	permits NodeIdFormalParameter, NodeIdListFormalParameter, NodeNameFormalParameter, NodeNameListFormalParameter {

	private final AccessRightSet reqCaps;

	public NodeFormalParameter(String name, Type<T> type, AccessRightSet reqCaps) {
		super(name, type);
		this.reqCaps = reqCaps;
	}

	public NodeFormalParameter(String name, Type<T> type, String ... accessRights) {
		super(name, type);
		this.reqCaps = new AccessRightSet(accessRights);
	}

	public AccessRightSet getRequiredCapabilities() {
		return reqCaps;
	}

}
