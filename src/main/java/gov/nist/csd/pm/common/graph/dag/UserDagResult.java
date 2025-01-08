package gov.nist.csd.pm.common.graph.dag;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.prohibition.Prohibition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public record UserDagResult(Map<String, AccessRightSet> borderTargets,
                            Set<Prohibition> prohibitions) {
    public UserDagResult() {
        this(new HashMap<>(), new HashSet<>());
    }
}
