package gov.nist.csd.pm.pdp.reviewer;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.audit.Explain;
import gov.nist.csd.pm.policy.model.audit.Path;
import gov.nist.csd.pm.policy.model.audit.PolicyClass;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.allAdminAccessRights;
import static org.junit.jupiter.api.Assertions.*;

class AccessReviewerTest {

    private static final AccessRightSet RWE = new AccessRightSet("read", "write", "execute");

    @Test
    void testComputeAccessibleChildren() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
                create pc "pc1" {
                    uas {
                        "ua1"
                    }
                    oas {
                        "oa1"
                            "oa2"                           
                    }
                    associations {
                        "ua1" and "oa1" with ["read", "write"]
                    }
                }
                
                create u "u1" assign to ["ua1"]
                create o "o1" assign to ["oa1"]
                create o "o2" assign to ["oa1"]
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        AccessReviewer accessReviewer = new AccessReviewer(pap);
        List<String> actual = accessReviewer.computeAccessibleChildren(new UserContext("u1"), "oa1");
        assertEquals(
                List.of("oa2", "o1", "o2"),
                actual
        );
    }

    @Test
    void testComputeAccessibleParents() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
                create pc "pc1" {
                    uas {
                        "ua1"
                    }
                    oas {
                        "oa1"
                        "oa2"
                        "oa3"                           
                    }
                    associations {
                        "ua1" and "oa1" with ["read", "write"]
                        "ua1" and "oa2" with ["read", "write"]
                    }
                }
                
                create u "u1" assign to ["ua1"]
                create o "o1" assign to ["oa1", "oa2"]
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        AccessReviewer accessReviewer = new AccessReviewer(pap);
        List<String> actual = accessReviewer.computeAccessibleParents(new UserContext("u1"), "o1");
        assertEquals(
                List.of("oa1", "oa2"),
                actual
        );
    }

    @Test
    void testBuildPOS() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
                create pc "pc1" {
                    uas {
                        "ua1"
                    }
                    oas {
                        "oa1"                        
                    }
                    associations {
                        "ua1" and "oa1" with ["read", "write"]
                    }
                }
                create pc "pc2" {
                    uas {
                        "ua2"
                    }
                    oas {
                        "oa2"
                            "oa3"
                        "oa4"                           
                    }
                    associations {
                        "ua2" and "oa2" with ["read", "write"]
                        "ua2" and "oa4" with ["read"]
                    }
                }
                
                create u "u1" assign to ["ua1", "ua2"]
                create o "o1" assign to ["oa1", "oa3"]
                create o "o2" assign to ["oa4"]
                
                create prohibition "p1"
                deny user "u1" 
                access rights ["write"]
                on union of ["oa1"]
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        AccessReviewer accessReviewer = new AccessReviewer(pap);
        Set<String> u1 = accessReviewer.buildPOS(new UserContext("u1"));
        assertEquals(
                Set.of("oa1", "oa2", "oa4"),
                u1
        );
    }

    @Test
    void testExplain() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
                create pc "pc1" {
                    uas {
                        "ua1"
                    }
                    oas {
                        "oa1"                        
                    }
                    associations {
                        "ua1" and "oa1" with ["read", "write"]
                    }
                }
                create pc "pc2" {
                    uas {
                        "ua2"
                    }
                    oas {
                        "oa2"
                            "oa3"                           
                    }
                    associations {
                        "ua2" and "oa2" with ["read", "write"]
                    }
                }
                
                create u "u1" assign to ["ua1", "ua2"]
                create o "o1" assign to ["oa1", "oa3"]
                
                create prohibition "p1"
                deny user "u1" 
                access rights ["write"]
                on union of ["oa1"]
                
                create prohibition "p2"
                deny user "u1" 
                access rights ["write"]
                on union of [!"oa1"]
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        AccessReviewer accessReviewer = new AccessReviewer(pap);
        Explain explain = accessReviewer.explain(new UserContext("u1"), "o1");
        Explain expected = new Explain(
                new AccessRightSet("read"),
                Map.of(
                        "pc1", new PolicyClass(
                                new AccessRightSet("read", "write"),
                                Set.of(new Path(
                                        List.of("u1", "ua1", "oa1"),
                                        List.of("o1", "oa1", "pc1"),
                                        new Association("ua1", "oa1", new AccessRightSet("read", "write"))
                                ))),
                        "pc2", new PolicyClass(
                                new AccessRightSet("read", "write"),
                                Set.of(new Path(
                                        List.of("u1", "ua2", "oa2"),
                                        List.of("o1", "oa3", "oa2", "pc2"),
                                        new Association("ua2", "oa2", new AccessRightSet("read", "write"))
                                )))
                ),
                new AccessRightSet("write"),
                List.of(
                        new Prohibition("p1", new ProhibitionSubject("u1", ProhibitionSubject.Type.USER), new AccessRightSet("write"), false, List.of(new ContainerCondition("oa1", false)))
                )
        );
        assertEquals(expected, explain);
    }

    @Test

    void testExplainOnObjAttrWithAssociation() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
                create pc "pc1" {
                    user attributes {
                        "ua1"
                    }
                    
                    object attributes {
                        "oa1"
                            "oa2"
                    }
                    
                    associations {
                        "ua1" and "oa1" with ["write"]
                        "ua1" and "oa2" with ["read"]
                    }
                }
                
                create user "u1" assign to ["ua1"]
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        AccessReviewer accessReviewer = new AccessReviewer(pap);
        Explain actual = accessReviewer.explain(new UserContext("u1"), "oa2");
        assertEquals(
                new Explain(
                        new AccessRightSet("read", "write"),
                        Map.of(
                                "pc1", new PolicyClass(
                                        new AccessRightSet("read", "write"),
                                        Set.of(
                                                new Path(
                                                        List.of("u1", "ua1", "oa1"),
                                                        List.of("oa2", "oa1", "pc1"),
                                                        new Association("ua1", "oa1", new AccessRightSet("write"))
                                                ),
                                                new Path(
                                                        List.of("u1", "ua1", "oa2"),
                                                        List.of("oa2", "oa1", "pc1"),
                                                        new Association("ua1", "oa2", new AccessRightSet("read"))
                                                )
                                        )
                                )
                        ),
                        new AccessRightSet(),
                        List.of()
                ),
                actual
        );
    }

    @Test
    void testComputeSubgraphPrivileges() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
                create pc "pc1" {
                    uas {
                        "ua1"
                    }
                    oas {
                        "oa1"
                            "oa2"                           
                    }
                    associations {
                        "ua1" and "oa1" with ["read", "write"]
                    }
                }
                
                create u "u1" assign to ["ua1"]
                create o "o1" assign to ["oa2"]
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        AccessReviewer accessReviewer = new AccessReviewer(pap);
        Map<String, AccessRightSet> u1 =
                accessReviewer.computeSubgraphPrivileges(new UserContext("u1"), "oa1");
        assertEquals(
                Map.of(
                        "oa2", new AccessRightSet("read", "write"),
                        "o1", new AccessRightSet("read", "write")
                ),
                u1
        );
    }

    @Test
    void testFindBorderAttributes() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
                create pc "pc1" {
                    uas {
                        "ua1"
                        "ua2"
                    }
                    oas {
                        "oa1"
                            "oa2"
                    }
                    associations {
                        "ua1" and "oa1" with ["read", "write"]
                        "ua2" and "oa2" with ["read"]
                    }
                }
                
                create u "u1" assign to ["ua1", "ua2"]
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        AccessReviewer accessReviewer = new AccessReviewer(pap);
        Map<String, AccessRightSet> u1 = accessReviewer.findBorderAttributes("u1");
        assertEquals(
                Map.of(
                        "oa1", new AccessRightSet("read", "write"),
                        "oa2", new AccessRightSet("read")
                ),
                u1
        );
    }

    @Test
    void testBuildACL() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
                create pc "pc1" {
                    uas {
                        "ua1"
                        "ua2"
                    }
                    oas {
                        "oa1"
                    }
                    associations {
                        "ua1" and "oa1" with ["read", "write"]
                        "ua2" and "oa1" with ["read"]
                    }
                }
                
                create u "u1" assign to ["ua1"]
                create u "u2" assign to ["ua2"]
                create o "o1" assign to ["oa1"]
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        AccessReviewer accessReviewer = new AccessReviewer(pap);
        Map<String, AccessRightSet> o1 = accessReviewer.buildACL("o1");
        assertEquals(
                Map.of(
                        "u1", new AccessRightSet("read", "write"),
                        "u2", new AccessRightSet("read")
                ),
                o1
        );
    }

    @Test
    void testBuildCapabilityList() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
                create pc "pc1" {
                    uas {
                        "ua1"
                    }
                    oas {
                        "oa1"
                        "oa2"
                    }
                    associations {
                        "ua1" and "oa1" with ["read", "write"]
                        "ua1" and "oa2" with ["read"]
                    }
                }
                
                create u "u1" assign to ["ua1"]
                create o "o1" assign to ["oa1"]
                create o "o2" assign to ["oa2"]
                
                create prohibition "p1"
                deny user "u1" 
                access rights ["write"]
                on union of ["oa1"]
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        AccessReviewer accessReviewer = new AccessReviewer(pap);
        Map<String, AccessRightSet> u1 = accessReviewer.buildCapabilityList(new UserContext("u1"));
        assertEquals(
                Map.of(
                        "o1", new AccessRightSet("read"),
                        "o2", new AccessRightSet("read"),
                        "oa1", new AccessRightSet("read"),
                        "oa2", new AccessRightSet("read")
                ),
                u1
        );
    }

    @Test
    void testComputeDeniedPrivileges() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
                create pc "pc1" {
                    uas {
                        "ua1"
                    }
                    oas {
                        "oa1"
                    }
                    associations {
                        "ua1" and "oa1" with ["read", "write"]
                    }
                }
                
                create u "u1" assign to ["ua1"]
                create o "o1" assign to ["oa1"]
                
                create prohibition "p1"
                deny user "u1" 
                access rights ["write"]
                on union of ["oa1"]
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        AccessReviewer accessReviewer = new AccessReviewer(pap);
        AccessRightSet deniedPrivileges = accessReviewer.computeDeniedPrivileges(new UserContext("u1"), "o1");
        assertEquals(new AccessRightSet("write"), deniedPrivileges);
    }

    @Test
    void testComputePolicyClassAccessRights() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
                create pc "pc1" {
                    uas {
                        "ua1"
                    }
                    oas {
                        "oa1"
                    }
                    associations {
                        "ua1" and "oa1" with ["read", "write"]
                    }
                }
                create pc "pc2" {
                    uas {
                        "ua2"
                    }
                    oas {
                        "oa2"
                    }
                    associations {
                        "ua2" and "oa2" with ["read"]
                    }
                }
                
                create u "u1" assign to ["ua1", "ua2"]
                create o "o1" assign to ["oa1", "oa2"]
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        AccessReviewer accessReviewer = new AccessReviewer(pap);
        Map<String, AccessRightSet> policyClassAccessRights =
                accessReviewer.computePolicyClassAccessRights(new UserContext("u1"), "o1");
        assertEquals(
                Map.of(
                        "pc1", new AccessRightSet("read", "write"),
                        "pc2", new AccessRightSet("read")
                ),
                policyClassAccessRights
        );
    }

    @Test
    void testGetChildren() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        AccessReviewer accessReviewer = new AccessReviewer(pap);

        pap.graph().setResourceAccessRights(RWE);

        String pc1 = pap.graph().createPolicyClass("pc1");
        String ua1 = pap.graph().createUserAttribute("ua1", pc1);
        String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
        String u1 = pap.graph().createUser("u1", ua1);
        String o1 = pap.graph().createObject("o1", oa1);
        String o2 = pap.graph().createObject("o2", oa1);
        String o3 = pap.graph().createObject("o3", oa1);

        AccessRightSet arset = new AccessRightSet("read", "write");
        pap.graph().associate(ua1, oa1, arset);

        Map<String, AccessRightSet> subgraph = accessReviewer.computeSubgraphPrivileges(new UserContext(u1), oa1);
        assertEquals(
                Map.of("o1", arset, "o2", arset, "o3", arset),
                subgraph
        );
    }

    @Test
    void testGetAccessibleNodes() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        AccessReviewer accessReviewer = new AccessReviewer(pap);

        pap.graph().setResourceAccessRights(RWE);

        String pc1 = pap.graph().createPolicyClass("pc1");
        String ua1 = pap.graph().createUserAttribute("ua1", pc1);
        String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
        String u1 = pap.graph().createUser("u1", ua1);
        String o1 = pap.graph().createObject("o1", oa1);
        String o2 = pap.graph().createObject("o2", oa1);
        String o3 = pap.graph().createObject("o3", oa1);

        AccessRightSet arset = new AccessRightSet("read", "write");
        pap.graph().associate(ua1, oa1, arset);

        Map<String, AccessRightSet> accessibleNodes = accessReviewer.buildCapabilityList(new UserContext(u1));

        assertTrue(accessibleNodes.containsKey(oa1));
        assertTrue(accessibleNodes.containsKey(o1));
        assertTrue(accessibleNodes.containsKey(o2));
        assertTrue(accessibleNodes.containsKey(o3));

        assertEquals(arset, accessibleNodes.get(oa1));
        assertEquals(arset, accessibleNodes.get(o1));
        assertEquals(arset, accessibleNodes.get(o2));
        assertEquals(arset, accessibleNodes.get(o3));
    }

    @Nested
    class GetPrivilegesTests {

        @Test
        void testGraph1() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);

            String pc1 = pap.graph().createPolicyClass("pc1");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String u1 = pap.graph().createUser("u1", ua1);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String o1 = pap.graph().createObject("o1", oa1);

            pap.graph().associate(ua1, oa1, new AccessRightSet("read", "write"));

            assertTrue(
                    accessReviewer.computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }

        @Test
        void testGraph2() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);

            String pc1 = pap.graph().createPolicyClass("pc1");
            String pc2 = pap.graph().createPolicyClass("pc2");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1, pc2);
            String ua2 = pap.graph().createUserAttribute("ua2", pc1);
            String u1 = pap.graph().createUser("u1", ua1, ua2);

            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String oa2 = pap.graph().createObjectAttribute("oa2", pc2);
            String o1 = pap.graph().createObject("o1", oa1, oa2);

            pap.graph().associate(ua1, oa1, new AccessRightSet("read"));

            assertTrue(accessReviewer.computePrivileges(new UserContext(u1), o1).isEmpty());
        }

        @Test
        void testGraph3() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);
            String pc1 = pap.graph().createPolicyClass("pc1");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String u1 = pap.graph().createUser("u1", ua1);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String o1 = pap.graph().createObject("o1", oa1);

            pap.graph().associate(ua1, oa1, new AccessRightSet("read", "write"));

            assertTrue(
                    accessReviewer.computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }

        @Test
        void testGraph4() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);
            String pc1 = pap.graph().createPolicyClass("pc1");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String ua2 = pap.graph().createUserAttribute("ua2", pc1);
            String u1 = pap.graph().createUser("u1", ua1, ua2);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String o1 = pap.graph().createObject("o1", oa1);

            pap.graph().associate(ua1, oa1, new AccessRightSet("read"));
            pap.graph().associate(ua2, oa1, new AccessRightSet("write"));

            assertTrue(
                    accessReviewer.computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }

        @Test
        void testGraph5() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);

            String pc1 = pap.graph().createPolicyClass("pc1");
            String pc2 = pap.graph().createPolicyClass("pc2");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String ua2 = pap.graph().createUserAttribute("ua2", pc2);
            String u1 = pap.graph().createUser("u1", ua1, ua2);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String oa2 = pap.graph().createObjectAttribute("oa2", pc2);
            String o1 = pap.graph().createObject("o1", oa1, oa2);

            pap.graph().associate(ua1, oa1, new AccessRightSet("read"));
            pap.graph().associate(ua2, oa2, new AccessRightSet("read", "write"));


            assertTrue(accessReviewer.computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read")));
        }

        @Test
        void testGraph6() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);
            String pc1 = pap.graph().createPolicyClass("pc1");
            String pc2 = pap.graph().createPolicyClass("pc2");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String ua2 = pap.graph().createUserAttribute("ua2", pc2);
            String u1 = pap.graph().createUser("u1", ua1, ua2);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String oa2 = pap.graph().createObjectAttribute("oa2", pc2);
            String o1 = pap.graph().createObject("o1", oa1, oa2);

            pap.graph().associate(ua1, oa1, new AccessRightSet("read", "write"));
            pap.graph().associate(ua2, oa2, new AccessRightSet("read"));


            assertTrue(accessReviewer.computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read")));
        }

        @Test
        void testGraph7() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);

            String pc1 = pap.graph().createPolicyClass("pc1");
            String pc2 = pap.graph().createPolicyClass("pc2");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String u1 = pap.graph().createUser("u1", ua1);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String oa2 = pap.graph().createObjectAttribute("oa2", pc2);
            String o1 = pap.graph().createObject("o1", oa1, oa2);

            pap.graph().associate(ua1, oa1, new AccessRightSet("read", "write"));


            assertTrue(accessReviewer.computePrivileges(new UserContext(u1), o1).isEmpty());
        }

        @Test
        void testGraph8() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);
            String pc1 = pap.graph().createPolicyClass("pc1");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String u1 = pap.graph().createUser("u1", ua1);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String o1 = pap.graph().createObject("o1", oa1);

            pap.graph().associate(ua1, oa1, new AccessRightSet("*"));


            Set<String> list = accessReviewer.computePrivileges(new UserContext(u1), o1);
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertTrue(list.containsAll(RWE));
        }

        @Test
        void testGraph9() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);
            String pc1 = pap.graph().createPolicyClass("pc1");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String ua2 = pap.graph().createUserAttribute("ua2", pc1);
            String u1 = pap.graph().createUser("u1", ua1);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String o1 = pap.graph().createObject("o1", oa1);

            pap.graph().associate(ua1, oa1, new AccessRightSet("*"));
            pap.graph().associate(ua2, oa1, new AccessRightSet("read", "write"));


            Set<String> list = accessReviewer.computePrivileges(new UserContext(u1), o1);
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertTrue(list.containsAll(RWE));
        }

        @Test
        void testGraph10() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);
            String pc1 = pap.graph().createPolicyClass("pc1");
            String pc2 = pap.graph().createPolicyClass("pc2");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String ua2 = pap.graph().createUserAttribute("ua2", pc2);
            String u1 = pap.graph().createUser("u1", ua1, ua2);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String oa2 = pap.graph().createObjectAttribute("oa2", pc2);
            String o1 = pap.graph().createObject("o1", oa1, oa2);

            pap.graph().associate(ua1, oa1, new AccessRightSet("*"));
            pap.graph().associate(ua2, oa2, new AccessRightSet("read", "write"));


            assertTrue(
                    accessReviewer.computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }

        @Test
        void testGraph11() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);
            String pc1 = pap.graph().createPolicyClass("pc1");
            String pc2 = pap.graph().createPolicyClass("pc2");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String u1 = pap.graph().createUser("u1", ua1);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String oa2 = pap.graph().createObjectAttribute("oa2", pc2);
            String o1 = pap.graph().createObject("o1", oa1, oa2);

            pap.graph().associate(ua1, oa1, new AccessRightSet("*"));


            assertTrue(accessReviewer.computePrivileges(new UserContext(u1), o1).isEmpty());
        }

        @Test
        void testGraph12() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);
            String pc1 = pap.graph().createPolicyClass("pc1");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String ua2 = pap.graph().createUserAttribute("ua2", pc1);
            String u1 = pap.graph().createUser("u1", ua1, ua2);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String o1 = pap.graph().createObject("o1", oa1);

            pap.graph().associate(ua1, oa1, new AccessRightSet("read"));
            pap.graph().associate(ua2, oa1, new AccessRightSet("write"));


            assertTrue(
                    accessReviewer.computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }

        @Test
        void testGraph13() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);
            String pc1 = pap.graph().createPolicyClass("pc1");
            String ua2 = pap.graph().createUserAttribute("ua2", pc1);
            String ua1 = pap.graph().createUserAttribute("ua1", ua2);
            String u1 = pap.graph().createUser("u1", ua1);
            String oa2 = pap.graph().createObjectAttribute("oa2", pc1);
            String oa1 = pap.graph().createObjectAttribute("oa1", oa2);
            String o1 = pap.graph().createObject("o1", oa1);

            pap.graph().associate(ua1, oa1, new AccessRightSet("*"));
            pap.graph().associate(ua2, oa2, new AccessRightSet("read"));


            Set<String> list = accessReviewer.computePrivileges(new UserContext(u1), o1);
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertTrue(list.contains("read"));
        }

        @Test
        void testGraph14() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);
            String pc1 = pap.graph().createPolicyClass("pc1");
            String pc2 = pap.graph().createPolicyClass("pc2");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String ua2 = pap.graph().createUserAttribute("ua2", pc1);
            String u1 = pap.graph().createUser("u1", ua1, ua2);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1, pc2);
            String o1 = pap.graph().createObject("o1", oa1);

            pap.graph().associate(ua1, oa1, new AccessRightSet("*"));
            pap.graph().associate(ua2, oa1, new AccessRightSet("*"));


            Set<String> list = accessReviewer.computePrivileges(new UserContext(u1), o1);
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertTrue(list.containsAll(RWE));
        }

        @Test
        void testGraph15() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);
            String pc1 = pap.graph().createPolicyClass("pc1");
            String ua2 = pap.graph().createUserAttribute("ua2", pc1);
            String ua1 = pap.graph().createUserAttribute("ua1", ua2);
            String u1 = pap.graph().createUser("u1", ua1);
            String oa2 = pap.graph().createObjectAttribute("oa2", pc1);
            String oa1 = pap.graph().createObjectAttribute("oa1", oa2);
            String o1 = pap.graph().createObject("o1", oa1);

            pap.graph().associate(ua1, oa1, new AccessRightSet("*"));
            pap.graph().associate(ua2, oa2, new AccessRightSet("read"));


            Set<String> list = accessReviewer.computePrivileges(new UserContext(u1), o1);
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertTrue(list.containsAll(RWE));
        }

        @Test
        void testGraph16() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);
            String pc1 = pap.graph().createPolicyClass("pc1");
            String ua2 = pap.graph().createUserAttribute("ua2", pc1);
            String ua1 = pap.graph().createUserAttribute("ua1", ua2);
            String u1 = pap.graph().createUser("u1", ua1);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String o1 = pap.graph().createObject("o1", oa1);

            pap.graph().associate(ua1, oa1, new AccessRightSet("read"));
            pap.graph().associate(ua2, oa1, new AccessRightSet("write"));


            assertTrue(
                    accessReviewer.computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }

        // removed graph7 due to adding the parent IDs to the createNode, need to always connect to the pap.graph().

        @Test
        void testGraph18() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);
            String pc1 = pap.graph().createPolicyClass("pc1");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String u1 = pap.graph().createUser("u1", ua1);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String oa2 = pap.graph().createObjectAttribute("oa2", pc1);
            String o1 = pap.graph().createObject("o1", oa2);

            pap.graph().associate(ua1, oa1, new AccessRightSet("read", "write"));


            assertTrue(accessReviewer.computePrivileges(new UserContext(u1), o1).isEmpty());
        }

        @Test
        void testGraph19() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);
            String pc1 = pap.graph().createPolicyClass("pc1");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String ua2 = pap.graph().createUserAttribute("ua2", pc1);
            String u1 = pap.graph().createUser("u1", ua2);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String o1 = pap.graph().createObject("o1", oa1);

            pap.graph().associate(ua1, oa1, new AccessRightSet("read"));


            assertTrue(accessReviewer.computePrivileges(new UserContext(u1), o1).isEmpty());
        }

        @Test
        void testGraph20() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);
            String pc1 = pap.graph().createPolicyClass("pc1");
            String pc2 = pap.graph().createPolicyClass("pc2");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String ua2 = pap.graph().createUserAttribute("ua2", pc1);
            String u1 = pap.graph().createUser("u1", ua1, ua2);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String oa2 = pap.graph().createObjectAttribute("oa2", pc2);
            String o1 = pap.graph().createObject("o1", oa1, oa2);

            pap.graph().associate(ua1, oa1, new AccessRightSet("read"));
            pap.graph().associate(ua2, oa2, new AccessRightSet("read", "write"));


            assertTrue(accessReviewer.computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read")));
        }

        @Test
        void testGraph21() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);
            String pc1 = pap.graph().createPolicyClass("pc1");
            String pc2 = pap.graph().createPolicyClass("pc2");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String ua2 = pap.graph().createUserAttribute("ua2", pc1);
            String u1 = pap.graph().createUser("u1", ua1, ua2);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String oa2 = pap.graph().createObjectAttribute("oa2", pc2);
            String o1 = pap.graph().createObject("o1", oa1, oa2);

            pap.graph().associate(ua1, oa1, new AccessRightSet("read"));
            pap.graph().associate(ua2, oa2, new AccessRightSet("write"));


            assertTrue(accessReviewer.computePrivileges(new UserContext(u1), o1).isEmpty());
        }

        @Test
        void testGraph22() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);
            String pc1 = pap.graph().createPolicyClass("pc1");
            String pc2 = pap.graph().createPolicyClass("pc2");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String u1 = pap.graph().createUser("u1", ua1);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String o1 = pap.graph().createObject("o1", oa1);

            pap.graph().associate(ua1, oa1, new AccessRightSet("read", "write"));


            assertTrue(
                    accessReviewer.computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }

        @Test
        void testGraph23WithProhibitions() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);

            String pc1 = pap.graph().createPolicyClass("pc1");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String u1 = pap.graph().createUser("u1", ua1);
            String oa3 = pap.graph().createObjectAttribute("oa3", pc1);
            String oa4 = pap.graph().createObjectAttribute("oa4", pc1);
            String oa2 = pap.graph().createObjectAttribute("oa2", oa3);
            String oa1 = pap.graph().createObjectAttribute("oa1", oa4);
            String o1 = pap.graph().createObject("o1", oa1, oa2);

            pap.graph().associate(ua1, oa3, new AccessRightSet("read", "write", "execute"));
            pap.prohibitions().create("deny", ProhibitionSubject.userAttribute("ua1"), new AccessRightSet("read"), true,
                                      new ContainerCondition(oa1, false),
                                      new ContainerCondition(oa2, false)
            );

            pap.prohibitions().create("deny2", ProhibitionSubject.user(u1), new AccessRightSet("write"),
                                      true,
                                      new ContainerCondition(oa3, false)
            );

            Set<String> list = accessReviewer.computePrivileges(new UserContext(u1), o1);
            assertEquals(1, list.size());
            assertTrue(list.contains("execute"));
        }

        @Test
        void testGraph24WithProhibitions() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);

            String pc1 = pap.graph().createPolicyClass("pc1");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String u1 = pap.graph().createUser("u1", ua1);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String oa2 = pap.graph().createObjectAttribute("oa2", pc1);
            String o1 = pap.graph().createObject("o1", oa1, oa2);
            String o2 = pap.graph().createObject("o2", oa2);

            pap.graph().associate(ua1, oa1, new AccessRightSet("read"));

            pap.prohibitions().create("deny", ProhibitionSubject.userAttribute(ua1),
                                      new AccessRightSet("read"),
                                      true,
                                      new ContainerCondition(oa1, false),
                                      new ContainerCondition(oa2, true)
            );

            assertTrue(accessReviewer.computePrivileges(new UserContext(u1), o1).contains("read"));
            assertTrue(accessReviewer.computePrivileges(new UserContext(u1), o2).isEmpty());

            pap.graph().associate(ua1, oa2, new AccessRightSet("read"));

            pap.prohibitions().create("deny-process", ProhibitionSubject.process("1234"),
                                      new AccessRightSet("read"),
                                      false,
                                      new ContainerCondition(oa1, false)
            );

            assertTrue(accessReviewer.computePrivileges(new UserContext(u1, "1234"), o1).isEmpty());
        }

        @Test
        void testGraph25WithProhibitions() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);

            String pc1 = pap.graph().createPolicyClass("pc1");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String u1 = pap.graph().createUser("u1", ua1);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String oa2 = pap.graph().createObjectAttribute("oa2", oa1);
            String oa3 = pap.graph().createObjectAttribute("oa3", oa1);
            String oa4 = pap.graph().createObjectAttribute("oa4", oa3);
            String oa5 = pap.graph().createObjectAttribute("oa5", oa2);
            String o1 = pap.graph().createObject("o1", oa4);

            pap.graph().associate(ua1, oa1, new AccessRightSet("read", "write"));

            pap.prohibitions().create("deny", ProhibitionSubject.user(u1), new AccessRightSet("read", "write"),
                                      true,
                                      new ContainerCondition(oa4, true),
                                      new ContainerCondition(oa1, false)
            );

            assertTrue(accessReviewer.computePrivileges(new UserContext(u1), oa5).isEmpty());
            assertTrue(
                    accessReviewer.computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }

        @Test
        void testGraph25WithProhibitions2() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);

            String pc1 = pap.graph().createPolicyClass("pc1");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String u1 = pap.graph().createUser("u1", ua1);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String oa2 = pap.graph().createObjectAttribute("oa2", pc1);
            String o1 = pap.graph().createObject("o1", oa1, oa2);

            pap.graph().associate(ua1, oa1, new AccessRightSet("read", "write"));


            pap.prohibitions().create("deny", ProhibitionSubject.user(u1), new AccessRightSet("read", "write"),
                                      true,
                                      new ContainerCondition(oa1, false),
                                      new ContainerCondition(oa2, false)
            );

            assertTrue(accessReviewer.computePrivileges(new UserContext(u1), o1).isEmpty());
        }

        @Test
        void testDeciderWithUA() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);

            String pc1 = pap.graph().createPolicyClass("pc1");
            String ua2 = pap.graph().createUserAttribute("ua2", pc1);
            String ua1 = pap.graph().createUserAttribute("ua1", ua2);
            String u1 = pap.graph().createUser("u1", ua1);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String oa2 = pap.graph().createObjectAttribute("oa2", pc1);
            String o1 = pap.graph().createObject("o1", oa1, oa2);
            String o2 = pap.graph().createObject("o2", oa2);

            pap.graph().associate(ua1, oa1, new AccessRightSet("read"));
            pap.graph().associate(ua2, oa1, new AccessRightSet("write"));

            assertTrue(accessReviewer.computePrivileges(new UserContext(ua1), oa1)
                                     .containsAll(Arrays.asList("read", "write")));
        }

        @Test
        void testProhibitionsAllCombinations() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);
            pap.graph().createPolicyClass("pc1");
            pap.graph().createObjectAttribute("oa1", "pc1");
            pap.graph().createObjectAttribute("oa2", "pc1");
            pap.graph().createObjectAttribute("oa3", "pc1");
            pap.graph().createObjectAttribute("oa4", "pc1");
            pap.graph().createObject("o1", "oa1", "oa2", "oa3");
            pap.graph().createObject("o2", "oa1", "oa4");

            pap.graph().createUserAttribute("ua1", "pc1");
            pap.graph().createUser("u1", "ua1");
            pap.graph().createUser("u2", "ua1");
            pap.graph().createUser("u3", "ua1");
            pap.graph().createUser("u4", "ua1");

            pap.graph().associate("ua1", "oa1", new AccessRightSet("read", "write"));


            pap.prohibitions().create(
                    "p1",
                    ProhibitionSubject.user("u1"),
                    new AccessRightSet("write"),
                    true,
                    new ContainerCondition("oa1", false),
                    new ContainerCondition("oa2", false),
                    new ContainerCondition("oa3", false)
            );

            pap.prohibitions().create(
                    "p2",
                    ProhibitionSubject.user("u2"),
                    new AccessRightSet("write"),
                    false,
                    new ContainerCondition("oa1", false),
                    new ContainerCondition("oa2", false),
                    new ContainerCondition("oa3", false)
            );

            pap.prohibitions().create(
                    "p3",
                    ProhibitionSubject.user("u3"),
                    new AccessRightSet("write"),
                    true,
                    new ContainerCondition("oa1", false),
                    new ContainerCondition("oa2", true)
            );

            pap.prohibitions().create(
                    "p4",
                    ProhibitionSubject.user("u4"),
                    new AccessRightSet("write"),
                    false,
                    new ContainerCondition("oa1", false),
                    new ContainerCondition("oa2", true)
            );

            pap.prohibitions().create(
                    "p5",
                    ProhibitionSubject.user("u4"),
                    new AccessRightSet("write"),
                    false,
                    new ContainerCondition("oa2", true)
            );

            Set<String> list = accessReviewer.computePrivileges(new UserContext("u1"), "o1");
            assertTrue(list.contains("read") && !list.contains("write"));

            list = accessReviewer.computePrivileges(new UserContext("u1"), "o2");
            assertTrue(list.contains("read") && list.contains("write"));

            list = accessReviewer.computePrivileges(new UserContext("u2"), "o2");
            assertTrue(list.contains("read") && !list.contains("write"));

            list = accessReviewer.computePrivileges(new UserContext("u3"), "o2");
            assertTrue(list.contains("read") && !list.contains("write"));

            list = accessReviewer.computePrivileges(new UserContext("u4"), "o1");
            assertTrue(list.contains("read") && !list.contains("write"));

            list = accessReviewer.computePrivileges(new UserContext("u4"), "o2");
            assertTrue(list.contains("read") && !list.contains("write"));
        }

        @Test
        void testPermissions() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);

            String pc1 = pap.graph().createPolicyClass("pc1");
            String ua1 = pap.graph().createUserAttribute("ua1", pc1);
            String u1 = pap.graph().createUser("u1", ua1);
            String oa1 = pap.graph().createObjectAttribute("oa1", pc1);
            String o1 = pap.graph().createObject("o1", oa1);

            pap.graph().associate(ua1, oa1, allAccessRights());

            Set<String> list = accessReviewer.computePrivileges(new UserContext("u1"), "o1");
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertTrue(list.containsAll(RWE));

            pap.graph().associate(ua1, oa1, allAdminAccessRights());
            list = accessReviewer.computePrivileges(new UserContext("u1"), "o1");
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertFalse(list.containsAll(RWE));

            pap.graph().associate(ua1, oa1, new AccessRightSet(ALL_RESOURCE_ACCESS_RIGHTS));
            list = accessReviewer.computePrivileges(new UserContext("u1"), "o1");
            assertFalse(list.containsAll(allAdminAccessRights()));
            assertTrue(list.containsAll(RWE));
        }

        @Test
        void testPermissionsInOnlyOnePC() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(RWE);
            pap.graph().createPolicyClass("pc1");
            pap.graph().createPolicyClass("pc2");
            pap.graph().createUserAttribute("ua3", "pc1");
            pap.graph().createUserAttribute("ua2", "ua3");
            pap.graph().createUserAttribute("u1", "ua2");

            pap.graph().createObjectAttribute("oa1", "pc1");
            pap.graph().createObjectAttribute("oa3", "pc2");
            pap.graph().assign("oa3", "oa1");
            pap.graph().createObject("o1", "oa3");

            pap.graph().associate("ua3", "oa1", new AccessRightSet("read"));

            assertTrue(accessReviewer.computePrivileges(new UserContext("u1"), "o1").isEmpty());
        }

        @Test
        void testProhibitionsWithContainerAsTarget() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(new AccessRightSet("read"));
            pap.graph().createPolicyClass("pc1");
            pap.graph().createUserAttribute("ua1", "pc1");
            pap.graph().createObjectAttribute("oa1", "pc1");
            pap.graph().createUser("u1", "ua1");
            pap.graph().associate("ua1", "oa1", new AccessRightSet("read"));

            pap.prohibitions().create("deny1", ProhibitionSubject.user("u1"), new AccessRightSet("read"), false,
                                      new ContainerCondition("oa1", false)
            );

            AccessRightSet deniedAccessRights = accessReviewer.computeDeniedPrivileges(new UserContext("u1"), "oa1");
            assertTrue(deniedAccessRights.contains("read"));
        }

        @Test
        void testProhibitionWithContainerAsTargetComplement() throws PMException {
            PAP pap = new PAP(new MemoryPolicyStore());
            AccessReviewer accessReviewer = new AccessReviewer(pap);

            pap.graph().setResourceAccessRights(new AccessRightSet("read"));
            pap.graph().createPolicyClass("pc1");
            pap.graph().createUserAttribute("ua1", "pc1");
            pap.graph().createObjectAttribute("oa1", "pc1");
            pap.graph().createUser("u1", "ua1");
            pap.graph().associate("ua1", "oa1", new AccessRightSet("read"));

            pap.prohibitions().create("deny1", ProhibitionSubject.user("u1"), new AccessRightSet("read"), false,
                                      new ContainerCondition("oa1", true)
            );

            AccessRightSet deniedAccessRights = accessReviewer.computeDeniedPrivileges(new UserContext("u1"), "oa1");
            assertFalse(deniedAccessRights.contains("read"));
        }
    }
}