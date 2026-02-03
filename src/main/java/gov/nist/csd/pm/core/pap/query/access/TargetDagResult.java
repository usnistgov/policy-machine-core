package gov.nist.csd.pm.core.pap.query.access;

import gov.nist.csd.pm.core.pap.operation.accessrights.AccessRightSet;
import java.util.Map;
import java.util.Set;

public record TargetDagResult(Map<Long, AccessRightSet> pcMap, Set<Long> reachedTargets) {

}
