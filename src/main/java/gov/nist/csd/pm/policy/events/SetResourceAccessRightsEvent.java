package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;

public class SetResourceAccessRightsEvent implements PolicyEvent {

    private final AccessRightSet accessRightSet;

    public SetResourceAccessRightsEvent(AccessRightSet accessRightSet) {
        this.accessRightSet = accessRightSet;
    }

    public AccessRightSet getAccessRightSet() {
        return accessRightSet;
    }

    @Override
    public String getEventName() {
        return "set_resource_access_rights";
    }
}
