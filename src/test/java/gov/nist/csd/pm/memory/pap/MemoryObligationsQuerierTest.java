package gov.nist.csd.pm.memory.pap;

import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.ObligationsQuerierTest;
import gov.nist.csd.pm.pap.exception.PMException;

class MemoryObligationsQuerierTest extends ObligationsQuerierTest {

    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP();
    }

}