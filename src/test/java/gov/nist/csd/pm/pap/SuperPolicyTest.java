package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.pap.naming.Naming;
import gov.nist.csd.pm.pdp.memory.MemoryPolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.SuperPolicy.*;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.allAdminAccessRights;
import static org.junit.jupiter.api.Assertions.*;

class SuperPolicyTest {

    @Test
    void testApplySuperPolicy() throws PMException {
        MemoryPAP memoryPAP = new MemoryPAP();
        MemoryPolicyReviewer reviewer = new MemoryPolicyReviewer(memoryPAP);
        UserContext userContext = new UserContext(SUPER_USER);
        AccessRightSet accessRights = reviewer.getAccessRights(userContext, SUPER_USER);
        assertTrue(accessRights.containsAll(allAdminAccessRights()));
        accessRights = reviewer.getAccessRights(userContext, SUPER_UA);
        assertFalse(accessRights.containsAll(allAdminAccessRights()));
        accessRights = reviewer.getAccessRights(userContext, SUPER_PC);
        assertFalse(accessRights.containsAll(allAdminAccessRights()));
        accessRights = reviewer.getAccessRights(userContext, Naming.baseUserAttribute(SUPER_PC));
        assertTrue(accessRights.containsAll(allAdminAccessRights()));
        accessRights = reviewer.getAccessRights(userContext, Naming.baseObjectAttribute(SUPER_PC));
        assertTrue(accessRights.containsAll(allAdminAccessRights()));
        accessRights = reviewer.getAccessRights(userContext, Naming.pcRepObjectAttribute(SUPER_PC));
        assertTrue(accessRights.containsAll(allAdminAccessRights()));
        accessRights = reviewer.getAccessRights(userContext, SUPER_OBJECT);
        assertTrue(accessRights.containsAll(allAdminAccessRights()));

        memoryPAP.graph.createPolicyClass("pc1");
        accessRights = reviewer.getAccessRights(userContext, Naming.baseUserAttribute("pc1"));
        assertTrue(accessRights.containsAll(allAdminAccessRights()));
        accessRights = reviewer.getAccessRights(userContext, Naming.baseObjectAttribute("pc1"));
        assertTrue(accessRights.containsAll(allAdminAccessRights()));
        accessRights = reviewer.getAccessRights(userContext, Naming.pcRepObjectAttribute("pc1"));
        assertTrue(accessRights.containsAll(allAdminAccessRights()));
    }
}