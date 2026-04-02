package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.store.GraphStore;
import java.util.Collection;

public final class UsernameContext implements SingleUserContext {

    private final String username;
    private final String process;

    public UsernameContext(String username, String process) {
        this.username = username;
        this.process = process;
    }

    public UsernameContext(String username) {
        this(username, "");
    }

    public String username() {
        return username;
    }

    @Override
    public String getProcess() {
        return process;
    }

    @Override
    public void checkExists(GraphStore graphStore) throws PMException {
        if (!graphStore.nodeExists(username)) {
            throw new NodeDoesNotExistException(username);
        }
    }

    @Override
    public Collection<Long> getAdjacentDescendants(GraphStore graphStore) throws PMException {
        return graphStore.getAdjacentDescendants(graphStore.getNodeByName(username).getId());
    }

    @Override
    public String toString() {
        String processStr = process != null && !process.isEmpty() ? ", process: " + process : "";
        return String.format("{user: %s%s}", username, processStr);
    }
}
