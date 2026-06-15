package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.NodeLookup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Represents an anonymous target context identified by a set of object attribute IDs or names.
 */
public class AnonymousTargetContext extends TargetContext {

    public static AnonymousTargetContext ofIds(Set<Long> attributeIds) {
        return new AnonymousTargetContext(attributeIds, null);
    }

    public static AnonymousTargetContext ofNames(Set<String> attributeNames) {
        return new AnonymousTargetContext(null, attributeNames);
    }

    private final Set<Long> attributeIds;
    private final Set<String> attributeNames;

    private AnonymousTargetContext(Set<Long> attributeIds, Set<String> attributeNames) {
        this.attributeIds = attributeIds;
        this.attributeNames = attributeNames;
    }

    public Set<Long> getAttributeIds() {
        return attributeIds;
    }

    public Set<String> getAttributeNames() {
        return attributeNames;
    }

    @Override
    public Collection<Long> resolveNodeIds(NodeLookup nodeLookup) throws PMException {
        if (attributeIds != null) {
            return attributeIds;
        }

        return namesToIds(nodeLookup);
    }

    private Collection<Long> namesToIds(NodeLookup nodeLookup) throws PMException {
        List<Long> ids = new ArrayList<>();

        for (String attributeName : attributeNames) {
            Node node = nodeLookup.getNodeByName(attributeName);
            ids.add(node.getId());
        }

        return ids;
    }
}
