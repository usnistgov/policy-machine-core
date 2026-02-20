package gov.nist.csd.pm.core.pap.operation.reqcap;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import org.junit.jupiter.api.Test;

class RequiredPrivilegeOnNodeTest {

    @Test
    void testIsSatisfiedWhenUserHasRequiredPrivileges() throws PMException {
        MemoryPAP pap = new TestPAP();
        String pml = """
                set resource access rights ["read"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" to PM_ADMIN_BASE_OA with ["admin:graph:node:create"]
                create u "u1" in ["ua1"]
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        RequiredPrivilegeOnNode reqPriv = new RequiredPrivilegeOnNode(
            AdminPolicyNode.PM_ADMIN_BASE_OA.nodeName(),
            new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE)
        );

        assertTrue(reqPriv.isSatisfied(pap, new UserContext(id("u1")), new Args()));
    }

    @Test
    void testIsSatisfiedReturnsFalseWhenUserLacksPrivileges() throws PMException {
        MemoryPAP pap = new TestPAP();
        String pml = """
                set resource access rights ["read"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" to "oa1" with ["read"]
                create u "u1" in ["ua1"]
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        RequiredPrivilegeOnNode reqPriv = new RequiredPrivilegeOnNode(
            AdminPolicyNode.PM_ADMIN_BASE_OA.nodeName(),
            new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE)
        );

        assertFalse(reqPriv.isSatisfied(pap, new UserContext(id("u1")), new Args()));
    }

    @Test
    void testConstructorWithAdminAccessRight() throws PMException {
        MemoryPAP pap = new TestPAP();
        String pml = """
                set resource access rights ["read"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" to PM_ADMIN_BASE_OA with ["admin:graph:node:create"]
                create u "u1" in ["ua1"]
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        RequiredPrivilegeOnNode reqPriv = new RequiredPrivilegeOnNode(
            AdminPolicyNode.PM_ADMIN_BASE_OA.nodeName(),
            AdminAccessRight.ADMIN_GRAPH_NODE_CREATE
        );

        assertTrue(reqPriv.isSatisfied(pap, new UserContext(id("u1")), new Args()));
        assertEquals(AdminPolicyNode.PM_ADMIN_BASE_OA.nodeName(), reqPriv.getName());
        assertTrue(reqPriv.getRequired().contains(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE.toString()));
    }

    @Test
    void testEqualsAndHashCode() {
        RequiredPrivilegeOnNode a = new RequiredPrivilegeOnNode("node1", new AccessRightSet("read"));
        RequiredPrivilegeOnNode b = new RequiredPrivilegeOnNode("node1", new AccessRightSet("read"));
        RequiredPrivilegeOnNode c = new RequiredPrivilegeOnNode("node2", new AccessRightSet("read"));
        RequiredPrivilegeOnNode d = new RequiredPrivilegeOnNode("node1", new AccessRightSet("write"));

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, d);
        assertNotEquals(a, null);
    }
}
