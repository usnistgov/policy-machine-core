package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.dag.GraphWalker;
import java.util.Objects;

/**
 * A NodeTargetContext with a target node ID.
 */
public final class IdTargetContext extends NodeTargetContext {

    private final long targetId;

    public IdTargetContext(long targetId) {
        this.targetId = targetId;
    }

    public long targetId() {
        return targetId;
    }

    @Override
    public void walk(GraphWalker walker, NodeLookup nodeLookup) throws PMException {
        walker.walk(targetId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdTargetContext that = (IdTargetContext) o;
        return targetId == that.targetId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(targetId);
    }

    @Override
    public String toString() {
        return String.format("{target: %d}", targetId);
    }
}
