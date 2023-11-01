package gov.nist.csd.pm.policy.model.audit;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PolicyClass {

    private AccessRightSet arset;
    private Set<Path> paths;

    public PolicyClass() {
        arset = new AccessRightSet();
        paths = new HashSet<>();
    }

    public PolicyClass(AccessRightSet arset, Set<Path> paths) {
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
        PolicyClass that = (PolicyClass) o;
        return Objects.equals(arset, that.arset) && Objects.equals(paths, that.paths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arset, paths);
    }
}
