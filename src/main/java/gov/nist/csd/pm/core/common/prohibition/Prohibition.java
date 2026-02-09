package gov.nist.csd.pm.core.common.prohibition;

import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
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
