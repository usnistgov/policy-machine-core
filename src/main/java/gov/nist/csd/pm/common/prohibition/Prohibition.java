package gov.nist.csd.pm.common.prohibition;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateProhibitionStatement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Object representing a Prohibition.
 */
public class Prohibition implements Serializable {

    private String name;
    private ProhibitionSubject      subject;
    private Collection<ContainerCondition> containers;
    private AccessRightSet accessRightSet;
    private boolean      intersection;

    public Prohibition(String name, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection,
                       Collection<ContainerCondition> containers) {
        if (subject == null) {
            throw new IllegalArgumentException("Prohibition subject cannot be null");
        }

        this.name = name;
        this.subject = subject;

        this.containers = Objects.requireNonNullElseGet(containers, ArrayList::new);
        this.accessRightSet = Objects.requireNonNullElseGet(accessRightSet, AccessRightSet::new);
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

    public Collection<ContainerCondition> getContainers() {
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
        if (!(o instanceof Prohibition)) {
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

    @Override
    public String toString() {
        return CreateProhibitionStatement.fromProhibition(this).toString();
    }
}
