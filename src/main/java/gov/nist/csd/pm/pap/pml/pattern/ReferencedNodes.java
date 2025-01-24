package gov.nist.csd.pm.pap.pml.pattern;

import java.nio.LongBuffer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public record ReferencedNodes(Set<Long> nodes, boolean isAny) {

    public ReferencedNodes(boolean isAny) {
        this(new HashSet<>(), isAny);
    }

    public void addNodes(Collection<Long> entity) {
        nodes.addAll(entity);
    }

    public void addNode(Long entity) {
        nodes.add(entity);
    }

    public ReferencedNodes combine(ReferencedNodes other) {
        Set<Long> combined = new HashSet<>(this.nodes);
        combined.addAll(other.nodes);

        return new ReferencedNodes(combined, other.isAny || this.isAny);
    }
}
