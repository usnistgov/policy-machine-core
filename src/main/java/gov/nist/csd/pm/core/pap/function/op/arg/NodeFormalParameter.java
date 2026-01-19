package gov.nist.csd.pm.core.pap.function.op.arg;

import gov.nist.csd.pm.core.pap.function.RequiredCapabilities;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;

public abstract sealed class NodeFormalParameter<T> extends FormalParameter<T>
	permits NodeIdFormalParameter, NodeIdListFormalParameter, NodeNameFormalParameter, NodeNameListFormalParameter {

	private RequiredCapabilities accessRights;

	public NodeFormalParameter(String name, Type<T> type, RequiredCapabilities accessRights) {
		super(name, type);
		this.accessRights = accessRights;
	}

	public NodeFormalParameter(String name, Type<T> type, String ... accessRights) {
		super(name, type);
		this.accessRights = new RequiredCapabilities(accessRights);
	}

	public RequiredCapabilities getAccessRights() {
		return accessRights;
	}

	public void setAccessRights(RequiredCapabilities accessRights) {
		this.accessRights = accessRights;
	}


}
