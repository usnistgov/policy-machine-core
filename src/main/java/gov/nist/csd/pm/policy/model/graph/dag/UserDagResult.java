package gov.nist.csd.pm.policy.model.graph.dag;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.Map;
import java.util.Set;

public record UserDagResult(Map<String, AccessRightSet> borderTargets,
                            Set<Prohibition> prohibitions,
                            Set<String> prohibitionTargets) {
}
