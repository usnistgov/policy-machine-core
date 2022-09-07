package gov.nist.csd.pm.policy.model.graph.dag;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;

import java.util.Map;
import java.util.Set;

public record TargetDagResult(Map<String, AccessRightSet> pcSet, Set<String> reachedTargets) {

}
