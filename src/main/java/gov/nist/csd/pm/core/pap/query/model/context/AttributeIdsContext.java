package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.store.GraphStore;
import java.util.Collection;

public final class AttributeIdsContext implements SingleUserContext {

    private final Collection<Long> attributeIds;
    private final String process;

    public AttributeIdsContext(Collection<Long> attributeIds, String process) {
        this.attributeIds = attributeIds;
        this.process = process;
    }

    public AttributeIdsContext(Collection<Long> attributeIds) {
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
    public void checkExists(GraphStore graphStore) throws PMException {
        for (long id : attributeIds) {
            if (!graphStore.nodeExists(id)) {
                throw new NodeDoesNotExistException(id);
            }
        }
    }

    @Override
    public Collection<Long> getAdjacentDescendants(GraphStore graphStore) throws PMException {
        return attributeIds;
    }

    @Override
    public String toString() {
        String processStr = process != null && !process.isEmpty() ? ", process: " + process : "";
        return String.format("{user: [%s]%s}", attributeIds.stream().map(String::valueOf).collect(java.util.stream.Collectors.joining(", ")), processStr);
    }
}
