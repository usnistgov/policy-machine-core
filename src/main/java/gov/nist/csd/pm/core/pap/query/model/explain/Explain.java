package gov.nist.csd.pm.core.pap.query.model.explain;

import com.google.gson.GsonBuilder;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Explain {
    private AccessRightSet privileges;
    private Collection<PolicyClassExplain> policyClasses;
    private AccessRightSet deniedPrivileges;
    private Collection<Prohibition> prohibitions;

    public Explain() {
        privileges = new AccessRightSet();
        policyClasses = new ArrayList<>();
        deniedPrivileges = new AccessRightSet();
        prohibitions = new ArrayList<>();
    }

    public Explain(AccessRightSet privileges, Collection<PolicyClassExplain> policyClasses) {
        this.privileges = privileges;
        this.policyClasses = policyClasses;
        this.deniedPrivileges = new AccessRightSet();
        this.prohibitions = new ArrayList<>();
    }

    public Explain(AccessRightSet privileges, Collection<PolicyClassExplain> policyClasses, AccessRightSet deniedPrivileges,
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

    public Collection<PolicyClassExplain> getPolicyClasses() {
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

    public Collection<Prohibition> getProhibitions() {
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
