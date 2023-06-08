package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.Objects;

public class DeleteProhibitionEvent implements PolicyEvent {

    private final Prohibition prohibition;

    public DeleteProhibitionEvent(Prohibition prohibition) {
        this.prohibition = prohibition;
    }

    public Prohibition getProhibition() {
        return prohibition;
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
        return Objects.equals(prohibition, that.prohibition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prohibition);
    }
}
