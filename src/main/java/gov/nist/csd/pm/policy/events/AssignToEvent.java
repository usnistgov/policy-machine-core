package gov.nist.csd.pm.policy.events;

public class AssignToEvent extends PolicyEvent {

    private final String child;
    private final String parent;

    public AssignToEvent(String child, String parent) {
        this.child = child;
        this.parent = parent;
    }

    public String getChild() {
        return child;
    }

    public String getParent() {
        return parent;
    }

}
