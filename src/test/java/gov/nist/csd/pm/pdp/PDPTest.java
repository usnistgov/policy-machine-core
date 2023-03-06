package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pdp.memory.MemoryPDP;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.CREATE_OBJECT_ATTRIBUTE;
import static org.junit.jupiter.api.Assertions.*;

class PDPTest {

    @Test
    void testRunTx() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        PDP pdp = new MemoryPDP(pap, false);

        UserContext superUser = new UserContext(SUPER_USER);
        pdp.runTx(superUser, (policy) -> {
            policy.createPolicyClass("pc1");
            policy.createUserAttribute("ua1", "pc1");
            policy.createObjectAttribute("oa1", "pc1");
            policy.createObjectAttribute("oa2", "pc1");
            policy.createUser("u1", "ua1");
            policy.createObject("o1", "oa1");
        });

        assertThrows(PMException.class, () -> pdp.runTx(new UserContext("u1"), ((policy) ->
                policy.associate("ua1", "oa1", new AccessRightSet(CREATE_OBJECT_ATTRIBUTE)))));

        assertTrue(pap.nodeExists("pc1"));
        assertTrue(pap.nodeExists("oa1"));
    }
}