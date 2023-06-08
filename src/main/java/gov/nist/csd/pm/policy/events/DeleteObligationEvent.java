package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.model.obligation.Obligation;

import java.util.Objects;

public class DeleteObligationEvent implements PolicyEvent {

    private final Obligation obligation;

    public DeleteObligationEvent(Obligation obligation) {
        this.obligation = obligation;
    }

    public Obligation getObligation() {
        return obligation;
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
        return Objects.equals(obligation, that.obligation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(obligation);
    }
}
