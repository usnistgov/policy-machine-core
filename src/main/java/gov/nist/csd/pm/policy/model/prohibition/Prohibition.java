package gov.nist.csd.pm.policy.model.prohibition;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Object representing a Prohibition.
 */
public class Prohibition implements Serializable {

    private String name;
    private ProhibitionSubject      subject;
    private List<ContainerCondition> containers;
    private AccessRightSet accessRightSet;
    private boolean      intersection;

    public Prohibition() {

    }

    public Prohibition(String name, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, List<ContainerCondition> containers) {
        if (subject == null) {
            throw new IllegalArgumentException("Prohibition subject cannot be null");
        }

        this.name = name;
        this.subject = subject;

        if (containers == null) {
            this.containers = new ArrayList<>();
        } else {
            this.containers = containers;
        }

        if (accessRightSet == null) {
            this.accessRightSet = new AccessRightSet();
        } else {
            this.accessRightSet = accessRightSet;
        }

        this.intersection = intersection;
    }

    public Prohibition(Prohibition prohibition) {
        this.name = prohibition.getName();
        this.subject = new ProhibitionSubject(prohibition.getSubject().getName(), prohibition.getSubject().getType());
        this.containers = new ArrayList<>(prohibition.containers);
        this.accessRightSet = new AccessRightSet(prohibition.getAccessRightSet());
        this.intersection = prohibition.isIntersection();
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

    public List<ContainerCondition> getContainers() {
        return containers;
    }

    public void setContainers(List<ContainerCondition> containers) {
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Prohibition that = (Prohibition) o;
        return intersection == that.intersection && Objects.equals(name, that.name) && Objects.equals(
                subject, that.subject) && Objects.equals(containers, that.containers) && Objects.equals(
                accessRightSet, that.accessRightSet);
    }

    public int hashCode() {
        return Objects.hash(name);
    }
}
