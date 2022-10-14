package gov.nist.csd.pm.policy.events;

public class DeleteNodeEvent implements PolicyEvent {

    private final String name;

    public DeleteNodeEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getEventName() {
        return "delete_node";
    }
}
