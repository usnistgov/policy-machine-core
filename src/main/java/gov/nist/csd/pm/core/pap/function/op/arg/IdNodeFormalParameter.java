package gov.nist.csd.pm.core.pap.function.op.arg;

import static gov.nist.csd.pm.core.pap.function.arg.type.Type.LONG_TYPE;

public class IdNodeFormalParameter extends NodeFormalParameter<Long> {
	public IdNodeFormalParameter(String name) {
		super(name, LONG_TYPE);
	}
}
