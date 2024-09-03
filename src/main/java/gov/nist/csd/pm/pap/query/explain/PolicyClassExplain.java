package gov.nist.csd.pm.pap.query.explain;

import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;

import java.util.*;

public final class PolicyClassExplain {
    private final String pc;
    private final AccessRightSet arset;
    private final List<List<ExplainNode>> paths;

    public PolicyClassExplain(String pc, AccessRightSet arset, List<List<ExplainNode>> paths) {
        this.pc = pc;
        this.arset = arset;
        this.paths = paths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PolicyClassExplain)) return false;
        PolicyClassExplain that = (PolicyClassExplain) o;
        return Objects.equals(pc, that.pc) && Objects.equals(arset, that.arset) && Objects.equals(paths, that.paths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pc, arset, paths);
    }

    public String pc() {
        return pc;
    }

    public AccessRightSet arset() {
        return arset;
    }

    public List<List<ExplainNode>> paths() {
        return paths;
    }

    @Override
    public String toString() {
        return "PolicyClassExplain[" +
                "pc=" + pc + ", " +
                "arset=" + arset + ", " +
                "paths=" + paths + ']';
    }

}
