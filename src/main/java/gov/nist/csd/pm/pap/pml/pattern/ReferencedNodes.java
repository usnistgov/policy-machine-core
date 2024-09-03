package gov.nist.csd.pm.pap.pml.pattern;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class ReferencedNodes {
    private final Set<String> nodes;
    private final boolean isAny;

    public ReferencedNodes(Set<String> nodes, boolean isAny) {
        this.nodes = nodes;
        this.isAny = isAny;
    }

    public ReferencedNodes(boolean isAny) {
        this(new HashSet<>(), isAny);
    }

    public void addNodes(Collection<String> entity) {
        nodes.addAll(entity);
    }

    public void addNode(String entity) {
        nodes.add(entity);
    }

    public ReferencedNodes combine(ReferencedNodes other) {
        Set<String> combined = new HashSet<>(this.nodes);
        combined.addAll(other.nodes);

        return new ReferencedNodes(combined, other.isAny || this.isAny);
    }

    public Set<String> nodes() {
        return nodes;
    }

    public boolean isAny() {
        return isAny;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ReferencedNodes) obj;
        return Objects.equals(this.nodes, that.nodes) &&
                this.isAny == that.isAny;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, isAny);
    }

    @Override
    public String toString() {
        return "ReferencedNodes[" +
                "nodes=" + nodes + ", " +
                "isAny=" + isAny + ']';
    }

}
