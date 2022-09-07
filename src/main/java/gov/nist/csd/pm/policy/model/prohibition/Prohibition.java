package gov.nist.csd.pm.policy.model.prohibition;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;

import java.util.*;

/**
 * Object representing a Prohibition.
 */
public class Prohibition {

    private final String label;
    private final ProhibitionSubject      subject;
    private final List<ContainerCondition> containers;
    private final AccessRightSet accessRightSet;
    private final boolean      intersection;

    public Prohibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, List<ContainerCondition> containers) {
        if (subject == null) {
            throw new IllegalArgumentException("Prohibition subject cannot be null");
        }

        this.label = label;
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
        this.label = prohibition.getLabel();
        this.subject = new ProhibitionSubject(prohibition.getSubject().name(), prohibition.getSubject().type());
        this.containers = new ArrayList<>(prohibition.containers);
        this.accessRightSet = new AccessRightSet(prohibition.getAccessRightSet());
        this.intersection = prohibition.isIntersection();
    }

    public ProhibitionSubject getSubject() {
        return subject;
    }

    public List<ContainerCondition> getContainers() {
        return containers;
    }

    public String getLabel() {
        return label;
    }

    public AccessRightSet getAccessRightSet() {
        return accessRightSet;
    }

    public boolean isIntersection() {
        return intersection;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Prohibition p)) {
            return false;
        }

        return this.getLabel().equals(p.getLabel());
    }

    public int hashCode() {
        return Objects.hash(label);
    }
}
