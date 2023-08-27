package gov.nist.csd.pm.policy.events.graph;

import gov.nist.csd.pm.policy.events.PolicyEvent;

import java.util.List;
import java.util.Objects;

public class DeassignAllEvent implements PolicyEvent {

    private final List<String> children;
    private final String target;

    public DeassignAllEvent(List<String> children, String target) {
        this.children = children;
        this.target = target;
    }

    public List<String> getChildren() {
        return children;
    }

    public String getTarget() {
        return target;
    }

    @Override
    public String getEventName() {
        return "deassign_all";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeassignAllEvent that = (DeassignAllEvent) o;
        return Objects.equals(children, that.children) && Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(children, target);
    }
}