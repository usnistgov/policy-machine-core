package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.admin.AdminPolicy;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAPTestInitializer;
import gov.nist.csd.pm.pap.prohibition.ContainerCondition;
import gov.nist.csd.pm.pap.prohibition.Prohibition;
import gov.nist.csd.pm.pap.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.query.model.explain.*;
import gov.nist.csd.pm.pap.query.model.subgraph.DescendantSubgraph;
import gov.nist.csd.pm.pap.query.model.subgraph.SubgraphPrivileges;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.*;
import static org.junit.jupiter.api.Assertions.*;

public abstract class AccessQuerierTest extends PAPTestInitializer {

    private static final AccessRightSet RWE = new AccessRightSet("read", "write", "execute");

    @Test
    void testComputeAdjacentAscendantPrivileges() throws PMException {
        String pml = """
                set resource operations ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["oa1"]
                associate "ua1" and "oa1" with ["read", "write"]
                
                create u "u1" in ["ua1"]
                create o "o1" in ["oa1"]
                create o "o2" in ["oa1"]
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        Map<String, AccessRightSet> actual = pap.query().access().computeAdjacentAscendantPrivileges(new UserContext("u1"), "oa1");
        assertEquals(
                Map.of("oa2", new AccessRightSet("read", "write"),
                        "o1", new AccessRightSet("read", "write"),
                        "o2", new AccessRightSet("read", "write")),
                new HashMap<>(actual)
        );
    }

    @Test
    void testComputeAdjacentDescendantPrivileges() throws PMException {
        String pml = """
                set resource operations ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["oa1"]
                create oa "oa3" in ["oa1"]
                
                associate "ua1" and "oa2" with ["read", "write"]
                
                create u "u1" in ["ua1"]
                create o "o1" in ["oa2", "oa3"]
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        Map<String, AccessRightSet> actual = pap.query().access().computeAdjacentDescendantPrivileges(new UserContext("u1"), "o1");
        assertEquals(
                Map.of("oa2", new AccessRightSet("read", "write"),
                        "oa3", new AccessRightSet()),
                new HashMap<>(actual)
        );
    }

    @Test
    void testBuildPOS() throws PMException {
        String pml = """
                set resource operations ["read", "write"]
                
                create pc "pc1"
                create oa "oa1" in ["pc1"]
                create oa "oa1-1" in ["oa1"]
                create oa "oa2" in ["pc1"]
                create oa "oa2-1" in ["oa2"]
                create oa "oa2-2" in ["oa2"]
                                
                create pc "pc2"
                create oa "oa3" in ["pc2"]
                create oa "oa3-1" in ["oa3"]
                
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                
                associate "ua1" and "oa2" with ["read", "write"]
                associate "ua1" and "oa2-1" with ["read", "write"]
                associate "ua1" and "oa3-1" with ["read", "write"]

                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        Map<String, AccessRightSet> actual = pap.query().access().computePersonalObjectSystem(new UserContext("u1"));
        assertEquals(
                Map.of("oa2", new AccessRightSet("read", "write"), "oa3-1", new AccessRightSet("read", "write")),
                actual
        );

        pap.modify().graph().dissociate("ua1", "oa2");

        actual = pap.query().access().computePersonalObjectSystem(new UserContext("u1"));
        assertEquals(
                Map.of("oa2-1", new AccessRightSet("read", "write"), "oa3-1", new AccessRightSet("read", "write")),
                actual
        );

        pap.modify().graph().associate("ua1", AdminPolicyNode.PM_ADMIN_OBJECT.nodeName(), new AccessRightSet("read"));

        actual = pap.query().access().computePersonalObjectSystem(new UserContext("u1"));
        assertEquals(
                Map.of("pc1", new AccessRightSet("read"), "pc2", new AccessRightSet("read"), "PM_ADMIN", new AccessRightSet("read")),
                actual
        );

        pml = """
                set resource operations ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["read", "write"]
                create pc "pc2"
                create ua "ua2" in ["pc2"]
                create oa "oa2" in ["pc2"]
                create oa "oa3" in ["oa2"]
                create oa "oa4" in ["pc2"]

                associate "ua2" and "oa2" with ["read", "write"]
                associate "ua2" and "oa4" with ["read"]               
                
                create u "u1" in ["ua1", "ua2"]
                create o "o1" in ["oa1", "oa3"]
                create o "o2" in ["oa4"]
                
                create prohibition "p1"
                deny user "u1" 
                access rights ["write"]
                on union of ["oa1"]
                """;
        pap.reset();
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());
        actual = pap.query().access().computePersonalObjectSystem(new UserContext("u1"));

        assertEquals(
                Map.of("oa1", new AccessRightSet("read"), "oa2", new AccessRightSet("read", "write"), "oa4", new AccessRightSet("read")),
                actual
        );
    }

    @Test
    void testExplain() throws PMException {
        String pml = """
                set resource operations ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["read", "write"]
                
                create pc "pc2"
                create ua "ua2" in ["pc2"]
                create oa "oa2" in ["pc2"]
                create oa "oa3" in ["oa2"]
                associate "ua2" and "oa2" with ["read", "write"]
                    
                create u "u1" in ["ua1", "ua2"]
                create o "o1" in ["oa1", "oa3"]
                
                create prohibition "p1"
                deny user "u1" 
                access rights ["write"]
                on union of ["oa1"]
                
                create prohibition "p2"
                deny user "u1" 
                access rights ["write"]
                on union of [!"oa1"]
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        Explain explain = pap.query().access().explain(new UserContext("u1"), "o1");
        Explain expected = new Explain(
                new AccessRightSet("read"),
                List.of(
                        new PolicyClassExplain(
                                "pc1",
                                new AccessRightSet("read", "write"),
                                List.of(
                                        List.of(
                                                new ExplainNode("o1", List.of()),
                                                new ExplainNode("oa1", List.of(
                                                        new ExplainAssociation("ua1", new AccessRightSet("read", "write"), List.of(new Path("u1", "ua1")))
                                                )),
                                                new ExplainNode("pc1", List.of())
                                        )
                                )),
                        new PolicyClassExplain("pc2",
                                new AccessRightSet("read", "write"),
                                List.of(
                                        List.of(
                                                new ExplainNode("o1", List.of()),
                                                new ExplainNode("oa3", List.of()),
                                                new ExplainNode("oa2", List.of(
                                                        new ExplainAssociation("ua2", new AccessRightSet("read", "write"), List.of(new Path("u1", "ua2")))
                                                )),
                                                new ExplainNode("pc2", List.of())
                                        )
                                ))
                ),
                new AccessRightSet("write"),
                List.of(
                        new Prohibition("p1", new ProhibitionSubject("u1", ProhibitionSubject.Type.USER), new AccessRightSet("write"), false, List.of(new ContainerCondition("oa1", false)))
                )
        );
        assertExplainEquals(expected, explain);
    }

    private void assertExplainEquals(Explain expected, Explain actual) {
        assertEquals(expected.getProhibitions(), actual.getProhibitions());
        assertEquals(expected.getDeniedPrivileges(), actual.getDeniedPrivileges());
        assertEquals(expected.getPrivileges(), actual.getPrivileges());

        List<PolicyClassExplain> expectedPolicyClasses = expected.getPolicyClasses();
        List<PolicyClassExplain> actualPolicyClasses = actual.getPolicyClasses();

        assertEquals(expectedPolicyClasses.size(), actualPolicyClasses.size());

        for (PolicyClassExplain expectedPolicyClass : expectedPolicyClasses) {
            assertTrue(actualPolicyClasses.contains(expectedPolicyClass), expectedPolicyClass.toString());
        }

        for (PolicyClassExplain actualPolicyClass : actualPolicyClasses) {
            assertTrue(expectedPolicyClasses.contains(actualPolicyClass), actualPolicyClass.toString());
        }
    }

    @Test
    void testExplain2() throws PMException {
        String pml = """
                set resource operations ["read", "write"]
                
                create pc "pc1"
                
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["ua1"]
                create ua "ua3" in ["ua1"]
                
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["oa1"]
                create oa "oa3" in ["oa1"]
                
                associate "ua2" and "oa2" with ["read"]
                associate "ua3" and "oa3" with ["read"]
                associate "ua1" and "oa1" with ["read"]
                
                create pc "pc2"
                create ua "ua4" in ["pc2"]
                create oa "oa4" in ["pc2"]
                associate "ua4" and "oa4" with ["read"]
                
                create user "u1" in ["ua2", "ua3", "ua4"]
                create object "o1" in ["oa2", "oa3", "oa4"]
                """;
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext(""), pml);

        Explain explain = pap.query().access().explain(new UserContext("u1"), "o1");
        Explain expected = new Explain(
                new AccessRightSet("read"),
                List.of(
                        new PolicyClassExplain(
                                "pc2",
                                new AccessRightSet("read"),
                                List.of(
                                        List.of(
                                                new ExplainNode("o1", List.of()),
                                                new ExplainNode("oa4", List.of(
                                                        new ExplainAssociation("ua4", new AccessRightSet("read"), List.of(new Path("u1", "ua4")))
                                                )),
                                                new ExplainNode("pc2", List.of())
                                        )
                                )
                        ),
                        new PolicyClassExplain(
                                "pc1",
                                new AccessRightSet("read"),
                                List.of(
                                        List.of(
                                                new ExplainNode("o1", List.of()),
                                                new ExplainNode("oa3", List.of(
                                                        new ExplainAssociation("ua3", new AccessRightSet("read"), List.of(new Path("u1", "ua3")))
                                                )),
                                                new ExplainNode("oa1", List.of(
                                                        new ExplainAssociation("ua1", new AccessRightSet("read"), List.of(new Path("u1", "ua2", "ua1"), new Path("u1", "ua3", "ua1")))
                                                )),
                                                new ExplainNode("pc1", List.of())
                                        ),
                                        List.of(
                                                new ExplainNode("o1", List.of()),
                                                new ExplainNode("oa2", List.of(
                                                        new ExplainAssociation("ua2", new AccessRightSet("read"), List.of(new Path("u1", "ua2")))
                                                )),
                                                new ExplainNode("oa1", List.of(
                                                        new ExplainAssociation("ua1", new AccessRightSet("read"), List.of(new Path("u1", "ua2", "ua1"), new Path("u1", "ua3", "ua1")))
                                                )),
                                                new ExplainNode("pc1", List.of())
                                        )
                                )
                        )
                )
        );
        assertExplainEquals(expected, explain);

        pap.modify().graph().deassign("o1", List.of("oa4"));

        explain = pap.query().access().explain(new UserContext("u1"), "o1");
        expected = new Explain(
                new AccessRightSet("read"),
                List.of(
                        new PolicyClassExplain(
                                "pc1",
                                new AccessRightSet("read"),
                                List.of(
                                        List.of(
                                                new ExplainNode("o1", List.of()),
                                                new ExplainNode("oa3", List.of(
                                                        new ExplainAssociation("ua3", new AccessRightSet("read"), List.of(new Path("u1", "ua3")))
                                                )),
                                                new ExplainNode("oa1", List.of(
                                                        new ExplainAssociation("ua1", new AccessRightSet("read"), List.of(new Path("u1", "ua2", "ua1"), new Path("u1", "ua3", "ua1")))
                                                )),
                                                new ExplainNode("pc1", List.of())
                                        ),
                                        List.of(
                                                new ExplainNode("o1", List.of()),
                                                new ExplainNode("oa2", List.of(
                                                        new ExplainAssociation("ua2", new AccessRightSet("read"), List.of(new Path("u1", "ua2")))
                                                )),
                                                new ExplainNode("oa1", List.of(
                                                        new ExplainAssociation("ua1", new AccessRightSet("read"), List.of(new Path("u1", "ua2", "ua1"), new Path("u1", "ua3", "ua1")))
                                                )),
                                                new ExplainNode("pc1", List.of())
                                        )
                                )
                        )
                )
        );
        assertExplainEquals(expected, explain);
    }

    @Test
    void testExplainOnObjAttrWithAssociation() throws PMException {
        String pml = """
                set resource operations ["read", "write"]
                create pc "pc1"
                    create UA "ua1" in ["pc1"]
                    create oa "oa1" in ["pc1"]
                    create oa "oa2" in ["oa1"]

                    associate "ua1" and "oa1" with ["write"]
                    associate "ua1" and "oa2" with ["read"]
               
                create user "u1" in ["ua1"]
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());
        Explain actual = pap.query().access().explain(new UserContext("u1"), "oa2");
        assertExplainEquals(
                new Explain(
                        new AccessRightSet("read", "write"),
                        List.of(
                                new PolicyClassExplain(
                                        "pc1",
                                        new AccessRightSet("read", "write"),
                                        List.of(
                                                List.of(
                                                        new ExplainNode("oa2", List.of(
                                                                new ExplainAssociation("ua1", new AccessRightSet("read"), List.of(new Path("u1", "ua1")))
                                                        )),
                                                        new ExplainNode("oa1", List.of(
                                                                new ExplainAssociation("ua1", new AccessRightSet("write"), List.of(new Path("u1", "ua1")))
                                                        )),
                                                        new ExplainNode("pc1", List.of())
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
    void testExplainNoPaths() throws PMException {
        String pml = """
                set resource operations ["read", "write"]
                create pc "pc1"
                    create UA "ua1" in ["pc1"]
                    create UA "ua2" in ["pc1"]
                    create oa "oa1" in ["pc1"]
                    create oa "oa2" in ["oa1"]

                    associate "ua1" and "oa1" with ["write"]
               
                create user "u1" in ["ua2"]
                create object "o1" in ["oa1"]
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());
        Explain actual = pap.query().access().explain(new UserContext("u1"), "o1");
        assertExplainEquals(
                new Explain(
                        new AccessRightSet(),
                        List.of(
                                new PolicyClassExplain(
                                        "pc1",
                                        new AccessRightSet(),
                                        List.of(
                                                List.of(
                                                        new ExplainNode("o1", List.of()),
                                                        new ExplainNode("oa1", List.of(
                                                                new ExplainAssociation("ua1", new AccessRightSet("write"), List.of())
                                                        )),
                                                        new ExplainNode("pc1", List.of())
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
    void testExplainPathsButNoPrivileges() throws PMException {
        String pml = """
                set resource operations ["read", "write"]
                create pc "pc1"
                    create UA "ua1" in ["pc1"]
                    create oa "oa1" in ["pc1"]

                    associate "ua1" and "oa1" with ["write"]
                    
                create pc "pc2"
                    create UA "ua2" in ["pc2"]
                    create OA "oa2" in ["pc2"]
                    
                    associate "ua2" and "oa2" with ["read"]
               
                create user "u1" in ["ua1", "ua2"]
                create object "o1" in ["oa1", "oa2"]
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());
        Explain actual = pap.query().access().explain(new UserContext("u1"), "o1");
        assertExplainEquals(
                new Explain(
                        new AccessRightSet(),
                        List.of(
                                new PolicyClassExplain(
                                        "pc1",
                                        new AccessRightSet("write"),
                                        List.of(
                                                List.of(
                                                        new ExplainNode("o1", List.of()),
                                                        new ExplainNode("oa1", List.of(
                                                                new ExplainAssociation("ua1", new AccessRightSet("write"), List.of(new Path("u1", "ua1")))
                                                        )),
                                                        new ExplainNode("pc1", List.of())
                                                )
                                        )
                                ),
                                new PolicyClassExplain(
                                        "pc2",
                                        new AccessRightSet("read"),
                                        List.of(
                                                List.of(
                                                        new ExplainNode("o1", List.of()),
                                                        new ExplainNode("oa2", List.of(
                                                                new ExplainAssociation("ua2", new AccessRightSet("read"), List.of(new Path("u1", "ua2")))
                                                        )),
                                                        new ExplainNode("pc2", List.of())
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
    void testExplainMultiplePolicyClasses() throws PMException {
        // test all pcs have same access rights
        String pml = """
                set resource operations ["read", "write"]
                create pc "pc1"
                    create UA "ua1" in ["pc1"]
                    create oa "oa1" in ["pc1"]

                    associate "ua1" and "oa1" with ["read", "write"]
                    
                create pc "pc2"
                    create UA "ua2" in ["pc2"]
                    create OA "oa2" in ["pc2"]
                    
                    associate "ua2" and "oa2" with ["read", "write"]
               
                create user "u1" in ["ua1", "ua2"]
                create object "o1" in ["oa1", "oa2"]
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());
        Explain actual = pap.query().access().explain(new UserContext("u1"), "o1");
        assertExplainEquals(
                new Explain(
                        new AccessRightSet("read", "write"),
                        List.of(
                                new PolicyClassExplain(
                                        "pc1",
                                        new AccessRightSet("read", "write"),
                                        List.of(
                                                List.of(
                                                        new ExplainNode("o1", List.of()),
                                                        new ExplainNode("oa1", List.of(
                                                                new ExplainAssociation("ua1", new AccessRightSet("read", "write"), List.of(new Path("u1", "ua1")))
                                                        )),
                                                        new ExplainNode("pc1", List.of())
                                                )
                                        )
                                ),
                                new PolicyClassExplain(
                                        "pc2",
                                        new AccessRightSet("read", "write"),
                                        List.of(
                                                List.of(
                                                        new ExplainNode("o1", List.of()),
                                                        new ExplainNode("oa2", List.of(
                                                                new ExplainAssociation("ua2", new AccessRightSet("read", "write"), List.of(new Path("u1", "ua2")))
                                                        )),
                                                        new ExplainNode("pc2", List.of())
                                                )
                                        )
                                )
                        ),
                        new AccessRightSet(),
                        List.of()
                ),
                actual
        );

        // test all pcs do not have same ars
        pml = """
                set resource operations ["read", "write"]
                create pc "pc1"
                    create UA "ua1" in ["pc1"]
                    create oa "oa1" in ["pc1"]

                    associate "ua1" and "oa1" with ["read", "write"]
                    
                create pc "pc2"
                    create UA "ua2" in ["pc2"]
                    create OA "oa2" in ["pc2"]
                    
                    associate "ua2" and "oa2" with ["read"]
               
                create user "u1" in ["ua1", "ua2"]
                create object "o1" in ["oa1", "oa2"]
                """;
        pap.reset();
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());
        actual = pap.query().access().explain(new UserContext("u1"), "o1");
        assertExplainEquals(
                new Explain(
                        new AccessRightSet("read"),
                        List.of(
                                new PolicyClassExplain(
                                        "pc1",
                                        new AccessRightSet("read", "write"),
                                        List.of(
                                                List.of(
                                                        new ExplainNode("o1", List.of()),
                                                        new ExplainNode("oa1", List.of(
                                                                new ExplainAssociation("ua1", new AccessRightSet("read", "write"), List.of(new Path("u1", "ua1")))
                                                        )),
                                                        new ExplainNode("pc1", List.of())
                                                )
                                        )
                                ),
                                new PolicyClassExplain(
                                        "pc2",
                                        new AccessRightSet("read"),
                                        List.of(
                                                List.of(
                                                        new ExplainNode("o1", List.of()),
                                                        new ExplainNode("oa2", List.of(
                                                                new ExplainAssociation("ua2", new AccessRightSet("read"), List.of(new Path("u1", "ua2")))
                                                        )),
                                                        new ExplainNode("pc2", List.of())
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
                set resource operations ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["oa1"]

                associate "ua1" and "oa1" with ["read", "write"]
               
                
                create u "u1" in ["ua1"]
                create o "o1" in ["oa2"]
                create o "o2" in ["oa2"]
                
                create prohibition "p1"
                deny user "u1"
                access rights ["write"]
                on union of ["o1"]
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());
        SubgraphPrivileges actual = pap.query().access().computeSubgraphPrivileges(new UserContext("u1"), "oa1");
        assertSubgraphPrivilegesEquals(new SubgraphPrivileges(
                "oa1", new AccessRightSet("read", "write"), List.of(
                new SubgraphPrivileges("oa2", new AccessRightSet("read", "write"), List.of(
                        new SubgraphPrivileges("o1", new AccessRightSet("read"), List.of()),
                        new SubgraphPrivileges("o2", new AccessRightSet("read", "write"), List.of())
                ))
        )
        ), actual);
    }

    private boolean assertSubgraphPrivilegesEquals(SubgraphPrivileges expected, SubgraphPrivileges actual) {
        if (!expected.name().equals(actual.name())) {
            return false;
        }

        int ok = 0;
        for (SubgraphPrivileges expectedSubgraph : expected.ascendants()) {
            for (SubgraphPrivileges actualSubgraph : actual.ascendants()) {
                if (assertSubgraphPrivilegesEquals(expectedSubgraph, actualSubgraph)) {
                    ok++;
                }
            }
        }

        return ok == expected.ascendants().size();
    }

    @Test
    void testFindBorderAttributes() throws PMException {
        String pml = """
                set resource operations ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                   
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["oa1"]
                associate "ua1" and "oa1" with ["read", "write"]
                associate "ua2" and "oa2" with ["read"]
                
                create u "u1" in ["ua1", "ua2"]
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());
        Map<String, AccessRightSet> u1 = pap.query().access().computeDestinationAttributes("u1");
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
                set resource operations ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["read", "write"]
                associate "ua2" and "oa1" with ["read"]
                
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                create o "o1" in ["oa1"]
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());
        Map<String, AccessRightSet> o1 = pap.query().access().computeACL("o1");
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
                set resource operations ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                associate "ua1" and "oa1" with ["read", "write"]
                associate "ua1" and "oa2" with ["read"]
                
                create u "u1" in ["ua1"]
                create o "o1" in ["oa1"]
                create o "o2" in ["oa2"]
                
                create prohibition "p1"
                deny user "u1" 
                access rights ["write"]
                on union of ["oa1"]
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());
        Map<String, AccessRightSet> u1 = pap.query().access().computeCapabilityList(new UserContext("u1"));
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
                set resource operations ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["read", "write"]
                
                create u "u1" in ["ua1"]
                create o "o1" in ["oa1"]
                
                create prohibition "p1"
                deny user "u1" 
                access rights ["write"]
                on union of ["oa1"]
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());
        AccessRightSet deniedPrivileges = pap.query().access().computeDeniedPrivileges(new UserContext("u1"), "o1");
        assertEquals(new AccessRightSet("write"), deniedPrivileges);
    }

    @Test
    void testComputePolicyClassAccessRights() throws PMException {
        String pml = """
                set resource operations ["read", "write"]
                create pc "pc1"
                create UA "ua1" in ["pc1"]
                create OA "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["read", "write"]

                create pc "pc2"
                create UA "ua2" in ["pc2"]
                create OA "oa2" in ["pc2"]
                associate "ua2" and "oa2" with ["read"]
                
                create u "u1" in ["ua1", "ua2"]
                create o "o1" in ["oa1", "oa2"]
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());
        Map<String, AccessRightSet> policyClassAccessRights =
                pap.query().access().computePolicyClassAccessRights(new UserContext("u1"), "o1");
        assertEquals(
                Map.of(
                        "pc1", new AccessRightSet("read", "write"),
                        "pc2", new AccessRightSet("read")
                ),
                policyClassAccessRights
        );
    }

    @Test
    void testGetAccessibleNodes() throws PMException {
        pap.modify().operations().setResourceOperations(RWE);

        String pc1 = pap.modify().graph().createPolicyClass("pc1");
        String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        String u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        String o1 = pap.modify().graph().createObject("o1", List.of(oa1));
        String o2 = pap.modify().graph().createObject("o2", List.of(oa1));
        String o3 = pap.modify().graph().createObject("o3", List.of(oa1));

        AccessRightSet arset = new AccessRightSet("read", "write");
        pap.modify().graph().associate(ua1, oa1, arset);
        Map<String, AccessRightSet> accessibleNodes = pap.query().access().computeCapabilityList(new UserContext(u1));

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
            pap.modify().operations().setResourceOperations(RWE);

            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read", "write"));
            assertTrue(
                    pap.query().access().computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }

        @Test
        void testGraph2() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);

            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String pc2 = pap.modify().graph().createPolicyClass("pc2");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1, pc2));
            String ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));

            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc2));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));


            assertTrue(pap.query().access().computePrivileges(new UserContext(u1), o1).isEmpty());
        }

        @Test
        void testGraph3() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);
            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read", "write"));


            assertTrue(
                    pap.query().access().computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }

        @Test
        void testGraph4() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);
            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            String ua2 = pap.modify().graph().createUserAttribute("ua2", List.of("pc1"));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
            pap.modify().graph().associate(ua2, oa1, new AccessRightSet("write"));


            assertEquals(
                    new AccessRightSet("read", "write"),
                    pap.query().access().computePrivileges(new UserContext(u1), o1)
            );
        }

        @Test
        void testGraph5() throws PMException {            pap.modify().operations().setResourceOperations(RWE);

            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String pc2 = pap.modify().graph().createPolicyClass("pc2");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc2));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc2));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
            pap.modify().graph().associate(ua2, oa2, new AccessRightSet("read", "write"));



            assertTrue(pap.query().access().computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read")));
        }

        @Test
        void testGraph6() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);
            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String pc2 = pap.modify().graph().createPolicyClass("pc2");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc2));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc2));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read", "write"));
            pap.modify().graph().associate(ua2, oa2, new AccessRightSet("read"));



            assertTrue(pap.query().access().computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read")));
        }

        @Test
        void testGraph7() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);

            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String pc2 = pap.modify().graph().createPolicyClass("pc2");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc2));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read", "write"));



            assertTrue(pap.query().access().computePrivileges(new UserContext(u1), o1).isEmpty());
        }

        @Test
        void testGraph8() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);
            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("*"));



            Set<String> list = pap.query().access().computePrivileges(new UserContext(u1), o1);
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertTrue(list.containsAll(RWE));
        }

        @Test
        void testGraph9() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);
            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("*"));
            pap.modify().graph().associate(ua2, oa1, new AccessRightSet("read", "write"));



            Set<String> list = pap.query().access().computePrivileges(new UserContext(u1), o1);
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertTrue(list.containsAll(RWE));
        }

        @Test
        void testGraph10() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);
            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String pc2 = pap.modify().graph().createPolicyClass("pc2");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc2));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc2));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("*"));
            pap.modify().graph().associate(ua2, oa2, new AccessRightSet("read", "write"));



            assertTrue(
                    pap.query().access().computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }

        @Test
        void testGraph11() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);
            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String pc2 = pap.modify().graph().createPolicyClass("pc2");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc2));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("*"));



            assertEquals(new AccessRightSet(), pap.query().access().computePrivileges(new UserContext(u1), o1));
        }

        @Test
        void testGraph12() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);
            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
            pap.modify().graph().associate(ua2, oa1, new AccessRightSet("write"));



            assertTrue(
                    pap.query().access().computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }

        @Test
        void testGraph13() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);
            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(ua2));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1));
            String oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(oa2));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("*"));
            pap.modify().graph().associate(ua2, oa2, new AccessRightSet("read"));



            Set<String> list = pap.query().access().computePrivileges(new UserContext(u1), o1);
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertTrue(list.contains("read"));
        }

        @Test
        void testGraph14() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);
            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String pc2 = pap.modify().graph().createPolicyClass("pc2");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1, pc2));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("*"));
            pap.modify().graph().associate(ua2, oa1, new AccessRightSet("*"));



            Set<String> list = pap.query().access().computePrivileges(new UserContext(u1), o1);
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertTrue(list.containsAll(RWE));
        }

        @Test
        void testGraph15() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);
            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(ua2));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1));
            String oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(oa2));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("*"));
            pap.modify().graph().associate(ua2, oa2, new AccessRightSet("read"));



            Set<String> list = pap.query().access().computePrivileges(new UserContext(u1), o1);
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertTrue(list.containsAll(RWE));
        }

        @Test
        void testGraph16() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);
            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(ua2));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
            pap.modify().graph().associate(ua2, oa1, new AccessRightSet("write"));



            assertTrue(
                    pap.query().access().computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }

        // removed graph7 due to adding the descendant IDs to the createNode, need to always connect to the testCtx.policy().graph().

        @Test
        void testGraph18() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);
            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa2));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read", "write"));



            assertTrue(pap.query().access().computePrivileges(new UserContext(u1), o1).isEmpty());
        }

        @Test
        void testGraph19() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);
            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua2));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));



            assertTrue(pap.query().access().computePrivileges(new UserContext(u1), o1).isEmpty());
        }

        @Test
        void testGraph20() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);
            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String pc2 = pap.modify().graph().createPolicyClass("pc2");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc2));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
            pap.modify().graph().associate(ua2, oa2, new AccessRightSet("read", "write"));



            assertTrue(pap.query().access().computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read")));
        }

        @Test
        void testGraph21() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);
            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String pc2 = pap.modify().graph().createPolicyClass("pc2");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc2));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
            pap.modify().graph().associate(ua2, oa2, new AccessRightSet("write"));



            assertTrue(pap.query().access().computePrivileges(new UserContext(u1), o1).isEmpty());
        }

        @Test
        void testGraph22() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);
            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String pc2 = pap.modify().graph().createPolicyClass("pc2");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read", "write"));



            assertTrue(
                    pap.query().access().computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }

        @Test
        void testGraph23WithProhibitions() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);

            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1));
            String oa3 = pap.modify().graph().createObjectAttribute("oa3", List.of(pc1));
            String oa4 = pap.modify().graph().createObjectAttribute("oa4", List.of(pc1));
            String oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(oa3));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(oa4));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

            pap.modify().graph().associate(ua1, oa3, new AccessRightSet("read", "write", "execute"));
            pap.modify().prohibitions().createProhibition("deny", ProhibitionSubject.userAttribute("ua1"), new AccessRightSet("read"), true,
                    List.of(new ContainerCondition(oa1, false),
                            new ContainerCondition(oa2, false))
            );

            pap.modify().prohibitions().createProhibition("deny2", ProhibitionSubject.user(u1), new AccessRightSet("write"),
                    true,
                    Collections.singleton(new ContainerCondition(oa3, false))
            );


            Set<String> list = pap.query().access().computePrivileges(new UserContext(u1), o1);
            assertEquals(1, list.size());
            assertTrue(list.contains("execute"));
        }

        @Test
        void testGraph24WithProhibitions() throws PMException {            pap.modify().operations().setResourceOperations(RWE);

            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));
            String o2 = pap.modify().graph().createObject("o2", List.of(oa2));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));

            pap.modify().prohibitions().createProhibition("deny", ProhibitionSubject.userAttribute(ua1),
                    new AccessRightSet("read"),
                    true,
                    List.of(
                            new ContainerCondition(oa1, false),
                            new ContainerCondition(oa2, true)
                    )
            );


            assertTrue(pap.query().access().computePrivileges(new UserContext(u1), o1).contains("read"));
            assertTrue(pap.query().access().computePrivileges(new UserContext(u1), o2).isEmpty());

            pap.modify().graph().associate(ua1, oa2, new AccessRightSet("read"));

            pap.modify().prohibitions().createProhibition("deny-process", ProhibitionSubject.process("1234"),
                    new AccessRightSet("read"),
                    false,
                    Collections.singleton(new ContainerCondition(oa1, false))
            );

            assertEquals(
                    new AccessRightSet(),
                    pap.query().access().computePrivileges(new UserContext(u1, "1234"), o1)
            );
        }

        @Test
        void testGraph25WithProhibitions() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);

            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(oa1));
            String oa3 = pap.modify().graph().createObjectAttribute("oa3", List.of(oa1));
            String oa4 = pap.modify().graph().createObjectAttribute("oa4", List.of(oa3));
            String oa5 = pap.modify().graph().createObjectAttribute("oa5", List.of(oa2));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa4));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read", "write"));

            pap.modify().prohibitions().createProhibition("deny", ProhibitionSubject.user(u1), new AccessRightSet("read", "write"),
                    true,
                    List.of(new ContainerCondition(oa4, true),
                            new ContainerCondition(oa1, false))
            );


            assertTrue(pap.query().access().computePrivileges(new UserContext(u1), oa5).isEmpty());
            assertTrue(
                    pap.query().access().computePrivileges(new UserContext(u1), o1).containsAll(Arrays.asList("read", "write")));
        }

        @Test
        void testGraph25WithProhibitions2() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);

            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read", "write"));


            pap.modify().prohibitions().createProhibition("deny", ProhibitionSubject.user(u1), new AccessRightSet("read", "write"),
                    true,
                    List.of(new ContainerCondition(oa1, false),
                            new ContainerCondition(oa2, false))
            );


            assertTrue(pap.query().access().computePrivileges(new UserContext(u1), o1).isEmpty());
        }

        @Test
        void testDeciderWithUA() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);

            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(ua2));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));
            String o2 = pap.modify().graph().createObject("o2", List.of(oa2));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
            pap.modify().graph().associate(ua2, oa1, new AccessRightSet("write"));


            assertTrue(pap.query().access().computePrivileges(new UserContext(ua1), oa1)
                    .containsAll(Arrays.asList("read", "write")));
        }

        @Test
        void testProhibitionsAllCombinations() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa3", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa4", List.of("pc1"));
            pap.modify().graph().createObject("o1", List.of("oa1", "oa2", "oa3"));
            pap.modify().graph().createObject("o2", List.of("oa1", "oa4"));

            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createUser("u1", List.of("ua1"));
            pap.modify().graph().createUser("u2", List.of("ua1"));
            pap.modify().graph().createUser("u3", List.of("ua1"));
            pap.modify().graph().createUser("u4", List.of("ua1"));

            pap.modify().graph().associate("ua1", "oa1", new AccessRightSet("read", "write"));


            pap.modify().prohibitions().createProhibition(
                    "p1",
                    ProhibitionSubject.user("u1"),
                    new AccessRightSet("write"),
                    true,
                    List.of(
                            new ContainerCondition("oa1", false),
                            new ContainerCondition("oa2", false),
                            new ContainerCondition("oa3", false)
                    )
            );

            pap.modify().prohibitions().createProhibition(
                    "p2",
                    ProhibitionSubject.user("u2"),
                    new AccessRightSet("write"),
                    false,
                    List.of(
                            new ContainerCondition("oa1", false),
                            new ContainerCondition("oa2", false),
                            new ContainerCondition("oa3", false)
                    )
            );

            pap.modify().prohibitions().createProhibition(
                    "p3",
                    ProhibitionSubject.user("u3"),
                    new AccessRightSet("write"),
                    true,
                    List.of(
                            new ContainerCondition("oa1", false),
                            new ContainerCondition("oa2", true)
                    )
            );

            pap.modify().prohibitions().createProhibition(
                    "p4",
                    ProhibitionSubject.user("u4"),
                    new AccessRightSet("write"),
                    false,
                    List.of(
                            new ContainerCondition("oa1", false),
                            new ContainerCondition("oa2", true)
                    )
            );

            pap.modify().prohibitions().createProhibition(
                    "p5",
                    ProhibitionSubject.user("u4"),
                    new AccessRightSet("write"),
                    false,
                    Collections.singleton(new ContainerCondition("oa2", true))
            );


            Set<String> list = pap.query().access().computePrivileges(new UserContext("u1"), "o1");
            assertTrue(list.contains("read") && !list.contains("write"));

            list = pap.query().access().computePrivileges(new UserContext("u1"), "o2");
            assertTrue(list.contains("read") && list.contains("write"));

            list = pap.query().access().computePrivileges(new UserContext("u2"), "o2");
            assertTrue(list.contains("read") && !list.contains("write"));

            list = pap.query().access().computePrivileges(new UserContext("u3"), "o2");
            assertTrue(list.contains("read") && !list.contains("write"));

            list = pap.query().access().computePrivileges(new UserContext("u4"), "o1");
            assertTrue(list.contains("read") && !list.contains("write"));

            list = pap.query().access().computePrivileges(new UserContext("u4"), "o2");
            assertTrue(list.contains("read") && !list.contains("write"));
        }

        @Test
        void testPermissions() throws PMException {            pap.modify().operations().setResourceOperations(RWE);

            String pc1 = pap.modify().graph().createPolicyClass("pc1");
            String ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            String u1 = pap.modify().graph().createUser("u1", List.of(ua1));
            String oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            String o1 = pap.modify().graph().createObject("o1", List.of(oa1));

            pap.modify().graph().associate(ua1, oa1, allAccessRights());


            Set<String> list = pap.query().access().computePrivileges(new UserContext("u1"), "o1");
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertTrue(list.containsAll(RWE));

            pap.modify().graph().associate(ua1, oa1, allAdminAccessRights());
            list = pap.query().access().computePrivileges(new UserContext("u1"), "o1");
            assertTrue(list.containsAll(allAdminAccessRights()));
            assertFalse(list.containsAll(RWE));

            pap.modify().graph().associate(ua1, oa1, new AccessRightSet(ALL_RESOURCE_ACCESS_RIGHTS));
            list = pap.query().access().computePrivileges(new UserContext("u1"), "o1");
            assertFalse(list.containsAll(allAdminAccessRights()));
            assertTrue(list.containsAll(RWE));
        }

        @Test
        void testPermissionsInOnlyOnePC() throws PMException {
            pap.modify().operations().setResourceOperations(RWE);
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createPolicyClass("pc2");
            pap.modify().graph().createUserAttribute("ua3", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua2", List.of("ua3"));
            pap.modify().graph().createUserAttribute("u1", List.of("ua2"));

            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa3", List.of("pc2"));
            pap.modify().graph().assign("oa3", List.of("oa1"));
            pap.modify().graph().createObject("o1", List.of("oa3"));

            pap.modify().graph().associate("ua3", "oa1", new AccessRightSet("read"));


            assertTrue(pap.query().access().computePrivileges(new UserContext("u1"), "o1").isEmpty());
        }

        @Test
        void testProhibitionsWithContainerAsTarget() throws PMException {
            pap.modify().operations().setResourceOperations(new AccessRightSet("read"));
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createUser("u1", List.of("ua1"));
            pap.modify().graph().associate("ua1", "oa1", new AccessRightSet("read"));

            pap.modify().prohibitions().createProhibition("deny1", ProhibitionSubject.user("u1"), new AccessRightSet("read"), false,
                    Collections.singleton(new ContainerCondition("oa1", false))
            );


            AccessRightSet deniedAccessRights = pap.query().access().computeDeniedPrivileges(new UserContext("u1"), "oa1");
            assertTrue(deniedAccessRights.contains("read"));
        }

        @Test
        void testProhibitionWithContainerAsTargetComplement() throws PMException {
            pap.modify().operations().setResourceOperations(new AccessRightSet("read"));
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createUser("u1", List.of("ua1"));
            pap.modify().graph().associate("ua1", "oa1", new AccessRightSet("read"));

            pap.modify().prohibitions().createProhibition("deny1", ProhibitionSubject.user("u1"), new AccessRightSet("read"), false,
                    Collections.singleton(new ContainerCondition("oa1", true))
            );


            AccessRightSet deniedAccessRights = pap.query().access().computeDeniedPrivileges(new UserContext("u1"), "oa1");
            assertFalse(deniedAccessRights.contains("read"));
        }

        @Test
        void testAssociationWithObject() throws PMException {
            pap.modify().operations().setResourceOperations(new AccessRightSet("read"));
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createObject("o1", List.of("pc1"));
            pap.modify().graph().createUser("u1", List.of("ua1"));
            pap.modify().graph().associate("ua1", "o1", new AccessRightSet("read"));

            AccessRightSet accessRightSet = pap.query().access().computePrivileges(new UserContext("u1"), "o1");
            assertEquals(new AccessRightSet("read"), accessRightSet);
        }

        @Test
        void testAssociationWithObjectAndProhibitions() throws PMException {
            pap.modify().operations().setResourceOperations(new AccessRightSet("read"));
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createObject("o1", List.of("pc1"));
            pap.modify().graph().createUser("u1", List.of("ua1"));
            pap.modify().graph().associate("ua1", "o1", new AccessRightSet("read"));

            pap.modify().prohibitions().createProhibition("deny1", ProhibitionSubject.user("u1"), new AccessRightSet("read"), false,
                    Collections.singleton(new ContainerCondition("o1",  false))
            );


            AccessRightSet accessRightSet = pap.query().access().computePrivileges(new UserContext("u1"), "o1");
            assertEquals(new AccessRightSet(), accessRightSet);
        }
    }
}
