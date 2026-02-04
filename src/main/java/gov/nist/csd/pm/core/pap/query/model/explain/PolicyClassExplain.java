package gov.nist.csd.pm.core.pap.query.model.explain;

import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public record PolicyClassExplain(Node pc, AccessRightSet arset, Collection<List<ExplainNode>> paths) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PolicyClassExplain that)) return false;
        return Objects.equals(pc, that.pc) && Objects.equals(arset, that.arset) && Objects.equals(paths, that.paths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pc, arset, paths);
    }
}
