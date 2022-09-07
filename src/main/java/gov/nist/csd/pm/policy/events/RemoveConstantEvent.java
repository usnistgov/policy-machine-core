package gov.nist.csd.pm.policy.events;

public class RemoveConstantEvent extends PolicyEvent {

    private String name;

    public RemoveConstantEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
