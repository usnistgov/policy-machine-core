package gov.nist.csd.pm.core.pap.query.access;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ALL_ADMIN_ACCESS_RIGHTS_SET;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ALL_ADMIN_GRAPH_ACCESS_RIGHTS_SET;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ALL_ADMIN_OBLIGATION_ACCESS_RIGHTS_SET;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ALL_ADMIN_OPERATION_ACCESS_RIGHTS_SET;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ALL_ADMIN_PROHIBITION_ACCESS_RIGHTS_SET;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ALL_ADMIN_ROUTINE_ACCESS_RIGHTS_SET;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ALL_GRAPH_QUERY_ACCESS_RIGHTS_SET;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ALL_OBLIGATION_QUERY_ACCESS_RIGHTS_SET;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ALL_OPERATION_QUERY_ACCESS_RIGHTS_SET;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ALL_PROHIBITION_QUERY_ACCESS_RIGHTS_SET;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ALL_QUERY_ACCESS_RIGHTS_SET;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ALL_ROUTINE_QUERY_ACCESS_RIGHTS_SET;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ASSIGN;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ASSOCIATE;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_OBJECT;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_POLICY_CLASS;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DEASSIGN;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DESERIALIZE_POLICY;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.RESET;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.SERIALIZE_POLICY;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.WC_ADMIN;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.WC_ADMIN_GRAPH;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.WC_ADMIN_OBLIGATION;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.WC_ADMIN_OPERATION;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.WC_ADMIN_PROHIBITION;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.WC_ADMIN_ROUTINE;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.WC_ALL;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.WC_QUERY;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.WC_QUERY_GRAPH;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.WC_QUERY_OBLIGATION;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.WC_QUERY_OPERATION;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.WC_QUERY_PROHIBITION;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.WC_QUERY_ROUTINE;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.WC_RESOURCE;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.WILDCARD_MAP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.graph.dag.TargetDagResult;
import gov.nist.csd.pm.core.common.graph.dag.UserDagResult;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AccessRightResolverTest {

    private static final AccessRightSet RESOURCE_OPS = new AccessRightSet("read", "write", "execute");

    @Test
    void testWildcardAllExpansion() {
        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of());
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, new AccessRightSet(WC_ALL)),
            Set.of()
        );

        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);
        
        // Should contain all admin rights plus resource operations
        assertTrue(result.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));
        assertTrue(result.containsAll(RESOURCE_OPS));
    }

    @Test
    void testWildcardResourceExpansion() {
        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of());
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, new AccessRightSet(WC_RESOURCE)),
            Set.of()
        );

        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);
        
        // Should contain only resource operations
        assertEquals(RESOURCE_OPS, result);
        assertFalse(result.contains(CREATE_POLICY_CLASS));
    }

    @Test
    void testWildcardAdminExpansion() {
        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of());
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, new AccessRightSet(WC_ADMIN)),
            Set.of()
        );

        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);
        
        // Should contain all admin rights but not resource operations
        assertTrue(result.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));
        assertFalse(result.contains("read"));
        assertFalse(result.contains("write"));
        assertFalse(result.contains("execute"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        WC_ADMIN_GRAPH, WC_ADMIN_PROHIBITION, WC_ADMIN_OBLIGATION, 
        WC_ADMIN_OPERATION, WC_ADMIN_ROUTINE
    })
    void testAdminWildcards(String wildcard) {
        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of());
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, new AccessRightSet(wildcard)),
            Set.of()
        );

        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);
        Set<String> expectedRights = WILDCARD_MAP.get(wildcard);
        
        assertTrue(result.containsAll(expectedRights));
        
        // Verify it doesn't contain rights from other categories
        for (Map.Entry<String, Set<String>> entry : WILDCARD_MAP.entrySet()) {
            if (!entry.getKey().equals(wildcard) && entry.getKey().startsWith("*a:")) {
                Set<String> otherRights = new HashSet<>(entry.getValue());
                otherRights.removeAll(expectedRights);
                assertFalse(result.containsAll(otherRights), 
                    "Should not contain rights from " + entry.getKey());
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
        WC_QUERY, WC_QUERY_GRAPH, WC_QUERY_PROHIBITION, 
        WC_QUERY_OBLIGATION, WC_QUERY_OPERATION, WC_QUERY_ROUTINE
    })
    void testQueryWildcards(String wildcard) {
        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of());
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, new AccessRightSet(wildcard)),
            Set.of()
        );

        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);
        Set<String> expectedRights = WILDCARD_MAP.get(wildcard);
        
        assertTrue(result.containsAll(expectedRights));
        assertFalse(result.containsAll(RESOURCE_OPS));
    }

    @Test
    void testAllAdminAccessRightsAreCovered() {
        Set<String> all = new HashSet<>();
        
        all.addAll(ALL_ADMIN_GRAPH_ACCESS_RIGHTS_SET);
        all.addAll(ALL_ADMIN_PROHIBITION_ACCESS_RIGHTS_SET);
        all.addAll(ALL_ADMIN_OBLIGATION_ACCESS_RIGHTS_SET);
        all.addAll(ALL_ADMIN_OPERATION_ACCESS_RIGHTS_SET);
        all.addAll(ALL_ADMIN_ROUTINE_ACCESS_RIGHTS_SET);
        all.add(RESET);
        all.add(SERIALIZE_POLICY);
        all.add(DESERIALIZE_POLICY);
        
        assertEquals(ALL_ADMIN_ACCESS_RIGHTS_SET, all);
    }

    @Test
    void testAllQueryAccessRightsAreCovered() {
        Set<String> coveredRights = new HashSet<>();
        
        coveredRights.addAll(ALL_GRAPH_QUERY_ACCESS_RIGHTS_SET);
        coveredRights.addAll(ALL_PROHIBITION_QUERY_ACCESS_RIGHTS_SET);
        coveredRights.addAll(ALL_OBLIGATION_QUERY_ACCESS_RIGHTS_SET);
        coveredRights.addAll(ALL_OPERATION_QUERY_ACCESS_RIGHTS_SET);
        coveredRights.addAll(ALL_ROUTINE_QUERY_ACCESS_RIGHTS_SET);
        
        assertEquals(ALL_QUERY_ACCESS_RIGHTS_SET, coveredRights);
    }

    @Test
    void testPolicyClassAccessRightsIntersection() {
        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of());
        
        Map<Long, AccessRightSet> pcMap = Map.of(
            1L, new AccessRightSet(CREATE_POLICY_CLASS, CREATE_OBJECT, ASSIGN),
            2L, new AccessRightSet(CREATE_OBJECT, ASSIGN, ASSOCIATE),
            3L, new AccessRightSet(ASSIGN, ASSOCIATE, DEASSIGN)
        );
        
        TargetDagResult targetCtx = new TargetDagResult(pcMap, Set.of());
        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);
        
        // Should only contain rights that are common to all policy classes
        AccessRightSet expected = new AccessRightSet(ASSIGN);
        assertEquals(expected, result);
    }

    @Test
    void testEmptyPolicyClassAccessRights() {
        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of());
        
        Map<Long, AccessRightSet> pcMap = Map.of(
            1L, new AccessRightSet(CREATE_POLICY_CLASS, CREATE_OBJECT),
            2L, new AccessRightSet() // Empty access rights
        );
        
        TargetDagResult targetCtx = new TargetDagResult(pcMap, Set.of());
        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void testProhibitionSatisfactionUnion() {
        ContainerCondition condition1 = new ContainerCondition(100L, false);
        ContainerCondition condition2 = new ContainerCondition(200L, false);
        
        Prohibition prohibition = new Prohibition(
            "test_prohibition",
            new ProhibitionSubject(1L),
            new AccessRightSet(CREATE_OBJECT, ASSIGN),
            false, // union (not intersection)
            List.of(condition1, condition2)
        );
        
        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of(prohibition));
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, new AccessRightSet(CREATE_OBJECT, ASSIGN, ASSOCIATE)),
            Set.of(100L)
        );
        
        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);
        AccessRightSet expected = new AccessRightSet(ASSOCIATE);
        assertEquals(expected, result);
    }

    @Test
    void testProhibitionSatisfactionIntersection() {
        ContainerCondition condition1 = new ContainerCondition(100L, false);
        ContainerCondition condition2 = new ContainerCondition(200L, false);
        
        Prohibition prohibition = new Prohibition(
            "test_prohibition",
            new ProhibitionSubject(1L),
            new AccessRightSet(CREATE_OBJECT, ASSIGN),
            true,
            List.of(condition1, condition2)
        );
        
        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of(prohibition));
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, new AccessRightSet(CREATE_OBJECT, ASSIGN, ASSOCIATE)),
            Set.of(100L, 200L)
        );
        
        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);
        AccessRightSet expected = new AccessRightSet(ASSOCIATE);
        assertEquals(expected, result);
    }

    @Test
    void testProhibitionWithComplementContainers() {
        ContainerCondition condition1 = new ContainerCondition(100L, true); // complement
        
        Prohibition prohibition = new Prohibition(
            "test_prohibition",
            new ProhibitionSubject(1L),
            new AccessRightSet(CREATE_OBJECT),
            false,
            List.of(condition1)
        );
        
        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of(prohibition));
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, new AccessRightSet(CREATE_OBJECT, ASSIGN)),
            Set.of(200L)
        );
        
        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);
        AccessRightSet expected = new AccessRightSet(ASSIGN);
        assertEquals(expected, result);
    }

    @Test
    void testComputeSatisfiedProhibitions() {
        ContainerCondition condition1 = new ContainerCondition(100L, false);
        ContainerCondition condition2 = new ContainerCondition(200L, false);
        
        Prohibition prohibition1 = new Prohibition(
            "prohibition1",
            new ProhibitionSubject(1L),
            new AccessRightSet(CREATE_OBJECT),
            false,
            List.of(condition1)
        );
        
        Prohibition prohibition2 = new Prohibition(
            "prohibition2",
            new ProhibitionSubject(1L),
            new AccessRightSet(ASSIGN),
            false,
            List.of(condition2)
        );
        
        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of(prohibition1, prohibition2));
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, new AccessRightSet(CREATE_OBJECT, ASSIGN)),
            Set.of(100L)
        );
        
        List<Prohibition> satisfied = AccessRightResolver.computeSatisfiedProhibitions(userCtx, targetCtx);
        
        assertEquals(1, satisfied.size());
        assertEquals("prohibition1", satisfied.get(0).getName());
    }

    @Test
    void testDeniedAccessRightsComputation() {
        ContainerCondition condition = new ContainerCondition(100L, false);
        
        Prohibition prohibition = new Prohibition(
            "test_prohibition",
            new ProhibitionSubject(1L),
            new AccessRightSet(CREATE_OBJECT, ASSIGN),
            false,
            List.of(condition)
        );
        
        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of(prohibition));
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, new AccessRightSet(CREATE_OBJECT, ASSIGN, ASSOCIATE)),
            Set.of(100L)
        );
        
        AccessRightSet denied = AccessRightResolver.resolveDeniedAccessRights(userCtx, targetCtx);
        
        AccessRightSet expected = new AccessRightSet(CREATE_OBJECT, ASSIGN);
        assertEquals(expected, denied);
    }

    @Test
    void testMixedWildcardAndLiteralAccessRights() {
        UserDagResult userCtx = new UserDagResult(Map.of(), Set.of());
        TargetDagResult targetCtx = new TargetDagResult(
            Map.of(1L, new AccessRightSet(WC_ADMIN_GRAPH, "custom_operation")),
            Set.of()
        );
        
        AccessRightSet result = AccessRightResolver.resolvePrivileges(userCtx, targetCtx, RESOURCE_OPS);
        
        assertTrue(result.containsAll(ALL_ADMIN_GRAPH_ACCESS_RIGHTS_SET));
        assertTrue(result.contains("custom_operation"));
        assertFalse(result.containsAll(ALL_ADMIN_PROHIBITION_ACCESS_RIGHTS_SET));
    }

    @Test
    void testWildcardMapCompleteness() {
        Map<String, Set<String>> wildcardMap = WILDCARD_MAP;
        
        assertTrue(wildcardMap.containsKey(WC_ADMIN));
        assertTrue(wildcardMap.containsKey(WC_ADMIN_GRAPH));
        assertTrue(wildcardMap.containsKey(WC_ADMIN_PROHIBITION));
        assertTrue(wildcardMap.containsKey(WC_ADMIN_OBLIGATION));
        assertTrue(wildcardMap.containsKey(WC_ADMIN_OPERATION));
        assertTrue(wildcardMap.containsKey(WC_ADMIN_ROUTINE));
        assertTrue(wildcardMap.containsKey(WC_QUERY));
        assertTrue(wildcardMap.containsKey(WC_QUERY_GRAPH));
        assertTrue(wildcardMap.containsKey(WC_QUERY_PROHIBITION));
        assertTrue(wildcardMap.containsKey(WC_QUERY_OBLIGATION));
        assertTrue(wildcardMap.containsKey(WC_QUERY_OPERATION));
        assertTrue(wildcardMap.containsKey(WC_QUERY_ROUTINE));
        
        assertEquals(ALL_ADMIN_ACCESS_RIGHTS_SET, wildcardMap.get(WC_ADMIN));
        assertEquals(ALL_QUERY_ACCESS_RIGHTS_SET, wildcardMap.get(WC_QUERY));
    }
} 