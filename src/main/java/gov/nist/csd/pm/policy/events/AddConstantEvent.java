package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.author.pal.model.expression.Value;

public class AddConstantEvent extends PolicyEvent {

    private String name;
    private Value value;

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
}
