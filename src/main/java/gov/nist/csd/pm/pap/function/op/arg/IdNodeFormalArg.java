package gov.nist.csd.pm.pap.function.op.arg;

import static gov.nist.csd.pm.pap.function.arg.type.SupportedArgTypes.longType;

public class IdNodeFormalArg extends NodeFormalArg<Long> {
	public IdNodeFormalArg(String name) {
		super(name, longType());
	}
}
