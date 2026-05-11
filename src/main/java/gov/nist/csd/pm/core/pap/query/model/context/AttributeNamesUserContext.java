package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.dag.GraphWalker;
import gov.nist.csd.pm.core.pap.query.GraphQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * An AnonymousUserContext with a set of attribute names.
 */
public final class AttributeNamesUserContext extends AnonymousUserContext {

    private final Set<String> attributeNames;

    public AttributeNamesUserContext(Set<String> attributeNames, String process) {
        super(process);
        this.attributeNames = attributeNames;
    }

    public AttributeNamesUserContext(Set<String> attributeNames) {
        this(attributeNames, "");
    }

    public Set<String> attributeNames() {
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
        AttributeNamesUserContext that = (AttributeNamesUserContext) o;
        return Objects.equals(attributeNames, that.attributeNames) && Objects.equals(getProcess(), that.getProcess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributeNames, getProcess());
    }

    @Override
    public String toString() {
        String process = getProcess();
        String processStr = process != null && !process.isEmpty() ? ", process: " + process : "";
        return String.format("{user: [%s]%s}", String.join(", ", attributeNames), processStr);
    }
}
