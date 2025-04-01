package gov.nist.csd.pm.pap.function.op.arg;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.listType;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.longType;

import java.util.List;

public class ListIdNodeFormalArg extends NodeFormalArg<List<Long>> {

	public ListIdNodeFormalArg(String name) {
		super(name, listType(longType()));
	}
}
