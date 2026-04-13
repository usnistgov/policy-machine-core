package gov.nist.csd.pm.core.pap.query.model.context;

import java.io.Serializable;

/**
 * Represents the user in an access decision.
 *
 * Three types are supported:
 *   - NodeUserContext: identifies a specific user node by id (IdUserContext) or by name (NameUserContext).
 *   - AnonymousUserContext: identifies a subject by a set of user attribute IDs (AttributeIdsUserContext) or names
 *                           (AttributeNamesUserContext).
 *   - ConjunctiveUserContext: combines multiple UserContext instances so that access is evaluated as the intersection
 *                             of all contexts.
 */
public sealed interface UserContext extends Serializable
        permits NodeUserContext, AnonymousUserContext, ConjunctiveUserContext {

    /**
     * Returns the process identifier associated with this user context.
     * @return the process identifier, or an empty string if none is set
     */
    String getProcess();
}
