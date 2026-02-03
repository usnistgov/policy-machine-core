package gov.nist.csd.pm.core.pap.query.model.subgraph;

import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.operation.accessrights.AccessRightSet;
import java.util.List;

public record SubgraphPrivileges(Node node, AccessRightSet privileges, List<SubgraphPrivileges> ascendants) {
}