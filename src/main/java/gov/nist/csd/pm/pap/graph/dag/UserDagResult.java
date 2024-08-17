package gov.nist.csd.pm.pap.graph.dag;

import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.prohibition.Prohibition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public record UserDagResult(Map<String, AccessRightSet> borderTargets,
                            Set<Prohibition> prohibitions,
                            Set<String> prohibitionTargets) {
    public UserDagResult() {
        this(new HashMap<>(), new HashSet<>(), new HashSet<>());
    }
}
