package gov.nist.csd.pm.core.pap.query.model.context;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public final class TargetAttributeIdsContext implements AnonymousTargetContext {

    private final Collection<Long> attributeIds;

    public TargetAttributeIdsContext(Collection<Long> attributeIds) {
        this.attributeIds = attributeIds;
    }

    public Collection<Long> attributeIds() {
        return attributeIds;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TargetAttributeIdsContext that = (TargetAttributeIdsContext) o;
        return Objects.equals(attributeIds, that.attributeIds);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(attributeIds);
    }

    @Override
    public String toString() {
        return String.format("{target: [%s]}", attributeIds.stream().map(String::valueOf).collect(Collectors.joining(", ")));
    }
}
