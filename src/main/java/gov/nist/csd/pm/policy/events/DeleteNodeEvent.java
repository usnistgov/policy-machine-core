package gov.nist.csd.pm.policy.events;

import java.util.Objects;

public class DeleteNodeEvent implements PolicyEvent {

    private final String name;

    public DeleteNodeEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getEventName() {
        return "delete_node";
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteNodeEvent that = (DeleteNodeEvent) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
