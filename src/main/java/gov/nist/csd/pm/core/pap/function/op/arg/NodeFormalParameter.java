package gov.nist.csd.pm.core.pap.function.op.arg;

import gov.nist.csd.pm.core.pap.function.RequiredCapabilities;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;

public abstract class NodeFormalParameter<T> extends FormalParameter<T> {

	private RequiredCapabilities reqCap;

	public NodeFormalParameter(String name, Type<T> type, RequiredCapabilities reqCap) {
		super(name, type);
		this.reqCap = reqCap;
	}

	public NodeFormalParameter(String name, Type<T> type, String ... reqCaps) {
		super(name, type);
		this.reqCap = new RequiredCapabilities(reqCaps);
	}

	public RequiredCapabilities getReqCap() {
		return reqCap;
	}

	public void setReqCap(RequiredCapabilities reqCap) {
		this.reqCap = reqCap;
	}


}
