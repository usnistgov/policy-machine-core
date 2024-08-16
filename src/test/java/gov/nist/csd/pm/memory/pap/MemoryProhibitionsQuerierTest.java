package gov.nist.csd.pm.memory.pap;

import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.ProhibitionsQuerierTest;
import gov.nist.csd.pm.pap.exception.PMException;

class MemoryProhibitionsQuerierTest extends ProhibitionsQuerierTest {

    @Override
    public PAP initializePAP() throws PMException {
        return new MemoryPAP();
    }
}