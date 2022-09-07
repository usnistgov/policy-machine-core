package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pdp.reviewer.MemoryPolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.pap.memory.MemoryPAP;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.naming.Naming.baseObjectAttribute;
import static gov.nist.csd.pm.pap.naming.Naming.baseUserAttribute;
import static gov.nist.csd.pm.pap.policies.SuperPolicy.SUPER_USER;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.CREATE_OBJECT_ATTRIBUTE;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;
import static org.junit.jupiter.api.Assertions.*;

class PDPTest {

    @Test
    void testRunTx() throws PMException {
        MemoryPAP memoryPAP = new MemoryPAP();
        PDP pdp = new PDP(memoryPAP, new MemoryPolicyReviewer());

        UserContext superUser = new UserContext(SUPER_USER);
        pdp.runTx(superUser, (policy) -> {
            policy.graph().createPolicyClass("pc1", noprops());
            policy.graph().createUserAttribute("ua1", noprops(), baseUserAttribute("pc1"));
            policy.graph().createObjectAttribute("oa1", noprops(), baseObjectAttribute("pc1"));
            policy.graph().createObjectAttribute("oa2", noprops(), baseObjectAttribute("pc1"));
            policy.graph().createUser("u1", noprops(), "ua1");
            policy.graph().createObject("o1", noprops(), "oa1");
        });

        assertThrows(PMException.class, () -> pdp.runTx(new UserContext("u1"), ((policy) ->
                policy.graph().associate("ua1", "oa1", new AccessRightSet(CREATE_OBJECT_ATTRIBUTE)))));

        assertTrue(memoryPAP.graph().nodeExists("pc1"));
        assertTrue(memoryPAP.graph().nodeExists("oa1"));
    }
}