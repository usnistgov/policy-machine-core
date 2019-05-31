package gov.nist.csd.pm.pdp.audit.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PolicyClass {
    private Set<String> operations;
    private List<Path> paths;

    public PolicyClass() {
        operations = new HashSet<>();
        paths = new ArrayList<>();
    }

    public PolicyClass(Set<String> operations, List<Path> paths) {
        this.operations = operations;
        this.paths = paths;
    }

    public Set<String> getOperations() {
        return operations;
    }

    public void setOperations(Set<String> operations) {
        this.operations = operations;
    }

    public List<Path> getPaths() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }
}
