package gov.nist.csd.pm.core.pap.query.model.context;

import java.util.Objects;

/**
 * A NodeTargetContext with a specific node name.
 */
public final class NameTargetContext implements NodeTargetContext {

    private final String targetName;

    public NameTargetContext(String targetName) {
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
        NameTargetContext that = (NameTargetContext) o;
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
