package gov.nist.csd.pm.core.pdp;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.UnauthorizedException;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.Test;

class PrivilegeCheckerTest {

    @Test
    void testAccessRightChecker() throws PMException {
        TestPAP pap = new TestPAP();

        pap.modify().operations().setResourceAccessRights(new AccessRightSet("read"));

        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
        pap.modify().graph().associate(ua1, AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet(
            AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_CREATE));

        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));

        pap.check(new UserContext(u1), id("o1"), List.of("read"));
        pap.check(new UserContext(u1), id("pc1"), AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_CREATE);
        assertThrows(UnauthorizedException.class, () -> pap.check(new UserContext(u1), id("pc1"),
            AdminAccessRight.ADMIN_GRAPH_NODE_CREATE));
    }

}
