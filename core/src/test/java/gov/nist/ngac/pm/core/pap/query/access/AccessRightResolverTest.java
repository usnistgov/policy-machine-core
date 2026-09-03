/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.query.access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.prohibition.NodeProhibition;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightResolver;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.ngac.pm.core.pap.operation.accessright.WildcardAccessRight;
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

        AccessRightSet denied = AccessRightResolver.resolveDeniedAccessRights(userCtx.prohibitions(), targetCtx);

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
