package gov.nist.csd.pm.util;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.node.Node;
import gov.nist.csd.pm.pap.graph.node.NodeType;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.graph.relationship.Association;
import gov.nist.csd.pm.pap.obligation.Obligation;
import gov.nist.csd.pm.pap.prohibition.Prohibition;
import gov.nist.csd.pm.pap.query.PolicyQuery;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import static gov.nist.csd.pm.pap.graph.node.Properties.NO_PROPERTIES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PolicyEquals {

    public static void assertPolicyEquals(PolicyQuery a, PolicyQuery b) throws PMException {
        // check nodes
        // assignments
        // associations
        Collection<String> aNodes = new HashSet<>(a.graph().search(NodeType.ANY, NO_PROPERTIES));
        Collection<String> bNodes = new HashSet<>(b.graph().search(NodeType.ANY, NO_PROPERTIES));
        assertEquals(aNodes, bNodes);

        for (String nodeName : aNodes) {
            Node aNode = a.graph().getNode(nodeName);
            Node bNode = b.graph().getNode(nodeName);
            assertEquals(aNode, bNode);

            Collection<String> aAscendants = new HashSet<>(a.graph().getAdjacentAscendants(nodeName));
            Collection<String> aDescendants = new HashSet<>(a.graph().getAdjacentDescendants(nodeName));

            Collection<String> bAscendants = new HashSet<>(b.graph().getAdjacentAscendants(nodeName));
            Collection<String> bDescendants = new HashSet<>(b.graph().getAdjacentDescendants(nodeName));

            assertEquals(aAscendants, bAscendants);
            assertEquals(aDescendants, bDescendants);

            Collection<Association> aSourceAssocs = new HashSet<>(a.graph().getAssociationsWithSource(nodeName));
            Collection<Association> aTargetAssocs = new HashSet<>(a.graph().getAssociationsWithTarget(nodeName));

            Collection<Association> bSourceAssocs = new HashSet<>(b.graph().getAssociationsWithSource(nodeName));
            Collection<Association> bTargetAssocs = new HashSet<>(b.graph().getAssociationsWithTarget(nodeName));

            assertEquals(aSourceAssocs, bSourceAssocs);
            assertEquals(aTargetAssocs, bTargetAssocs);
        }

        // check prohibitions
        Map<String, Collection<Prohibition>> aProhibitions = a.prohibitions().getProhibitions();
        Map<String, Collection<Prohibition>> bProhibitions = b.prohibitions().getProhibitions();

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
