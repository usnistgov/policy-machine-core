package gov.nist.csd.pm.pap.query.model.subgraph;

import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;

import java.util.List;

public record SubgraphPrivileges(Node node, AccessRightSet privileges, List<SubgraphPrivileges> ascendants) {
}