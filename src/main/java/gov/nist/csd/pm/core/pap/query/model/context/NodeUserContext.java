package gov.nist.csd.pm.core.pap.query.model.context;

/**
 * Represents a user context backed by a single node (by id or name).
 */
public abstract sealed class NodeUserContext extends UserContext
        permits IdUserContext, NameUserContext {

    protected NodeUserContext(String process) {
        super(process);
    }
}
