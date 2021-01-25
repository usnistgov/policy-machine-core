package gov.nist.csd.pm.pip.graph;

import com.google.gson.Gson;
import gov.nist.csd.pm.exceptions.PIPException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.mysql.MySQLConnection;
import gov.nist.csd.pm.pip.graph.mysql.MySQLGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;
import static org.junit.jupiter.api.Assertions.*;

public class MySQLGraphTest {


    private MySQLGraph graph;

    @BeforeEach
    void init() throws Exception {
        MySQLConnection connection = new MySQLConnection();
        this.graph = new MySQLGraph(connection);
        this.graph.deleteAll();
        this.graph = new MySQLGraph(connection);
    }

    @Test
    void testCreateNode() throws PIPException {

        Node pc = graph.createPolicyClass( "pc1", null);
        assertTrue(graph.getPolicyClasses().contains(pc.getName()));

        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(null, null, null, "pc")),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode(null, OA, null, "pc")),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.createNode("name", null, null, "pc"))
        );


        // add non pc
        Node node = graph.createNode("oa2", OA, Node.toProperties("namespace", "test"), pc.getName());

        // check node is added
        node = graph.getNode(node.getName());
        assertEquals("oa2", node.getName());
        assertEquals(OA, node.getType());
    }

    //@Test
    void testUpdateNode() throws PIPException {
        //We use the same method with the ID so we keep the exception
        // create another method getNode from id to retrieve the proper node before updating it with the proper values

        Node node = graph.createPolicyClass("node PC 3", Node.toProperties("namespace", "test"));

        // node not found
        assertThrows(IllegalArgumentException.class, () -> graph.updateNode(123,"not an existing node", null));

        // update name
        graph.updateNode(node.getId(), "updated name 3", null);
        assertEquals(graph.getNode(node.getName()).getName(), "updated name 3");

        // update properties
        graph.updateNode(node.getId(), "updated name 3", Node.toProperties("newKey", "newValue"));
        assertEquals(graph.getNode(node.getName()).getProperties().get("newKey"), "newValue");
    }

    @Test
    void testDeleteNode() throws PIPException {
        Node node = graph.createPolicyClass("node test 4", Node.toProperties("namespace", "test"));
        graph.deleteNode(node.getName());

        // deleted from the graph
        assertFalse(graph.exists(node.getName()));

        graph.createPolicyClass("new pc", null);
        // deleted from list of policies
        assertFalse(graph.getPolicyClasses().contains(node.getName()));

        //todo: handle exception foreign key constraint
    }

    @Test
    void testExists() throws PIPException {
        graph.createPolicyClass("pc1", null);
        Node oa1 = graph.createNode("OA 5", OA,null, "pc1");
        Node oa = graph.createNode("oa 6", OA, null, oa1.getName());

        assertTrue(graph.exists(oa.getName()));
        assertFalse(graph.exists("Not an existing node"));
    }

    @Test
    void testGetPolicies() throws PIPException {

        graph.createPolicyClass("pc1", null);
        int total = graph.getPolicyClasses().size();
        graph.createPolicyClass("nodePC7", null);
        graph.createPolicyClass("nodePC8", null);
        graph.createPolicyClass("nodePC9", null);

        assertEquals(total+3, graph.getPolicyClasses().size());
    }

    @Test
    void testGetChildren() throws PIPException {

        assertThrows(PIPException.class, () -> graph.getChildren("Not an existing node"));

        Node parentNode = graph.createPolicyClass("parent10",Node.toProperties("firstValue", "test"));
        Node child1Node = graph.createNode("child11", OA, null, "parent10");
        Node child2Node = graph.createNode("child12", OA, null, "parent10");

        Set<String> children = graph.getChildren(parentNode.getName());
        assertTrue(children.containsAll(Arrays.asList(child1Node.getName(), child2Node.getName())));
    }

    @Test
    void testGetParents() throws PIPException {

        assertThrows(PIPException.class, () -> graph.getChildren("Not an existing node"));

        Node parent1Node = graph.createPolicyClass( "parent13", null);
        Node parent2Node = graph.createNode("parent14", OA, null, "parent13");
        Node child1Node = graph.createNode("child15", OA, null, "parent13", "parent14");

        Set<String> parents = graph.getParents(child1Node.getName());
        assertTrue(parents.contains(parent1Node.getName()));
        assertTrue(parents.contains(parent2Node.getName()));
    }

    @Test
    void testAssign() throws PIPException {

        Node parent1Node = graph.createPolicyClass("parent16", null);
        Node child1Node = graph.createNode("childtest17", OA, null, "parent16");
        Node child2Node = graph.createNode("childtest18", OA, null, "parent16");

        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> graph.assign("parent16", "Not an existing node")),
                () -> assertThrows(IllegalArgumentException.class, () -> graph.assign("Not an existing node", "parent16"))
        );

        graph.assign(child1Node.getName(), child2Node.getName());

        assertTrue(graph.getChildren(parent1Node.getName()).contains(child1Node.getName()));
        assertTrue(graph.getParents(child1Node.getName()).contains(parent1Node.getName()));
    }

    @Test
    void testDeassign() throws PIPException {

        Node parent1Node = graph.createPolicyClass("parent19", null);
        Node child1Node = graph.createNode("childtest20", OA, null, "parent19");

        assertThrows(IllegalArgumentException.class, () -> graph.assign("0", "0"));
        assertThrows(IllegalArgumentException.class, () -> graph.assign(child1Node.getName(), "0"));

        graph.deassign(child1Node.getName(), parent1Node.getName());

        assertFalse(graph.getChildren(parent1Node.getName()).contains(child1Node.getName()));
        assertFalse(graph.getParents(child1Node.getName()).contains(parent1Node.getName()));
    }

    @Test
    void testAssociate() throws PIPException {
        Node pcNode = graph.createPolicyClass("pc21", null);
        Node uaNode = graph.createNode("subject 22", UA, null, "pc21");
        Node targetNode = graph.createNode("target 23", OA, null, "pc21");

        graph.associate(uaNode.getName(), targetNode.getName(), new OperationSet("read", "write"));

        Map<String, OperationSet> associations = graph.getSourceAssociations(uaNode.getName());

        assertTrue(associations.containsKey(targetNode.getName()));
        assertTrue(associations.get(targetNode.getName()).containsAll(Arrays.asList("read", "write")));


        associations = graph.getTargetAssociations(targetNode.getName());
        assertTrue(associations.containsKey(uaNode.getName()));
        assertTrue(associations.get(uaNode.getName()).containsAll(Arrays.asList("read", "write")));

    }

    @Test
    void testDissociate() throws PIPException {

        Node pcNode = graph.createPolicyClass( "pc24", null);
        Node uaNode = graph.createNode("subject 25", UA, null, "pc24");
        Node targetNode = graph.createNode("target 26", OA, null, "pc24");

        graph.associate(uaNode.getName(), targetNode.getName(), new OperationSet("read", "write"));
        graph.dissociate(uaNode.getName(), targetNode.getName());

        Map<String, OperationSet> associations = graph.getSourceAssociations(uaNode.getName());
        assertFalse(associations.containsKey(targetNode.getName()));

        associations = graph.getTargetAssociations(targetNode.getName());
        assertFalse(associations.containsKey(uaNode.getName()));
    }


    @Test
    void testGetSourceAssociations() throws PIPException {

        Node pcNode = graph.createPolicyClass("pc 27", null);
        Node uaNode = graph.createNode("subject 28", UA, null, "pc 27");
        Node targetNode = graph.createNode("target 29", OA, null, "pc 27");

        graph.associate(uaNode.getName(), targetNode.getName(), new OperationSet("read", "write"));

        Map<String, OperationSet> associations = graph.getSourceAssociations(uaNode.getName());

        assertTrue(associations.containsKey(targetNode.getName()));
        assertThrows(PIPException.class, () -> graph.getSourceAssociations("Not an existing node"));
        assertTrue(associations.get(targetNode.getName()).containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testGetTargetAssociations() throws PIPException {

        Node pcNode = graph.createPolicyClass("pc 30", null);
        Node uaNode = graph.createNode("subject 31", UA, null, "pc 30");
        Node targetNode = graph.createNode("target 32", OA, null, "pc 30");

        graph.associate(uaNode.getName(), targetNode.getName(), new OperationSet("read", "write"));

        Map<String, OperationSet> associations = graph.getTargetAssociations(targetNode.getName());

        assertTrue(associations.containsKey(uaNode.getName()));
        assertThrows(PIPException.class, () -> graph.getTargetAssociations("Not an existing node"));

        assertTrue(associations.get(uaNode.getName()).containsAll(Arrays.asList("read", "write")));
    }

    @Test
    void testSearch() throws PIPException {
        int count_OA = graph.search(OA, null).size();

        graph.createPolicyClass("pc 33", null);
        graph.createNode("oa 34", OA, Node.toProperties("namespace specific", "test specific"), "pc 33");
        graph.createNode("oa 35", OA, Node.toProperties("specific key 1", "specific value 1"), "pc 33");

        Map<String, String> map = new HashMap<>();
                            map.put("specific key 1", "specific value 1");
                            map.put("specific key 2", "specific value 2");
        graph.createNode("oa 36", OA, map, "pc 33");

        // complete search
        Set<Node> nodes = graph.search(OA, map);
        assertEquals(1, nodes.size());

        // one property
        nodes = graph.search(null, Node.toProperties("specific key 1", "specific value 1"));
        assertEquals(2, nodes.size());

        // just namespace
        nodes = graph.search(null, Node.toProperties("namespace specific", "test specific"));
        assertEquals(1, nodes.size());

        // name, type, namespace
        nodes = graph.search(OA, Node.toProperties("namespace specific", "test specific"));
        assertEquals(1, nodes.size());


        nodes = graph.search(OA, null);
        assertEquals(3 + count_OA, nodes.size());

        nodes = graph.search(null, null);
        assertEquals(graph.getNodes().size(), nodes.size());
    }

    @Test
    void testGetNodes() throws PIPException {
        int count_nodes = graph.getNodes().size();

        graph.createPolicyClass("pc 40", null);
        graph.createNode("node 41", OA, null, "pc 40");
        graph.createNode("node 42", OA, null, "pc 40");
        graph.createNode("node 43", OA, null, "pc 40");

        assertEquals(4 + count_nodes, graph.getNodes().size());
    }

    @Test
    void testGetNode() throws PIPException {
        Node node = graph.createPolicyClass("pc 44", null);
        node = graph.getNode(node.getName());
        assertEquals("pc 44", node.getName());
        assertEquals(PC, node.getType());
    }

    String json = "{" +
            "\"nodes\": [" +
                    "{" +
                        "\"name\": \"sample PC\", " +
                        "\"type\": \"PC\", " +
                        "\"properties\": {" +
                        "\"namespace\": \"super_sample\"\n" +
                        "}" +
                    "}," +
                    "{" +
                        "\"name\": \"sample UA\", " +
                        "\"type\": \"UA\", " +
                        "\"properties\": {" +
                        "\"namespace\": \"super_ua\"\n" +
                        "}" +
                    "}," +
                    "{" +
                        "\"name\": \"sample OA\", " +
                        "\"type\": \"OA\", " +
                        "\"properties\": {" +
                        "\"namespace\": \"super_oa\"\n" +
                        "}" +
                    "}," +
                    "{" +
                        "\"name\": \"sample UA2\", " +
                        "\"type\": \"UA\", " +
                        "\"properties\": {" +
                        "\"namespace\": \"super_ua2\"\n" +
                        "}" +
                    "}" +
            "]," +
            "\"assignments\": [" +
                    "[" +
                        "\"sample UA\"," +
                        "\"sample PC\"" +
                    "]," +
                    "[" +
                        "\"sample OA\"," +
                        "\"sample PC\"" +
                    "]," +
                    "[" +
                        "\"sample UA2\"," +
                        "\"sample PC\"" +
                    "]" +
            "]," +
            "\"associations\": [" +
                    "{" +
                        "\"source\": \"sample UA\"," +
                        "\"target\": \"sample UA2\", " +
                        "\"operations\": [" +
                        "\"*\"" +
                        "]" +
                    "}" +
            "]" +
    "}";
    @Test
    void testToJson() throws PMException {
        graph.createPolicyClass("pc1", null);
        graph.createNode("new node", UA, Node.toProperties("namespace", "super"), "pc1");

        MySQLGraph.JsonGraph jsonGraph = new Gson().fromJson(json, MySQLGraph.JsonGraph.class);

        assertThrows(NoSuchElementException.class, () -> jsonGraph.getNodes().stream().filter(node ->
                node.getName().equalsIgnoreCase("not in the JSON")).iterator().next());
    }

    @Test
    void testFromJson() throws PMException {
        graph.createPolicyClass("pc1", null);
        graph.createNode("new node", UA, Node.toProperties("namespace", "super"), "pc1");

        String json2 = graph.toJson();
        assertTrue(json2.contains("\"name\": \"new node\""));
        String assign = "\"new node\",\n" +
                "      \"pc1\"";
        /*String associate = "      \"source\": \"sample UA\",\n" +
                "      \"target\": \"sample UA2\",\n" +
                "      \"operations\": [\n" +
                "        \"*\"\n" +
                "      ]";*/
        assertTrue(json2.contains(assign));
    }
}
