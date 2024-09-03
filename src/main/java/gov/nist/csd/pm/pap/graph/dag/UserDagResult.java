package gov.nist.csd.pm.pap.graph.dag;

import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.prohibition.Prohibition;

import java.util.*;

public final class UserDagResult {
    private final Map<String, AccessRightSet> borderTargets;
    private final Set<Prohibition> prohibitions;
    private final Set<String> prohibitionTargets;

    public UserDagResult(Map<String, AccessRightSet> borderTargets,
                         Set<Prohibition> prohibitions,
                         Set<String> prohibitionTargets) {
        this.borderTargets = borderTargets;
        this.prohibitions = prohibitions;
        this.prohibitionTargets = prohibitionTargets;
    }

    public UserDagResult() {
        this(new HashMap<>(), new HashSet<>(), new HashSet<>());
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserDagResult) obj;
        return Objects.equals(this.borderTargets, that.borderTargets) &&
                Objects.equals(this.prohibitions, that.prohibitions) &&
                Objects.equals(this.prohibitionTargets, that.prohibitionTargets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(borderTargets, prohibitions, prohibitionTargets);
    }

    @Override
    public String toString() {
        return "UserDagResult[" +
                "borderTargets=" + borderTargets + ", " +
                "prohibitions=" + prohibitions + ", " +
                "prohibitionTargets=" + prohibitionTargets + ']';
    }

}
