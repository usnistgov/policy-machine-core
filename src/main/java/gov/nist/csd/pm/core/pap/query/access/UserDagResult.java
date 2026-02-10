package gov.nist.csd.pm.core.pap.query.access;

import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.Map;
import java.util.Set;

public record UserDagResult(Map<Long, AccessRightSet> borderTargets, Set<Prohibition> prohibitions) {

}
