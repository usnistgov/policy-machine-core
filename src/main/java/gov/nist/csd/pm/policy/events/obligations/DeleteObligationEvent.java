package gov.nist.csd.pm.policy.events.obligations;

import gov.nist.csd.pm.policy.events.PolicyEvent;

import java.util.Objects;

public class DeleteObligationEvent implements PolicyEvent {

    private final String id;

    public DeleteObligationEvent(String id) {
        this.id = id;
    }

    public String getObligation() {
        return id;
    }

    @Override
    public String getEventName() {
        return "delete_obligation";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteObligationEvent that = (DeleteObligationEvent) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
