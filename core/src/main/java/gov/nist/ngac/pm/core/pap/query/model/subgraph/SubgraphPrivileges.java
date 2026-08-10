package gov.nist.ngac.pm.core.pap.query.model.subgraph;

import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.List;

/**
 * A node paired with the caller's privileges on it, plus the same for each ascendant, recursively.
 *
 * @param node the node
 * @param privileges the caller's privileges on the node
 * @param ascendants the same information for each of the node's ascendants
 */
public record SubgraphPrivileges(Node node, AccessRightSet privileges, List<SubgraphPrivileges> ascendants) {
}