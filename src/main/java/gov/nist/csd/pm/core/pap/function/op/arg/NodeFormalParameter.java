package gov.nist.csd.pm.core.pap.function.op.arg;

import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;

public class NodeFormalParameter<T> extends FormalParameter<T> {
	public NodeFormalParameter(String name, Type<T> type) {
		super(name, type);
	}
}
