package gov.nist.csd.pm.common.graph.dag;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;

import java.util.Map;
import java.util.Set;

public record TargetDagResult(Map<String, AccessRightSet> pcSet, Set<String> reachedTargets) {

}
