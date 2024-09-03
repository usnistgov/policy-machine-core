package gov.nist.csd.pm.pap.query.explain;

import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;

import java.util.List;
import java.util.Objects;

public final class ExplainAssociation {
    private final String ua;
    private final AccessRightSet arset;
    private final List<Path> userPaths;

    public ExplainAssociation(String ua, AccessRightSet arset, List<Path> userPaths) {
        this.ua = ua;
        this.arset = arset;
        this.userPaths = userPaths;
    }

    public String ua() {
        return ua;
    }

    public AccessRightSet arset() {
        return arset;
    }

    public List<Path> userPaths() {
        return userPaths;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ExplainAssociation) obj;
        return Objects.equals(this.ua, that.ua) &&
                Objects.equals(this.arset, that.arset) &&
                Objects.equals(this.userPaths, that.userPaths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ua, arset, userPaths);
    }

    @Override
    public String toString() {
        return "ExplainAssociation[" +
                "ua=" + ua + ", " +
                "arset=" + arset + ", " +
                "userPaths=" + userPaths + ']';
    }


}
