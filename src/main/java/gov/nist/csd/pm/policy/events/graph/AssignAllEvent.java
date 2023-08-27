package gov.nist.csd.pm.policy.events.graph;

import java.util.List;
import java.util.Objects;

public class AssignAllEvent extends GraphEvent {

    private final List<String> children;
    private final String target;

    public AssignAllEvent(List<String> children, String target) {
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
        return "assign_all";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignAllEvent that = (AssignAllEvent) o;
        return Objects.equals(children, that.children) && Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(children, target);
    }
}
