package gov.nist.csd.pm.core.pap.serialization.json;

import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import java.util.Objects;

public class JSONAssociation {

    private long target;
    private AccessRightSet arset;

    public JSONAssociation() {
        arset = new AccessRightSet();
    }

    public JSONAssociation(long target, AccessRightSet arset) {
        this.target = target;
        this.arset = arset;
    }

    public long getTarget() {
        return target;
    }

    public void setTarget(long target) {
        this.target = target;
    }

    public AccessRightSet getArset() {
        return arset;
    }

    public void setArset(AccessRightSet arset) {
        this.arset = arset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JSONAssociation that)) return false;
        return Objects.equals(target, that.target) && Objects.equals(arset, that.arset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(target, arset);
    }
}
