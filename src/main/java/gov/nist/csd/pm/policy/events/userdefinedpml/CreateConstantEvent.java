package gov.nist.csd.pm.policy.events.userdefinedpml;

import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.pml.model.expression.Value;

import java.util.Objects;

public class CreateConstantEvent implements PolicyEvent {

    private final String name;
    private final Value value;

    public CreateConstantEvent(String name, Value value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String getEventName() {
        return "add_constant";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateConstantEvent that = (CreateConstantEvent) o;
        return Objects.equals(name, that.name) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
