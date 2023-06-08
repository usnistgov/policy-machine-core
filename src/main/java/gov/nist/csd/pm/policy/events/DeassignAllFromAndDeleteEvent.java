package gov.nist.csd.pm.policy.events;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeassignAllFromAndDeleteEvent that = (DeassignAllFromAndDeleteEvent) o;
        return Objects.equals(node, that.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node);
    }
}
