package gov.nist.csd.pm.pdp;

import java.util.Map;
import java.util.Objects;

public final class OperationRequest {
    private final String name;
    private final Map<String, Object> operands;

    public OperationRequest(String name, Map<String, Object> operands) {
        this.name = name;
        this.operands = operands;
    }

    public String name() {
        return name;
    }

    public Map<String, Object> operands() {
        return operands;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (OperationRequest) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.operands, that.operands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, operands);
    }

    @Override
    public String toString() {
        return "OperationRequest[" +
                "name=" + name + ", " +
                "operands=" + operands + ']';
    }

}
