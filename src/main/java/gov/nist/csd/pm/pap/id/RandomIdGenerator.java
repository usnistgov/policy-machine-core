package gov.nist.csd.pm.pap.id;

import java.security.SecureRandom;

public class RandomIdGenerator implements IdGenerator {

	private SecureRandom secureRandom;

	public RandomIdGenerator() {
		this.secureRandom = new SecureRandom();
	}

	@Override
	public long generateId() {
		return this.secureRandom.nextLong();
	}
}
