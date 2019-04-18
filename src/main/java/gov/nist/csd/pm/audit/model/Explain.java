package gov.nist.csd.pm.audit.model;

import java.util.*;

public class Explain {
    private Set<String>              operations;
    private Map<String, PolicyClass> policyClasses;

    public Explain() {
        operations = new HashSet<>();
        policyClasses = new HashMap<>();
    }

    public Explain(Set<String> operations, Map<String, PolicyClass> policyClasses) {
        this.operations = operations;
        this.policyClasses = policyClasses;
    }

    public Set<String> getOperations() {
        return operations;
    }

    public void setOperations(Set<String> operations) {
        this.operations = operations;
    }

    public Map<String, PolicyClass> getPolicyClasses() {
        return policyClasses;
    }

    public void setPolicyClasses(Map<String, PolicyClass> policyClasses) {
        this.policyClasses = policyClasses;
    }
}
