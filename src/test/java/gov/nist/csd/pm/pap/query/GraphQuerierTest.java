package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.Properties;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.pap.PAPTest;
import gov.nist.csd.pm.pap.PAPTestInitializer;
import gov.nist.csd.pm.pap.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.pap.PAP;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static gov.nist.csd.pm.common.graph.node.NodeType.*;
import static gov.nist.csd.pm.common.graph.node.Properties.NO_PROPERTIES;
import static gov.nist.csd.pm.common.graph.node.Properties.toProperties;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class GraphQuerierTest extends PAPTestInitializer {

    @Nested
    class NodeExists {
        @Test
        void testSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            assertTrue(pap.query().graph().nodeExists("pc1"));
            assertTrue(pap.query().graph().nodeExists("ua1"));
            assertFalse(pap.query().graph().nodeExists("pc2"));
        }
    }

    @Nested
    class GetNodeTest {

        @Test
        void testNodeDoesNotExistException() {
            assertThrows(NodeDoesNotExistException.class, () -> pap.query().graph().getNode("pc1"));
        }

        @Test
        void testSuccessPolicyClass() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().setNodeProperties("pc1", toProperties("k", "v"));

            Node pc1 = pap.query().graph().getNode("pc1");

            assertEquals("pc1", pc1.getName());
            assertEquals(PC, pc1.getType());
            assertEquals("v", pc1.getProperties().get("k"));
        }

        @Test
        void testSuccessObjectAttribute() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().setNodeProperties("oa1", Properties.toProperties("k", "v"));

            Node oa1 = pap.query().graph().getNode("oa1");

            assertEquals("oa1", oa1.getName());
            assertEquals(OA, oa1.getType());
            assertEquals("v", oa1.getProperties().get("k"));
        }
    }

    @Nested
    class Search {
        @Test
        void testSearch() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().setNodeProperties("oa1", toProperties("namespace", "test"));
            pap.modify().graph().createObjectAttribute("oa2", List.of("pc1"));
            pap.modify().graph().setNodeProperties("oa2", toProperties("key1", "value1"));
            pap.modify().graph().createObjectAttribute("oa3", List.of("pc1"));
            pap.modify().graph().setNodeProperties("oa3", toProperties("key1", "value1", "key2", "value2"));

            Collection<String> nodes = pap.query().graph().search(OA, NO_PROPERTIES);
            assertEquals(4, nodes.size());

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
            assertEquals(6, nodes.size());
        }
    }


    @Nested
    class GetPolicyClasses {
        @Test
        void testSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createPolicyClass("pc2");
            pap.modify().graph().createPolicyClass("pc3");

            assertTrue(pap.query().graph().getPolicyClasses().containsAll(Arrays.asList("pc1", "pc2", "pc3")));
        }
    }

    @Nested
    class GetAdjacentAscendantsTest {

        @Test
        void NodeDoesNotExist() {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.query().graph().getAdjacentAscendants("oa1"));
        }

        @Test
        void Success() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa3", List.of("pc1"));


            assertTrue(pap.query().graph().getAdjacentAscendants("pc1").containsAll(List.of("oa1", "oa2", "oa3")));
        }
    }

    @Nested
    class GetAdjacentDescendantsTest {

        @Test
        void NodeDoesNotExist() {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.query().graph().getAdjacentDescendants("oa1"));
        }

        @Test
        void Success() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa3", List.of("pc1"));
            pap.modify().graph().createObject("o1", List.of("oa1"));
            pap.modify().graph().assign("o1", List.of("oa2"));
            pap.modify().graph().assign("o1", List.of("oa3"));

            assertTrue(pap.query().graph().getAdjacentDescendants("o1").containsAll(List.of("oa1", "oa2", "oa3")));
        }
    }

    @Nested
    class GetAssociationsWithSourceTest {

        @Test
        void testNodeDoesNotExistException() {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.query().graph().getAssociationsWithSource("ua1"));
        }

        @Test
        void testSuccess() throws PMException {
            pap.modify().operations().setResourceOperations(new AccessRightSet("read", "write"));
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().associate("ua1", "oa1", new AccessRightSet("read"));
            pap.modify().graph().associate("ua1", "oa2", new AccessRightSet("read", "write"));

            Collection<Association> assocs = pap.query().graph().getAssociationsWithSource("ua1");

            assertEquals(2, assocs.size());

            for (Association assoc : assocs) {
                checkAssociation(assoc);
            }
        }

        private void checkAssociation(Association association) {
            if (association.getTarget().equals("oa1")) {
                assertEquals(new AccessRightSet("read"), association.getAccessRightSet());
            } else if (association.getTarget().equals("oa2")) {
                assertEquals(new AccessRightSet("read", "write"), association.getAccessRightSet());
            }
        }
    }

    @Nested
    class GetAssociationsWithTargetTest {

        @Test
        void testNodeDoesNotExistException() {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.query().graph().getAssociationsWithTarget("oa1"));
        }

        @Test
        void Success() throws PMException {
            pap.modify().operations().setResourceOperations(new AccessRightSet("read", "write"));
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua2", List.of("pc1"));
            pap.modify().graph().associate("ua1", "oa1", new AccessRightSet("read"));
            pap.modify().graph().associate("ua2", "oa1", new AccessRightSet("read", "write"));

            Collection<Association> assocs = pap.query().graph().getAssociationsWithTarget("oa1");

            assertEquals(2, assocs.size());

            for (Association assoc : assocs) {
                checkAssociation(assoc);
            }
        }

        private void checkAssociation(Association association) {
            if (association.getSource().equals("ua1")) {
                assertEquals(new AccessRightSet("read"), association.getAccessRightSet());
            } else if (association.getSource().equals("ua2")) {
                assertEquals(new AccessRightSet("read", "write"), association.getAccessRightSet());
            }
        }
    }

    @Test
    void testGetAttributeDescendants() throws PMException {
        String pml =
                """
                set resource operations ["read", "write"]
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
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        Collection<String> conts = pap.query().graph().getAttributeDescendants("o1");
        List<String> expected = List.of("oa3", "oa2", "oa1", "oa6", "oa5");
        assertTrue(conts.containsAll(expected));
        assertTrue(expected.containsAll(conts));
    }

    @Test
    void testGetPolicyClassDescendants() throws PMException {
        String pml = """
                      set resource operations ["read", "write"]
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
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        Collection<String> pcs = pap.query().graph().getPolicyClassDescendants("o1");
        List<String> expected = List.of("pc1", "pc2");
        assertTrue(pcs.containsAll(expected));
        assertTrue(expected.containsAll(pcs));
    }

    @Test
    void testIsAscendant() throws PMException {
        String pml = """
                      set resource operations ["read", "write"]
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
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        assertTrue(pap.query().graph().isAscendant("o1", "oa1"));
        assertTrue(pap.query().graph().isAscendant("o1", "oa2"));
        assertTrue(pap.query().graph().isAscendant("o1", "oa3"));
        assertTrue(pap.query().graph().isAscendant("o1", "pc1"));
        assertTrue(pap.query().graph().isAscendant("o1", "pc2"));
        assertFalse(pap.query().graph().isAscendant("o1", "pc3"));
    }

    @Test
    void testIsDescendant() throws PMException {
        String pml = """
                      set resource operations ["read", "write"]
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
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        assertTrue(pap.query().graph().isDescendant("o1", "oa1"));
        assertTrue(pap.query().graph().isDescendant("o1", "oa2"));
        assertTrue(pap.query().graph().isDescendant("o1", "oa3"));
        assertTrue(pap.query().graph().isDescendant("o1", "pc1"));
        assertTrue(pap.query().graph().isDescendant("o1", "pc2"));
        assertFalse(pap.query().graph().isDescendant("o1", "pc3"));
    }

    @Test
    void testGetAscendants() throws PMException {
        String pml =
                """
                set resource operations ["read", "write"]
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
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        Collection<String> conts = pap.query().graph().getAscendants("pc1");
        List<String> expected = List.of("oa1", "oa2", "oa3", "oa4", "o1");
        assertEquals(new HashSet<>(expected), new HashSet<>(conts));

        conts = pap.query().graph().getAscendants("oa1");
        expected = List.of("oa3", "oa2", "o1");
        assertEquals(new HashSet<>(expected), new HashSet<>(conts));
    }

    @Test
    void testGetDescendants() throws PMException {
        String pml =
                """
                set resource operations ["read", "write"]
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
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        Collection<String> conts = pap.query().graph().getDescendants("o1");
        List<String> expected = List.of("oa3", "oa2", "oa5", "oa6", "pc2", "oa1", "pc1");
        assertEquals(new HashSet<>(expected), new HashSet<>(conts));

        conts = pap.query().graph().getDescendants("pc1");
        expected = List.of();
        assertEquals(new HashSet<>(expected), new HashSet<>(conts));
    }
}
