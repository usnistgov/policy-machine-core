package gov.nist.csd.pm.pap.executable.op.arg;

import gov.nist.csd.pm.pap.executable.arg.FormalArg;

public abstract class NodeFormalArg<T> extends FormalArg<T> {
	public NodeFormalArg(String name, Class<T> type) {
		super(name, type);
	}
}
