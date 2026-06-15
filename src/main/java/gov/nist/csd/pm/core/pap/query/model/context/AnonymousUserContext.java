package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.NodeLookup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class AnonymousUserContext extends UserContext{

    public static AnonymousUserContext ofIds(Set<Long> attributeIds, String process) {
        return new AnonymousUserContext(process, attributeIds, null);
    }

    public static AnonymousUserContext ofIds(Set<Long> attributeIds) {
        return new AnonymousUserContext(null, attributeIds, null);
    }

    public static AnonymousUserContext ofNames(Set<String> attributeNames, String process) {
        return new AnonymousUserContext(process, null, attributeNames);
    }

    public static AnonymousUserContext ofNames(Set<String> attributeNames) {
        return new AnonymousUserContext(null, null, attributeNames);
    }

    private final Set<Long> attributeIds;
    private final Set<String> attributeNames;

    private AnonymousUserContext(String process, Set<Long> attributeIds, Set<String> attributeNames) {
        super(process);
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
