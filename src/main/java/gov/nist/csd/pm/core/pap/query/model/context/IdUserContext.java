package gov.nist.csd.pm.core.pap.query.model.context;

import java.util.Objects;

public final class IdUserContext implements NodeUserContext {

    private final long userId;
    private final String process;

    public IdUserContext(long userId, String process) {
        this.userId = userId;
        this.process = process;
    }

    public IdUserContext(long userId) {
        this(userId, "");
    }

    public long userId() {
        return userId;
    }

    @Override
    public String getProcess() {
        return process;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdUserContext that)) return false;
        return userId == that.userId && Objects.equals(process, that.process);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, process);
    }

    @Override
    public String toString() {
        String processStr = process != null && !process.isEmpty() ? ", process: " + process : "";
        return String.format("{user: %d%s}", userId, processStr);
    }
}
