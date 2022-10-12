package gov.nist.csd.pm.policy.events;

public class DeassignEvent implements PolicyEvent {

    private final String child;
    private final String parent;

    public DeassignEvent(String child, String parent) {
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
        return "deassign";
    }
}
