package gov.nist.csd.pm.pap.query.explain;

import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;

import java.util.List;

public record ExplainAssociation(String ua, AccessRightSet arset, List<Path> userPaths) {

}
