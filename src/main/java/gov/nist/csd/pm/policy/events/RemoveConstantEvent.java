package gov.nist.csd.pm.policy.events;

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
}
