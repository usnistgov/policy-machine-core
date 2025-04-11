package gov.nist.csd.pm.pap.function.op.arg;

import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;

public class NodeFormalParameter<T> extends FormalParameter<T> {
	public NodeFormalParameter(String name, ArgType<T> type) {
		super(name, type);
	}
}
