package gov.nist.csd.pm.policy.events;

public class RemoveFunctionEvent implements PolicyEvent {

    private String functionName;

    public RemoveFunctionEvent(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return functionName;
    }

    @Override
    public String getEventName() {
        return "remove_function";
    }
}
