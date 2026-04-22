package gov.nist.csd.pm.core.pap.query.model.context;

/**
 * Represents a user context defined by a set of user attribute ids or names.
 */
public abstract sealed class AnonymousUserContext extends UserContext
        permits AttributeIdsUserContext, AttributeNamesUserContext {

    protected AnonymousUserContext(String process) {
        super(process);
    }
}
