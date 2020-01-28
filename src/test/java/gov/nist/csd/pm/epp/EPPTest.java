package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.epp.events.AssignEvent;
import gov.nist.csd.pm.epp.events.AssignToEvent;
import gov.nist.csd.pm.epp.events.DeassignEvent;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.obligations.MemObligations;
import gov.nist.csd.pm.pip.obligations.evr.EVRParser;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.obligations.model.Rule;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class EPPTest {

    private PDP pdp;
    private Node u1;
    private Node ua1;
    private Node o1;
    private Node oa1;
    private Node pc1;

    @BeforeEach
    void setup() throws PMException {
        Graph graph = new MemGraph();
        pc1 = graph.createPolicyClass(new Random().nextLong(), "pc1", null);
        ua1 = graph.createNode(new Random().nextLong(), "ua1", NodeType.UA, null, pc1.getID());
        oa1 = graph.createNode(new Random().nextLong(), "oa1", NodeType.OA, null, pc1.getID());
        o1 = graph.createNode(new Random().nextLong(), "o1", NodeType.O, null, oa1.getID());
        u1 = graph.createNode(new Random().nextLong(), "u1", NodeType.U, null, ua1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read", "write"));

        pdp = new PDP(new PAP(graph, new MemProhibitions(), new MemObligations()), null);
    }

    @Test
    void TestEvent() throws PMException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("epp/event_test.yml");
        Obligation obligation = EVRParser.parse(is);
        pdp.getPAP().getObligationsPAP().add(obligation, true);

        // test u1 assign to
        pdp.getEPP().processEvent(new AssignToEvent(oa1, o1), u1.getID(), 123);
        Set<Node> search = pdp.getPAP().getGraphPAP().search("u1 assign to success", null, null);
        assertFalse(search.isEmpty());
        Node node = search.iterator().next();
        assertTrue(node.getProperties().containsKey("prop1"));
        assertTrue(node.getProperties().get("prop1").equalsIgnoreCase("val1"));

        // test anyUser assign
        pdp.getEPP().processEvent(new AssignEvent(o1, oa1), u1.getID(), 123);
        search = pdp.getPAP().getGraphPAP().search("anyUser assign success", null, null);
        assertFalse(search.isEmpty());

        // test anyUser in list deassign
        pdp.getEPP().processEvent(new DeassignEvent(o1, oa1), u1.getID(),123);
        search = pdp.getPAP().getGraphPAP().search("anyUser in list deassign success", null, null);
        assertFalse(search.isEmpty());
    }

    @Test
    void TestResponse() throws PMException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("epp/response_test.yml");
        Obligation obligation = EVRParser.parse(is);
        pdp.getPAP().getObligationsPAP().add(obligation, true);

        pdp.getEPP().processEvent(new AssignToEvent(oa1, o1), u1.getID(), 123);

        // check that the rule was created
        Obligation o = pdp.getPAP().getObligationsPAP().get("test");
        List<Rule> rules = o.getRules();
        boolean found = false;
        for (Rule r : rules) {
            if (r.getLabel().equals("created rule")) {
                found = true;
                break;
            }
        }
        assertTrue(found);

        // check that the new OA was created
        Set<Node> search = pdp.getPAP().getGraphPAP().search("new OA", null, null);
        assertFalse(search.isEmpty());
        Node newOA = search.iterator().next();

        // check that the new OA was assigned to the oa1
        Set<Node> parents = pdp.getPAP().getGraphPAP().getParents(newOA.getID());
        assertFalse(parents.isEmpty());
        assertTrue(parents.iterator().next().getID() == oa1.getID());

        // check ua1 was associated with new OA
        Map<Long, Set<String>> sourceAssociations = pdp.getPAP().getGraphPAP().getSourceAssociations(ua1.getID());
        assertTrue(sourceAssociations.containsKey(newOA.getID()));

        // check that the deny was created
        // an exception is thrown if one doesnt exist
        pdp.getPAP().getProhibitionsPAP().get("deny");
    }
}