package gov.nist.csd.pm.policy.events.graph;

import gov.nist.csd.pm.policy.events.PolicyEvent;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeassignEvent that = (DeassignEvent) o;
        return Objects.equals(child, that.child) && Objects.equals(parent, that.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(child, parent);
    }
}
