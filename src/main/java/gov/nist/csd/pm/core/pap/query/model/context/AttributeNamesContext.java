package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.store.GraphStore;
import java.util.ArrayList;
import java.util.Collection;

public final class AttributeNamesContext implements SingleUserContext {

    private final Collection<String> attributeNames;
    private final String process;

    public AttributeNamesContext(Collection<String> attributeNames, String process) {
        this.attributeNames = attributeNames;
        this.process = process;
    }

    public AttributeNamesContext(Collection<String> attributeNames) {
        this(attributeNames, "");
    }

    public Collection<String> attributeNames() {
        return attributeNames;
    }

    @Override
    public String getProcess() {
        return process;
    }

    @Override
    public void checkExists(GraphStore graphStore) throws PMException {
        for (String name : attributeNames) {
            if (!graphStore.nodeExists(name)) {
                throw new NodeDoesNotExistException(name);
            }
        }
    }

    @Override
    public Collection<Long> getAdjacentDescendants(GraphStore graphStore) throws PMException {
        Collection<Long> ids = new ArrayList<>();
        for (String name : attributeNames) {
            ids.add(graphStore.getNodeByName(name).getId());
        }
        return ids;
    }

    @Override
    public String toString() {
        String processStr = process != null && !process.isEmpty() ? ", process: " + process : "";
        return String.format("{user: [%s]%s}", String.join(", ", attributeNames), processStr);
    }
}
