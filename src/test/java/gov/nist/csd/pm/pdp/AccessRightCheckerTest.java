package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.AdminPolicyNode;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pdp.memory.MemoryPolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.UnauthorizedException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.AdminAccessRights;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccessRightCheckerTest {

    MemoryPolicyStore memoryPolicyStore;

    @BeforeEach
    void setup() throws PMException {
        memoryPolicyStore = new MemoryPolicyStore();

        memoryPolicyStore.graph().setResourceAccessRights(new AccessRightSet("read"));

        memoryPolicyStore.graph().createPolicyClass("pc1");
        memoryPolicyStore.graph().createUserAttribute("ua1", "pc1");
        memoryPolicyStore.graph().createObjectAttribute("oa1", "pc1");

        memoryPolicyStore.graph().associate("ua1", "oa1", new AccessRightSet("read"));
        memoryPolicyStore.graph().associate("ua1", AdminPolicyNode.POLICY_CLASS_TARGETS.nodeName(), new AccessRightSet(AdminAccessRights.ASSIGN_TO));

        memoryPolicyStore.graph().createUser("u1", "ua1");
        memoryPolicyStore.graph().createObject("o1", "oa1");
    }

    @Test
    void testAccessRightChecker() throws PMException {
        AccessRightChecker accessRightChecker = new AccessRightChecker(new PAP(memoryPolicyStore), new MemoryPolicyReviewer(memoryPolicyStore));
        accessRightChecker.check(new UserContext("u1"), "o1", "read");
        accessRightChecker.check(new UserContext("u1"), "pc1", AdminAccessRights.ASSIGN_TO);
        assertThrows(UnauthorizedException.class, () -> accessRightChecker.check(new UserContext("u1"), "pc1", AdminAccessRights.DELETE_POLICY_CLASS));
    }

}