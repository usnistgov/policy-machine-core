package gov.nist.csd.pm.policy.events;

public class DeassignAllFromAndDeleteEvent implements PolicyEvent{

    private final String node;

    public DeassignAllFromAndDeleteEvent(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }

    @Override
    public String getEventName() {
        return "deassign_all_from_and_delete";
    }
}
