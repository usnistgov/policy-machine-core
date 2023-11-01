package gov.nist.csd.pm.policy.model.audit;

import gov.nist.csd.pm.policy.Prohibitions;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.*;

public class Explain {
    private AccessRightSet privileges;
    private Map<String, PolicyClass> policyClasses;
    private AccessRightSet deniedPrivileges;
    private List<Prohibition> prohibitions;

    public Explain() {
        privileges = new AccessRightSet();
        policyClasses = new HashMap<>();
        deniedPrivileges = new AccessRightSet();
        prohibitions = new ArrayList<>();
    }

    public Explain(AccessRightSet privileges, Map<String, PolicyClass> policyClasses) {
        this.privileges = privileges;
        this.policyClasses = policyClasses;
    }

    public Explain(AccessRightSet privileges, Map<String, PolicyClass> policyClasses, AccessRightSet deniedPrivileges,
                   List<Prohibition> prohibitions) {
        this.privileges = privileges;
        this.policyClasses = policyClasses;
        this.deniedPrivileges = deniedPrivileges;
        this.prohibitions = prohibitions;
    }

    public AccessRightSet getPrivileges() {
        return privileges;
    }

    public void setPrivileges(AccessRightSet privileges) {
        this.privileges = privileges;
    }

    public Map<String, PolicyClass> getPolicyClasses() {
        return policyClasses;
    }

    public void setPolicyClasses(Map<String, PolicyClass> policyClasses) {
        this.policyClasses = policyClasses;
    }

    public AccessRightSet getDeniedPrivileges() {
        return deniedPrivileges;
    }

    public void setDeniedPrivileges(AccessRightSet deniedPrivileges) {
        this.deniedPrivileges = deniedPrivileges;
    }

    public List<Prohibition> getProhibitions() {
        return prohibitions;
    }

    public void setProhibitions(List<Prohibition> prohibitions) {
        this.prohibitions = prohibitions;
    }

    public String toString() {
        StringBuilder str = new StringBuilder("Privileges: " + privileges.toString());
        str.append("\nDenied: ").append(deniedPrivileges);

        for (String pc : policyClasses.keySet()) {
            PolicyClass policyClass = policyClasses.get(pc);
            str.append("\n\t\t").append(pc).append(": ").append(policyClass.getArset());
        }

        str.append("\nPaths:");
        for (String pc : policyClasses.keySet()) {
            PolicyClass policyClass = policyClasses.get(pc);
            str.append("\n\t\t").append(pc).append(": ").append(policyClass.getArset());
            Set<Path> paths = policyClass.getPaths();
            for (Path path : paths) {
                str.append("\n\t\t\t- ").append(path);
            }
        }

        if (!deniedPrivileges.isEmpty()) {
            str.append("\nProhibitions:");
            for (Prohibition p : prohibitions) {
                str.append("\n- ").append(p);
            }
        }



        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Explain explain = (Explain) o;
        return Objects.equals(privileges, explain.privileges) && Objects.equals(
                policyClasses, explain.policyClasses) && Objects.equals(
                deniedPrivileges, explain.deniedPrivileges) && Objects.equals(
                prohibitions, explain.prohibitions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(privileges, policyClasses, deniedPrivileges, prohibitions);
    }
}
