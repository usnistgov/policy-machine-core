package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.store.GraphStore;
import java.util.Collection;

/**
 * Represents a single (non-composite) user context.
 */
public sealed interface SingleUserContext extends UserContext
        permits UserIdContext, UsernameContext, AttributeIdsContext, AttributeNamesContext {

    /**
     * Get the adjacent descendants of the user.
     * @param graphStore the GraphStore to get the adjacent descendants.
     * @return a list of adjacent descendant Ids.
     * @throws PMException if any error occurs while retrieving.
     */
    Collection<Long> getAdjacentDescendants(GraphStore graphStore) throws PMException;
}
