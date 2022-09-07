package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;

import java.util.List;

public class UpdateProhibitionEvent extends PolicyEvent {

    private final String       name;
    private final ProhibitionSubject subject;
    private final List<ContainerCondition> containers;
    private final AccessRightSet accessRightSet;
    private final boolean      intersection;

    public UpdateProhibitionEvent(String name, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, List<ContainerCondition> containers) {
        this.name = name;
        this.subject = subject;
        this.accessRightSet = accessRightSet;
        this.intersection = intersection;
        this.containers = containers;
    }

    public String getName() {
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
}
