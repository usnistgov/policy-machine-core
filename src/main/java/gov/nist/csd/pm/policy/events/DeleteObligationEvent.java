package gov.nist.csd.pm.policy.events;

public class DeleteObligationEvent implements PolicyEvent {

    private final String label;

    public DeleteObligationEvent(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String getEventName() {
        return "delete_obligation";
    }
}
