/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.query.model.explain;

import com.google.gson.GsonBuilder;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * The full explanation of a user's access decision on a target.
 */
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
