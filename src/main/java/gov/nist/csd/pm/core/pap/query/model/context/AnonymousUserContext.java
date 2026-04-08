package gov.nist.csd.pm.core.pap.query.model.context;

/**
 * Represents a user context defined by a set of user attribute ids or names.
 */
public sealed interface AnonymousUserContext extends UserContext
        permits AttributeIdsUserContext, AttributeNamesUserContext {
}
