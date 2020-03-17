package gov.nist.csd.pm.pip.graph.model.relationships;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class will serve as a parent class for Assignments and Associations.  Both types of relations have a source node
 * and a target node.
 */
public class Relationship implements Serializable {
    String source;
    String target;

    public Relationship() {

    }

    public Relationship(String source, String target) {
        this.source = source;
        this.target = target;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Relationship)) {
            return false;
        }

        Relationship r = (Relationship) o;
        return this.source.equals(r.source)
                && this.target.equals(r.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}
