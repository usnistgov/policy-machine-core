package gov.nist.ngac.pm.core.pap.query.access;

import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.Map;
import java.util.Set;

public record TargetDagResult(Map<Long, AccessRightSet> pcMap, Set<Long> reachedTargets) {

}
