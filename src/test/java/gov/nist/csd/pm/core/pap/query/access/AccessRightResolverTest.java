package gov.nist.csd.pm.core.pap.query.access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightResolver;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.NodeProhibition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.accessright.WildcardAccessRight;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AccessRightResolverTest {

    private static final AccessRightSet RESOURCE_OPS = new AccessRightSet("read", "write", "execute");

    @Test
    void testWildcardAllExpansion() {
        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of());
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, AccessRightSet.wildcard()),
            Set.of()
        );

        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);

        // Should contain all admin rights plus resource operations
        assertTrue(result.containsAll(WildcardAccessRight.ADMIN_WILDCARD.getAccessRights()));
        assertTrue(result.containsAll(RESOURCE_OPS));
    }

    @Test
    void testWildcardResourceExpansion() {
        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of());
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, AccessRightSet.resourceWildcard()),
            Set.of()
        );

        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);

        // Should contain only resource operations
        assertEquals(RESOURCE_OPS, result);
        assertFalse(result.contains(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE.toString()));
    }

    @Test
    void testWildcardAdminExpansion() {
        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of());
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, AccessRightSet.adminWildcard()),
            Set.of()
        );

        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);

        // Should contain all admin rights but not resource operations
        assertTrue(result.containsAll(WildcardAccessRight.ADMIN_WILDCARD.getAccessRights()));
        assertFalse(result.contains("read"));
        assertFalse(result.contains("write"));
        assertFalse(result.contains("execute"));
    }

    @Test
    void testPolicyClassAccessRightsIntersection() {
        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of());

        Map<Long, AccessRightSet> pcMap = Map.of(
            1L, new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE,
                AdminAccessRight.ADMIN_GRAPH_NODE_CREATE,
                AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_CREATE),
            2L, new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE,
                AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_CREATE,
                AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_UA_CREATE),
            3L, new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_CREATE,
                AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_UA_CREATE,
                AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_DELETE)
        );

        TargetDagResult targetCtx = new TargetDagResult(pcMap, Set.of());
        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);

        // Should only contain rights that are common to all policy classes
        AccessRightSet expected = new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_CREATE);
        assertEquals(expected, result);
    }

    @Test
    void testEmptyPolicyClassAccessRights() {
        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of());

        Map<Long, AccessRightSet> pcMap = Map.of(
            1L, new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE),
            2L, new AccessRightSet() // Empty access rights
        );

        TargetDagResult targetCtx = new TargetDagResult(pcMap, Set.of());
        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);

        assertTrue(result.isEmpty());
    }

    @Test
    void testProhibitionSatisfactionUnion() {
        Prohibition prohibition = new NodeProhibition(
            "test_prohibition",
            1L,
            new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE, AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_CREATE),
            Set.of(100L, 200L),
            Set.of(),
            false // union (not intersection)
        );

        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of(prohibition));
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE, AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_CREATE, AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_UA_CREATE)),
            Set.of(100L)
        );

        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);
        AccessRightSet expected = new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_UA_CREATE);
        assertEquals(expected, result);
    }

    @Test
    void testProhibitionSatisfactionIntersection() {
        Prohibition prohibition = new NodeProhibition(
            "test_prohibition",
            1L,
            new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE, AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_CREATE),
            Set.of(100L, 200L),
            Set.of(),
            true
        );

        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of(prohibition));
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE, AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_CREATE, AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_UA_CREATE)),
            Set.of(100L, 200L)
        );

        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);
        AccessRightSet expected = new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_UA_CREATE);
        assertEquals(expected, result);
    }

    @Test
    void testProhibitionWithComplementContainers() {
        Prohibition prohibition = new NodeProhibition(
            "test_prohibition",
            1L,
            new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE),
            Set.of(),
            Set.of(100L), // complement
            false
        );

        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of(prohibition));
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE, AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_CREATE)),
            Set.of(200L)
        );

        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);
        AccessRightSet expected = new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_CREATE);
        assertEquals(expected, result);
    }

    @Test
    void testComputeSatisfiedProhibitions() {
        Prohibition prohibition1 = new NodeProhibition(
            "prohibition1",
            1L,
            new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE),
            Set.of(100L),
            Set.of(),
            false
        );

        Prohibition prohibition2 = new NodeProhibition(
            "prohibition2",
            1L,
            new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_CREATE),
            Set.of(200L),
            Set.of(),
            false
        );

        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of(prohibition1, prohibition2));
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE, AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_CREATE)),
            Set.of(100L)
        );

        List<Prohibition> satisfied = AccessRightResolver.computeSatisfiedProhibitions(userCtx, targetCtx);

        assertEquals(1, satisfied.size());
        assertEquals("prohibition1", satisfied.get(0).getName());
    }

    @Test
    void testDeniedAccessRightsComputation() {
        Prohibition prohibition = new NodeProhibition(
            "test_prohibition",
            1L,
            new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE, AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_CREATE),
            Set.of(100L),
            Set.of(),
            false
        );

        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of(prohibition));
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE, AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_CREATE, AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_UA_CREATE)),
            Set.of(100L)
        );

        AccessRightSet denied = AccessRightResolver.resolveDeniedAccessRights(userCtx, targetCtx);

        AccessRightSet expected = new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_CREATE, AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_CREATE);
        assertEquals(expected, denied);
    }

    @Test
    void testMixedWildcardAndLiteralAccessRights() {
        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of());
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, new AccessRightSet(WildcardAccessRight.ADMIN_GRAPH_WILDCARD.toString(), "read")),
            Set.of()
        );

        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);

        assertTrue(result.containsAll(WildcardAccessRight.ADMIN_GRAPH_WILDCARD.getAccessRights()));
        assertTrue(result.contains("read"));
    }
}
