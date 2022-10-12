package gov.nist.csd.pm.policy.events;

public class DeassignFromEvent implements PolicyEvent {

    private final String child;
    private final String parent;

    public DeassignFromEvent(String child, String parent) {
        this.child = child;
        this.parent = parent;
    }

    public String getChild() {
        return child;
    }

    public String getParent() {
        return parent;
    }

    @Override
    public String getEventName() {
        return "deassign_from";
    }
}
