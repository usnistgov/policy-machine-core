package gov.nist.csd.pm.common.event;

import gov.nist.csd.pm.common.op.Operation;

import java.util.Map;
import java.util.Objects;

public class EventContext {

    private final long userId;
    private final String process;
    private final String opName;
    private final Map<String, Object> operands;

    public EventContext(long userId, String process, String opName, Map<String, Object> operands) {
        this.userId = userId;
        this.process = process;
        this.opName = opName;
        this.operands = operands;
    }

    public EventContext(long userId, String process, Operation<?> op, Map<String, Object> operands) {
        this.userId = userId;
        this.process = process;
        this.opName = op.getName();
        this.operands = operands;
    }

    public long getUserId() {
        return userId;
    }

    public String getProcess() {
        return process;
    }

    public String getOpName() {
        return opName;
    }

    public Map<String, Object> getOperands() {
        return operands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventContext that)) return false;
	    return userId == that.userId && Objects.equals(process, that.process) && Objects.equals(opName, that.opName) && Objects.equals(operands, that.operands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, process, opName, operands);
    }

    @Override
    public String toString() {
        return "EventContext{" +
                "userId=" + userId +
                ", process='" + process + '\'' +
                ", opName='" + opName + '\'' +
                ", operands=" + operands +
                '}';
    }
}
