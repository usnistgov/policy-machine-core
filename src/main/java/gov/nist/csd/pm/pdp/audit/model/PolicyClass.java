package gov.nist.csd.pm.pdp.audit.model;

import java.util.HashSet;
import java.util.Set;

public class PolicyClass {
    private Set<String> operations;
    private Set<Path> paths;

    public PolicyClass() {
        operations = new HashSet<>();
        paths = new HashSet<>();
    }

    public PolicyClass(Set<String> operations, Set<Path> paths) {
        this.operations = operations;
        this.paths = paths;
    }

    public Set<String> getOperations() {
        return operations;
    }

    public void setOperations(Set<String> operations) {
        this.operations = operations;
    }

    public Set<Path> getPaths() {
        return paths;
    }

    public void setPaths(Set<Path> paths) {
        this.paths = paths;
    }
}
