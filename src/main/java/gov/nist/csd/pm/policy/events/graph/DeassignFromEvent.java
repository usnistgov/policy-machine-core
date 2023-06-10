package gov.nist.csd.pm.policy.events.graph;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.Objects;

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

    @Override
    public void apply(Policy policy) throws PMException {
        policy.graph().deassign(child, parent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeassignFromEvent that = (DeassignFromEvent) o;
        return Objects.equals(child, that.child) && Objects.equals(parent, that.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(child, parent);
    }
}