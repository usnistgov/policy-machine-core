package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.epp.events.AssignEvent;
import gov.nist.csd.pm.epp.events.AssignToEvent;
import gov.nist.csd.pm.epp.events.DeassignEvent;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.MemPAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.audit.PReviewAuditor;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pdp.services.GraphService;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.common.FunctionalEntity;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.memory.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.memory.MemObligations;
import gov.nist.csd.pm.pip.memory.MemPIP;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.evr.EVRParser;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.obligations.model.Rule;
import gov.nist.csd.pm.pip.memory.MemProhibitions;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        OperationSet ops = new OperationSet("read", "write", "execute");
        FunctionalEntity functionalEntity = new MemPIP(new MemGraph(), new MemProhibitions(), new MemObligations());
        pdp = PDP.newPDP(
                new MemPAP(functionalEntity),
                new EPPOptions(),
                new PReviewDecider(functionalEntity.getGraph(), functionalEntity.getProhibitions(), ops),
                new PReviewAuditor(functionalEntity.getGraph(), ops)
        );
        Graph graph = pdp.withUser(new UserContext("super")).getGraph();
        pc1 = graph.createPolicyClass("pc1", null);
        oa1 = graph.createNode("oa1", NodeType.OA, null, pc1.getName());
        graph.createNode("oa2", NodeType.OA, null, pc1.getName());
        o1 = graph.createNode("o1", NodeType.O, null, oa1.getName());
        ua1 = graph.createNode("ua1", NodeType.UA, null, pc1.getName());
        u1 = graph.createNode("u1", NodeType.U, null, ua1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("read", "write"));
    }

    @Test
    void TestEvent() throws PMException, IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("epp/event_test.yml");
        String yml = IOUtils.toString(is, StandardCharsets.UTF_8.name());
        Obligation obligation = new EVRParser()
                .parse("super", yml);

        UserContext superCtx = new UserContext("super");
        pdp.withUser(superCtx).getObligations().add(obligation, true);

        // test u1 assign to
        pdp.getEPP().processEvent(new AssignToEvent(new UserContext(u1.getName(), "123"), oa1, o1));
        Node node = pdp.withUser(superCtx).getGraph().getNode("u1 assign to success");
        assertTrue(node.getProperties().containsKey("prop1"));
        assertTrue(node.getProperties().get("prop1").equalsIgnoreCase("val1"));

        // test anyUser assign
        pdp.getEPP().processEvent(new AssignEvent(new UserContext(u1.getName(), "123"), o1, oa1));
        node = pdp.withUser(superCtx).getGraph().getNode("anyUser assign success");

        // test anyUser in list deassign
        pdp.getEPP().processEvent(new DeassignEvent(new UserContext(u1.getName(),"123"), o1, oa1));
        node = pdp.withUser(superCtx).getGraph().getNode("anyUser in list deassign success");
    }

    @Test
    void TestResponse() throws PMException, IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("epp/response_test.yml");
        String yml = IOUtils.toString(is, StandardCharsets.UTF_8.name());
        UserContext superCtx = new UserContext("super");

        Obligation obligation = new EVRParser().parse(superCtx.getUser(), yml);
        pdp.withUser(superCtx).getObligations().add(obligation, true);

        pdp.getEPP().processEvent(new AssignToEvent(new UserContext(u1.getName(), "123"), oa1, o1));

        // check that the rule was created
        Obligation o = pdp.withUser(superCtx).getObligations().get("test");
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
        Node newOA = pdp.withUser(superCtx).getGraph().getNode("new OA");

        // check that the new OA was assigned to the oa1
        Set<String> parents = pdp.withUser(superCtx).getGraph().getParents(newOA.getName());
        assertFalse(parents.isEmpty());
        assertEquals("oa2" , parents.iterator().next());

        // check ua1 was associated with new OA
        Map<String, OperationSet> sourceAssociations = pdp.withUser(superCtx).getGraph().getSourceAssociations(ua1.getName());
        assertTrue(sourceAssociations.containsKey(newOA.getName()));

        // check that the deny was created
        // an exception is thrown if one doesnt exist
        pdp.withUser(superCtx).getProhibitions().get("deny");
    }

    @Test
    void testUserContainedIn() throws PMException, IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("epp/UserContainedIn.yml");
        String yml = IOUtils.toString(is, StandardCharsets.UTF_8.name());
        Obligation obligation = new EVRParser().parse("super", yml);

        Obligations obligations = new MemObligations();
        obligations.add(obligation, true);

        OperationSet ops = new OperationSet("read", "write", "execute");
        FunctionalEntity functionalEntity = new MemPIP(new MemGraph(), new MemProhibitions(), obligations);
        PDP pdp = PDP.newPDP(
                new MemPAP(functionalEntity),
                new EPPOptions(),
                new PReviewDecider(functionalEntity.getGraph(), functionalEntity.getProhibitions(), ops),
                new PReviewAuditor(functionalEntity.getGraph(), ops)
        );
        Graph graph = pdp.withUser(new UserContext("super")).getGraph();

        graph.createPolicyClass("pc1", null);
        graph.createNode("oa1", OA, null, "pc1");
        Node oa2 = graph.createNode("oa2", OA, null, "pc1");
        Node o1 = graph.createNode("o1", OA, null, "oa1");

        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("ua1-1", UA, null, "ua1");
        graph.createNode("u1", U, null, "ua1-1");

        pdp.getEPP().processEvent(new AssignToEvent(new UserContext("u1"), oa2, o1));

        assertTrue(graph.exists("new OA"));
    }
}
