package gov.nist.csd.pm.core.pap.function.op.arg;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.NODE_TYPE;

import gov.nist.csd.pm.core.pap.function.RequiredCapabilities;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.NodeArg;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import java.util.List;

public class NodeListFormalParameter extends FormalParameter<List<NodeArg<?>>> {

	private RequiredCapabilities reqCap;

	public NodeListFormalParameter(String name, RequiredCapabilities reqCap) {
		super(name, ListType.of(NODE_TYPE));
		this.reqCap = reqCap;
	}

	public NodeListFormalParameter(String name, String ... reqCap) {
		super(name, ListType.of(NODE_TYPE));
		this.reqCap = new RequiredCapabilities(reqCap);
	}

	public RequiredCapabilities getReqCap() {
		return reqCap;
	}

	public void setReqCap(RequiredCapabilities reqCap) {
		this.reqCap = reqCap;
	}
}
