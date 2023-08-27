package gov.nist.csd.pm.policy.events.graph;

import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;

import java.util.Objects;

public class AssociateEvent implements PolicyEvent {

    private final String ua;
    private final String target;
    private final AccessRightSet accessRightSet;

    public AssociateEvent(String ua, String target, AccessRightSet accessRightSet) {
        this.ua = ua;
        this.target = target;
        this.accessRightSet = accessRightSet;
    }

    public String getUa() {
        return ua;
    }

    public String getTarget() {
        return target;
    }

    public AccessRightSet getAccessRightSet() {
        return accessRightSet;
    }

    @Override
    public String getEventName() {
        return "associate";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssociateEvent that = (AssociateEvent) o;
        return Objects.equals(ua, that.ua) && Objects.equals(target, that.target) && Objects.equals(accessRightSet, that.accessRightSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ua, target, accessRightSet);
    }
}
