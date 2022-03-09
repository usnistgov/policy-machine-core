package gov.nist.csd.pm.policy.model.access;

import java.io.Serializable;
import java.util.Objects;

public class UserContext implements Serializable {

    public static final String NO_PROCESS = "";
    private String user;
    private String process;

    public UserContext(String user, String process) {
        this.user = user;
        this.process = process;
    }

    public UserContext(String user) {
        this.user = user;
        this.process = NO_PROCESS;
    }

    public UserContext() {
        this.user = "";
        this.process = "";
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

    @Override
    public String toString() {
        return "user=" + user + (process.isEmpty() ? "" : "process=" + process);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserContext that = (UserContext) o;
        return Objects.equals(user, that.user) && Objects.equals(process, that.process);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, process);
    }
}
