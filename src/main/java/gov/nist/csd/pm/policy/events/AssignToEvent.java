package gov.nist.csd.pm.policy.events;

import java.util.Objects;

public class AssignToEvent implements PolicyEvent {

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

    @Override
    public String getEventName() {
        return "assign_to";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignToEvent that = (AssignToEvent) o;
        return Objects.equals(child, that.child) && Objects.equals(parent, that.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(child, parent);
    }
}
