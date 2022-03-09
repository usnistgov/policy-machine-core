package gov.nist.csd.pm.policy.events;

public class DeleteProhibitionEvent extends PolicyEvent {

    private final String label;

    public DeleteProhibitionEvent(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
