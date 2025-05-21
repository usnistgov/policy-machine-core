package gov.nist.csd.pm.pap.function.op.arg;

import static gov.nist.csd.pm.pap.function.arg.type.Type.LONG_TYPE;


import gov.nist.csd.pm.pap.function.arg.type.ListType;
import java.util.List;

public class ListIdNodeFormalParameter extends NodeFormalParameter<List<Long>> {

	public ListIdNodeFormalParameter(String name) {
		super(name, ListType.of(LONG_TYPE));
	}
}
