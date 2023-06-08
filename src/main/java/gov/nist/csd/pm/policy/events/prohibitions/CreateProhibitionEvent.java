package gov.nist.csd.pm.policy.events.prohibitions;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;

import java.util.List;
import java.util.Objects;

public class CreateProhibitionEvent implements PolicyEvent {

    private final String label;
    private final ProhibitionSubject      subject;
    private final List<ContainerCondition> containers;
    private final AccessRightSet accessRightSet;
    private final boolean      intersection;

    public CreateProhibitionEvent(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, List<ContainerCondition> containers) {
        this.label = label;
        this.subject = subject;
        this.accessRightSet = accessRightSet;
        this.intersection = intersection;
        this.containers = containers;
    }

    public String getLabel() {
        return label;
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
    public void apply(Policy policy) throws PMException {
        policy.prohibitions().create(label, subject, accessRightSet, intersection, containers.toArray(ContainerCondition[]::new));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateProhibitionEvent that = (CreateProhibitionEvent) o;
        return intersection == that.intersection && Objects.equals(label, that.label) && Objects.equals(subject, that.subject) && Objects.equals(containers, that.containers) && Objects.equals(accessRightSet, that.accessRightSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, subject, containers, accessRightSet, intersection);
    }
}
