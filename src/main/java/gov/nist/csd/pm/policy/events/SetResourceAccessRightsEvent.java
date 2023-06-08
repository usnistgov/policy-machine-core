package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SetResourceAccessRightsEvent that = (SetResourceAccessRightsEvent) o;
        return Objects.equals(accessRightSet, that.accessRightSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessRightSet);
    }
}
