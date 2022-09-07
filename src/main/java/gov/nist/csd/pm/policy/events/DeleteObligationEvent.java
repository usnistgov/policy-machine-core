package gov.nist.csd.pm.policy.events;

public class DeleteObligationEvent extends PolicyEvent {

    private final String label;

    public DeleteObligationEvent(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
