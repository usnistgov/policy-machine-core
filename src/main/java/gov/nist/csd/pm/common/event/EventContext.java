package gov.nist.csd.pm.common.event;

import gov.nist.csd.pm.common.event.operand.OperandValue;
import gov.nist.csd.pm.common.op.Operation;

import java.util.Map;
import java.util.Objects;

public class EventContext {

    private final String user;
    private final String process;
    private final String opName;
    private final Map<String, OperandValue> operands;

    public EventContext(String user, String process, String opName, Map<String, OperandValue> operands) {
        this.user = user;
        this.process = process;
        this.opName = opName;
        this.operands = operands;
    }

    public EventContext(String user, String process, Operation<?> op, Map<String, OperandValue> operands) {
        this.user = user;
        this.process = process;
        this.opName = op.getName();
        this.operands = operands;
    }

    public String getUser() {
        return user;
    }

    public String getProcess() {
        return process;
    }

    public String getOpName() {
        return opName;
    }

    public Map<String, OperandValue> getOperands() {
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
                "userName='" + user + '\'' +
                ", process='" + process + '\'' +
                ", opName='" + opName + '\'' +
                ", operands=" + operands +
                '}';
    }
}
