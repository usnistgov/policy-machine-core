package gov.nist.csd.pm.pap.query.model.explain;

import com.google.gson.GsonBuilder;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.prohibition.Prohibition;

import java.util.*;

public class Explain {
    private AccessRightSet privileges;
    private List<PolicyClassExplain> policyClasses;
    private AccessRightSet deniedPrivileges;
    private List<Prohibition> prohibitions;

    public Explain() {
        privileges = new AccessRightSet();
        policyClasses = new ArrayList<>();
        deniedPrivileges = new AccessRightSet();
        prohibitions = new ArrayList<>();
    }

    public Explain(AccessRightSet privileges, List<PolicyClassExplain> policyClasses) {
        this.privileges = privileges;
        this.policyClasses = policyClasses;
        this.deniedPrivileges = new AccessRightSet();
        this.prohibitions = new ArrayList<>();
    }

    public Explain(AccessRightSet privileges, List<PolicyClassExplain> policyClasses, AccessRightSet deniedPrivileges,
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

    public List<PolicyClassExplain> getPolicyClasses() {
        return policyClasses;
    }

    public void setPolicyClasses(List<PolicyClassExplain> policyClasses) {
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

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Explain explain)) return false;
        return Objects.equals(privileges, explain.privileges) && Objects.equals(policyClasses, explain.policyClasses)
                && Objects.equals(deniedPrivileges, explain.deniedPrivileges) && Objects.equals(prohibitions, explain.prohibitions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(privileges, policyClasses, deniedPrivileges, prohibitions);
    }
}
