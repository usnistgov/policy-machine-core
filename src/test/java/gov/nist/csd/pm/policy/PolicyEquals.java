package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PolicyEquals {

    public static void check(Policy a, Policy b) throws PMException {
        // check nodes
        // assignments
        // associations
        List<String> aNodes = a.graph().search(NodeType.ANY, NO_PROPERTIES);
        List<String> bNodes = b.graph().search(NodeType.ANY, NO_PROPERTIES);
        assertTrue(aNodes.containsAll(bNodes));
        assertTrue(bNodes.containsAll(aNodes));

        for (String nodeName : aNodes) {
            Node aNode = a.graph().getNode(nodeName);
            Node bNode = b.graph().getNode(nodeName);
            assertEquals(aNode, bNode);

            List<String> aChildren = a.graph().getChildren(nodeName);
            List<String> aParents = a.graph().getParents(nodeName);

            List<String> bChildren = b.graph().getChildren(nodeName);
            List<String> bParents = b.graph().getParents(nodeName);

            assertTrue(aChildren.containsAll(bChildren));
            assertTrue(bChildren.containsAll(aChildren));
            assertTrue(aParents.containsAll(bParents));
            assertTrue(bParents.containsAll(aParents));

            List<Association> aSourceAssocs = a.graph().getAssociationsWithSource(nodeName);
            List<Association> aTargetAssocs = a.graph().getAssociationsWithTarget(nodeName);

            List<Association> bSourceAssocs = b.graph().getAssociationsWithSource(nodeName);
            List<Association> bTargetAssocs = b.graph().getAssociationsWithTarget(nodeName);

            assertTrue(aSourceAssocs.containsAll(bSourceAssocs));
            assertTrue(bSourceAssocs.containsAll(aSourceAssocs));
            assertTrue(aTargetAssocs.containsAll(bTargetAssocs));
            assertTrue(bTargetAssocs.containsAll(aTargetAssocs));
        }

        // check prohibitions
        Map<String, List<Prohibition>> aProhibitions = a.prohibitions().getProhibitions();
        Map<String, List<Prohibition>> bProhibitions = a.prohibitions().getProhibitions();

        assertTrue(aProhibitions.keySet().containsAll(bProhibitions.keySet()));
        assertTrue(aProhibitions.values().containsAll(bProhibitions.values()));

        // check obligations
        List<Obligation> aObligations = a.obligations().getObligations();
        List<Obligation> bObligations = b.obligations().getObligations();

        assertTrue(aObligations.containsAll(bObligations));
        assertTrue(bObligations.containsAll(aObligations));
    }

}
