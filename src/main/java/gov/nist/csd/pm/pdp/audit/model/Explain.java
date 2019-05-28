package gov.nist.csd.pm.pdp.audit.model;

import java.util.*;

public class Explain {
    private Set<String>              permissions;
    private Map<String, PolicyClass> policyClasses;

    public Explain() {
        permissions = new HashSet<>();
        policyClasses = new HashMap<>();
    }

    public Explain(Set<String> permissions, Map<String, PolicyClass> policyClasses) {
        this.permissions = permissions;
        this.policyClasses = policyClasses;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Map<String, PolicyClass> getPolicyClasses() {
        return policyClasses;
    }

    public void setPolicyClasses(Map<String, PolicyClass> policyClasses) {
        this.policyClasses = policyClasses;
    }
}
