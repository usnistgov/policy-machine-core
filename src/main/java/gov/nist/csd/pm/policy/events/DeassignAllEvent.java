package gov.nist.csd.pm.policy.events;

import java.util.List;

public class DeassignAllEvent implements PolicyEvent{

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
}