package gov.nist.ngac.pm.core.pap.query.access;

import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.Map;
import java.util.Set;

/**
 * The result of walking a user's ascendant graph.
 *
 * @param borderTargets the access rights granted per border target node id
 * @param prohibitions the prohibitions reachable from the user or their process
 */
public record UserDagResult(Map<Long, AccessRightSet> borderTargets, Set<Prohibition> prohibitions) {

}
