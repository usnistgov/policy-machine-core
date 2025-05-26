package gov.nist.csd.pm.core.util;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;

public class TestPAP extends MemoryPAP {
	public TestPAP() throws PMException {
		withIdGenerator(new TestIdGenerator());
	}
}
