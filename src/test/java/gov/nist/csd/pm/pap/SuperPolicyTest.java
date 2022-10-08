package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.pap.naming.Naming;
import gov.nist.csd.pm.pdp.memory.MemoryPolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.SuperPolicy.*;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.ALL_ADMIN_ACCESS_RIGHTS_SET;
import static org.junit.jupiter.api.Assertions.*;

class SuperPolicyTest {

    @Test
    void testApplySuperPolicy() throws PMException {
        MemoryPAP memoryPAP = new MemoryPAP();
        MemoryPolicyReviewer reviewer = new MemoryPolicyReviewer(memoryPAP);
        UserContext userContext = new UserContext(SUPER_USER);
        AccessRightSet accessRights = reviewer.getAccessRights(userContext, SUPER_USER);
        assertTrue(accessRights.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));
        accessRights = reviewer.getAccessRights(userContext, SUPER_UA);
        assertFalse(accessRights.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));
        accessRights = reviewer.getAccessRights(userContext, SUPER_PC);
        assertFalse(accessRights.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));
        accessRights = reviewer.getAccessRights(userContext, Naming.baseUserAttribute(SUPER_PC));
        assertTrue(accessRights.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));
        accessRights = reviewer.getAccessRights(userContext, Naming.baseObjectAttribute(SUPER_PC));
        assertTrue(accessRights.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));
        accessRights = reviewer.getAccessRights(userContext, Naming.pcRepObjectAttribute(SUPER_PC));
        assertTrue(accessRights.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));
        accessRights = reviewer.getAccessRights(userContext, SUPER_OBJECT);
        assertTrue(accessRights.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));

        memoryPAP.graph.createPolicyClass("pc1");
        accessRights = reviewer.getAccessRights(userContext, Naming.baseUserAttribute("pc1"));
        assertTrue(accessRights.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));
        accessRights = reviewer.getAccessRights(userContext, Naming.baseObjectAttribute("pc1"));
        assertTrue(accessRights.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));
        accessRights = reviewer.getAccessRights(userContext, Naming.pcRepObjectAttribute("pc1"));
        assertTrue(accessRights.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));
    }

}