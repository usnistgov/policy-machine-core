package gov.nist.csd.pm.policy.events;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoveFunctionEvent that = (RemoveFunctionEvent) o;
        return Objects.equals(functionName, that.functionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionName);
    }
}
