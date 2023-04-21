package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.pml.model.expression.Value;

public class AddConstantEvent implements PolicyEvent {

    private final String name;
    private final Value value;

    public AddConstantEvent(String name, Value value) {
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
}
