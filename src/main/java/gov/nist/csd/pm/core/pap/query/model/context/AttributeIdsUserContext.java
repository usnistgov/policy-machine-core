package gov.nist.csd.pm.core.pap.query.model.context;

import java.util.Collection;
import java.util.Objects;

public final class AttributeIdsUserContext implements AnonymousUserContext {

    private final Collection<Long> attributeIds;
    private final String process;

    public AttributeIdsUserContext(Collection<Long> attributeIds, String process) {
        this.attributeIds = attributeIds;
        this.process = process;
    }

    public AttributeIdsUserContext(Collection<Long> attributeIds) {
        this(attributeIds, "");
    }

    public Collection<Long> attributeIds() {
        return attributeIds;
    }

    @Override
    public String getProcess() {
        return process;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttributeIdsUserContext that = (AttributeIdsUserContext) o;
        return Objects.equals(attributeIds, that.attributeIds) && Objects.equals(process, that.process);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributeIds, process);
    }

    @Override
    public String toString() {
        String processStr = process != null && !process.isEmpty() ? ", process: " + process : "";
        return String.format("{user: [%s]%s}", attributeIds.stream().map(String::valueOf).collect(java.util.stream.Collectors.joining(", ")), processStr);
    }
}
