package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.op.AdminAccessRights;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pdp.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrivilegeCheckerTest {

    @Test
    void testAccessRightChecker() throws PMException {
        PAP pap = new MemoryPAP();

        pap.modify().operations().setResourceOperations(new AccessRightSet("read"));

        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
        pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));

        pap.modify().graph().associate("ua1", "oa1", new AccessRightSet("read"));
        pap.modify().graph().associate("ua1", AdminPolicyNode.ADMIN_POLICY_OBJECT.nodeName(), new AccessRightSet(
                AdminAccessRights.ASSIGN_TO));

        pap.modify().graph().createUser("u1", List.of("ua1"));
        pap.modify().graph().createObject("o1", List.of("oa1"));

        PrivilegeChecker.check(pap, new UserContext("u1"), "o1", "read");
        PrivilegeChecker.check(pap, new UserContext("u1"), "pc1", AdminAccessRights.ASSIGN_TO);
        assertThrows(UnauthorizedException.class, () -> PrivilegeChecker.check(pap, new UserContext("u1"), "pc1", AdminAccessRights.DELETE_POLICY_CLASS));
    }

}