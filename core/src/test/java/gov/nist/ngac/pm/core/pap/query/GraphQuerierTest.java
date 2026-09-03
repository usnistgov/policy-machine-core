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

package gov.nist.ngac.pm.core.pap.query;

import static gov.nist.ngac.pm.core.common.graph.node.NodeType.ANY;
import static gov.nist.ngac.pm.core.common.graph.node.NodeType.OA;
import static gov.nist.ngac.pm.core.common.graph.node.NodeType.PC;
import static gov.nist.ngac.pm.core.common.graph.node.Properties.NO_PROPERTIES;
import static gov.nist.ngac.pm.core.common.graph.node.Properties.toProperties;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.common.graph.node.Properties;
import gov.nist.ngac.pm.core.pap.PAPTestInitializer;
import gov.nist.ngac.pm.core.pap.graph.Association;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.query.model.subgraph.Subgraph;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

public abstract class GraphQuerierTest extends PAPTestInitializer {

    @Nested
    class NodeExists {
        @Test
        void testSuccess() throws PMException {
            long pc1 = pap.modify().graph().createPolicyClass("pc1");
            long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            assertTrue(pap.query().graph().nodeExists("pc1"));
            assertTrue(pap.query().graph().nodeExists("ua1"));
            assertFalse(pap.query().graph().nodeExists("pc2"));
        }
    }

    @Nested
    class GetNodeTest {

        @Test
        void testNodeDoesNotExistException() {
            assertThrows(NodeDoesNotExistException.class, () -> pap.query().graph().getNodeByName("pc1"));
        }

        @Test
        void testSuccessPolicyClass() throws PMException {
            long pc1Id = pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().setNodeProperties(pc1Id, toProperties("k", "v"));

            Node pc1 = pap.query().graph().getNodeByName("pc1");
            assertEquals("pc1", pc1.getName());
            assertEquals(PC, pc1.getType());
            assertEquals("v", pc1.getProperties().get("k"));
        }

        @Test
        void testSuccessObjectAttribute() throws PMException {
            long pc1 = pap.modify().graph().createPolicyClass("pc1");
            long oa1Id = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            pap.modify().graph().setNodeProperties(oa1Id, Properties.toProperties("k", "v"));

            Node oa1 = pap.query().graph().getNodeByName("oa1");

            assertEquals("oa1", oa1.getName());
            assertEquals(OA, oa1.getType());
            assertEquals("v", oa1.getProperties().get("k"));
        }
    }

    @Nested
    class Search {
        @Test
        void testSearch() throws PMException {
            long pc1 = pap.modify().graph().createPolicyClass("pc1");
            long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            pap.modify().graph().setNodeProperties(oa1, toProperties("namespace", "test"));
            long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
            pap.modify().graph().setNodeProperties(oa2, toProperties("key1", "value1"));
            long oa3 = pap.modify().graph().createObjectAttribute("oa3", List.of(pc1));
            pap.modify().graph().setNodeProperties(oa3, toProperties("key1", "value1", "key2", "value2"));

            Collection<Node> nodes = pap.query().graph().search(OA, NO_PROPERTIES);
            assertEquals(9, nodes.size());

            nodes = pap.query().graph().search(ANY, toProperties("key1", "value1"));
            assertEquals(2, nodes.size());

            nodes = pap.query().graph().search(ANY, toProperties("namespace", "test"));
            assertEquals(1, nodes.size());

            nodes = pap.query().graph().search(OA, toProperties("namespace", "test"));
            assertEquals(1, nodes.size());
            nodes = pap.query().graph().search(OA, toProperties("key1", "value1"));
            assertEquals(2, nodes.size());
            nodes = pap.query().graph().search(OA, toProperties("key1", "*"));
            assertEquals(2, nodes.size());
            nodes = pap.query().graph().search(OA, toProperties("key1", "value1", "key2", "value2"));
            assertEquals(1, nodes.size());
            nodes = pap.query().graph().search(OA, toProperties("key1", "value1", "key2", "*"));
            assertEquals(1, nodes.size());
            nodes = pap.query().graph().search(OA, toProperties("key1", "value1", "key2", "no_value"));
            assertEquals(0, nodes.size());
            nodes = pap.query().graph().search(ANY, NO_PROPERTIES);
            assertEquals(11, nodes.size());
        }
    }


    @Nested
    class GetPolicyClasses {
        @Test
        void testSuccess() throws PMException {
            long pc1 = pap.modify().graph().createPolicyClass("pc1");
            long pc2 = pap.modify().graph().createPolicyClass("pc2");
            long pc3 = pap.modify().graph().createPolicyClass("pc3");

            assertTrue(pap.query().graph().getPolicyClasses().containsAll(Arrays.asList(pc1, pc2, pc3)));
        }
    }

    @Nested
    class GetAdjacentAscendantsTest {

        @Test
        void NodeDoesNotExist() {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.query().graph().getAdjacentAscendants(-99));
        }

        @Test
        void Success() throws PMException {
            long pc1 = pap.modify().graph().createPolicyClass("pc1");
            long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
            long oa3 = pap.modify().graph().createObjectAttribute("oa3", List.of(pc1));

            assertTrue(pap.query().graph().getAdjacentAscendants(pc1).containsAll(List.of(oa1, oa2, oa3)));
        }
    }

    @Nested
    class GetAdjacentDescendantsTest {

        @Test
        void NodeDoesNotExist() {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.query().graph().getAdjacentDescendants(-99));
        }

        @Test
        void Success() throws PMException {
            long pc1 = pap.modify().graph().createPolicyClass("pc1");
            long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
            long oa3 = pap.modify().graph().createObjectAttribute("oa3", List.of(pc1));
            long o1 = pap.modify().graph().createObject("o1", List.of(oa1));
            pap.modify().graph().assign(o1, List.of(oa2));
            pap.modify().graph().assign(o1, List.of(oa3));

            assertTrue(pap.query().graph().getAdjacentDescendants(o1).containsAll(List.of(oa1, oa2, oa3)));
        }
    }

    @Nested
    class GetAssociationsWithSourceTest {

        @Test
        void testNodeDoesNotExistException() {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.query().graph().getAssociationsWithSource(-99));
        }

        @Test
        void testSuccess() throws PMException {
            pap.modify().operations().setResourceAccessRights(new AccessRightSet("read", "write"));
            long pc1 = pap.modify().graph().createPolicyClass("pc1");
            long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            long oa2 = pap.modify().graph().createObjectAttribute("oa2", List.of(pc1));
            long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
            pap.modify().graph().associate(ua1, oa2, new AccessRightSet("read", "write"));

            Collection<Association> assocs = pap.query().graph().getAssociationsWithSource(ua1);

            assertEquals(2, assocs.size());

            for (Association assoc : assocs) {
                checkAssociation(assoc);
            }
        }

        private void checkAssociation(Association association) throws PMException {
            if (association.target() == id("oa1")) {
                assertEquals(new AccessRightSet("read"), association.arset());
            } else if (association.target() == id("oa2")) {
                assertEquals(new AccessRightSet("read", "write"), association.arset());
            }
        }
    }

    @Nested
    class GetAssociationsWithTargetTest {

        @Test
        void testNodeDoesNotExistException() {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.query().graph().getAssociationsWithTarget(0));
        }

        @Test
        void Success() throws PMException {
            pap.modify().operations().setResourceAccessRights(new AccessRightSet("read", "write"));
            long pc1 = pap.modify().graph().createPolicyClass("pc1");
            long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
            pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read"));
            pap.modify().graph().associate(ua2, oa1, new AccessRightSet("read", "write"));

            Collection<Association> assocs = pap.query().graph().getAssociationsWithTarget(oa1);

            assertEquals(2, assocs.size());

            for (Association assoc : assocs) {
                checkAssociation(assoc);
            }
        }

        private void checkAssociation(Association association) throws PMException {
            if (association.source() == id("ua1")) {
                assertEquals(new AccessRightSet("read"), association.arset());
            } else if (association.source() == id("ua2")) {
                assertEquals(new AccessRightSet("read", "write"), association.arset());
            }
        }
    }

    @Test
    void testGetAttributeDescendants() throws PMException {
        String pml =
                """
                set resource access rights ["read", "write"]
                create pc "pc1"
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["oa1"]
                create oa "oa3" in ["oa2"]
                create oa "oa4" in ["pc1"]
    
                create pc "pc2"
                create oa "oa5" in ["pc2"]
                create oa "oa6" in ["oa5"]
                                
                create pc "pc3"
                                
                create o "o1" in ["oa3", "oa6"]
                """;
        pap.executePML(NodeUserContext.of("u1"), pml);

        Collection<Long> conts = pap.query().graph().getAttributeDescendants(id("o1"));
        List<Long> expected = ids("oa3", "oa2", "oa1", "oa6", "oa5");
        assertTrue(conts.containsAll(expected));
        assertTrue(expected.containsAll(conts));
    }

    @Test
    void testGetPolicyClassDescendants() throws PMException {
        String pml = """
                      set resource access rights ["read", "write"]
                      create pc "pc1"
                      create oa "oa1" in ["pc1"]
                      create oa "oa2" in ["oa1"]
                      create oa "oa3" in ["oa2"]
                      create oa "oa4" in ["pc1"]

                      create pc "pc2"
                      create oa "oa5" in ["pc2"]
                      create oa "oa6" in ["oa5"]
                                      
                      create pc "pc3"
                                      
                      create o "o1" in ["oa3", "oa6"]
                      """;
        pap.executePML(NodeUserContext.of("u1"), pml);

        Collection<Long> pcs = pap.query().graph().getPolicyClassDescendants(id("o1"));
        List<Long> expected = ids("pc1", "pc2");
        assertTrue(pcs.containsAll(expected));
        assertTrue(expected.containsAll(pcs));
    }

    @Test
    void testIsAscendant() throws PMException {
        String pml = """
                      set resource access rights ["read", "write"]
                      create pc "pc1"
                      create oa "oa1" in ["pc1"]
                      create oa "oa2" in ["oa1"]
                      create oa "oa3" in ["oa2"]
                      create oa "oa4" in ["pc1"]

                      create pc "pc2"
                      create oa "oa5" in ["pc2"]
                      create oa "oa6" in ["oa5"]
                     
                      create pc "pc3"
                                      
                      create o "o1" in ["oa3", "oa6"]
                      """;
        pap.executePML(NodeUserContext.of("u1"), pml);

        long o1 = id("o1");
        assertTrue(pap.query().graph().isAscendant(o1, o1));
        assertTrue(pap.query().graph().isAscendant(o1, id("oa1")));
        assertTrue(pap.query().graph().isAscendant(o1, id("oa2")));
        assertTrue(pap.query().graph().isAscendant(o1, id("oa3")));
        assertTrue(pap.query().graph().isAscendant(o1, id("pc1")));
        assertTrue(pap.query().graph().isAscendant(o1, id("pc2")));
        assertFalse(pap.query().graph().isAscendant(o1, id("pc3")));
    }

    @Test
    void testIsDescendant() throws PMException {
        String pml = """
                      set resource access rights ["read", "write"]
                      create pc "pc1"
                      create oa "oa1" in ["pc1"]
                      create oa "oa2" in ["oa1"]
                      create oa "oa3" in ["oa2"]
                      create oa "oa4" in ["pc1"]

                      create pc "pc2"
                      create oa "oa5" in ["pc2"]
                      create oa "oa6" in ["oa5"]
                     
                      create pc "pc3"
                                      
                      create o "o1" in ["oa3", "oa6"]
                      """;
        pap.executePML(NodeUserContext.of("u1"), pml);

        long o1 = id("o1");
        assertTrue(pap.query().graph().isDescendant(o1, o1));
        assertTrue(pap.query().graph().isDescendant(o1, id("oa1")));
        assertTrue(pap.query().graph().isDescendant(o1, id("oa2")));
        assertTrue(pap.query().graph().isDescendant(o1, id("oa3")));
        assertTrue(pap.query().graph().isDescendant(o1, id("pc1")));
        assertTrue(pap.query().graph().isDescendant(o1, id("pc2")));
        assertFalse(pap.query().graph().isDescendant(o1, id("pc3")));
    }

    @Test
    void testGetAscendantSubgraph() throws PMException {
        String pml =
                """
                set resource access rights ["read", "write"]
                create pc "pc1"
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["oa1"]
                create oa "oa3" in ["oa2"]
                create oa "oa4" in ["pc1"]
    
                create pc "pc2"
                create oa "oa5" in ["pc2"]
                create oa "oa6" in ["oa5"]

                create pc "pc3"

                create o "o1" in ["oa3", "oa6"]
                """;
        pap.executePML(NodeUserContext.of("u1"), pml);

        Subgraph actual = pap.query().graph().getAscendantSubgraph(id("pc1"));
        assertSubgraphEquals(new Subgraph(
                node("pc1"), Set.of(
                new Subgraph(
                        node("oa1"), Set.of(
                        new Subgraph(
                                node("oa2"), Set.of(
                                new Subgraph(
                                        node("oa3"), Set.of(
                                        new Subgraph(
                                                node("o1"), Set.of()
                                        ))
                                ))
                        ))
                ),
                new Subgraph(node("oa4"), Set.of()))
        ), actual);
    }

    @Test
    void testGetDescendantSubgraph() throws PMException {
        String pml =
                """
                set resource access rights ["read", "write"]
                create pc "pc1"
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["oa1"]
                create oa "oa3" in ["oa2"]
                create oa "oa4" in ["pc1"]
    
                create pc "pc2"
                create oa "oa5" in ["pc2"]
                create oa "oa6" in ["oa5"]

                create pc "pc3"

                create o "o1" in ["oa3", "oa6"]
                """;
        pap.executePML(NodeUserContext.of("u1"), pml);

        Subgraph actual = pap.query().graph().getDescendantSubgraph(id("o1"));
        assertSubgraphEquals(new Subgraph(
                node("oa3"), Set.of(
                new Subgraph(
                        node("oa2"), Set.of(
                        new Subgraph(
                                node("oa1"), Set.of(
                                new Subgraph(
                                        node("pc1"), Set.of()
                                ))
                        ))
                ),
                new Subgraph(node("oa6"), Set.of(
                        new Subgraph(node("oa5"), Set.of(
                                new Subgraph(node("pc2"), Set.of())
                        ))
                )))
        ), actual);
    }

    private boolean assertSubgraphEquals(Subgraph expected, Subgraph actual) {
        if (!expected.node().equals(actual.node())) {
            return false;
        }

        int ok = 0;
        for (Subgraph expectedSubgraph : expected.subgraphs()) {
            for (Subgraph actualSubgraph : actual.subgraphs()) {
                if (assertSubgraphEquals(expectedSubgraph, actualSubgraph)) {
                    ok++;
                }
            }
        }

        return ok == expected.subgraphs().size();
    }
}
