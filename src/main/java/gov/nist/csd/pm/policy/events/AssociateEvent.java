package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;

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
}
