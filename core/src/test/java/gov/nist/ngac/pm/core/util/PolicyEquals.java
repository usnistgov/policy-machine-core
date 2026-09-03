/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.util;

import static gov.nist.ngac.pm.core.common.graph.node.Properties.NO_PROPERTIES;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.common.graph.node.NodeType;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.graph.Association;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
        Set<Obligation> aObligations = new HashSet<>(a.obligations().getObligations());
        Set<Obligation> bObligations = new HashSet<>(b.obligations().getObligations());

        assertEquals(aObligations, bObligations);

        // check operations
        AccessRightSet aResOps = a.operations().getResourceAccessRights();
        AccessRightSet bResOps = b.operations().getResourceAccessRights();
        assertEquals(aResOps, bResOps);

        Collection<String> aOps = new HashSet<>(a.operations().getOperationNames());
        Collection<String> bOps = new HashSet<>(b.operations().getOperationNames());
        assertEquals(aOps, bOps);
    }
}
