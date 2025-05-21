package gov.nist.csd.pm.util;

import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.pap.id.IdGenerator;
import it.unimi.dsi.fastutil.longs.LongList;

public class TestIdGenerator implements IdGenerator {
	@Override
	public long generateId(String name, NodeType type) {
		return name.hashCode();
	}

	public static long id(String name) {
		return name.hashCode();
	}

	public static LongList ids(String ... name) {
		long[] ids = new long[name.length];
		for (int i = 0; i < name.length; i++) {
			ids[i] = id(name[i]);
		}
		return LongList.of(ids);
	}
}
