package gov.nist.csd.pm.core.pap.query.model.context;

import java.util.Objects;
import java.util.Set;

/**
 * An AnonymousUserContext with a set of attribute names.
 */
public final class AttributeNamesUserContext implements AnonymousUserContext {

    private final Set<String> attributeNames;
    private final String process;

    public AttributeNamesUserContext(Set<String> attributeNames, String process) {
        this.attributeNames = attributeNames;
        this.process = process;
    }

    public AttributeNamesUserContext(Set<String> attributeNames) {
        this(attributeNames, "");
    }

    public Set<String> attributeNames() {
        return attributeNames;
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
        AttributeNamesUserContext that = (AttributeNamesUserContext) o;
        return Objects.equals(attributeNames, that.attributeNames) && Objects.equals(process,
            that.process);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributeNames, process);
    }

    @Override
    public String toString() {
        String processStr = process != null && !process.isEmpty() ? ", process: " + process : "";
        return String.format("{user: [%s]%s}", String.join(", ", attributeNames), processStr);
    }
}
