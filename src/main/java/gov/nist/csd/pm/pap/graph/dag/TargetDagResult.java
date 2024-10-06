package gov.nist.csd.pm.pap.graph.dag;

import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;

import java.util.Map;
import java.util.Set;

public record TargetDagResult(Map<String, AccessRightSet> pcMap, Set<String> reachedTargets) {

}
