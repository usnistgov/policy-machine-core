package gov.nist.csd.pm.core.pap.function.op.arg;

import gov.nist.csd.pm.core.pap.function.RequiredCapabilities;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.NodeArg;
import gov.nist.csd.pm.core.pap.function.arg.type.NodeType;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;

public class NodeFormalParameter extends FormalParameter<NodeArg<?>> {

	private RequiredCapabilities reqCap;

	public NodeFormalParameter(String name, RequiredCapabilities reqCap) {
		super(name, new NodeType());
		this.reqCap = reqCap;
	}

	public NodeFormalParameter(String name, String ... reqCaps) {
		super(name, new NodeType());
		this.reqCap = new RequiredCapabilities(reqCaps);
	}

	public RequiredCapabilities getReqCap() {
		return reqCap;
	}

	public void setReqCap(RequiredCapabilities reqCap) {
		this.reqCap = reqCap;
	}


}
