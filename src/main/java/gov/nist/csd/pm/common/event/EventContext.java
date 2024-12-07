package gov.nist.csd.pm.common.event;

import gov.nist.csd.pm.common.op.Operation;

import java.util.Map;
import java.util.Objects;

public class EventContext {

    private final String user;
    private final String process;
    private final String opName;
    private final Map<String, Object> operands;

    public EventContext(String user, String process, String opName, Map<String, Object> operands) {
        this.user = user;
        this.process = process;
        this.opName = opName;
        this.operands = operands;
    }

    public EventContext(String user, String opName, Map<String, Object> operands) {
        this(user, null, opName, operands);
    }

    public EventContext(String user, String process, Operation<?> op, Map<String, Object> operands) {
        this(user, process, op.getName(), operands);
    }

    public EventContext(String user, Operation<?> op, Map<String, Object> operands) {
        this(user, null, op.getName(), operands);
    }

    public String user() {
        return user;
    }

    public String process() {
        return process;
    }

    public String opName() {
        return opName;
    }

    public Map<String, Object> operands() {
        return operands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventContext that)) return false;
        return Objects.equals(user, that.user) && Objects.equals(process, that.process) && Objects.equals(opName, that.opName) && Objects.equals(operands, that.operands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, process, opName, operands);
    }

    @Override
    public String toString() {
        return "EventContext{" +
                "user='" + user + '\'' +
                ", process='" + process + '\'' +
                ", opName='" + opName + '\'' +
                ", operands=" + operands +
                '}';
    }
}
