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
        long pc1 = graph.createNode(new Node(1, "pc1", PC, Node.toProperties("key1", "value1")));
        long oa1 = graph.createNode(new Node(2, "oa1", OA, Node.toProperties("key1", "value1")));
        long o1 = graph.createNode(new Node(3, "o1", O, Node.toProperties("key1", "value1")));
        long ua1 = graph.createNode(new Node(4, "ua1", UA, Node.toProperties("key1", "value1")));
        long u1 = graph.createNode(new Node(5, "u1", U, Node.toProperties("key1", "value1")));

        // create assignments
        graph.assign(new Node(o1, O), new Node(oa1, OA));
        graph.assign(new Node(oa1, OA), new Node(pc1, PC));
        graph.assign(new Node(u1, U), new Node(ua1, UA));
        graph.assign(new Node(ua1, UA), new Node(pc1, PC));

        // create an association
        graph.associate(new Node(ua1, UA), new Node(oa1, OA), new HashSet<>(Arrays.asList("read", "write")));

        // create a prohibition for u1 on oa1
        Prohibition prohibition = new Prohibition();
        prohibition.setName("deny123");
        prohibition.setSubject(new Prohibition.Subject(u1, Prohibition.Subject.Type.USER));
        prohibition.setIntersection(false);
        prohibition.setOperations(new HashSet<>(Arrays.asList("read")));
        prohibition.addNode(new Prohibition.Node(oa1, false));

        // create a new policy decider
        ProhibitionDecider decider = new MemProhibitionDecider(graph, Arrays.asList(prohibition));

        Set<String> permissions = decider.listProhibitedPermissions(u1, o1);
        assertTrue(permissions.contains("read"));
    }
}