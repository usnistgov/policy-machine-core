package gov.nist.csd.pm.core.pap.query.model.context;

import java.util.List;
import java.util.Objects;

public final class CompositeUserContext implements UserContext {

    private final List<UserContext> contexts;

    public CompositeUserContext(List<UserContext> contexts) {
        this.contexts = contexts;
    }

    public List<UserContext> contexts() {
        return contexts;
    }

    @Override
    public String getProcess() {
        return contexts.getFirst().getProcess();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompositeUserContext that = (CompositeUserContext) o;
        return Objects.equals(contexts, that.contexts);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(contexts);
    }

    @Override
    public String toString() {
        return String.format("{user: composite[%d contexts]}", contexts.size());
    }
}
