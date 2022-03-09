package gov.nist.csd.pm.policy.events;

public class RemoveFunctionEvent extends PolicyEvent {

    private String functionName;

    public RemoveFunctionEvent(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return functionName;
    }
}
