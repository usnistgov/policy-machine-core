package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.dag.GraphWalker;
import java.util.List;
import java.util.Objects;

/**
 * A UserContext with a list of sub contexts. Using this in an access decision enforces the intersection of privileges
 * for all sub contexts.
 */
public final class ConjunctiveUserContext extends UserContext {

    private final List<UserContext> contexts;

    public ConjunctiveUserContext(List<UserContext> contexts) {
        super(contexts.isEmpty() ? "" : contexts.getFirst().getProcess());
        this.contexts = contexts;
    }

    public List<UserContext> contexts() {
        return contexts;
    }

    @Override
    public void walk(GraphWalker walker, NodeLookup nodeLookup) throws PMException {
        for (UserContext ctx : contexts) {
            ctx.walk(walker, nodeLookup);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConjunctiveUserContext that = (ConjunctiveUserContext) o;
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
