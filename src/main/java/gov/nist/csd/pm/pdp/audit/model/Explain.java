package gov.nist.csd.pm.pdp.audit.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    public String toString() {
        StringBuilder str = new StringBuilder("Permissions: " + permissions.toString());
        for (String pc : policyClasses.keySet()) {
            PolicyClass policyClass = policyClasses.get(pc);
            str.append("\n\t\t").append(pc).append(": ").append(policyClass.getOperations());
        }

        str.append("\nPaths:");
        for (String pc : policyClasses.keySet()) {
            PolicyClass policyClass = policyClasses.get(pc);
            str.append("\n\t\t").append(pc).append(": ").append(policyClass.getOperations());
            Set<Path> paths = policyClass.getPaths();
            for (Path path : paths) {
                str.append("\n\t\t\t- ").append(path);
            }
        }

        return str.toString();
    }
}
