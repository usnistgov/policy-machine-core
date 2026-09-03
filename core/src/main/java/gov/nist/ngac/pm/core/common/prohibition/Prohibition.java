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

package gov.nist.ngac.pm.core.common.prohibition;

import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * Object representing a Prohibition.
 */
public abstract sealed class Prohibition implements Serializable permits ProcessProhibition, NodeProhibition {

    private String name;
    private AccessRightSet accessRightSet;
    private Set<Long> inclusionSet;
    private Set<Long> exclusionSet;
    private boolean isConjunctive;

    public Prohibition(String name, AccessRightSet accessRightSet, Set<Long> inclusionSet, Set<Long> exclusionSet, boolean isConjunctive) {
        this.name = name;
        this.accessRightSet = accessRightSet;
        this.inclusionSet = inclusionSet;
        this.exclusionSet = exclusionSet;
        this.isConjunctive = isConjunctive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccessRightSet getAccessRightSet() {
        return accessRightSet;
    }

    public void setAccessRightSet(AccessRightSet accessRightSet) {
        this.accessRightSet = accessRightSet;
    }

    public Set<Long> getInclusionSet() {
        return inclusionSet;
    }

    public void setInclusionSet(Set<Long> inclusionSet) {
        this.inclusionSet = inclusionSet;
    }

    public Set<Long> getExclusionSet() {
        return exclusionSet;
    }

    public void setExclusionSet(Set<Long> exclusionSet) {
        this.exclusionSet = exclusionSet;
    }

    public boolean isConjunctive() {
        return isConjunctive;
    }

    public void setConjunctive(boolean conjunctive) {
        isConjunctive = conjunctive;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Prohibition that = (Prohibition) o;
        return isConjunctive == that.isConjunctive && Objects.equals(name, that.name) && Objects.equals(
            accessRightSet, that.accessRightSet) && Objects.equals(inclusionSet, that.inclusionSet)
            && Objects.equals(exclusionSet, that.exclusionSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, accessRightSet, inclusionSet, exclusionSet, isConjunctive);
    }
}
