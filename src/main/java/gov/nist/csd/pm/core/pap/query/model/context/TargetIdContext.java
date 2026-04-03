package gov.nist.csd.pm.core.pap.query.model.context;

import java.util.Objects;

public final class TargetIdContext implements TargetNodeContext {

    private final long targetId;

    public TargetIdContext(long targetId) {
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
        TargetIdContext that = (TargetIdContext) o;
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
