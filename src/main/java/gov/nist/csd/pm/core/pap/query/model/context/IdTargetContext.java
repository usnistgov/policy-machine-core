package gov.nist.csd.pm.core.pap.query.model.context;

import java.util.Objects;

/**
 * A NodeTargetContext with a target node ID.
 */
public final class IdTargetContext implements NodeTargetContext {

    private final long targetId;

    public IdTargetContext(long targetId) {
        this.targetId = targetId;
    }

    public long targetId() {
        return targetId;
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
