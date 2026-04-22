package gov.nist.csd.pm.core.pap.query.model.context;

import java.util.Objects;
import java.util.Set;

/**
 * An AnonymousUserContext with a set of attribute IDs.
 */
public final class AttributeIdsUserContext extends AnonymousUserContext {

    private final Set<Long> attributeIds;

    public AttributeIdsUserContext(Set<Long> attributeIds, String process) {
        super(process);
        this.attributeIds = attributeIds;
    }

    public AttributeIdsUserContext(Set<Long> attributeIds) {
        this(attributeIds, "");
    }

    public Set<Long> attributeIds() {
        return attributeIds;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttributeIdsUserContext that = (AttributeIdsUserContext) o;
        return Objects.equals(attributeIds, that.attributeIds) && Objects.equals(getProcess(), that.getProcess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributeIds, getProcess());
    }

    @Override
    public String toString() {
        String process = getProcess();
        String processStr = process != null && !process.isEmpty() ? ", process: " + process : "";
        return String.format("{user: [%s]%s}", attributeIds.stream().map(String::valueOf).collect(java.util.stream.Collectors.joining(", ")), processStr);
    }
}
