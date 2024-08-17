package gov.nist.csd.pm.pap.obligation;

import gov.nist.csd.pm.pap.op.Operation;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EventContext {

    private final String user;
    private final String process;
    private final String opName;
    private final Map<String, Object> operands;
    private final List<String> nodeOperands;

    public EventContext(String user, String process, String opName, Map<String, Object> operands, List<String> nodeOperands) {
        this.user = user;
        this.process = process;
        this.opName = opName;
        this.operands = operands;
        this.nodeOperands = nodeOperands;
    }

    public EventContext(String user, String opName, Map<String, Object> operands, List<String> nodeOperands) {
        this(user, "", opName, operands, nodeOperands);
    }

    public EventContext(String user, String process, Operation<?> op, Map<String, Object> operands) {
        this(user, process, op.getName(), operands, op.getNodeOperands());
    }

    public EventContext(String user, String opName, Map<String, Object> operands) {
        this(user, "", opName, operands, List.of());
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

    public List<String> nodeOperands() {
        return nodeOperands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventContext that)) return false;
        return Objects.equals(user, that.user) && Objects.equals(process, that.process) && Objects.equals(opName, that.opName) && Objects.equals(operands, that.operands) && Objects.equals(nodeOperands, that.nodeOperands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, process, opName, operands, nodeOperands);
    }

    @Override
    public String toString() {
        return "EventContext{" +
                "user='" + user + '\'' +
                ", process='" + process + '\'' +
                ", opName='" + opName + '\'' +
                ", operands=" + operands +
                ", nodeOperands=" + nodeOperands +
                '}';
    }
}