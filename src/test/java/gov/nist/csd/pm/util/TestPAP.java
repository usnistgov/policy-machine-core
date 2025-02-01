package gov.nist.csd.pm.util;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;

public class TestPAP extends MemoryPAP {
	public TestPAP() throws PMException {
		withIdGenerator(new TestIdGenerator());
	}
}
