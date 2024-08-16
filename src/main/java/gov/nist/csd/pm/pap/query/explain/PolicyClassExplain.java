package gov.nist.csd.pm.pap.query.explain;

import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PolicyClassExplain {

    private AccessRightSet arset;
    private Set<Path> paths;

    public PolicyClassExplain() {
        arset = new AccessRightSet();
        paths = new HashSet<>();
    }

    public PolicyClassExplain(AccessRightSet arset, Set<Path> paths) {
        this.arset = arset;
        this.paths = paths;
    }

    public AccessRightSet getArset() {
        return arset;
    }

    public void setArset(AccessRightSet arset) {
        this.arset = arset;
    }

    public Set<Path> getPaths() {
        return paths;
    }

    public void setPaths(Set<Path> paths) {
        this.paths = paths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PolicyClassExplain that = (PolicyClassExplain) o;
        return Objects.equals(arset, that.arset) && Objects.equals(paths, that.paths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arset, paths);
    }
}
