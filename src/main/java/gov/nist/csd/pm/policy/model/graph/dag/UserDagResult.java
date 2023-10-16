package gov.nist.csd.pm.policy.model.graph.dag;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.Map;
import java.util.Set;

public class UserDagResult {

    private final Map<String, AccessRightSet> borderTargets;
    private final Set<Prohibition> prohibitions;
    private final Set<String> prohibitionTargets;

    public UserDagResult(Map<String, AccessRightSet> borderTargets, Set<Prohibition> prohibitions,
                         Set<String> prohibitionTargets) {
        this.borderTargets = borderTargets;
        this.prohibitions = prohibitions;
        this.prohibitionTargets = prohibitionTargets;
    }

    public Map<String, AccessRightSet> borderTargets() {
        return borderTargets;
    }

    public Set<Prohibition> prohibitions() {
        return prohibitions;
    }

    public Set<String> prohibitionTargets() {
        return prohibitionTargets;
    }
}
