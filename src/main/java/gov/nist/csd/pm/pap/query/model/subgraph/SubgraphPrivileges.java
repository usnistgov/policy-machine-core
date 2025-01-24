package gov.nist.csd.pm.pap.query.model.subgraph;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;

import java.util.List;

public record SubgraphPrivileges(long name, AccessRightSet privileges, List<SubgraphPrivileges> ascendants) {
}