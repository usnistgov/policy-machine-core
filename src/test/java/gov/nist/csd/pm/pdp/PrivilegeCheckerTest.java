package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.AdminAccessRights;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.common.exception.PMException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.util.TestMemoryPAP.id;
import static org.junit.jupiter.api.Assertions.*;

class PrivilegeCheckerTest {

    @Test
    void testAccessRightChecker() throws PMException {
        PAP pap = new MemoryPAP();

        pap.modify().operations().setResourceOperations(new AccessRightSet("read"));

        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
        pap.modify().graph().associate(ua1, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), new AccessRightSet(
                AdminAccessRights.ASSIGN_TO));

        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));

        PrivilegeChecker privilegeChecker = new PrivilegeChecker(pap);
        privilegeChecker.check(new UserContext(u1), "o1", List.of("read"));
        privilegeChecker.check(new UserContext(u1), "pc1", List.of(AdminAccessRights.ASSIGN_TO));
        assertThrows(UnauthorizedException.class, () -> privilegeChecker.check(new UserContext(u1), "pc1", List.of(AdminAccessRights.DELETE_POLICY_CLASS)));
    }

}