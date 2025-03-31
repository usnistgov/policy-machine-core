package gov.nist.csd.pm.pap.function.op.arg;

import static gov.nist.csd.pm.pap.function.arg.type.SupportedArgTypes.listType;
import static gov.nist.csd.pm.pap.function.arg.type.SupportedArgTypes.longType;

import java.util.List;

public class ListIdNodeFormalArg extends NodeFormalArg<List<Long>> {

	public ListIdNodeFormalArg(String name) {
		super(name, listType(longType()));
	}
}
