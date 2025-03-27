package gov.nist.csd.pm.pap.executable.op.arg;

import it.unimi.dsi.fastutil.longs.LongArrayList;

public class ListIdNodeFormalArg extends NodeFormalArg<LongArrayList> {
	public ListIdNodeFormalArg(String name) {
		super(name, LongArrayList.class);
	}
}
