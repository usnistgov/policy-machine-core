package gov.nist.csd.pm.common.graph.relationship;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class will serve as a descendant class for Assignments and Associations.  Both types of relations have a source node
 * and a target node.
 */
public class Relationship implements Serializable {
    private long source;
    private long target;
    private AccessRightSet accessRightSet;

    public Relationship() {

    }

    public Relationship(long source, long target) {
        this.source = source;
        this.target = target;
        this.accessRightSet = null;
    }

    public Relationship(long source, long target, AccessRightSet accessRightSet) {
        this.source = source;
        this.target = target;
        this.accessRightSet = accessRightSet;
    }

    public Relationship(AccessRightSet accessRightSet) {
        this.accessRightSet = accessRightSet;
    }

    public long getSource() {
        return source;
    }

    public void setSource(long source) {
        this.source = source;
    }

    public long getTarget() {
        return target;
    }

    public void setTarget(long target) {
        this.target = target;
    }

    public boolean isAssociation() {
        return this.accessRightSet != null;
    }

    public AccessRightSet getAccessRightSet() {
        return accessRightSet;
    }

    public void setAccessRightSet(AccessRightSet accessRightSet) {
        this.accessRightSet = accessRightSet;
    }

    public String toString() {
        return source + "->" + target + (isAssociation() ? accessRightSet : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Relationship that)) {
            return false;
        }
	    return Objects.equals(source, that.source) && Objects.equals(
                target, that.target) && Objects.equals(accessRightSet, that.accessRightSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target, accessRightSet);
    }
}
