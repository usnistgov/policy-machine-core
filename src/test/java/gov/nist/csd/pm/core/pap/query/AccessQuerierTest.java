package gov.nist.csd.pm.core.pap.query;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ALL_ADMIN_ACCESS_RIGHTS_SET;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.WC_ADMIN;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.WC_ALL;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.WC_RESOURCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.pap.PAPTestInitializer;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.query.model.explain.Explain;
import gov.nist.csd.pm.core.pap.query.model.explain.ExplainAssociation;
import gov.nist.csd.pm.core.pap.query.model.explain.ExplainNode;
import gov.nist.csd.pm.core.pap.query.model.explain.Path;
import gov.nist.csd.pm.core.pap.query.model.explain.PolicyClassExplain;
import gov.nist.csd.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
import gov.nist.csd.pm.core.util.TestUserContext;
import it.unimi.dsi.fastutil.longs.LongList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

public abstract class AccessQuerierTest extends PAPTestInitializer {

    public static final AccessRightSet RWE = new AccessRightSet("read", "write", "execute");

    @Test
    void testComputeAdjacentAscendantPrivileges() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["oa1"]
                associate "ua1" and "oa1" with ["read", "write"]
                
                create u "u1" in ["ua1"]
                create o "o1" in ["oa1"]
                create o "o2" in ["oa1"]
                """;
        pap.executePML(new TestUserContext("u1"), pml);

        Map<Node, AccessRightSet> actual = pap.query().access().computeAdjacentAscendantPrivileges(new UserContext(id("u1")), id("oa1"));
        assertEquals(
            Map.of(node("oa2"), new AccessRightSet("read", "write"),
                node("o1"), new AccessRightSet("read", "write"),
                node("o2"), new AccessRightSet("read", "write")),
            new HashMap<>(actual)
        );
    }

    @Test
    void testComputeAdjacentDescendantPrivileges() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["oa1"]
                create oa "oa3" in ["oa1"]
                
                associate "ua1" and "oa2" with ["read", "write"]
                
                create u "u1" in ["ua1"]
                create o "o1" in ["oa2", "oa3"]
                """;
        pap.executePML(new TestUserContext("u1"), pml);

        Map<Node, AccessRightSet> actual = pap.query().access().computeAdjacentDescendantPrivileges(new UserContext(id("u1")), id("o1"));
        assertEquals(
            Map.of(
                node("oa2"), new AccessRightSet("read", "write"),
                node("oa3"), new AccessRightSet()
            ),
            new HashMap<>(actual)
        );
    }

    @Test
    void testBuildPOS() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
                
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
        pap.executePML(new TestUserContext("u1"), pml);

        Map<Node, AccessRightSet> actual = pap.query().access().computePersonalObjectSystem(new UserContext(id("u1")));
        assertEquals(
            Map.of(node("oa2"), new AccessRightSet("read", "write"), node("oa3-1"), new AccessRightSet("read", "write")),
            actual
        );

        pap.modify().graph().dissociate(id("ua1"), id("oa2"));

        actual = pap.query().access().computePersonalObjectSystem(new UserContext(id("u1")));
        assertEquals(
            Map.of(node("oa2-1"), new AccessRightSet("read", "write"), node("oa3-1"), new AccessRightSet("read", "write")),
            actual
        );

        pap.modify().graph().associate(id("ua1"), AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet("read"));

        actual = pap.query().access().computePersonalObjectSystem(new UserContext(id("u1")));
        assertEquals(
            Map.of(
                node("pc1"), new AccessRightSet("read"),
                node("pc2"), new AccessRightSet("read"),
                node("PM_ADMIN"), new AccessRightSet("read")
            ),
            actual
        );

        pml = """
                set resource access rights ["read", "write"]
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
                deny U "u1"
                access rights ["write"]
                on union of {"oa1": false}
                """;
        pap.reset();
        pap.executePML(new TestUserContext("u1"), pml);
        actual = pap.query().access().computePersonalObjectSystem(new UserContext(id("u1")));

        assertEquals(
            Map.of(node("oa1"), new AccessRightSet("read"),
                node("oa2"), new AccessRightSet("read", "write"),
                node("oa4"), new AccessRightSet("read")),
            actual
        );
    }

    @Test
    void testExplain() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
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
                deny U "u1" 
                access rights ["write"]
                on union of {"oa1": false}
                
                create prohibition "p2"
                deny U "u1" 
                access rights ["write"]
                on union of {"oa1": true}
                """;
        pap.executePML(new TestUserContext("u1"), pml);

        Explain actual = pap.query().access().explain(new UserContext(id("u1")), new TargetContext(id("o1")));
        Explain expected = new Explain(
            new AccessRightSet("read"),
            List.of(
                new PolicyClassExplain(
                    node("pc1"),
                    new AccessRightSet("read", "write"),
                    List.of(
                        List.of(
                            new ExplainNode(node("o1"), List.of()),
                            new ExplainNode(node("oa1"), List.of(
                                new ExplainAssociation(node("ua1"), new AccessRightSet("read", "write"), List.of(new Path(node("u1"), node("ua1"))))
                            )),
                            new ExplainNode(node("pc1"), List.of())
                        )
                    )),
                new PolicyClassExplain(node("pc2"),
                    new AccessRightSet("read", "write"),
                    List.of(
                        List.of(
                            new ExplainNode(node("o1"), List.of()),
                            new ExplainNode(node("oa3"), List.of()),
                            new ExplainNode(node("oa2"), List.of(
                                new ExplainAssociation(node("ua2"), new AccessRightSet("read", "write"), List.of(new Path(node("u1"), node("ua2"))))
                            )),
                            new ExplainNode(node("pc2"), List.of())
                        )
                    ))
            ),
            new AccessRightSet("write"),
            List.of(
                new Prohibition("p1", new ProhibitionSubject(id("u1")), new AccessRightSet("write"), false, List.of(new ContainerCondition(id("oa1"), false)))
            )
        );
        assertExplainEquals(expected, actual);
    }

    private void assertExplainEquals(Explain expected, Explain actual) {
        assertEquals(expected.getProhibitions(), actual.getProhibitions());
        assertEquals(expected.getDeniedPrivileges(), actual.getDeniedPrivileges());
        assertEquals(expected.getPrivileges(), actual.getPrivileges());

        Collection<PolicyClassExplain> expectedPolicyClasses = expected.getPolicyClasses();
        Collection<PolicyClassExplain> actualPolicyClasses = actual.getPolicyClasses();

        assertEquals(expectedPolicyClasses.size(), actualPolicyClasses.size());

        for (PolicyClassExplain expectedPolicyClass : expectedPolicyClasses) {
            for (PolicyClassExplain actualPolicyClass : actualPolicyClasses) {
                if (!actualPolicyClass.pc().equals(expectedPolicyClass.pc())) {
                    continue;
                }

                assertPolicyClassExplainEquals(expectedPolicyClass, actualPolicyClass);

                break;
            }
        }
    }

    private void assertPolicyClassExplainEquals(PolicyClassExplain expected, PolicyClassExplain actual) {
        assertEquals(expected.pc(), actual.pc());
        assertEquals(expected.arset(), actual.arset());

        Collection<List<ExplainNode>> expectedPaths = new HashSet<>(expected.paths());
        Collection<List<ExplainNode>> actualPaths = new HashSet<>(actual.paths());

        for (List<ExplainNode> expectedPath : expectedPaths) {
            assertTrue(actualPaths.contains(expectedPath));
        }

        for (List<ExplainNode> actualPath : actualPaths) {
            assertTrue(actualPaths.contains(actualPath));
        }
    }

    @Test
    void testExplain2() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
                
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
                
                create U "u1" in ["ua2", "ua3", "ua4"]
                create O "o1" in ["oa2", "oa3", "oa4"]
                """;
        pap.executePML(new UserContext(0), pml);

        Explain explain = pap.query().access().explain(new UserContext(id("u1")), new TargetContext(id("o1")));
        Explain expected = new Explain(
            new AccessRightSet("read"),
            List.of(
                new PolicyClassExplain(
                    node("pc2"),
                    new AccessRightSet("read"),
                    List.of(
                        List.of(
                            new ExplainNode(node("o1"), List.of()),
                            new ExplainNode(node("oa4"), List.of(
                                new ExplainAssociation(node("ua4"), new AccessRightSet("read"), List.of(new Path(node("u1"), node("ua4"))))
                            )),
                            new ExplainNode(node("pc2"), List.of())
                        )
                    )
                ),
                new PolicyClassExplain(
                    node("pc1"),
                    new AccessRightSet("read"),
                    List.of(
                        List.of(
                            new ExplainNode(node("o1"), List.of()),
                            new ExplainNode(node("oa3"), List.of(
                                new ExplainAssociation(node("ua3"), new AccessRightSet("read"), List.of(new Path(node("u1"), node("ua3"))))
                            )),
                            new ExplainNode(node("oa1"), List.of(
                                new ExplainAssociation(node("ua1"), new AccessRightSet("read"), List.of(new Path(node("u1"), node("ua2"), node("ua1")), new Path(node("u1"), node("ua3"), node("ua1"))))
                            )),
                            new ExplainNode(node("pc1"), List.of())
                        ),
                        List.of(
                            new ExplainNode(node("o1"), List.of()),
                            new ExplainNode(node("oa2"), List.of(
                                new ExplainAssociation(node("ua2"), new AccessRightSet("read"), List.of(new Path(node("u1"), node("ua2"))))
                            )),
                            new ExplainNode(node("oa1"), List.of(
                                new ExplainAssociation(node("ua1"), new AccessRightSet("read"), List.of(new Path(node("u1"), node("ua2"), node("ua1")), new Path(node("u1"), node("ua3"), node("ua1"))))
                            )),
                            new ExplainNode(node("pc1"), List.of())
                        )
                    )
                )
            )
        );
        assertExplainEquals(expected, explain);

        pap.modify().graph().deassign(id("o1"), List.of(id("oa4")));

        explain = pap.query().access().explain(new UserContext(id("u1")), new TargetContext(id("o1")));
        expected = new Explain(
            new AccessRightSet("read"),
            Set.of(
                new PolicyClassExplain(
                    node("pc1"),
                    new AccessRightSet("read"),
                    List.of(
                        List.of(
                            new ExplainNode(node("o1"), List.of()),
                            new ExplainNode(node("oa3"), List.of(
                                new ExplainAssociation(node("ua3"), new AccessRightSet("read"), List.of(new Path(node("u1"), node("ua3"))))
                            )),
                            new ExplainNode(node("oa1"), List.of(
                                new ExplainAssociation(node("ua1"), new AccessRightSet("read"), List.of(new Path(node("u1"), node("ua2"), node("ua1")), new Path(node("u1"), node("ua3"), node("ua1"))))
                            )),
                            new ExplainNode(node("pc1"), List.of())
                        ),
                        List.of(
                            new ExplainNode(node("o1"), List.of()),
                            new ExplainNode(node("oa2"), List.of(
                                new ExplainAssociation(node("ua2"), new AccessRightSet("read"), List.of(new Path(node("u1"), node("ua2"))))
                            )),
                            new ExplainNode(node("oa1"), List.of(
                                new ExplainAssociation(node("ua1"), new AccessRightSet("read"), List.of(new Path(node("u1"), node("ua2"), node("ua1")), new Path(node("u1"), node("ua3"), node("ua1"))))
                            )),
                            new ExplainNode(node("pc1"), List.of())
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
                set resource access rights ["read", "write"]
                create pc "pc1"
                    create UA "ua1" in ["pc1"]
                    create oa "oa1" in ["pc1"]
                    create oa "oa2" in ["oa1"]

                    associate "ua1" and "oa1" with ["write"]
                    associate "ua1" and "oa2" with ["read"]
               
                create U "u1" in ["ua1"]
                """;
        pap.executePML(new TestUserContext("u1"), pml);
        Explain actual = pap.query().access().explain(new UserContext(id("u1")), new TargetContext(id("oa2")));
        assertExplainEquals(
            new Explain(
                new AccessRightSet("read", "write"),
                List.of(
                    new PolicyClassExplain(
                        node("pc1"),
                        new AccessRightSet("read", "write"),
                        List.of(
                            List.of(
                                new ExplainNode(node("oa2"), List.of(
                                    new ExplainAssociation(node("ua1"), new AccessRightSet("read"), List.of(new Path(node("u1"), node("ua1"))))
                                )),
                                new ExplainNode(node("oa1"), List.of(
                                    new ExplainAssociation(node("ua1"), new AccessRightSet("write"), List.of(new Path(node("u1"), node("ua1"))))
                                )),
                                new ExplainNode(node("pc1"), List.of())
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
                set resource access rights ["read", "write"]
                create pc "pc1"
                    create UA "ua1" in ["pc1"]
                    create UA "ua2" in ["pc1"]
                    create oa "oa1" in ["pc1"]
                    create oa "oa2" in ["oa1"]

                    associate "ua1" and "oa1" with ["write"]
               
                create U "u1" in ["ua2"]
                create O "o1" in ["oa1"]
                """;
        pap.executePML(new TestUserContext("u1"), pml);
        Explain actual = pap.query().access().explain(new UserContext(id("u1")), new TargetContext(id("o1")));
        assertExplainEquals(
            new Explain(
                new AccessRightSet(),
                List.of(
                    new PolicyClassExplain(
                        node("pc1"),
                        new AccessRightSet(),
                        List.of(
                            List.of(
                                new ExplainNode(node("o1"), List.of()),
                                new ExplainNode(node("oa1"), List.of(
                                    new ExplainAssociation(node("ua1"), new AccessRightSet("write"), List.of())
                                )),
                                new ExplainNode(node("pc1"), List.of())
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
                set resource access rights ["read", "write"]
                create pc "pc1"
                    create UA "ua1" in ["pc1"]
                    create oa "oa1" in ["pc1"]

                    associate "ua1" and "oa1" with ["write"]
                    
                create pc "pc2"
                    create UA "ua2" in ["pc2"]
                    create OA "oa2" in ["pc2"]
                    
                    associate "ua2" and "oa2" with ["read"]
               
                create U "u1" in ["ua1", "ua2"]
                create O "o1" in ["oa1", "oa2"]
                """;
        pap.executePML(new TestUserContext("u1"), pml);
        Explain actual = pap.query().access().explain(new UserContext(id("u1")), new TargetContext(id("o1")));
        assertExplainEquals(
            new Explain(
                new AccessRightSet(),
                List.of(
                    new PolicyClassExplain(
                        node("pc1"),
                        new AccessRightSet("write"),
                        List.of(
                            List.of(
                                new ExplainNode(node("o1"), List.of()),
                                new ExplainNode(node("oa1"), List.of(
                                    new ExplainAssociation(node("ua1"), new AccessRightSet("write"), List.of(new Path(node("u1"), node("ua1"))))
                                )),
                                new ExplainNode(node("pc1"), List.of())
                            )
                        )
                    ),
                    new PolicyClassExplain(
                        node("pc2"),
                        new AccessRightSet("read"),
                        List.of(
                            List.of(
                                new ExplainNode(node("o1"), List.of()),
                                new ExplainNode(node("oa2"), List.of(
                                    new ExplainAssociation(node("ua2"), new AccessRightSet("read"), List.of(new Path(node("u1"), node("ua2"))))
                                )),
                                new ExplainNode(node("pc2"), List.of())
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
                set resource access rights ["read", "write"]
                create pc "pc1"
                    create UA "ua1" in ["pc1"]
                    create oa "oa1" in ["pc1"]

                    associate "ua1" and "oa1" with ["read", "write"]
                    
                create pc "pc2"
                    create UA "ua2" in ["pc2"]
                    create OA "oa2" in ["pc2"]
                    
                    associate "ua2" and "oa2" with ["read", "write"]
               
                create U "u1" in ["ua1", "ua2"]
                create O "o1" in ["oa1", "oa2"]
                """;
        pap.executePML(new TestUserContext("u1"), pml);
        Explain actual = pap.query().access().explain(new UserContext(id("u1")), new TargetContext(id("o1")));
        assertExplainEquals(
            new Explain(
                new AccessRightSet("read", "write"),
                List.of(
                    new PolicyClassExplain(
                        node("pc1"),
                        new AccessRightSet("read", "write"),
                        List.of(
                            List.of(
                                new ExplainNode(node("o1"), List.of()),
                                new ExplainNode(node("oa1"), List.of(
                                    new ExplainAssociation(node("ua1"), new AccessRightSet("read", "write"), List.of(new Path(node("u1"), node("ua1"))))
                                )),
                                new ExplainNode(node("pc1"), List.of())
                            )
                        )
                    ),
                    new PolicyClassExplain(
                        node("pc2"),
                        new AccessRightSet("read", "write"),
                        List.of(
                            List.of(
                                new ExplainNode(node("o1"), List.of()),
                                new ExplainNode(node("oa2"), List.of(
                                    new ExplainAssociation(node("ua2"), new AccessRightSet("read", "write"), List.of(new Path(node("u1"), node("ua2"))))
                                )),
                                new ExplainNode(node("pc2"), List.of())
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
                set resource access rights ["read", "write"]
                create pc "pc1"
                    create UA "ua1" in ["pc1"]
                    create oa "oa1" in ["pc1"]

                    associate "ua1" and "oa1" with ["read", "write"]
                    
                create pc "pc2"
                    create UA "ua2" in ["pc2"]
                    create OA "oa2" in ["pc2"]
                    
                    associate "ua2" and "oa2" with ["read"]
               
                create U "u1" in ["ua1", "ua2"]
                create O "o1" in ["oa1", "oa2"]
                """;
        pap.reset();
        pap.executePML(new TestUserContext("u1"), pml);
        actual = pap.query().access().explain(new UserContext(id("u1")), new TargetContext(id("o1")));
        assertExplainEquals(
            new Explain(
                new AccessRightSet("read"),
                List.of(
                    new PolicyClassExplain(
                        node("pc1"),
                        new AccessRightSet("read", "write"),
                        List.of(
                            List.of(
                                new ExplainNode(node("o1"), List.of()),
                                new ExplainNode(node("oa1"), List.of(
                                    new ExplainAssociation(node("ua1"), new AccessRightSet("read", "write"), List.of(new Path(node("u1"), node("ua1"))))
                                )),
                                new ExplainNode(node("pc1"), List.of())
                            )
                        )
                    ),
                    new PolicyClassExplain(
                        node("pc2"),
                        new AccessRightSet("read"),
                        List.of(
                            List.of(
                                new ExplainNode(node("o1"), List.of()),
                                new ExplainNode(node("oa2"), List.of(
                                    new ExplainAssociation(node("ua2"), new AccessRightSet("read"), List.of(new Path(node("u1"), node("ua2"))))
                                )),
                                new ExplainNode(node("pc2"), List.of())
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
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["oa1"]

                associate "ua1" and "oa1" with ["read", "write"]
               
                
                create u "u1" in ["ua1"]
                create o "o1" in ["oa2"]
                create o "o2" in ["oa2"]
                
                create prohibition "p1"
                deny U "u1"
                access rights ["write"]
                on union of {"o1": false}
                """;
        pap.executePML(new TestUserContext("u1"), pml);
        SubgraphPrivileges actual = pap.query().access().computeSubgraphPrivileges(new UserContext(id("u1")), id("oa1"));
        assertSubgraphPrivilegesEquals(new SubgraphPrivileges(
            node("oa1"), new AccessRightSet("read", "write"), List.of(
            new SubgraphPrivileges(node("oa2"), new AccessRightSet("read", "write"), List.of(
                new SubgraphPrivileges(node("o1"), new AccessRightSet("read"), List.of()),
                new SubgraphPrivileges(node("o2"), new AccessRightSet("read", "write"), List.of())
            ))
        )
        ), actual);
    }

    private boolean assertSubgraphPrivilegesEquals(SubgraphPrivileges expected, SubgraphPrivileges actual) {
        if (!expected.node().equals(actual.node())) {
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
                set resource access rights ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                   
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["oa1"]
                associate "ua1" and "oa1" with ["read", "write"]
                associate "ua2" and "oa2" with ["read"]
                
                create u "u1" in ["ua1", "ua2"]
                """;
        pap.executePML(new TestUserContext("u1"), pml);
        Map<Long, AccessRightSet> u1 = pap.query().access().computeDestinationAttributes(new UserContext(id("u1")));
        assertEquals(
            Map.of(
                id("oa1"), new AccessRightSet("read", "write"),
                id("oa2"), new AccessRightSet("read")
            ),
            u1
        );
    }

    @Test
    void testBuildACL() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
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
        pap.executePML(new TestUserContext("u1"), pml);
        Map<Long, AccessRightSet> o1 = pap.query().access().computeACL(new TargetContext(id("o1")));
        assertEquals(
            Map.of(
                id("u1"), new AccessRightSet("read", "write"),
                id("u2"), new AccessRightSet("read")
            ),
            o1
        );
    }

    @Test
    void testBuildCapabilityList() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
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
                deny U "u1" 
                access rights ["write"]
                on union of {"oa1": false}
                """;
        pap.executePML(new TestUserContext("u1"), pml);
        Map<Long, AccessRightSet> u1 = pap.query().access().computeCapabilityList(new UserContext(id("u1")));
        assertEquals(
            Map.of(
                id("o1"), new AccessRightSet("read"),
                id("o2"), new AccessRightSet("read"),
                id("oa1"), new AccessRightSet("read"),
                id("oa2"), new AccessRightSet("read")
            ),
            u1
        );

        pap.modify().graph().associate(id("ua1"), AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet("read"));
        u1 = pap.query().access().computeCapabilityList(new UserContext(id("u1")));

        Map<Long, AccessRightSet> map = new HashMap<>();
        map.put(id("o1"), new AccessRightSet("read"));
        map.put(id("o2"), new AccessRightSet("read"));
        map.put(id("oa1"), new AccessRightSet("read"));
        map.put(id("oa2"), new AccessRightSet("read"));
        map.put(id("pc1"), new AccessRightSet("read"));
        map.put(AdminPolicyNode.PM_ADMIN_PC.nodeId(), new AccessRightSet("read"));
        map.put(AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet("read"));
        map.put(AdminPolicyNode.PM_ADMIN_POLICY_CLASSES.nodeId(), new AccessRightSet("read"));
        map.put(AdminPolicyNode.PM_ADMIN_OBLIGATIONS.nodeId(), new AccessRightSet("read"));
        map.put(AdminPolicyNode.PM_ADMIN_PROHIBITIONS.nodeId(), new AccessRightSet("read"));
        map.put(AdminPolicyNode.PM_ADMIN_OPERATIONS.nodeId(), new AccessRightSet("read"));
        map.put(AdminPolicyNode.PM_ADMIN_ROUTINES.nodeId(), new AccessRightSet("read"));
        assertEquals(map.entrySet(), u1.entrySet());
    }

    @Test
    void testComputeDeniedPrivileges() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["read", "write"]
                
                create u "u1" in ["ua1"]
                create o "o1" in ["oa1"]
                
                create prohibition "p1"
                deny U "u1" 
                access rights ["write"]
                on union of {"oa1": false}
                """;
        pap.executePML(new TestUserContext("u1"), pml);
        AccessRightSet deniedPrivileges = pap.query().access().computeDeniedPrivileges(new UserContext(id("u1")), new TargetContext(id("o1")));
        assertEquals(new AccessRightSet("write"), deniedPrivileges);
    }

    @Test
    void testGetAccessibleNodes() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);

        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));
        long o2 = pap.modify().graph().createObject("o2", List.of(oa1));
        long o3 = pap.modify().graph().createObject("o3", List.of(oa1));

        AccessRightSet arset = new AccessRightSet("read", "write");
        pap.modify().graph().associate(ua1, oa1, arset);
        Map<Long, AccessRightSet> accessibleNodes = pap.query().access().computeCapabilityList(new UserContext(u1));

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
        pap.modify().operations().setResourceAccessRights(RWE);

        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read", "write"));
        assertTrue(pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1)).containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testGraph2() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);

        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long pc2 = pap.modify().graph().createPolicyClass("pc2");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1, pc2));
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));

        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc2));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));


        assertTrue(pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1)).isEmpty());
    }

    @Test
    void testGraph3() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read", "write"));


        assertTrue(
            pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1)).containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testGraph4() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(id("pc1")));
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(id("pc1")));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
        pap.modify().graph().associate(ua2, oa1, new AccessRightSet("write"));


        assertEquals(
            new AccessRightSet("read", "write"),
            pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1))
        );
    }

    @Test
    void testGraph5() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);

        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long pc2 = pap.modify().graph().createPolicyClass("pc2");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc2));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc2));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
        pap.modify().graph().associate(ua2, oa2, new AccessRightSet("read", "write"));



        assertTrue(pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1)).contains("read"));
    }

    @Test
    void testGraph6() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long pc2 = pap.modify().graph().createPolicyClass("pc2");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc2));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc2));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read", "write"));
        pap.modify().graph().associate(ua2, oa2, new AccessRightSet("read"));



        assertTrue(pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1)).contains("read"));
    }

    @Test
    void testGraph7() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);

        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long pc2 = pap.modify().graph().createPolicyClass("pc2");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc2));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read", "write"));



        assertTrue(pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1)).isEmpty());
    }

    @Test
    void testGraph8() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("*"));



        Set<String> list = pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1));
        assertTrue(list.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));
        assertTrue(list.containsAll(RWE));
    }

    @Test
    void testGraph9() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("*"));
        pap.modify().graph().associate(ua2, oa1, new AccessRightSet("read", "write"));



        Set<String> list = pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1));
        assertTrue(list.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));
        assertTrue(list.containsAll(RWE));
    }

    @Test
    void testGraph10() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long pc2 = pap.modify().graph().createPolicyClass("pc2");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc2));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc2));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("*"));
        pap.modify().graph().associate(ua2, oa2, new AccessRightSet("read", "write"));

        assertTrue(
            pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1)).containsAll(Arrays.asList("read", "write"))
        );
    }

    @Test
    void testGraph11() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long pc2 = pap.modify().graph().createPolicyClass("pc2");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc2));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("*"));



        assertEquals(new AccessRightSet(), pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1)));
    }

    @Test
    void testGraph12() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
        pap.modify().graph().associate(ua2, oa1, new AccessRightSet("write"));

        assertTrue(
            pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1)).containsAll(Arrays.asList("read", "write"))
        );
    }

    @Test
    void testGraph13() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(ua2));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(oa2));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("*"));
        pap.modify().graph().associate(ua2, oa2, new AccessRightSet("read"));



        Set<String> list = pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1));
        assertTrue(list.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));
        assertTrue(list.contains("read"));
    }

    @Test
    void testGraph14() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long pc2 = pap.modify().graph().createPolicyClass("pc2");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1, pc2));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("*"));
        pap.modify().graph().associate(ua2, oa1, new AccessRightSet("*"));



        Set<String> list = pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1));
        assertTrue(list.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));
        assertTrue(list.containsAll(RWE));
    }

    @Test
    void testGraph15() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(ua2));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(oa2));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("*"));
        pap.modify().graph().associate(ua2, oa2, new AccessRightSet("read"));



        Set<String> list = pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1));
        assertTrue(list.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));
        assertTrue(list.containsAll(RWE));
    }

    @Test
    void testGraph16() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(ua2));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
        pap.modify().graph().associate(ua2, oa1, new AccessRightSet("write"));



        assertTrue(
            pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1)).containsAll(Arrays.asList("read", "write")));
    }

    // removed graph7 due to adding the descendant IDs to the createNode, need to always connect to the testCtx.policy().graph().

    @Test
    void testGraph18() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa2));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read", "write"));



        assertTrue(pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1)).isEmpty());
    }

    @Test
    void testGraph19() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua2));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));



        assertTrue(pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1)).isEmpty());
    }

    @Test
    void testGraph20() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long pc2 = pap.modify().graph().createPolicyClass("pc2");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc2));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
        pap.modify().graph().associate(ua2, oa2, new AccessRightSet("read", "write"));

        assertTrue(pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1)).contains("read"));
    }

    @Test
    void testGraph21() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long pc2 = pap.modify().graph().createPolicyClass("pc2");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc2));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
        pap.modify().graph().associate(ua2, oa2, new AccessRightSet("write"));



        assertTrue(pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1)).isEmpty());
    }

    @Test
    void testGraph22() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long pc2 = pap.modify().graph().createPolicyClass("pc2");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read", "write"));



        assertTrue(
            pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1)).containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testGraph23WithProhibitions() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);

        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long oa3 = pap.modify().graph().createObjectAttribute("oa3", List.of(pc1));
        long oa4 = pap.modify().graph().createObjectAttribute("oa4", List.of(pc1));
        long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(oa3));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(oa4));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

        pap.modify().graph().associate(ua1, oa3, new AccessRightSet("read", "write", "execute"));
        pap.modify().prohibitions().createProhibition("deny", new ProhibitionSubject(id("ua1")) , new AccessRightSet("read"),
            true,
            List.of(new ContainerCondition(oa1, false),
                new ContainerCondition(oa2, false)));

        pap.modify().prohibitions().createProhibition("deny2", new ProhibitionSubject(id("u1")),
            new AccessRightSet("write"),
            true,
            Collections.singleton(new ContainerCondition(oa3, false)));


        Set<String> list = pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1));
        assertEquals(1, list.size());
        assertTrue(list.contains("execute"));
    }

    @Test
    void testGraph24WithProhibitions() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);

        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));
        long o2 = pap.modify().graph().createObject("o2", List.of(oa2));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));

        pap.modify().prohibitions().createProhibition("deny", new ProhibitionSubject(id("ua1")) ,
            new AccessRightSet("read"),
            true,
            List.of(
                new ContainerCondition(oa1, false),
                new ContainerCondition(oa2, true)
            ));


        assertTrue(pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1)).contains("read"));
        assertTrue(pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o2)).isEmpty());

        pap.modify().graph().associate(ua1, oa2, new AccessRightSet("read"));

        pap.modify().prohibitions().createProhibition("deny-process", new ProhibitionSubject("1234"),
            new AccessRightSet("read"),
            false,
            Collections.singleton(new ContainerCondition(oa1, false)));

        assertEquals(
            new AccessRightSet(),
            pap.query().access().computePrivileges(new UserContext(u1, "1234"), new TargetContext(o1))
        );
    }

    @Test
    void testGraph25WithProhibitions() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);

        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(oa1));
        long oa3 = pap.modify().graph().createObjectAttribute("oa3", List.of(oa1));
        long oa4 = pap.modify().graph().createObjectAttribute("oa4", List.of(oa3));
        long oa5 = pap.modify().graph().createObjectAttribute("oa5", List.of(oa2));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa4));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read", "write"));

        pap.modify().prohibitions().createProhibition("deny", new ProhibitionSubject(u1),
            new AccessRightSet("read", "write"),
            true,
            List.of(new ContainerCondition(oa4, true),
                new ContainerCondition(oa1, false)));


        assertTrue(pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(oa5)).isEmpty());
        assertTrue(
            pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1)).containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testGraph25WithProhibitions2() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);

        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read", "write"));


        pap.modify().prohibitions().createProhibition("deny", new ProhibitionSubject(u1),
            new AccessRightSet("read", "write"),
            true,
            List.of(new ContainerCondition(oa1, false),
                new ContainerCondition(oa2, false)));


        assertTrue(pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1)).isEmpty());
    }

    @Test
    void testDeciderWithUA() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);

        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(ua2));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1, oa2));
        long o2 = pap.modify().graph().createObject("o2", List.of(oa2));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
        pap.modify().graph().associate(ua2, oa1, new AccessRightSet("write"));


        assertTrue(pap.query().access().computePrivileges(new UserContext(ua1), new TargetContext(oa1))
            .containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testProhibitionsAllCombinations() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
        long oa2 = pap.modify().graph().createObjectAttribute("oa2", ids("pc1"));
        long oa3 = pap.modify().graph().createObjectAttribute("oa3", ids("pc1"));
        long oa4 = pap.modify().graph().createObjectAttribute("oa4", ids("pc1"));
        long o1 = pap.modify().graph().createObject("o1", ids("oa1", "oa2", "oa3"));
        long o2 = pap.modify().graph().createObject("o2", ids("oa1", "oa4"));

        long ua1 = pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
        long u1 = pap.modify().graph().createUser("u1", ids("ua1"));
        long u2 = pap.modify().graph().createUser("u2", ids("ua1"));
        long u3 = pap.modify().graph().createUser("u3", ids("ua1"));
        long u4 = pap.modify().graph().createUser("u4", ids("ua1"));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read", "write"));

        pap.modify().prohibitions().createProhibition(
            "p1",
            new ProhibitionSubject(id("u1")),
            new AccessRightSet("write"),
            true,
            List.of(
                new ContainerCondition(oa1, false),
                new ContainerCondition(oa2, false),
                new ContainerCondition(oa3, false)
            ));

        pap.modify().prohibitions().createProhibition(
            "p2",
            new ProhibitionSubject(u2),
            new AccessRightSet("write"),
            false,
            List.of(
                new ContainerCondition(oa1, false),
                new ContainerCondition(oa2, false),
                new ContainerCondition(oa3, false)
            ));

        pap.modify().prohibitions().createProhibition(
            "p3",
            new ProhibitionSubject(u3),
            new AccessRightSet("write"),
            true,
            List.of(
                new ContainerCondition(oa1, false),
                new ContainerCondition(oa2, true)
            ));

        pap.modify().prohibitions().createProhibition(
            "p4",
            new ProhibitionSubject(u4),
            new AccessRightSet("write"),
            false,
            List.of(
                new ContainerCondition(oa1, false),
                new ContainerCondition(oa2, true)
            ));

        pap.modify().prohibitions().createProhibition(
            "p5",
            new ProhibitionSubject(u4),
            new AccessRightSet("write"),
            false,
            Collections.singleton(new ContainerCondition(oa2, true)));


        Set<String> list = pap.query().access().computePrivileges(new UserContext(id("u1")), new TargetContext(id("o1")));
        assertTrue(list.contains("read") && !list.contains("write"));

        list = pap.query().access().computePrivileges(new UserContext(id("u1")), new TargetContext(o2));
        assertTrue(list.contains("read") && list.contains("write"));

        list = pap.query().access().computePrivileges(new UserContext(u2), new TargetContext(o2));
        assertTrue(list.contains("read") && !list.contains("write"));

        list = pap.query().access().computePrivileges(new UserContext(u3), new TargetContext(o2));
        assertTrue(list.contains("read") && !list.contains("write"));

        list = pap.query().access().computePrivileges(new UserContext(u4), new TargetContext(id("o1")));
        assertTrue(list.contains("read") && !list.contains("write"));

        list = pap.query().access().computePrivileges(new UserContext(u4), new TargetContext(o2));
        assertTrue(list.contains("read") && !list.contains("write"));
    }

    @Test
    void testPermissions() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);

        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet(WC_ALL));

        Set<String> list = pap.query().access().computePrivileges(new UserContext(id("u1")), new TargetContext(id("o1")));
        assertTrue(list.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));
        assertTrue(list.containsAll(RWE));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet(WC_ADMIN));
        list = pap.query().access().computePrivileges(new UserContext(id("u1")), new TargetContext(id("o1")));
        assertTrue(list.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));
        assertFalse(list.containsAll(RWE));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet(WC_RESOURCE));
        list = pap.query().access().computePrivileges(new UserContext(id("u1")), new TargetContext(id("o1")));
        assertFalse(list.containsAll(ALL_ADMIN_ACCESS_RIGHTS_SET));
        assertTrue(list.containsAll(RWE));
    }

    @Test
    void testPermissionsInOnlyOnePC() throws PMException {
        pap.modify().operations().setResourceAccessRights(RWE);
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createPolicyClass("pc2");
        pap.modify().graph().createUserAttribute("ua3", ids("pc1"));
        pap.modify().graph().createUserAttribute("ua2", ids("ua3"));
        pap.modify().graph().createUserAttribute("u1", ids("ua2"));

        pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
        pap.modify().graph().createObjectAttribute("oa3", ids("pc2"));
        pap.modify().graph().assign(id("oa3"), ids("oa1"));
        pap.modify().graph().createObject("o1", ids("oa3"));

        pap.modify().graph().associate(id("ua3"), id("oa1"), new AccessRightSet("read"));


        assertTrue(pap.query().access().computePrivileges(new UserContext(id("u1")), new TargetContext(id("o1"))).isEmpty());
    }

    @Test
    void testProhibitionsWithContainerAsTarget() throws PMException {
        pap.modify().operations().setResourceAccessRights(new AccessRightSet("read"));
        pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
        long u1 = pap.modify().graph().createUser("u1", ids("ua1"));
        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));

        pap.modify().prohibitions().createProhibition("deny1", new ProhibitionSubject(u1), new AccessRightSet("read"),
            false,
            Collections.singleton(new ContainerCondition(oa1, false)));


        AccessRightSet deniedAccessRights = pap.query().access().computeDeniedPrivileges(new UserContext(id("u1")), new TargetContext(id("oa1")));
        assertTrue(deniedAccessRights.contains("read"));
    }

    @Test
    void testProhibitionWithContainerAsTargetComplement() throws PMException {
        pap.modify().operations().setResourceAccessRights(new AccessRightSet("read"));
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));

        pap.modify().prohibitions().createProhibition("deny1", new ProhibitionSubject(u1), new AccessRightSet("read"),
            false,
            Collections.singleton(new ContainerCondition(oa1, true)));

        AccessRightSet deniedAccessRights = pap.query().access().computeDeniedPrivileges(new UserContext(id("u1")), new TargetContext(id("oa1")));
        assertFalse(deniedAccessRights.contains("read"));
    }

    @Test
    void testAssociationWithObject() throws PMException {
        pap.modify().operations().setResourceAccessRights(new AccessRightSet("read"));
        pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
        long o1 = pap.modify().graph().createObject("o1", ids("pc1"));
        pap.modify().graph().createUser("u1", ids("ua1"));
        pap.modify().graph().associate(ua1, o1, new AccessRightSet("read"));

        AccessRightSet accessRightSet = pap.query().access().computePrivileges(new UserContext(id("u1")), new TargetContext(id("o1")));
        assertEquals(new AccessRightSet("read"), accessRightSet);
    }

    @Test
    void testAssociationWithObjectAndProhibitions() throws PMException {
        pap.modify().operations().setResourceAccessRights(new AccessRightSet("read"));
        pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
        long o1 = pap.modify().graph().createObject("o1", ids("pc1"));
        long u1 = pap.modify().graph().createUser("u1", ids("ua1"));
        pap.modify().graph().associate(ua1, o1, new AccessRightSet("read"));

        pap.modify().prohibitions().createProhibition("deny1", new ProhibitionSubject(u1), new AccessRightSet("read"),
            false,
            Collections.singleton(new ContainerCondition(o1, false)));


        AccessRightSet accessRightSet = pap.query().access().computePrivileges(new UserContext(id("u1")), new TargetContext(id("o1")));
        assertEquals(new AccessRightSet(), accessRightSet);
    }

    @Test
    void testAttributesInUserAndTargetContexts() throws PMException {
        String pml = """
                set resource access rights ["read", "write"]
                
                create pc "pc1"
                create pc "pc2"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc2"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc2"]
                
                associate "ua1" and "oa1" with ["read", "write"]
                associate "ua2" and "oa2" with ["read"]

                create u "u1" in ["ua1", "ua2"]                
                create o "o1" in ["oa1", "oa2"]                
                """;
        pap.executePML(new TestUserContext("u1"), pml);

        AccessRightSet actual = pap.query().access().computePrivileges(new UserContext(id("u1")), new TargetContext(id("o1")));
        assertEquals(new AccessRightSet("read"), actual);

        actual = pap.query().access().computePrivileges(new UserContext(LongList.of(id("ua1"), id("ua2"))), new TargetContext(id("o1")));
        assertEquals(new AccessRightSet("read"), actual);

        actual = pap.query().access().computePrivileges(new UserContext(id("u1")), new TargetContext(LongList.of(id("oa1"), id("oa2"))));
        assertEquals(new AccessRightSet("read"), actual);

        actual = pap.query().access().computePrivileges(new UserContext(LongList.of(id("ua1"), id("ua2"))), new TargetContext(LongList.of(id("oa1"), id("oa2"))));
        assertEquals(new AccessRightSet("read"), actual);

        // create a prohibition for the user on the object
        pml = """
                create prohibition "p1"
                deny U "u1"
                access rights ["read"]
                on union of {"o1": false}
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        actual = pap.query().access().computePrivileges(new UserContext(id("u1")), new TargetContext(id("o1")));
        assertEquals(new AccessRightSet(), actual);

        actual = pap.query().access().computePrivileges(new UserContext(LongList.of(id("ua1"), id("ua2"))), new TargetContext(id("o1")));
        assertEquals(new AccessRightSet("read"), actual);

        actual = pap.query().access().computePrivileges(new UserContext(id("u1")), new TargetContext(LongList.of(id("oa1"), id("oa2"))));
        assertEquals(new AccessRightSet("read"), actual);

        actual = pap.query().access().computePrivileges(new UserContext(LongList.of(id("ua1"), id("ua2"))), new TargetContext(LongList.of(id("oa1"), id("oa2"))));
        assertEquals(new AccessRightSet("read"), actual);

        pml = """
                delete prohibition "p1"
                
                create prohibition "p1"
                deny U "u1"
                access rights ["read"]
                on intersection of {"oa1": false, "oa2": false}
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        actual = pap.query().access().computePrivileges(new UserContext(id("u1")), new TargetContext(id("o1")));
        assertEquals(new AccessRightSet(), actual);

        actual = pap.query().access().computePrivileges(new UserContext(LongList.of(id("ua1"), id("ua2"))), new TargetContext(id("o1")));
        assertEquals(new AccessRightSet("read"), actual);

        actual = pap.query().access().computePrivileges(new UserContext(id("u1")), new TargetContext(LongList.of(id("oa1"), id("oa2"))));
        assertEquals(new AccessRightSet(), actual);

        actual = pap.query().access().computePrivileges(new UserContext(LongList.of(id("ua1"), id("ua2"))), new TargetContext(LongList.of(id("oa1"), id("oa2"))));
        assertEquals(new AccessRightSet("read"), actual);

    }
}
