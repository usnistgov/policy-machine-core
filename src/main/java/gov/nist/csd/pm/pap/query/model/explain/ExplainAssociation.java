package gov.nist.csd.pm.pap.query.model.explain;

import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;

import java.util.List;

public record ExplainAssociation(Node ua, AccessRightSet arset, List<Path> userPaths) {

}
