package gov.nist.csd.pm.util;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import static gov.nist.csd.pm.util.TestIdGenerator.id;

public class TestUserContext extends UserContext {

	private String userName;

	public TestUserContext(String userName) {
		super(userName.hashCode(), null);

		this.userName = userName;
	}

	@Override
	public long getUser() {
		return id(userName);
	}
}
