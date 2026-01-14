package gov.nist.csd.pm.core.pap.id;

import gov.nist.csd.pm.core.common.graph.node.NodeType;
import java.security.SecureRandom;

public class RandomIdGenerator implements IdGenerator {

	private final SecureRandom secureRandom;

	public RandomIdGenerator() {
		this.secureRandom = new SecureRandom();
	}

	@Override
	public long generateId(String name, NodeType type) {
		return Math.abs(this.secureRandom.nextLong());
	}
}
