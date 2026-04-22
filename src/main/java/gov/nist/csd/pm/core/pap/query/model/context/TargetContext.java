package gov.nist.csd.pm.core.pap.query.model.context;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * Represents the target resource in an access decision.
 * Two types are supported:
 *   - NodeTargetContext: identifies a specific target node by id (IdTargetContext) or by name (NameTargetContext).
 *   - AnonymousTargetContext: identifies a target by a set of attribute IDs (AttributeIdsTargetContext) or names
 *                             (AttributeNamesTargetContext).
 */
public abstract sealed class TargetContext implements Serializable
        permits NodeTargetContext, AnonymousTargetContext {

    /**
     * Creates a context identifying the target by name.
     * @param targetName the name of the target node
     * @return a NameTargetContext for the given name
     */
    public static TargetContext of(String targetName) {
        return new NameTargetContext(targetName);
    }

    /**
     * Creates a context identifying the target by node ID.
     * @param targetId the ID of the target node
     * @return an IdTargetContext for the given ID
     */
    public static TargetContext of(long targetId) {
        return new IdTargetContext(targetId);
    }

    /**
     * Creates an anonymous context identifying the target by a set of object attribute names.
     * @param attributeNames the names of the object attributes
     * @return an AttributeNamesTargetContext for the given attribute names
     */
    public static TargetContext of(Collection<String> attributeNames) {
        return new AttributeNamesTargetContext(attributeNames);
    }

    /**
     * Creates an anonymous context identifying the target by a set of object attribute IDs.
     * @param attributeIds the IDs of the object attributes
     * @return an AttributeIdsTargetContext for the given attribute IDs
     */
    public static TargetContext of(Set<Long> attributeIds) {
        return new AttributeIdsTargetContext(attributeIds);
    }
}
