package gov.nist.ngac.pm.core.pap.query.model.subgraph;

import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.List;

/**
 * A node paired with the caller's privileges on it, plus the same for each ascendant, recursively.
 */
public record SubgraphPrivileges(Node node, AccessRightSet privileges, List<SubgraphPrivileges> ascendants) {
}