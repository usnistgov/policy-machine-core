package gov.nist.csd.pm.policy.events.obligations;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.obligation.Obligation;

import java.util.Objects;

public class DeleteObligationEvent implements PolicyEvent {

    private final String label;

    public DeleteObligationEvent(String label) {
        this.label = label;
    }

    public String getObligation() {
        return label;
    }

    @Override
    public String getEventName() {
        return "delete_obligation";
    }

    @Override
    public void apply(Policy policy) throws PMException {
        policy.obligations().delete(label);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteObligationEvent that = (DeleteObligationEvent) o;
        return Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }
}
