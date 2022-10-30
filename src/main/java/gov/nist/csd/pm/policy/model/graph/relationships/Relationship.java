package gov.nist.csd.pm.policy.model.graph.relationships;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import org.jgrapht.graph.DefaultEdge;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class will serve as a parent class for Assignments and Associations.  Both types of relations have a source node
 * and a target node.
 */
public class Relationship implements Serializable {
    private String source;
    private String target;
    private AccessRightSet accessRightSet;

    public Relationship() {

    }

    public Relationship(String source, String target) {
        this.source = source;
        this.target = target;
        this.accessRightSet = null;
    }

    public Relationship(String source, String target, AccessRightSet accessRightSet) {
        this.source = source;
        this.target = target;
        this.accessRightSet = accessRightSet;
    }

    public Relationship(AccessRightSet accessRightSet) {
        this.accessRightSet = accessRightSet;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
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
        if (!(o instanceof Relationship ge)) {
            return false;
        }

        return this.source.equals(ge.source) && this.target.equals(ge.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}
