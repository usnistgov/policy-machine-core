package gov.nist.csd.pm.core.pap.query.model.context;

import java.util.Objects;

/**
 * A NodeUserContext with a specific user node name.
 */
public final class NameUserContext extends NodeUserContext {

    private final String username;

    public NameUserContext(String username, String process) {
        super(process);
        this.username = username;
    }

    public NameUserContext(String username) {
        this(username, "");
    }

    public String username() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NameUserContext that)) return false;
        return Objects.equals(username, that.username) && Objects.equals(getProcess(), that.getProcess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, getProcess());
    }

    @Override
    public String toString() {
        String process = getProcess();
        String processStr = process != null && !process.isEmpty() ? ", process: " + process : "";
        return String.format("{user: %s%s}", username, processStr);
    }
}
