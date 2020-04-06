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
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.evr.EVRParser;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.obligations.model.Rule;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.*;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;
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
        pc1 = graph.createPolicyClass("pc1", null);
        ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        oa1 = graph.createNode("oa1", OA, null, pc1.getName());
        graph.createNode("oa2", OA, null, pc1.getName());
        o1 = graph.createNode("o1", NodeType.O, null, oa1.getName());
        u1 = graph.createNode("u1", U, null, ua1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read", "write"));

        pdp = new PDP(new PAP(graph, new MemProhibitions(), new MemObligations()), null);
    }

    @Test
    void TestEvent() throws PMException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("epp/event_test.yml");
        Obligation obligation = EVRParser.parse(is);
        pdp.getPAP().getObligationsPAP().add(obligation, true);

        // test u1 assign to
        pdp.getEPP().processEvent(new AssignToEvent(oa1, o1), u1.getName(), "123");
        Node node = pdp.getPAP().getGraphPAP().getNode("u1 assign to success");
        assertTrue(node.getProperties().containsKey("prop1"));
        assertTrue(node.getProperties().get("prop1").equalsIgnoreCase("val1"));

        // test anyUser assign
        pdp.getEPP().processEvent(new AssignEvent(o1, oa1), u1.getName(), "123");
        node = pdp.getPAP().getGraphPAP().getNode("anyUser assign success");

        // test anyUser in list deassign
        pdp.getEPP().processEvent(new DeassignEvent(o1, oa1), u1.getName(),"123");
        node = pdp.getPAP().getGraphPAP().getNode("anyUser in list deassign success");
    }

    @Test
    void TestResponse() throws PMException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("epp/response_test.yml");
        Obligation obligation = EVRParser.parse(is);
        pdp.getPAP().getObligationsPAP().add(obligation, true);

        pdp.getEPP().processEvent(new AssignToEvent(oa1, o1), u1.getName(), "123");

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
        Node newOA = pdp.getPAP().getGraphPAP().getNode("new OA");

        // check that the new OA was assigned to the oa1
        Set<String> parents = pdp.getPAP().getGraphPAP().getParents(newOA.getName());
        assertFalse(parents.isEmpty());
        assertEquals(oa1.getName(), parents.iterator().next());

        // check ua1 was associated with new OA
        Map<String, OperationSet> sourceAssociations = pdp.getPAP().getGraphPAP().getSourceAssociations(ua1.getName());
        assertTrue(sourceAssociations.containsKey(newOA.getName()));

        // check that the deny was created
        // an exception is thrown if one doesnt exist
        pdp.getPAP().getProhibitionsPAP().get("deny");
    }

    @Test
    void testUserContainedIn() throws PMException {
        Graph graph = new MemGraph();

        graph.createPolicyClass("pc1", null);
        graph.createNode("oa1", OA, null, "pc1");
        Node oa2 = graph.createNode("oa2", OA, null, "pc1");
        Node o1 = graph.createNode("o1", OA, null, "oa1");

        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("ua1-1", UA, null, "ua1");
        graph.createNode("u1", U, null, "ua1-1");

        InputStream is = getClass().getClassLoader().getResourceAsStream("epp/UserContainedIn.yml");
        Obligation obligation = EVRParser.parse(is);

        Obligations obligations = new MemObligations();
        obligations.add(obligation, true);

        PDP pdp = new PDP(new PAP(graph, new MemProhibitions(), obligations), new EPPOptions());
        pdp.getEPP().processEvent(new AssignToEvent(oa2, o1), "u1", "");

        assertTrue(graph.exists("new OA"));
    }
}