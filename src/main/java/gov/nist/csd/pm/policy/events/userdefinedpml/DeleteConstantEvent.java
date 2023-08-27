package gov.nist.csd.pm.policy.events.userdefinedpml;

import gov.nist.csd.pm.policy.events.PolicyEvent;

import java.util.Objects;

public class DeleteConstantEvent implements PolicyEvent {

    private final String name;

    public DeleteConstantEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getEventName() {
        return "remove_constant";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteConstantEvent that = (DeleteConstantEvent) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
