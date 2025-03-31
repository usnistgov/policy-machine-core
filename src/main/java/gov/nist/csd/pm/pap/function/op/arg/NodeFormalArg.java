package gov.nist.csd.pm.pap.function.op.arg;

import gov.nist.csd.pm.pap.function.arg.FormalArg;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;

public abstract class NodeFormalArg<T> extends FormalArg<T> {
	public NodeFormalArg(String name, ArgType<T> type) {
		super(name, type);
	}
}
