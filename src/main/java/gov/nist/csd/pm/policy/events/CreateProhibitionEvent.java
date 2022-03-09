package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;

import java.util.List;

public class CreateProhibitionEvent extends PolicyEvent {

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
}
