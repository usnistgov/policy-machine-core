package gov.nist.csd.pm.core.pap.operation.param;

import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;

public abstract sealed class NodeFormalParameter<T> extends FormalParameter<T>
	permits NodeIdFormalParameter, NodeIdListFormalParameter, NodeNameFormalParameter, NodeNameListFormalParameter {

	public NodeFormalParameter(String name, Type<T> type) {
		super(name, type);
	}
}
