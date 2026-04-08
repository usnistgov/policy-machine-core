package gov.nist.csd.pm.core.pap.query.model.context;

import java.util.Collection;
import java.util.Objects;

public final class AttributeNamesTargetContext implements AnonymousTargetContext {

    private final Collection<String> attributeNames;

    public AttributeNamesTargetContext(Collection<String> attributeNames) {
        this.attributeNames = attributeNames;
    }

    public Collection<String> attributeNames() {
        return attributeNames;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttributeNamesTargetContext that = (AttributeNamesTargetContext) o;
        return Objects.equals(attributeNames, that.attributeNames);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(attributeNames);
    }

    @Override
    public String toString() {
        return String.format("{target: [%s]}", String.join(", ", attributeNames));
    }
}
