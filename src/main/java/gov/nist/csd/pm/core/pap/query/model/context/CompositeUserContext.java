package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.store.GraphStore;
import java.util.List;

public final class CompositeUserContext implements UserContext {

    private final List<SingleUserContext> contexts;

    public CompositeUserContext(List<SingleUserContext> contexts) {
        this.contexts = contexts;
    }

    public List<SingleUserContext> contexts() {
        return contexts;
    }

    @Override
    public String getProcess() {
        return "";
    }

    @Override
    public void checkExists(GraphStore graphStore) throws PMException {
        for (SingleUserContext ctx : contexts) {
            ctx.checkExists(graphStore);
        }
    }

    @Override
    public String toString() {
        return String.format("{user: composite[%d contexts]}", contexts.size());
    }
}
