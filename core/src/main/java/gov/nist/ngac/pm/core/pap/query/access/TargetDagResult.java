package gov.nist.ngac.pm.core.pap.query.access;

import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.Map;
import java.util.Set;

/**
 * The result of walking a target's ascendant graph, recording the access rights reachable under each
 * policy class and the prohibition-referenced node ids visited.
 *
 * @param pcMap the access rights reachable under each policy class node id
 * @param reachedTargets the prohibition-referenced node ids reached during the walk
 */
public record TargetDagResult(Map<Long, AccessRightSet> pcMap, Set<Long> reachedTargets) {

}
