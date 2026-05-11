package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.dag.GraphWalker;
import gov.nist.csd.pm.core.pap.query.GraphQuery;
import java.util.Objects;

/**
 * A NodeUserContext with a user node ID.
 */
public final class IdUserContext extends NodeUserContext {

    private final long userId;

    public IdUserContext(long userId, String process) {
        super(process);
        this.userId = userId;
    }

    public IdUserContext(long userId) {
        this(userId, "");
    }

    public long userId() {
        return userId;
    }

    @Override
    public void walk(GraphWalker walker, GraphQuery graphQuery) throws PMException {
        walker.walk(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdUserContext that)) return false;
        return userId == that.userId && Objects.equals(getProcess(), that.getProcess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, getProcess());
    }

    @Override
    public String toString() {
        String process = getProcess();
        String processStr = process != null && !process.isEmpty() ? ", process: " + process : "";
        return String.format("{user: %d%s}", userId, processStr);
    }
}
