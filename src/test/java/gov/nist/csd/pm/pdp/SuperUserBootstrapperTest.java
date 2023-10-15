package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pdp.SuperUserBootstrapper;
import gov.nist.csd.pm.pdp.memory.MemoryPDP;
import gov.nist.csd.pm.policy.exceptions.PMException;
import org.junit.jupiter.api.Test;

class SuperUserBootstrapperTest {

    @Test
    void test() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        PDP pdp = new MemoryPDP(pap);
        pdp.bootstrap(new SuperUserBootstrapper());

        // crate pc
        // create ua in pc
        // create ua in ua - should not create association
    }

}