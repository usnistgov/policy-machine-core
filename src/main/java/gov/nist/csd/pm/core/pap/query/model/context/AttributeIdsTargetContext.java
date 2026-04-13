package gov.nist.csd.pm.core.pap.query.model.context;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An AnonymousTargetContext with a set of attribute IDs.
 */
public final class AttributeIdsTargetContext implements AnonymousTargetContext {

    private final Set<Long> attributeIds;

    public AttributeIdsTargetContext(Set<Long> attributeIds) {
        this.attributeIds = attributeIds;
    }

    public Set<Long> attributeIds() {
        return attributeIds;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttributeIdsTargetContext that = (AttributeIdsTargetContext) o;
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
