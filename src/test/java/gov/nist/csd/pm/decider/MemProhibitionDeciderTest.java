package gov.nist.csd.pm.decider;

import gov.nist.csd.pm.exceptions.PMDBException;
import gov.nist.csd.pm.exceptions.PMGraphException;
import gov.nist.csd.pm.graph.Graph;
import gov.nist.csd.pm.graph.MemGraph;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import gov.nist.csd.pm.graph.model.nodes.NodeUtils;
import gov.nist.csd.pm.prohibitions.model.Prohibition;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static gov.nist.csd.pm.graph.model.nodes.NodeType.*;
import static org.junit.jupiter.api.Assertions.*;

class MemProhibitionDeciderTest {

    @Test
    void testListProhibitedPermissions() throws PMGraphException, PMDBException {
        Graph graph = new MemGraph();
        // create a node for each type
        long pc1 = graph.createNode(new NodeContext(1, "pc1", PC, NodeUtils.toProperties("key1", "value1")));
        long oa1 = graph.createNode(new NodeContext(2, "oa1", OA, NodeUtils.toProperties("key1", "value1")));
        long o1 = graph.createNode(new NodeContext(3, "o1", O, NodeUtils.toProperties("key1", "value1")));
        long ua1 = graph.createNode(new NodeContext(4, "ua1", UA, NodeUtils.toProperties("key1", "value1")));
        long u1 = graph.createNode(new NodeContext(5, "u1", U, NodeUtils.toProperties("key1", "value1")));

        // create assignments
        graph.assign(new NodeContext(o1, O), new NodeContext(oa1, OA));
        graph.assign(new NodeContext(oa1, OA), new NodeContext(pc1, PC));
        graph.assign(new NodeContext(u1, U), new NodeContext(ua1, UA));
        graph.assign(new NodeContext(ua1, UA), new NodeContext(pc1, PC));

        // create an association
        graph.associate(new NodeContext(ua1, UA), new NodeContext(oa1, OA), new HashSet<>(Arrays.asList("read", "write")));

        // create a prohibition for u1 on oa1
        Prohibition prohibition = new Prohibition();
        prohibition.setName("deny123");
        prohibition.setSubject(new Prohibition.Subject(u1, Prohibition.SubjectType.USER));
        prohibition.setIntersection(false);
        prohibition.setOperations(new HashSet<>(Arrays.asList("read")));
        prohibition.addNode(new NodeContext().id(oa1).complement(false));

        // create a new policy decider
        ProhibitionDecider decider = new MemProhibitionDecider(graph, Arrays.asList(prohibition));

        HashSet<String> permissions = decider.listProhibitedPermissions(u1, o1);
        assertTrue(permissions.contains("read"));
    }
}