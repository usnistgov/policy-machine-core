package gov.nist.csd.pm.pip.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;
import static org.junit.jupiter.api.Assertions.*;

public class MySQLGraphTest {


    private MySQLGraph graph;

    @BeforeEach
    void init() throws Exception {
        MySQLConnection connection = new MySQLConnection();
        this.graph = new MySQLGraph(connection);
    }

    @Test
    void testCreateNode() throws PMException {

        Node pc = graph.createPolicyClass(1, "pc1", null);
        assertTrue(graph.getPolicyClasses().contains(pc.getID()));

        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(0, null, null, null, 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(123, null, OA, null, 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(123, "", OA, null, 0)),
                () -> assertThrows(PMException.class, ()             -> graph.createNode(123, "name node", PC, null, 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(123, "name", null, null, 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(123, "name", OA, null, 0))
        );



        // add non pc
        Node node = graph.createNode(2, "oa2", OA, Node.toProperties("namespace", "test"), pc.getID());

        // check node is added
        node = graph.getNode(node.getID());
        assertEquals("oa2", node.getName());
        assertEquals(OA, node.getType());
    }

    @Test
    void testUpdateNode() throws PMException {

        Node node = graph.createPolicyClass(3, "node PC 3", Node.toProperties("namespace", "test"));

        //id = 0
        assertThrows(IllegalArgumentException.class, () -> graph.updateNode(0, "newNodeName", null));

        // node not found
        assertThrows(IllegalArgumentException.class, () -> graph.updateNode(12345678, "newNodeName", null));

        // update name
        graph.updateNode(node.getID(), "updated name 3", null);
        assertEquals(graph.getNode(node.getID()).getName(), "updated name 3");

        // update properties
        graph.updateNode(node.getID(), "updated name 3", Node.toProperties("newKey", "newValue"));
        assertEquals(graph.getNode(node.getID()).getProperties().get("newKey"), "newValue");
    }

    @Test
    void testDeleteNode() throws PMException {
        Node node = graph.createPolicyClass(4, "node test 4", Node.toProperties("namespace", "test"));
        graph.deleteNode(node.getID());

        assertThrows(IllegalArgumentException.class, () -> graph.getNode(node.getID()));

        // deleted from the graph
        assertFalse(graph.exists(node.getID()));

        // deleted from list of policies
        assertFalse(graph.getPolicyClasses().contains(node.getID()));

        //todo: handle exception foreign key constraint
    }

    @Test
    void testExists() throws PMException {
        Node oa1 = graph.createNode(5, "OA 5", OA,null, 1);
        Node oa = graph.createNode(6, "oa 6", OA, null, oa1.getID());

        assertTrue(graph.exists(oa.getID()));
        assertFalse(graph.exists(123456789));
    }

    @Test
    void testGetPolicies() throws PMException {

        //getPolicyClass should not be empty since we have the super_pc node
        //assertTrue(graph.getPolicyClasses().isEmpty());

        int total = graph.getPolicyClasses().size();
        graph.createPolicyClass(7, "nodePC7", null);
        graph.createPolicyClass(8, "nodePC8", null);
        graph.createPolicyClass(9, "nodePC9", null);

        assertEquals(total+3, graph.getPolicyClasses().size());
    }

    @Test
    void testGetChildren() throws PMException {

        assertThrows(PMException.class, () -> graph.getChildren(1));

        Node parentNode = graph.createPolicyClass(10, "parent10",Node.toProperties("firstValue", "test"));
        Node child1Node = graph.createNode(11, "child11", OA, null, 10);
        Node child2Node = graph.createNode(12, "child12", OA, null, 10);

        Set<Long> children = graph.getChildren(parentNode.getID());
        assertTrue(children.containsAll(Arrays.asList(child1Node.getID(), child2Node.getID())));
    }

    @Test
    void testGetParents() throws PMException {

        assertThrows(PMException.class, () -> graph.getChildren(1));

        Node parent1Node = graph.createPolicyClass(13, "parent13", null);
        Node parent2Node = graph.createNode(14, "parent14", OA, null, 13);
        Node child1Node = graph.createNode(15, "child15", OA, null, 13, 14);

        Set<Long> parents = graph.getParents(child1Node.getID());
        assertTrue(parents.contains(parent1Node.getID()));
        assertTrue(parents.contains(parent2Node.getID()));
    }

    @Test
    void testAssign() throws PMException {

        Node parent1Node = graph.createPolicyClass(16, "parent16", null);
        Node child1Node = graph.createNode(17, "childtest17", OA, null, 16);
        Node child2Node = graph.createNode(18, "childtest18", OA, null, 16);

        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> graph.assign(123456, 123456789)),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.assign(50, 12341234))
        );

        graph.assign(child1Node.getID(), child2Node.getID());

        assertTrue(graph.getChildren(parent1Node.getID()).contains(child1Node.getID()));
        assertTrue(graph.getParents(child1Node.getID()).contains(parent1Node.getID()));
    }

    @Test
    void testDeassign() throws PMException {

        Node parent1Node = graph.createPolicyClass(19, "parent19", null);
        Node child1Node = graph.createNode(20, "childtest20", OA, null, 19);

        assertThrows(IllegalArgumentException.class, () -> graph.assign(0, 0));
        assertThrows(IllegalArgumentException.class, () -> graph.assign(child1Node.getID(), 0));

        graph.deassign(child1Node.getID(), parent1Node.getID());

        assertFalse(graph.getChildren(parent1Node.getID()).contains(child1Node.getID()));
        assertFalse(graph.getParents(child1Node.getID()).contains(parent1Node.getID()));
    }

    //@Test
    void testAssociate() throws PMException {

        Node pcNode = graph.createPolicyClass(21, "pc21", null);
        Node uaNode = graph.createNode(22, "subject 22", UA, null, 21);
        Node targetNode = graph.createNode(23, "target 23", OA, null, 21);

        graph.associate(uaNode.getID(), targetNode.getID(), new OperationSet("read", "write"));

        Map<Long, OperationSet> associations = graph.getSourceAssociations(uaNode.getID());
        assertTrue(associations.containsKey(targetNode.getID()));
        System.out.println("operation set : " + associations.get(targetNode.getID()));
        System.out.println("arraylist as  : " + Arrays.asList("read", "write"));
        assertTrue(associations.get(targetNode.getID()).containsAll(Arrays.asList("read", "write")));

        associations = graph.getTargetAssociations(targetNode.getID());
        assertTrue(associations.containsKey(uaNode.getID()));
        assertTrue(associations.get(uaNode.getID()).containsAll(Arrays.asList("read", "write")));
        System.out.println("=== source ");
        System.out.println("operation set : " + associations.get(uaNode.getID()));
        System.out.println("arraylist as  : " + Arrays.asList("read", "write"));
    }

    @Test
    void testDissociate() throws PMException {

        Node pcNode = graph.createPolicyClass(24, "pc", null);
        Node uaNode = graph.createNode(25, "subject 25", UA, null, 24);
        Node targetNode = graph.createNode(26, "target 26", OA, null, 24);

        graph.associate(uaNode.getID(), targetNode.getID(), new OperationSet("read", "write"));
        graph.dissociate(uaNode.getID(), targetNode.getID());

        Map<Long, OperationSet> associations = graph.getSourceAssociations(uaNode.getID());
        assertFalse(associations.containsKey(targetNode.getID()));

        associations = graph.getTargetAssociations(targetNode.getID());
        assertFalse(associations.containsKey(uaNode.getID()));
    }


    @Test
    void testGetSourceAssociations() throws PMException {

        Node pcNode = graph.createPolicyClass(27, "pc 27", null);
        Node uaNode = graph.createNode(28, "subject 28", UA, null, 27);
        Node targetNode = graph.createNode(29, "target 29", OA, null, 27);

        graph.associate(uaNode.getID(), targetNode.getID(), new OperationSet("read", "write"));

        Map<Long, OperationSet> associations = graph.getSourceAssociations(uaNode.getID());

        assertTrue(associations.containsKey(targetNode.getID()));
        assertThrows(PMException.class, () -> graph.getSourceAssociations(123456789));

        //this assert does not work because of the serialization even if it retrieve the proper value
        //todo : change parser
        //assertTrue(associations.get(targetNode.getID()).containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testGetTargetAssociations() throws PMException {

        Node pcNode = graph.createPolicyClass(30, "pc 30", null);
        Node uaNode = graph.createNode(31, "subject 31", UA, null, 30);
        Node targetNode = graph.createNode(32, "target 32", OA, null, 30);

        graph.associate(uaNode.getID(), targetNode.getID(), new OperationSet("read", "write"));

        Map<Long, OperationSet> associations = graph.getTargetAssociations(targetNode.getID());

        assertTrue(associations.containsKey(uaNode.getID()));
        assertThrows(PMException.class, () -> graph.getTargetAssociations(123456789));

        //this assert does not work because of the serialization
        //todo : change parser
        //assertTrue(associations.get(uaNode.getID()).containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testSearch() throws PMException {
        int count_OA = graph.search(null, OA, null).size();

        graph.createPolicyClass(33, "pc 33", null);
        graph.createNode(34, "oa 34", OA, Node.toProperties("namespace specific", "test specific"), 33);
        graph.createNode(35, "oa 35", OA, Node.toProperties("specific key 1", "specific value 1"), 33);

        Map<String, String> map = new HashMap<>();
                            map.put("specific key 1", "specific value 1");
                            map.put("specific key 2", "specific value 2");
        graph.createNode(36, "oa 36", OA, map, 33);

        // complete search
        Set<Node> nodes = graph.search("oa 36", OA, map);
        //todo : search method - edit method ?
        assertEquals(1, nodes.size());

        // one property
        nodes = graph.search(null, null, Node.toProperties("specific key 1", "specific value 1"));
        assertEquals(2, nodes.size());

        // just namespace
        nodes = graph.search(null, null, Node.toProperties("namespace specific", "test specific"));
        assertEquals(1, nodes.size());

        // name, type, namespace
        nodes = graph.search("oa 34", OA, Node.toProperties("namespace specific", "test specific"));
        assertEquals(1, nodes.size());


        nodes = graph.search(null, OA, null);
        assertEquals(3 + count_OA, nodes.size());

        nodes = graph.search(null, null, null);
        assertEquals(graph.getNodes().size(), nodes.size());
    }

    @Test
    void testGetNodes() throws PMException {
        int count_nodes = graph.getNodes().size();

        graph.createPolicyClass(40, "pc 40", null);
        graph.createNode(41, "node 41", OA, null, 40);
        graph.createNode(42, "node 42", OA, null, 40);
        graph.createNode(43, "node 43", OA, null, 40);

        assertEquals(4 + count_nodes, graph.getNodes().size());
    }

    @Test
    void testGetNode() throws PMException {
        assertThrows(IllegalArgumentException.class, () -> graph.getNode(123456789));

        Node node = graph.createPolicyClass(44, "pc 44", null);
        node = graph.getNode(node.getID());
        assertEquals("pc 44", node.getName());
        assertEquals(PC, node.getType());
    }

}
