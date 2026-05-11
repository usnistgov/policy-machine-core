package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.dag.GraphWalker;
import gov.nist.csd.pm.core.pap.query.GraphQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * An AnonymousTargetContext with a set of attribute names.
 */
public final class AttributeNamesTargetContext extends AnonymousTargetContext {

    private final Collection<String> attributeNames;

    public AttributeNamesTargetContext(Collection<String> attributeNames) {
        this.attributeNames = attributeNames;
    }

    public Collection<String> attributeNames() {
        return attributeNames;
    }

    @Override
    public void walk(GraphWalker walker, GraphQuery graphQuery) throws PMException {
        List<Long> ids = new ArrayList<>();
        for (String name : attributeNames) {
            ids.add(graphQuery.getNodeByName(name).getId());
        }
        walker.walk(ids);
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
