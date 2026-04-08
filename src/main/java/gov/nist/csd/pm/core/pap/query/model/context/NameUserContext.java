package gov.nist.csd.pm.core.pap.query.model.context;

import java.util.Objects;

public final class NameUserContext implements NodeUserContext {

    private final String username;
    private final String process;

    public NameUserContext(String username, String process) {
        this.username = username;
        this.process = process;
    }

    public NameUserContext(String username) {
        this(username, "");
    }

    public String username() {
        return username;
    }

    @Override
    public String getProcess() {
        return process;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NameUserContext that)) return false;
        return Objects.equals(username, that.username) && Objects.equals(process, that.process);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, process);
    }

    @Override
    public String toString() {
        String processStr = process != null && !process.isEmpty() ? ", process: " + process : "";
        return String.format("{user: %s%s}", username, processStr);
    }
}
