package gov.nist.csd.pm.pdp.memory;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static org.junit.jupiter.api.Assertions.*;

class MemoryPolicyReviewerTest {

    @Nested
    class GetAccessRights {
        private static final AccessRightSet RWE = new AccessRightSet("read", "write", "execute");

        @Test
        void testGetChildren() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);

            String pc1 = pap.createPolicyClass("pc1");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String u1 = pap.createUser("u1", ua1);
            String o1 = pap.createObject("o1", oa1);
            String o2 = pap.createObject("o2", oa1);
            String o3 = pap.createObject("o3", oa1);

            AccessRightSet arset = new AccessRightSet("read", "write");
            pap.associate(ua1, oa1, arset);

            Map<String, AccessRightSet> subgraph = pdp.policyReviewer().getSubgraphAccessRights(new UserContext(u1), oa1);
            assertEquals(
                    Map.of("o1", arset, "o2", arset, "o3", arset),
                    subgraph
            );
        }

        @Test
        void testGetAccessibleNodes() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);

            String pc1 = pap.createPolicyClass("pc1");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String u1 = pap.createUser("u1", ua1);
            String o1 = pap.createObject("o1", oa1);
            String o2 = pap.createObject("o2", oa1);
            String o3 = pap.createObject("o3", oa1);

            AccessRightSet arset = new AccessRightSet("read", "write");
            pap.associate(ua1, oa1, arset);

            Map<String, AccessRightSet> accessibleNodes = pdp.policyReviewer().buildCapabilityList(new UserContext(u1));

            assertTrue(accessibleNodes.containsKey(oa1));
            assertTrue(accessibleNodes.containsKey(o1));
            assertTrue(accessibleNodes.containsKey(o2));
            assertTrue(accessibleNodes.containsKey(o3));

            assertEquals(arset, accessibleNodes.get(oa1));
            assertEquals(arset, accessibleNodes.get(o1));
            assertEquals(arset, accessibleNodes.get(o2));
            assertEquals(arset, accessibleNodes.get(o3));
        }

        @Test
        void testGraph1() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);

            String pc1 = pap.createPolicyClass("pc1");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String u1 = pap.createUser("u1", ua1);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String o1 = pap.createObject("o1", oa1);

            pap.associate(ua1, oa1, new AccessRightSet("read", "write"));

            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }
        @Test
        void testGraph2() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);

            String pc1 = pap.createPolicyClass("pc1");
            String pc2 = pap.createPolicyClass("pc2");
            String ua1 = pap.createUserAttribute("ua1", pc1, pc2);
            String ua2 = pap.createUserAttribute("ua2", pc1);
            String u1 = pap.createUser("u1", ua1, ua2);

            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String oa2 = pap.createObjectAttribute("oa2", pc2);
            String o1 = pap.createObject("o1", oa1, oa2);

            pap.associate(ua1, oa1, new AccessRightSet("read"));

            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o1).isEmpty());
        }
        @Test
        void testGraph3() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);
            String pc1 = pap.createPolicyClass("pc1");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String u1 = pap.createUser("u1", ua1);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String o1 = pap.createObject("o1", oa1);

            pap.associate(ua1, oa1, new AccessRightSet("read", "write"));

            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }
        @Test
        void testGraph4() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);
            String pc1 = pap.createPolicyClass("pc1");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String ua2 = pap.createUserAttribute("ua2", pc1);
            String u1 = pap.createUser("u1", ua1, ua2);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String o1 = pap.createObject("o1", oa1);

            pap.associate(ua1, oa1, new AccessRightSet("read"));
            pap.associate(ua2, oa1, new AccessRightSet("write"));

            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }
        @Test
        void testGraph5() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);

            String pc1 = pap.createPolicyClass("pc1");
            String pc2 = pap.createPolicyClass("pc2");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String ua2 = pap.createUserAttribute("ua2", pc2);
            String u1 = pap.createUser("u1", ua1, ua2);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String oa2 = pap.createObjectAttribute("oa2", pc2);
            String o1 = pap.createObject("o1", oa1, oa2);

            pap.associate(ua1, oa1, new AccessRightSet("read"));
            pap.associate(ua2, oa2, new AccessRightSet("read", "write"));


            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o1).containsAll(Arrays.asList("read")));
        }
        @Test
        void testGraph6() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);
            String pc1 = pap.createPolicyClass("pc1");
            String pc2 = pap.createPolicyClass("pc2");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String ua2 = pap.createUserAttribute("ua2", pc2);
            String u1 = pap.createUser("u1", ua1, ua2);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String oa2 = pap.createObjectAttribute("oa2", pc2);
            String o1 = pap.createObject("o1", oa1, oa2);

            pap.associate(ua1, oa1, new AccessRightSet("read", "write"));
            pap.associate(ua2, oa2, new AccessRightSet("read"));


            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o1).containsAll(Arrays.asList("read")));
        }
        @Test
        void testGraph7() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);

            String pc1 = pap.createPolicyClass("pc1");
            String pc2 = pap.createPolicyClass("pc2");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String u1 = pap.createUser("u1", ua1);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String oa2 = pap.createObjectAttribute("oa2", pc2);
            String o1 = pap.createObject("o1", oa1, oa2);

            pap.associate(ua1, oa1, new AccessRightSet("read", "write"));


            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o1).isEmpty());
        }
        @Test
        void testGraph8() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);
            String pc1 = pap.createPolicyClass("pc1");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String u1 = pap.createUser("u1", ua1);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String o1 = pap.createObject("o1", oa1);

            pap.associate(ua1, oa1, new AccessRightSet("*"));


            Set<String> list = pdp.policyReviewer().getAccessRights(new UserContext(u1), o1);
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertTrue(list.containsAll(RWE));
        }
        @Test
        void testGraph9() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);
            String pc1 = pap.createPolicyClass("pc1");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String ua2 = pap.createUserAttribute("ua2", pc1);
            String u1 = pap.createUser("u1", ua1);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String o1 = pap.createObject("o1", oa1);

            pap.associate(ua1, oa1, new AccessRightSet("*"));
            pap.associate(ua2, oa1, new AccessRightSet("read", "write"));


            Set<String> list = pdp.policyReviewer().getAccessRights(new UserContext(u1), o1);
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertTrue(list.containsAll(RWE));
        }
        @Test
        void testGraph10() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);
            String pc1 = pap.createPolicyClass("pc1");
            String pc2 = pap.createPolicyClass("pc2");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String ua2 = pap.createUserAttribute("ua2", pc2);
            String u1 = pap.createUser("u1", ua1, ua2);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String oa2 = pap.createObjectAttribute("oa2", pc2);
            String o1 = pap.createObject("o1", oa1, oa2);

            pap.associate(ua1, oa1, new AccessRightSet("*"));
            pap.associate(ua2, oa2, new AccessRightSet("read", "write"));


            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }
        @Test
        void testGraph11() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);
            String pc1 = pap.createPolicyClass("pc1");
            String pc2 = pap.createPolicyClass("pc2");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String u1 = pap.createUser("u1", ua1);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String oa2 = pap.createObjectAttribute("oa2", pc2);
            String o1 = pap.createObject("o1", oa1, oa2);

            pap.associate(ua1, oa1, new AccessRightSet("*"));


            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o1).isEmpty());
        }
        @Test
        void testGraph12() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);
            String pc1 = pap.createPolicyClass("pc1");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String ua2 = pap.createUserAttribute("ua2", pc1);
            String u1 = pap.createUser("u1", ua1, ua2);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String o1 = pap.createObject("o1", oa1);

            pap.associate(ua1, oa1, new AccessRightSet("read"));
            pap.associate(ua2, oa1, new AccessRightSet("write"));


            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }
        @Test
        void testGraph13() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);
            String pc1 = pap.createPolicyClass("pc1");
            String ua2 = pap.createUserAttribute("ua2", pc1);
            String ua1 = pap.createUserAttribute("ua1", ua2);
            String u1 = pap.createUser("u1", ua1);
            String oa2 = pap.createObjectAttribute("oa2", pc1);
            String oa1 = pap.createObjectAttribute("oa1", oa2);
            String o1 = pap.createObject("o1", oa1);

            pap.associate(ua1, oa1, new AccessRightSet("*"));
            pap.associate(ua2, oa2, new AccessRightSet("read"));


            Set<String> list = pdp.policyReviewer().getAccessRights(new UserContext(u1), o1);
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertTrue(list.contains("read"));
        }
        @Test
        void testGraph14() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);
            String pc1 = pap.createPolicyClass("pc1");
            String pc2 = pap.createPolicyClass("pc2");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String ua2 = pap.createUserAttribute("ua2", pc1);
            String u1 = pap.createUser("u1", ua1, ua2);
            String oa1 = pap.createObjectAttribute("oa1", pc1, pc2);
            String o1 = pap.createObject("o1", oa1);

            pap.associate(ua1, oa1, new AccessRightSet("*"));
            pap.associate(ua2, oa1, new AccessRightSet("*"));


            Set<String> list = pdp.policyReviewer().getAccessRights(new UserContext(u1), o1);
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertTrue(list.containsAll(RWE));
        }
        @Test
        void testGraph15() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);
            String pc1 = pap.createPolicyClass("pc1");
            String ua2 = pap.createUserAttribute("ua2", pc1);
            String ua1 = pap.createUserAttribute("ua1", ua2);
            String u1 = pap.createUser("u1", ua1);
            String oa2 = pap.createObjectAttribute("oa2", pc1);
            String oa1 = pap.createObjectAttribute("oa1", oa2);
            String o1 = pap.createObject("o1", oa1);

            pap.associate(ua1, oa1, new AccessRightSet("*"));
            pap.associate(ua2, oa2, new AccessRightSet("read"));


            Set<String> list = pdp.policyReviewer().getAccessRights(new UserContext(u1), o1);
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertTrue(list.containsAll(RWE));
        }
        @Test
        void testGraph16() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);
            String pc1 = pap.createPolicyClass("pc1");
            String ua2 = pap.createUserAttribute("ua2", pc1);
            String ua1 = pap.createUserAttribute("ua1", ua2);
            String u1 = pap.createUser("u1", ua1);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String o1 = pap.createObject("o1", oa1);

            pap.associate(ua1, oa1, new AccessRightSet("read"));
            pap.associate(ua2, oa1, new AccessRightSet("write"));


            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }

        // removed graph7 due to adding the parent IDs to the createNode, need to always connect to the pap.

        @Test
        void testGraph18() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);
            String pc1 = pap.createPolicyClass("pc1");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String u1 = pap.createUser("u1", ua1);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String oa2 = pap.createObjectAttribute("oa2", pc1);
            String o1 = pap.createObject("o1", oa2);

            pap.associate(ua1, oa1, new AccessRightSet("read", "write"));


            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o1).isEmpty());
        }
        @Test
        void testGraph19() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);
            String pc1 = pap.createPolicyClass("pc1");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String ua2 = pap.createUserAttribute("ua2", pc1);
            String u1 = pap.createUser("u1", ua2);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String o1 = pap.createObject("o1", oa1);

            pap.associate(ua1, oa1, new AccessRightSet("read"));


            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o1).isEmpty());
        }
        @Test
        void testGraph20() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);
            String pc1 = pap.createPolicyClass("pc1");
            String pc2 = pap.createPolicyClass("pc2");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String ua2 = pap.createUserAttribute("ua2", pc1);
            String u1 = pap.createUser("u1", ua1, ua2);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String oa2 = pap.createObjectAttribute("oa2", pc2);
            String o1 = pap.createObject("o1", oa1, oa2);

            pap.associate(ua1, oa1, new AccessRightSet("read"));
            pap.associate(ua2, oa2, new AccessRightSet("read", "write"));


            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o1).containsAll(Arrays.asList("read")));
        }
        @Test
        void testGraph21() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);
            String pc1 = pap.createPolicyClass("pc1");
            String pc2 = pap.createPolicyClass("pc2");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String ua2 = pap.createUserAttribute("ua2", pc1);
            String u1 = pap.createUser("u1", ua1, ua2);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String oa2 = pap.createObjectAttribute("oa2", pc2);
            String o1 = pap.createObject("o1", oa1, oa2);

            pap.associate(ua1, oa1, new AccessRightSet("read"));
            pap.associate(ua2, oa2, new AccessRightSet("write"));


            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o1).isEmpty());
        }
        @Test
        void testGraph22() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);
            String pc1 = pap.createPolicyClass("pc1");
            String pc2 = pap.createPolicyClass("pc2");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String u1 = pap.createUser("u1", ua1);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String o1 = pap.createObject("o1", oa1);

            pap.associate(ua1, oa1, new AccessRightSet("read", "write"));


            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }

        @Test
        void testGraph23WithProhibitions() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);

            String pc1 = pap.createPolicyClass("pc1");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String u1 = pap.createUser("u1", ua1);
            String oa3 = pap.createObjectAttribute("oa3", pc1);
            String oa4 = pap.createObjectAttribute("oa4", pc1);
            String oa2 = pap.createObjectAttribute("oa2", oa3);
            String oa1 = pap.createObjectAttribute("oa1", oa4);
            String o1 = pap.createObject("o1", oa1, oa2);

            pap.associate(ua1, oa3, new AccessRightSet("read", "write", "execute"));
            pap.createProhibition("deny", ProhibitionSubject.userAttribute("ua1"), new AccessRightSet("read"), true,
                    new ContainerCondition(oa1, false),
                    new ContainerCondition(oa2, false)
            );

            pap.createProhibition("deny2", ProhibitionSubject.user(u1), new AccessRightSet("write"),
                    true,
                    new ContainerCondition(oa3, false));

            Set<String> list = pdp.policyReviewer().getAccessRights(new UserContext(u1), o1);
            assertEquals(1, list.size());
            assertTrue(list.contains("execute"));
        }

        @Test
        void testGraph24WithProhibitions() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);

            String pc1 = pap.createPolicyClass("pc1");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String u1 = pap.createUser("u1", ua1);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String oa2 = pap.createObjectAttribute("oa2", pc1);
            String o1 = pap.createObject("o1", oa1, oa2);
            String o2 = pap.createObject("o2", oa2);

            pap.associate(ua1, oa1, new AccessRightSet("read"));

            pap.createProhibition("deny", ProhibitionSubject.userAttribute(ua1),
                    new AccessRightSet("read"),
                    true,
                    new ContainerCondition(oa1, false),
                    new ContainerCondition(oa2, true)
            );

            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o1).contains("read"));
            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o2).isEmpty());

            pap.associate(ua1, oa2, new AccessRightSet("read"));

            pap.createProhibition("deny-process", ProhibitionSubject.process("1234"),
                    new AccessRightSet("read"),
                    false,
                    new ContainerCondition(oa1, false));

            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1, "1234"), o1).isEmpty());
        }

        @Test
        void testGraph25WithProhibitions() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);

            String pc1 = pap.createPolicyClass("pc1");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String u1 = pap.createUser("u1", ua1);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String oa2 = pap.createObjectAttribute("oa2", oa1);
            String oa3 = pap.createObjectAttribute("oa3", oa1);
            String oa4 = pap.createObjectAttribute("oa4", oa3);
            String oa5 = pap.createObjectAttribute("oa5", oa2);
            String o1 = pap.createObject("o1", oa4);

            pap.associate(ua1, oa1, new AccessRightSet("read", "write"));

            pap.createProhibition("deny", ProhibitionSubject.user(u1), new AccessRightSet("read", "write"),
                    true,
                    new ContainerCondition(oa4, true),
                    new ContainerCondition(oa1, false));

            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), oa5).isEmpty());
            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }

        @Test
        void testGraph25WithProhibitions2() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);

            String pc1 = pap.createPolicyClass("pc1");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String u1 = pap.createUser("u1", ua1);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String oa2 = pap.createObjectAttribute("oa2", pc1);
            String o1 = pap.createObject("o1", oa1, oa2);

            pap.associate(ua1, oa1, new AccessRightSet("read", "write"));


            pap.createProhibition("deny", ProhibitionSubject.user(u1), new AccessRightSet("read", "write"),
                    true,
                    new ContainerCondition(oa1, false),
                    new ContainerCondition(oa2, false));

            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(u1), o1).isEmpty());
        }

        @Test
        void testDeciderWithUA() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);

            String pc1 = pap.createPolicyClass("pc1");
            String ua2 = pap.createUserAttribute("ua2", pc1);
            String ua1 = pap.createUserAttribute("ua1", ua2);
            String u1 = pap.createUser("u1", ua1);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String oa2 = pap.createObjectAttribute("oa2", pc1);
            String o1 = pap.createObject("o1", oa1, oa2);
            String o2 = pap.createObject("o2", oa2);

            pap.associate(ua1, oa1, new AccessRightSet("read"));
            pap.associate(ua2, oa1, new AccessRightSet("write"));

            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext(ua1), oa1).containsAll(Arrays.asList("read", "write")));
        }

        @Test
        void testProhibitionsAllCombinations() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);
            pap.createPolicyClass("pc1");
            pap.createObjectAttribute("oa1", "pc1");
            pap.createObjectAttribute("oa2", "pc1");
            pap.createObjectAttribute("oa3", "pc1");
            pap.createObjectAttribute("oa4", "pc1");
            pap.createObject("o1", "oa1", "oa2", "oa3");
            pap.createObject("o2", "oa1", "oa4");

            pap.createUserAttribute("ua1", "pc1");
            pap.createUser("u1", "ua1");
            pap.createUser("u2", "ua1");
            pap.createUser("u3", "ua1");
            pap.createUser("u4", "ua1");

            pap.associate("ua1", "oa1", new AccessRightSet("read", "write"));


            pap.createProhibition(
                    "p1",
                    ProhibitionSubject.user("u1"),
                    new AccessRightSet("write"),
                    true,
                    new ContainerCondition("oa1", false),
                    new ContainerCondition("oa2", false),
                    new ContainerCondition("oa3", false)
            );

            pap.createProhibition(
                    "p2",
                    ProhibitionSubject.user("u2"),
                    new AccessRightSet("write"),
                    false,
                    new ContainerCondition("oa1", false),
                    new ContainerCondition("oa2", false),
                    new ContainerCondition("oa3", false)
            );

            pap.createProhibition(
                    "p3",
                    ProhibitionSubject.user("u3"),
                    new AccessRightSet("write"),
                    true,
                    new ContainerCondition("oa1", false),
                    new ContainerCondition("oa2", true)
            );

            pap.createProhibition(
                    "p4",
                    ProhibitionSubject.user("u4"),
                    new AccessRightSet("write"),
                    false,
                    new ContainerCondition("oa1", false),
                    new ContainerCondition("oa2", true)
            );

            pap.createProhibition(
                    "p5",
                    ProhibitionSubject.user("u4"),
                    new AccessRightSet("write"),
                    false,
                    new ContainerCondition("oa2", true)
            );

            Set<String> list = pdp.policyReviewer().getAccessRights(new UserContext("u1"), "o1");
            assertTrue(list.contains("read") && !list.contains("write"));

            list = pdp.policyReviewer().getAccessRights(new UserContext("u1"), "o2");
            assertTrue(list.contains("read") && list.contains("write"));

            list =pdp.policyReviewer().getAccessRights(new UserContext("u2"), "o2");
            assertTrue(list.contains("read") && !list.contains("write"));

            list = pdp.policyReviewer().getAccessRights(new UserContext("u3"), "o2");
            assertTrue(list.contains("read") && !list.contains("write"));

            list = pdp.policyReviewer().getAccessRights(new UserContext("u4"), "o1");
            assertTrue(list.contains("read") && !list.contains("write"));

            list = pdp.policyReviewer().getAccessRights(new UserContext("u4"), "o2");
            assertTrue(list.contains("read") && !list.contains("write"));
        }

        @Test
        void testPermissions() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);

            String pc1 = pap.createPolicyClass("pc1");
            String ua1 = pap.createUserAttribute("ua1", pc1);
            String u1 = pap.createUser("u1", ua1);
            String oa1 = pap.createObjectAttribute("oa1", pc1);
            String o1 = pap.createObject("o1", oa1);

            pap.associate(ua1, oa1, allAccessRights());


            Set<String> list = pdp.policyReviewer().getAccessRights(new UserContext("u1"), "o1");
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertTrue(list.containsAll(RWE));

            pap.associate(ua1, oa1, allAdminAccessRights());
            list = pdp.policyReviewer().getAccessRights(new UserContext("u1"), "o1");
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertFalse(list.containsAll(RWE));

            pap.associate(ua1, oa1, new AccessRightSet(ALL_RESOURCE_ACCESS_RIGHTS));
            list = pdp.policyReviewer().getAccessRights(new UserContext("u1"), "o1");
            assertFalse(list.containsAll(allAdminAccessRights()));
            assertTrue(list.containsAll(RWE));
        }

        @Test
        void testPermissionsInOnlyOnePC() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(RWE);
            pap.createPolicyClass("pc1");
            pap.createPolicyClass("pc2");
            pap.createUserAttribute("ua3", "pc1");
            pap.createUserAttribute("ua2", "ua3");
            pap.createUserAttribute("u1", "ua2");

            pap.createObjectAttribute("oa1", "pc1");
            pap.createObjectAttribute("oa3", "pc2");
            pap.assign("oa3", "oa1");
            pap.createObject("o1", "oa3");

            pap.associate("ua3", "oa1", new AccessRightSet("read"));

            assertTrue(pdp.policyReviewer().getAccessRights(new UserContext("u1"), "o1").isEmpty());
        }

        @Test
        void testProhibitionsWithContainerAsTarget() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            PDP pdp = new MemoryPDP(pap, false);

            pap.setResourceAccessRights(new AccessRightSet("read"));
            pap.createPolicyClass("pc1");
            pap.createUserAttribute("ua1", "pc1");
            pap.createObjectAttribute("oa1", "pc1");
            pap.createUser("u1", "ua1");
            pap.associate("ua1", "oa1", new AccessRightSet("read"));

            pap.createProhibition("deny1", ProhibitionSubject.user("u1"), new AccessRightSet("read"), false,
                    new ContainerCondition("oa1", false));

            AccessRightSet deniedAccessRights = pdp.policyReviewer().getDeniedAccessRights(new UserContext("u1"), "oa1");
            assertTrue(deniedAccessRights.contains("read"));
        }
    }

}