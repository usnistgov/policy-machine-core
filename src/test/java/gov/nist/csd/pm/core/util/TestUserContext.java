package gov.nist.csd.pm.core.util;

import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;

public class TestUserContext extends UserContext {

	private final String userName;

	public TestUserContext(String userName) {
		super(userName.hashCode(), null);

		this.userName = userName;
	}

	@Override
	public long getUser() {
		return id(userName);
	}
}
