package gov.nist.csd.pm.common.event;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.op.Operation;
import gov.nist.csd.pm.pap.executable.op.arg.ListIdNodeFormalArg;
import gov.nist.csd.pm.pap.executable.op.arg.IdNodeFormalArg;
import gov.nist.csd.pm.pap.executable.op.arg.NodeFormalArg;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class EventContext {

    private String user;
    private String process;
    private String opName;
    private Map<String, Object> args;

    public EventContext(String user, String process, String opName, Map<String, Object> args) {
        this.user = user;
        this.process = process;
        this.opName = opName;
        this.args = args;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventContext that)) return false;
        return Objects.equals(user, that.user) && Objects.equals(process, that.process) && Objects.equals(opName, that.opName) && Objects.equals(
                args, that.args);
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
