package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.policy.exceptions.PMException;
import org.junit.jupiter.api.Test;

public class PAPConcurrencyTest {

    @Test
    void testConcurrency() throws PMException {
        PAP pap = new MemoryPAP();
    }

}
