package gov.nist.ngac.pm.core.pap.query.model.subgraph;

import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.List;

public record SubgraphPrivileges(Node node, AccessRightSet privileges, List<SubgraphPrivileges> ascendants) {
}