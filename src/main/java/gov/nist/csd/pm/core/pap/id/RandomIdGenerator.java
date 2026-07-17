package gov.nist.csd.pm.core.pap.id;

import gov.nist.csd.pm.core.common.graph.node.NodeType;
import java.security.SecureRandom;
import java.util.Random;

public class RandomIdGenerator implements IdGenerator {

	private final Random random;

	public RandomIdGenerator() {
		this(new SecureRandom());
	}

	RandomIdGenerator(Random random) {
		this.random = random;
	}

	@Override
	public long generateId(String name, NodeType type) {
		long value;
		do {
			value = random.nextLong();
		} while (value == Long.MIN_VALUE);
		return Math.abs(value);
	}
}
