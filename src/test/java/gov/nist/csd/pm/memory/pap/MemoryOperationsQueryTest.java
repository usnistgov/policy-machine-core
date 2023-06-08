package gov.nist.csd.pm.memory.pap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.OperationsQuerierTest;

public class MemoryOperationsQueryTest extends OperationsQuerierTest {
    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP();
    }
}
