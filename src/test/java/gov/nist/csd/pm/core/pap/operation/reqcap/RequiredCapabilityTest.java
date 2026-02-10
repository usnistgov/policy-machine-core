package gov.nist.csd.pm.core.pap.operation.reqcap;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.Test;

class RequiredCapabilityTest {

    @Test
    void testIsSatisfiedWithEmptyPrivileges() throws PMException {
        MemoryPAP pap = new TestPAP();
        RequiredCapability reqCap = new RequiredCapability(List.of());

        assertTrue(reqCap.isSatisfied(pap, new UserContext(id("u1")), new Args()));
    }

    @Test
    void testIsSatisfiedWhenAllPrivilegesMet() throws PMException {
        MemoryPAP pap = new TestPAP();
        String pml = """
                set resource access rights ["read"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" and PM_ADMIN_BASE_OA with ["admin:graph:node:create", "admin:graph:node:delete"]
                create u "u1" in ["ua1"]
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        RequiredCapability reqCap = new RequiredCapability(
            new RequiredPrivilegeOnNode(
                AdminPolicyNode.PM_ADMIN_BASE_OA.nodeName(),
                new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE)
            ),
            new RequiredPrivilegeOnNode(
                AdminPolicyNode.PM_ADMIN_BASE_OA.nodeName(),
                new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_DELETE)
            )
        );

        assertTrue(reqCap.isSatisfied(pap, new UserContext(id("u1")), new Args()));
    }

    @Test
    void testIsSatisfiedReturnsFalseWhenOnePrivilegeFails() throws PMException {
        MemoryPAP pap = new TestPAP();
        String pml = """
                set resource access rights ["read"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" and PM_ADMIN_BASE_OA with ["admin:graph:node:create"]
                create u "u1" in ["ua1"]
                """;

        pap.executePML(new UserContext(id("u1")), pml);

        RequiredCapability reqCap = new RequiredCapability(
            new RequiredPrivilegeOnNode(
                AdminPolicyNode.PM_ADMIN_BASE_OA.nodeName(),
                new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE)
            ),
            new RequiredPrivilegeOnNode(
                AdminPolicyNode.PM_ADMIN_BASE_OA.nodeName(),
                new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_DELETE)
            )
        );

        assertFalse(reqCap.isSatisfied(pap, new UserContext(id("u1")), new Args()));
    }
}
