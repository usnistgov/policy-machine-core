package gov.nist.csd.pm.pap.query.model.explain;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;

import java.util.*;

public record PolicyClassExplain(String pc, AccessRightSet arset, List<List<ExplainNode>> paths) {

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
