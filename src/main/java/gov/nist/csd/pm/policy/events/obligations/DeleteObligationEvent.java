package gov.nist.csd.pm.policy.events.obligations;

import gov.nist.csd.pm.policy.events.PolicyEvent;

import java.util.Objects;

public class DeleteObligationEvent implements PolicyEvent {

    private final String name;

    public DeleteObligationEvent(String name) {
        this.name = name;
    }

    public String getObligation() {
        return name;
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
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
