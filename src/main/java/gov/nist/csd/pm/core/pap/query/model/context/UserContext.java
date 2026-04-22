package gov.nist.csd.pm.core.pap.query.model.context;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents the user in an access decision.
 * Three types are supported:
 *   - NodeUserContext: identifies a specific user node by id (IdUserContext) or by name (NameUserContext).
 *   - AnonymousUserContext: identifies a subject by a set of user attribute IDs (AttributeIdsUserContext) or names
 *                           (AttributeNamesUserContext).
 *   - ConjunctiveUserContext: combines multiple UserContext instances so that access is evaluated as the intersection
 *                             of all contexts.
 */
public abstract sealed class UserContext implements Serializable
        permits NodeUserContext, AnonymousUserContext, ConjunctiveUserContext {

    private final String process;

    protected UserContext(String process) {
        this.process = process;
    }

    /**
     * Returns the process identifier associated with this user context.
     * @return the process identifier, or an empty string if none is set
     */
    public String getProcess() {
        return process;
    }

    /**
     * Creates a context identifying the user by name.
     * @param username the name of the user node
     * @return a NameUserContext for the given username
     */
    public static UserContext of(String username) {
        return new NameUserContext(username);
    }

    /**
     * Creates a context identifying the user by node ID.
     * @param userId the ID of the user node
     * @return an IdUserContext for the given ID
     */
    public static UserContext of(long userId) {
        return new IdUserContext(userId);
    }

    /**
     * Creates an anonymous context identifying the subject by a set of user attribute names.
     * @param attributeNames the names of the user attributes
     * @return an AttributeNamesUserContext for the given attribute names
     */
    public static UserContext of(List<String> attributeNames) {
        return new AttributeNamesUserContext(new HashSet<>(attributeNames));
    }

    /**
     * Creates an anonymous context identifying the subject by a set of user attribute IDs.
     * @param attributeIds the IDs of the user attributes
     * @return an AttributeIdsUserContext for the given attribute IDs
     */
    public static UserContext of(Set<Long> attributeIds) {
        return new AttributeIdsUserContext(attributeIds);
    }

    /**
     * Creates a conjunctive context that combines multiple UserContext instances.
     * Access is evaluated as the intersection of privileges across all sub-contexts.
     * @param contexts the list of contexts to combine
     * @return a ConjunctiveUserContext wrapping the given contexts
     */
    public static UserContext conjunctive(List<UserContext> contexts) {
        return new ConjunctiveUserContext(contexts);
    }
}
