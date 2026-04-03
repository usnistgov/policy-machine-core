package gov.nist.csd.pm.core.pap.query.model.context;

/**
 * Represents a user context backed by a single node (by id or name).
 */
public sealed interface UserNodeContext extends UserContext
        permits UserIdContext, UsernameContext {
}
