package gov.nist.csd.pm.core.pdp;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.admin.AdminAccessRights;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.op.PrivilegeChecker;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrivilegeCheckerTest {

    @Test
    void testAccessRightChecker() throws PMException {
        PAP pap = new TestPAP();

        pap.modify().operations().setResourceOperations(new AccessRightSet("read"));

        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
        pap.modify().graph().associate(ua1, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), new AccessRightSet(
                AdminAccessRights.ASSIGN_TO));

        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));

        pap.privilegeChecker().check(new UserContext(u1), id("o1"), List.of("read"));
        pap.privilegeChecker().check(new UserContext(u1), id("pc1"), List.of(AdminAccessRights.ASSIGN_TO));
        assertThrows(UnauthorizedException.class, () -> pap.privilegeChecker().check(new UserContext(u1), id("pc1"), List.of(AdminAccessRights.DELETE_POLICY_CLASS)));
    }

}