package gov.nist.csd.pm.util;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.pap.obligation.Obligation;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.pap.query.PolicyQuery;

import java.util.Collection;
import java.util.HashSet;

import static gov.nist.csd.pm.common.graph.node.Properties.NO_PROPERTIES;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PolicyEquals {

    public static void assertPolicyEquals(PolicyQuery a, PolicyQuery b) throws PMException {
        // check nodes
        // assignments
        // associations
        HashSet<Node> aNodes = new HashSet<>(a.graph().search(NodeType.ANY, NO_PROPERTIES));
        HashSet<Node> bNodes = new HashSet<>(b.graph().search(NodeType.ANY, NO_PROPERTIES));
        assertEquals(aNodes, bNodes);

        for (Node node : aNodes) {
            Node aNode = a.graph().getNodeByName(node.getName());
            Node bNode = b.graph().getNodeByName(node.getName());
            assertEquals(aNode, bNode);

            Collection<Long> aAscendants = new HashSet<>(a.graph().getAdjacentAscendants(node.getId()));
            Collection<Long> aDescendants = new HashSet<>(a.graph().getAdjacentDescendants(node.getId()));

            Collection<Long> bAscendants = new HashSet<>(b.graph().getAdjacentAscendants(node.getId()));
            Collection<Long> bDescendants = new HashSet<>(b.graph().getAdjacentDescendants(node.getId()));

            assertEquals(aAscendants, bAscendants);
            assertEquals(aDescendants, bDescendants);

            Collection<Association> aSourceAssocs = new HashSet<>(a.graph().getAssociationsWithSource(node.getId()));
            Collection<Association> aTargetAssocs = new HashSet<>(a.graph().getAssociationsWithTarget(node.getId()));

            Collection<Association> bSourceAssocs = new HashSet<>(b.graph().getAssociationsWithSource(node.getId()));
            Collection<Association> bTargetAssocs = new HashSet<>(b.graph().getAssociationsWithTarget(node.getId()));

            assertEquals(aSourceAssocs, bSourceAssocs);
            assertEquals(aTargetAssocs, bTargetAssocs);
        }

        // check prohibitions
        Collection<Prohibition> aProhibitions = a.prohibitions().getProhibitions();
        Collection<Prohibition> bProhibitions = b.prohibitions().getProhibitions();

        assertEquals(aProhibitions, bProhibitions);

        // check obligations
        Collection<Obligation> aObligations = new HashSet<>(a.obligations().getObligations());
        Collection<Obligation> bObligations = new HashSet<>(b.obligations().getObligations());

        assertEquals(aObligations, bObligations);

        // check operations
        AccessRightSet aResOps = a.operations().getResourceOperations();
        AccessRightSet bResOps = b.operations().getResourceOperations();
        assertEquals(aResOps, bResOps);

        Collection<String> aOps = new HashSet<>(a.operations().getAdminOperationNames());
        Collection<String> bOps = new HashSet<>(b.operations().getAdminOperationNames());
        assertEquals(aOps, bOps);

        // check routines
        Collection<String> aRoutines = new HashSet<>(a.routines().getAdminRoutineNames());
        Collection<String> bRoutines = new HashSet<>(b.routines().getAdminRoutineNames());
        assertEquals(aRoutines, bRoutines);
    }
}
