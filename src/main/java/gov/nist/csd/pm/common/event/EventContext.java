package gov.nist.csd.pm.common.event;

import java.util.*;

public class EventContext {

    private final String user;
    private final String process;
    private final String opName;
    private final Map<String, Object> args;

    public EventContext(String user, String process, String opName, Map<String, Object> args) {
        this.user = Objects.requireNonNull(user);
        this.process = process;
        this.opName = Objects.requireNonNull(opName);
        this.args = Objects.requireNonNull(args);
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

    public Map<String, Object> getArgs() {
        return args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof EventContext that))
            return false;
        return Objects.equals(user, that.user) && Objects.equals(process, that.process)
            && Objects.equals(opName, that.opName) && Objects.equals(args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, process, opName, args);
    }

    @Override
    public String toString() {
        return "EventContext{" +
                "userName='" + user + '\'' +
                ", process='" + process + '\'' +
                ", opName='" + opName + '\'' +
                ", args=" + args +
                '}';
    }
}
