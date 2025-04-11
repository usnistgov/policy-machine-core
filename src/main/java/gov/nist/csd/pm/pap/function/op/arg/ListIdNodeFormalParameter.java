package gov.nist.csd.pm.pap.function.op.arg;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.LONG_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.listType;

import java.util.List;

public class ListIdNodeFormalParameter extends NodeFormalParameter<List<Long>> {

	public ListIdNodeFormalParameter(String name) {
		super(name, listType(LONG_TYPE));
	}
}
