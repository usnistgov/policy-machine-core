package gov.nist.csd.pm.core.common.graph.dag;

import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;

import java.util.Map;
import java.util.Set;

public record TargetDagResult(Map<Long, AccessRightSet> pcMap, Set<Long> reachedTargets) {

}
