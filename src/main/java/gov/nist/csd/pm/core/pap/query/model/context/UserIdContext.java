package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.store.GraphStore;
import java.util.Collection;

public final class UserIdContext implements SingleUserContext {

    private final long userId;
    private final String process;

    public UserIdContext(long userId, String process) {
        this.userId = userId;
        this.process = process;
    }

    public UserIdContext(long userId) {
        this(userId, "");
    }

    public long userId() {
        return userId;
    }

    @Override
    public String getProcess() {
        return process;
    }

    @Override
    public void checkExists(GraphStore graphStore) throws PMException {
        if (!graphStore.nodeExists(userId)) {
            throw new NodeDoesNotExistException(userId);
        }
    }

    @Override
    public Collection<Long> getAdjacentDescendants(GraphStore graphStore) throws PMException {
        return graphStore.getAdjacentDescendants(userId);
    }

    @Override
    public String toString() {
        String processStr = process != null && !process.isEmpty() ? ", process: " + process : "";
        return String.format("{user: %d%s}", userId, processStr);
    }
}
