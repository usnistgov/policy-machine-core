package gov.nist.csd.pm.util;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

public class TestUserContext extends UserContext {

	private String userName;
	private PAP pap;

	public TestUserContext(String userName, PAP pap) {
		super(-1, null);

		this.userName = userName;
		this.pap = pap;
	}

	@Override
	public long getUser() {
		try {
			return pap.query().graph().getNodeId(userName);
		} catch (PMException e) {
			throw new RuntimeException(e);
		}
	}
}
