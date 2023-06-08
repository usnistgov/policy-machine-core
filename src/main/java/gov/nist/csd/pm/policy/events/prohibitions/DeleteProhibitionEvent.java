package gov.nist.csd.pm.policy.events.prohibitions;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.Objects;

public class DeleteProhibitionEvent implements PolicyEvent {

    private final String label;

    public DeleteProhibitionEvent(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String getEventName() {
        return "delete_prohibition";
    }

    @Override
    public void apply(Policy policy) throws PMException {
        policy.prohibitions().delete(label);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteProhibitionEvent that = (DeleteProhibitionEvent) o;
        return Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }
}
