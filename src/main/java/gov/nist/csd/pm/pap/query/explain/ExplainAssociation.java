package gov.nist.csd.pm.pap.query.explain;

import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public record ExplainAssociation(String ua, String target, AccessRightSet arset, List<Path> userPaths) {

}
