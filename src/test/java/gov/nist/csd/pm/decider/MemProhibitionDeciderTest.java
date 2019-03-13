package gov.nist.csd.pm.decider;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.Graph;
import gov.nist.csd.pm.graph.MemGraph;
import gov.nist.csd.pm.graph.model.nodes.Node;
import gov.nist.csd.pm.prohibitions.model.Prohibition;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static gov.nist.csd.pm.graph.model.nodes.NodeType.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemProhibitionDeciderTest {

    @Test
    void testListProhibitedPermissions() throws PMException {
        Graph graph = new MemGraph();
        // create a node for each type
        Node pc1 = graph.createNode(1, "pc1", PC, Node.toProperties("key1", "value1"));
        Node oa1 = graph.createNode(2, "oa1", OA, Node.toProperties("key1", "value1"));
        Node o1 = graph.createNode(3, "o1", O, Node.toProperties("key1", "value1"));
        Node ua1 = graph.createNode(4, "ua1", UA, Node.toProperties("key1", "value1"));
        Node u1 = graph.createNode(5, "u1", U, Node.toProperties("key1", "value1"));

        // create assignments
        graph.assign(o1.getID(), oa1.getID());
        graph.assign(oa1.getID(), pc1.getID());
        graph.assign(u1.getID(), ua1.getID());
        graph.assign(ua1.getID(), pc1.getID());

        // create an association
        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read", "write")));

        // create a prohibition for u1 on oa1
        Prohibition prohibition = new Prohibition();
        prohibition.setName("deny123");
        prohibition.setSubject(new Prohibition.Subject(u1.getID(), Prohibition.Subject.Type.USER));
        prohibition.setIntersection(false);
        prohibition.setOperations(new HashSet<>(Arrays.asList("read")));
        prohibition.addNode(new Prohibition.Node(oa1.getID(), false));

        // create a new policy decider
        ProhibitionDecider decider = new MemProhibitionDecider(graph, Arrays.asList(prohibition));

        Set<String> permissions = decider.listProhibitedPermissions(u1.getID(), o1.getID());
        assertTrue(permissions.contains("read"));
    }
}