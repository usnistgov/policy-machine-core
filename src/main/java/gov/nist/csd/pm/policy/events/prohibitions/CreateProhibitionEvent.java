package gov.nist.csd.pm.policy.events.prohibitions;

import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;

import java.util.List;
import java.util.Objects;

public class CreateProhibitionEvent implements PolicyEvent {

    private final String name;
    private final ProhibitionSubject      subject;
    private final List<ContainerCondition> containers;
    private final AccessRightSet accessRightSet;
    private final boolean      intersection;

    public CreateProhibitionEvent(String name, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, List<ContainerCondition> containers) {
        this.name = name;
        this.subject = subject;
        this.accessRightSet = accessRightSet;
        this.intersection = intersection;
        this.containers = containers;
    }

    public String getId() {
        return name;
    }

    public ProhibitionSubject getSubject() {
        return subject;
    }

    public List<ContainerCondition> getContainers() {
        return containers;
    }

    public AccessRightSet getAccessRightSet() {
        return accessRightSet;
    }

    public boolean isIntersection() {
        return intersection;
    }

    @Override
    public String getEventName() {
        return "create_prohibition";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateProhibitionEvent that = (CreateProhibitionEvent) o;
        return intersection == that.intersection && Objects.equals(name, that.name) && Objects.equals(subject, that.subject) && Objects.equals(containers, that.containers) && Objects.equals(accessRightSet, that.accessRightSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, subject, containers, accessRightSet, intersection);
    }
}
