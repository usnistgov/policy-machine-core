package gov.nist.csd.pm.policy.events;

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
}
