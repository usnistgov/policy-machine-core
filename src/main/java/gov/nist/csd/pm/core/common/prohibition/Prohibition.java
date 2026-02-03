package gov.nist.csd.pm.core.common.prohibition;

import gov.nist.csd.pm.core.pap.operation.accessrights.AccessRightSet;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * Object representing a Prohibition.
 */
public class Prohibition implements Serializable {

    private String name;
    private ProhibitionSubject subject;
    private AccessRightSet accessRightSet;
    private boolean      intersection;
    private Collection<ContainerCondition> containers;

    public Prohibition(String name, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, Collection<ContainerCondition> containers) {
        this.name = name;
        this.subject = subject;
        this.accessRightSet = accessRightSet;
        this.intersection = intersection;
        this.containers = containers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProhibitionSubject getSubject() {
        return subject;
    }

    public void setSubject(ProhibitionSubject subject) {
        this.subject = subject;
    }

    public Collection<ContainerCondition> getContainers() {
        return containers;
    }

    public void setContainers(Collection<ContainerCondition> containers) {
        this.containers = containers;
    }

    public AccessRightSet getAccessRightSet() {
        return accessRightSet;
    }

    public void setAccessRightSet(AccessRightSet accessRightSet) {
        this.accessRightSet = accessRightSet;
    }

    public boolean isIntersection() {
        return intersection;
    }

    public void setIntersection(boolean intersection) {
        this.intersection = intersection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prohibition that)) return false;
	    return intersection == that.intersection &&
                Objects.equals(name, that.name) &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(containers, that.containers) &&
                Objects.equals(accessRightSet, that.accessRightSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, subject, containers, accessRightSet, intersection);
    }

    @Override
    public String toString() {
        return "Prohibition{" +
                "name='" + name + '\'' +
                ", subject=" + subject +
                ", containers=" + containers +
                ", accessRightSet=" + accessRightSet +
                ", intersection=" + intersection +
                '}';
    }
}
