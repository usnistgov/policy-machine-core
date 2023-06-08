package gov.nist.csd.pm.policy.events;

import java.util.Objects;

public class RemoveConstantEvent implements PolicyEvent {

    private final String name;

    public RemoveConstantEvent(String name) {
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
        RemoveConstantEvent that = (RemoveConstantEvent) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
