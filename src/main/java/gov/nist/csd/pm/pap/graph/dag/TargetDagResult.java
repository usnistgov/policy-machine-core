package gov.nist.csd.pm.pap.graph.dag;

import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class TargetDagResult {
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TargetDagResult) obj;
        return Objects.equals(this.pcSet, that.pcSet) &&
                Objects.equals(this.reachedTargets, that.reachedTargets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pcSet, reachedTargets);
    }

    @Override
    public String toString() {
        return "TargetDagResult[" +
                "pcSet=" + pcSet + ", " +
                "reachedTargets=" + reachedTargets + ']';
    }


}
