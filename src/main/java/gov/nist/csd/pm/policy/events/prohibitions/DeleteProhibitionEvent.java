package gov.nist.csd.pm.policy.events.prohibitions;

import gov.nist.csd.pm.policy.events.PolicyEvent;

import java.util.Objects;

public class DeleteProhibitionEvent implements PolicyEvent {

    private final String id;

    public DeleteProhibitionEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getEventName() {
        return "delete_prohibition";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteProhibitionEvent that = (DeleteProhibitionEvent) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
