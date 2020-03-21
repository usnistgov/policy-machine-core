package gov.nist.csd.pm.pdp.policy;

import gov.nist.csd.pm.epp.EPPOptions;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.obligations.MemObligations;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static gov.nist.csd.pm.operations.Operations.*;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.O;
import static org.junit.jupiter.api.Assertions.*;

class SuperPolicyTest {

    @Test
    void testSuperPolicyWithEmptyGraph() throws PMException {
        Graph graph = new MemGraph();
        SuperPolicy superPolicy = new SuperPolicy();
        superPolicy.configure(graph);
        testForSuperPolicy(graph);
    }

    private void testForSuperPolicy(Graph graph) throws PMException {
        assertTrue(graph.exists("super_pc"));
        assertTrue(graph.exists("super_pc_rep"));
        assertTrue(graph.exists("super_ua1"));
        assertTrue(graph.exists("super_ua2"));
        assertTrue(graph.exists("super"));
        assertTrue(graph.exists("super_oa"));

        assertTrue(graph.getParents("super").containsAll(Arrays.asList("super_ua1", "super_ua2")));
        assertTrue(graph.getParents("super_ua1").containsAll(Arrays.asList("super_pc")));
        assertTrue(graph.getParents("super_ua2").containsAll(Arrays.asList("super_pc")));
        assertTrue(graph.getParents("super_oa").containsAll(Arrays.asList("super_pc")));
        assertTrue(graph.getParents("super_pc_rep").containsAll(Arrays.asList("super_oa")));

        assertTrue(graph.getSourceAssociations("super_ua1").containsKey("super_oa"));
        assertTrue(graph.getSourceAssociations("super_ua2").containsKey("super_ua1"));
    }

    @Test
    void UsingTheFunctionalComponents() throws PMException {
        // instantiate objects in the Policy Information Point (PIP) package: MemGraph, MemProhibitions, and MemObligations
        Graph graph = new MemGraph();
        Prohibitions prohibitions = new MemProhibitions();
        Obligations obligations = new MemObligations();

        // add some nodes, assignments, and associations to the graph
        // create a policy class
        Node pc1 = graph.createPolicyClass("pc1", Node.toProperties("k", "v"));
        // create an object and user attribute and assign to pc1
        Node oa1 = graph.createNode("oa1", NodeType.OA, Node.toProperties("k1", "v1"), pc1.getName());
        Node ua1 = graph.createNode("ua1", NodeType.UA, Node.toProperties("k1", "v1"), pc1.getName());
        // create and object and user
        Node o1 = graph.createNode("o1", O, Node.toProperties("k", "v"), oa1.getName());
        Node u1 = graph.createNode("u1", NodeType.U, Node.toProperties("k", "v"), ua1.getName());
        // associate ua1 and oa1
        graph.associate(ua1.getName(), oa1.getName(), new OperationSet(READ, WRITE, ASSIGN, ASSIGN_TO));

        // add a prohibition
        Prohibition prohibition = new Prohibition.Builder("test-prohibition", "ua1", new OperationSet("write"))
                .addContainer("oa1", false)
                .build();
        prohibitions.add(prohibition);

        // note: obligations will be demonstrated in another tutorial

        // create a new Policy Administration Point (PAP)
        PAP pap = new PAP(graph, prohibitions, obligations);

        // create a new Policy Decision Point
        // we'll provide an empty EPPOptions to the PDP, they will be explained later
        PDP pdp = new PDP(pap, new EPPOptions());

        // access the PDP's GraphService (which sits in front of the Graph made earlier) as u1
        Graph graphService = pdp.getGraphService(new UserContext(u1.getName(), ""));
        Node newNode = graphService.createNode("newNode", O, null, oa1.getName());


    }
}