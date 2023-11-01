package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.AdminPolicyNode;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pdp.adjudicator.PrivilegeChecker;
import gov.nist.csd.pm.pdp.reviewer.PolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.UnauthorizedException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.AdminAccessRights;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrivilegeCheckerTest {

    PAP pap;

    @BeforeEach
    void setup() throws PMException {
        pap = new PAP(new MemoryPolicyStore());

        pap.graph().setResourceAccessRights(new AccessRightSet("read"));

        pap.graph().createPolicyClass("pc1");
        pap.graph().createUserAttribute("ua1", "pc1");
        pap.graph().createObjectAttribute("oa1", "pc1");

        pap.graph().associate("ua1", "oa1", new AccessRightSet("read"));
        pap.graph().associate("ua1", AdminPolicyNode.POLICY_CLASS_TARGETS.nodeName(), new AccessRightSet(AdminAccessRights.ASSIGN_TO));

        pap.graph().createUser("u1", "ua1");
        pap.graph().createObject("o1", "oa1");
    }

    @Test
    void testAccessRightChecker() throws PMException {
        PrivilegeChecker privilegeChecker = new PrivilegeChecker(pap, new PolicyReviewer(pap));
        privilegeChecker.check(new UserContext("u1"), "o1", "read");
        privilegeChecker.check(new UserContext("u1"), "pc1", AdminAccessRights.ASSIGN_TO);
        assertThrows(UnauthorizedException.class, () -> privilegeChecker.check(new UserContext("u1"), "pc1", AdminAccessRights.DELETE_POLICY_CLASS));
    }

}