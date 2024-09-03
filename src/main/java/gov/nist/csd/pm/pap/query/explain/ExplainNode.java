package gov.nist.csd.pm.pap.query.explain;

import java.util.List;
import java.util.Objects;

public final class ExplainNode {
    private final String node;
    private final List<ExplainAssociation> associations;

    public ExplainNode(String node, List<ExplainAssociation> associations) {
        this.node = node;
        this.associations = associations;
    }

    public String node() {
        return node;
    }

    public List<ExplainAssociation> associations() {
        return associations;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ExplainNode) obj;
        return Objects.equals(this.node, that.node) &&
                Objects.equals(this.associations, that.associations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, associations);
    }

    @Override
    public String toString() {
        return "ExplainNode[" +
                "node=" + node + ", " +
                "associations=" + associations + ']';
    }


}
