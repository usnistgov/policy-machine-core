package gov.nist.ngac.pm.core.pap.graph.dag;

import gov.nist.ngac.pm.core.common.exception.PMException;
import java.util.Collection;

/**
 * Supplies the nodes adjacent to a node for a {@link GraphWalker} to traverse next — ascendants or
 * descendants, depending on the direction the walker is configured for.
 */
@FunctionalInterface
public interface AdjacencyRetriever {

    /**
     * Returns the ids of the nodes adjacent to the given node.
     *
     * @param nodeId the node to look up adjacents for
     * @return the adjacent node ids
     * @throws PMException if the lookup fails
     */
    Collection<Long> getAdjacent(long nodeId) throws PMException;

}