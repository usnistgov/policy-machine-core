package gov.nist.csd.pm.core.pap.query.model.context;

import java.util.Objects;

public final class TargetNameContext implements TargetNodeContext {

    private final String targetName;

    public TargetNameContext(String targetName) {
        this.targetName = targetName;
    }

    public String targetName() {
        return targetName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TargetNameContext that = (TargetNameContext) o;
        return Objects.equals(targetName, that.targetName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(targetName);
    }

    @Override
    public String toString() {
        return String.format("{target: %s}", targetName);
    }
}
