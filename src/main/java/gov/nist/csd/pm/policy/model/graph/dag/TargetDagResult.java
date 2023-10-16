package gov.nist.csd.pm.policy.model.graph.dag;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;

import java.util.Map;
import java.util.Set;

public class TargetDagResult {

    private final Map<String, AccessRightSet> pcSet;
    private final Set<String> reachedTargets;

    public TargetDagResult(Map<String, AccessRightSet> pcSet, Set<String> reachedTargets) {
        this.pcSet = pcSet;
        this.reachedTargets = reachedTargets;
    }

    public Map<String, AccessRightSet> pcSet() {
        return pcSet;
    }

    public Set<String> reachedTargets() {
        return reachedTargets;
    }
}
